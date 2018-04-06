using ProtoBuf;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using UnityEngine;
using UnityStandardUtils;

namespace SGNFClient
{
    public class Client:SingletonMonoBehaviour<Client>
    {
        private const string HOST = "127.0.0.1";
        private const int PORT = 8080;
        public static TcpClient client;
        
        private byte[] recieveData;
        private int len;
        private bool isHead;

        void Start()
        {
            if (client == null)
            {
                Connect();
            }
            isHead = true;
            recieveData = new byte[800];
            client.GetStream().BeginRead(recieveData, 0, 800, ReceiveMsg, client.GetStream());//在start里面开始异步接收消息
        }


        void OnApplicationQuit()
        {
            client.Close();
        }

        public void Connect()
        {
            client = new TcpClient();
            try
            {
                client.Connect(HOST, PORT);
            }
            catch (Exception e)
            {
                Debug.LogException(e);
                client.Close();
            }
        }


        public void SendMsg(SocketModel socketModel)
        {
            byte[] msg = Serial(socketModel);
            //消息体结构：消息体长度+消息体
            byte[] data = new byte[4 + msg.Length];
            IntToBytes(msg.Length).CopyTo(data, 0);
            msg.CopyTo(data, 4);
            client.GetStream().Write(data, 0, data.Length);
            //print("send");
        }

        public void ReceiveMsg(IAsyncResult ar)//异步接收消息
        {
            NetworkStream stream = (NetworkStream)ar.AsyncState;
            stream.EndRead(ar);
            //读取消息体的长度
            if (isHead)
            {
                byte[] lenByte = new byte[4];
                System.Array.Copy(recieveData, lenByte, 4);
                len = BytesToInt(lenByte, 0);
                isHead = false;
            }
            //读取消息体内容
            if (!isHead)
            {
                byte[] msgByte = new byte[len];
                System.Array.ConstrainedCopy(recieveData, 4, msgByte, 0, len);
                isHead = true;
                len = 0;
                SocketModel message = DeSerial(msgByte);
            }
            stream.BeginRead(recieveData, 0, 800, ReceiveMsg, stream);
        }
        private byte[] Serial(SocketModel socketModel)//将SocketModel转化成字节数组
        {
            using (MemoryStream ms = new MemoryStream())
            {
                Serializer.Serialize(ms, socketModel);
                byte[] data = new byte[ms.Length];
                ms.Position = 0;
                ms.Read(data, 0, data.Length);
                return data;
            }
        }
        private SocketModel DeSerial(byte[] msg)//将字节数组转化成我们的消息类型SocketModel
        {
            using (MemoryStream ms = new MemoryStream())
            {
                ms.Write(msg, 0, msg.Length);
                ms.Position = 0;
                SocketModel socketModel = Serializer.Deserialize<SocketModel>(ms);
                return socketModel;
            }
        }
        public static int BytesToInt(byte[] data, int offset)
        {
            int num = 0;
            for (int i = offset; i < offset + 4; i++)
            {
                num <<= 8;
                num |= (data[i] & 0xff);
            }
            return num;
        }
        public static byte[] IntToBytes(int num)
        {
            byte[] bytes = new byte[4];
            for (int i = 0; i < 4; i++)
            {
                bytes[i] = (byte)(num >> (24 - i * 8));
            }
            return bytes;
        }

    }
}
