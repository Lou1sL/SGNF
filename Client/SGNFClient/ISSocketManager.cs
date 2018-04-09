using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using UnityStandardUtils;

namespace SGNFClient
{
    internal class ISSocketManager : Singleton<ISSocketManager>
    {
        private bool _isConnected = false;
        internal bool IsConnceted => _isConnected;

        private Socket clientSocket = null;
        private Thread receiveThread = null;



        byte[] _tmpReceiveBuff = new byte[4096];
        private SocketUtil.DataBuffer _databuffer = new SocketUtil.DataBuffer();
        private byte[] _socketData = new byte[0];



        /// <summary>
        /// 连接服务器
        /// </summary>
        /// <param name="_currIP"></param>
        /// <param name="_currPort"></param>
        internal void Connect(string _currIP, int _currPort)
        {
            if (!IsConnceted)
            {
                try
                {
                    //创建套接字
                    clientSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
                    //解析IP地址
                    IPAddress ipAddress = IPAddress.Parse(_currIP);
                    IPEndPoint ipEndpoint = new IPEndPoint(ipAddress, _currPort);
                    //异步连接
                    IAsyncResult result = clientSocket.BeginConnect(ipEndpoint, new AsyncCallback(_onConnect_Sucess), clientSocket);
                    bool success = result.AsyncWaitHandle.WaitOne(5000, true);

                    if (!success)
                    {
                        //超时
                        Close();
                    }
                }
                catch (Exception _e)
                {
                    //失败
                    Close();
                }
            }
        }

        /// <summary>
        /// 连接成功，建立接受线程
        /// </summary>
        /// <param name="iar"></param>
        private void _onConnect_Sucess(IAsyncResult iar)
        {
            try
            {
                Socket client = (Socket)iar.AsyncState;
                client.EndConnect(iar);

                receiveThread = new Thread(new ThreadStart(_onReceiveSocket));
                receiveThread.IsBackground = true;
                receiveThread.Start();
                _isConnected = true;
                Console.WriteLine("Connection Established!");
                
            }
            catch (Exception _e)
            {
                Close();
            }
        }
        /// <summary>
        /// 接受网络数据，将接受到的放入消息队列
        /// </summary>
        private void _onReceiveSocket()
        {
            while (true)
            {
                if (!clientSocket.Connected)
                {
                    _isConnected = false;
                    break;
                }
                try
                {
                    int receiveLength = clientSocket.Receive(_tmpReceiveBuff);
                    if (receiveLength > 0)
                    {
                        //将收到的数据添加到缓存器中
                        _databuffer.AddBuffer(_tmpReceiveBuff, receiveLength);
                        //取出一条完整数据
                        while (_databuffer.GetData(out _socketData))
                        {
                            ISSocketModel DeData = SocketUtil.ISDeSerial(_socketData);


                            //如果数据属于内部协议
                            if (Enum.IsDefined(typeof(SocketUtil.InternalCommand), DeData.Command))
                            {
                                if (DeData.Command == (int)SocketUtil.InternalCommand.NULL)
                                {
                                    Console.WriteLine("Got NULL From Server..");
                                }
                            }
                            else
                            {
                                //锁死消息中心消息队列，并添加数据
                                lock (ISMessageCenter.Instance._netMessageDataQueue)
                                {
                                    ISMessageCenter.Instance._netMessageDataQueue.Enqueue(DeData);
                                }
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    clientSocket.Disconnect(true);
                    clientSocket.Shutdown(SocketShutdown.Both);
                    clientSocket.Close();
                    break;
                }
            }
        }

        /// <summary>
        /// 发送消息基本方法
        /// </summary>
        /// <param name="_protocalType"></param>
        /// <param name="_data"></param>
        internal void SendMsgBase(byte[] _data)
        {
            if (clientSocket == null || !clientSocket.Connected)
            {
                return;
            }

            byte[] data = new byte[4 + _data.Length];
            SocketUtil.IntToBytes(_data.Length).CopyTo(data, 0);
            _data.CopyTo(data, 4);


            clientSocket.BeginSend(data, 0, data.Length, SocketFlags.None, new AsyncCallback(_onSendMsg), clientSocket);
        }

        /// <summary>
        /// 发送消息结果回调，可判断当前网络状态
        /// </summary>
        /// <param name="asyncSend"></param>
        private void _onSendMsg(IAsyncResult asyncSend)
        {
            try
            {
                Socket client = (Socket)asyncSend.AsyncState;
                client.EndSend(asyncSend);
            }
            catch (Exception e)
            {
                Console.WriteLine("send msg exception:" + e.StackTrace);
            }
        }

        /// <summary>
        /// 断开
        /// </summary>
        internal void Close()
        {
            if (!_isConnected)
                return;

            _isConnected = false;

            if (receiveThread != null)
            {
                receiveThread.Abort();
                receiveThread = null;
            }

            if (clientSocket != null && clientSocket.Connected)
            {
                clientSocket.Close();
                clientSocket = null;
            }
        }
    }

}