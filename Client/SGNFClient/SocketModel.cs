﻿using System.Collections.Generic;
using ProtoBuf;
using System;
using System.IO;
using System.Net.Sockets;
using UnityEngine;

namespace SGNFClient
{

    //SocketModel
    [ProtoContract]
    public class ISSocketModel
    {
        [ProtoMember(1, IsRequired = true)]
        public int Command;
        [ProtoMember(2, IsRequired = false)]
        public List<string> Message;
    }

    [ProtoContract]
    public class SSSocketModel
    {
        [ProtoMember(1, IsRequired = true)]
        public int Command = -1;
        [ProtoMember(2, IsRequired = true)]
        public int CurrentTick = 0;
        [ProtoMember(3, IsRequired = true)]
        public List<int> Message = new List<int>();
        [ProtoMember(4, IsRequired = false)]
        public List<Vec> Vector = new List<Vec>();

    }
    
    [ProtoContract]
    public class Vec
    {
        [ProtoMember(1, IsRequired = true)]
        public int Tag = -1;
        [ProtoMember(2, IsRequired = true)]
        public float X = 0;
        [ProtoMember(3, IsRequired = true)]
        public float Y = 0;
        [ProtoMember(4, IsRequired = false)]
        public float Z = 0;


        public Vec() { }
        public Vec(int t,Vector3 v)
        {
            Tag = t;
            X = v.x;
            Y = v.y;
            Z = v.z;
        }
        public Vector3 ToVector3()
        {
            return new Vector3(X, Y, Z);
        }
    }



    public static class SocketUtil
    {
        public struct SSInfo
        {
            public string Tag;
            public string IP;
            public int Port;

            public override string ToString()
            {
                return "TAG: " + Tag + " IP: " + IP + " PORT: " + Port;
            }
        }

        internal enum InternalCommand
        {
            //Common
            NULL = 0xF000, //当服务器收到该消息时抛弃掉
            PING = 0xF001, //客户端发来一个随机数，服务器接受此指令后返回该随机数

            //Clt-IS
            SSINFO = 0xF002,//客户端从IS获取全部SS列

            //Clt-SS
            TICK = 0xF003,//客户端从SS获取tick
        }

        internal static byte[] ISSerial(ISSocketModel socketModel)//将SocketModel转化成字节数组
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
        internal static ISSocketModel ISDeSerial(byte[] msg)//将字节数组转化成我们的消息类型SocketModel
        {
            using (MemoryStream ms = new MemoryStream())
            {
                ms.Write(msg, 0, msg.Length);
                ms.Position = 0;
                ISSocketModel socketModel = Serializer.Deserialize<ISSocketModel>(ms);
                return socketModel;
            }
        }

        internal static byte[] SSSerial(SSSocketModel socketModel)//将SocketModel转化成字节数组
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
        internal static SSSocketModel SSDeSerial(byte[] msg)//将字节数组转化成我们的消息类型SocketModel
        {
            using (MemoryStream ms = new MemoryStream())
            {
                ms.Write(msg, 0, msg.Length);
                ms.Position = 0;
                SSSocketModel socketModel = Serializer.Deserialize<SSSocketModel>(ms);
                return socketModel;
            }
        }


        internal static int BytesToInt(byte[] data, int offset)
        {
            int num = 0;
            for (int i = offset; i < offset + 4; i++)
            {
                num <<= 8;
                num |= (data[i] & 0xff);
            }
            return num;
        }
        internal static byte[] IntToBytes(int num)
        {
            byte[] bytes = new byte[4];
            for (int i = 0; i < 4; i++)
            {
                bytes[i] = (byte)(num >> (24 - i * 8));
            }
            return bytes;
        }

        /// <summary>
        /// 计算用常量
        /// </summary>
        private static class Constant
        {
            //消息：数据总长度(4byte) + 数据(N byte)
            public static int HEAD_DATA_LEN => 4;

        }

        /// <summary>
        /// 网络数据缓存器
        /// </summary>
        [System.Serializable]
        internal class DataBuffer
        {
            //自动大小数据缓存器
            private int _minBuffLen;
            private byte[] _buff;
            private int _curBuffPosition;
            private int _buffLength = 0;
            private int _dataLength;

            /// <summary>
            /// 构造函数
            /// </summary>
            public DataBuffer()
            {
                _minBuffLen = 1024;
                _buff = new byte[1024];
            }

            /// <summary>
            /// 添加缓存数据
            /// </summary>
            /// <param name="_data"></param>
            /// <param name="_dataLen"></param>
            public void AddBuffer(byte[] _data, int _dataLen)
            {
                if (_dataLen > _buff.Length - _curBuffPosition)//超过当前缓存
                {
                    byte[] _tmpBuff = new byte[_curBuffPosition + _dataLen];
                    Array.Copy(_buff, 0, _tmpBuff, 0, _curBuffPosition);
                    Array.Copy(_data, 0, _tmpBuff, _curBuffPosition, _dataLen);
                    _buff = _tmpBuff;
                    _tmpBuff = null;
                }
                else
                {
                    Array.Copy(_data, 0, _buff, _curBuffPosition, _dataLen);
                }
                _curBuffPosition += _dataLen;//修改当前数据标记

                SGNFDebug.HEXLog("current buffer", _buff, _buff.Length);
            }

            /// <summary>
            /// 更新数据长度
            /// </summary>
            public void UpdateDataLength()
            {
                if (_dataLength == 0 && _curBuffPosition >= Constant.HEAD_DATA_LEN)
                {
                    byte[] lenByte = new byte[4];
                    Array.Copy(_buff, lenByte, 4);
                    Array.Reverse(lenByte);
                    
                    _dataLength = BytesToInt(lenByte, 0);
                    _buffLength = _dataLength + 4;
                }
            }

            /// <summary>
            /// 获取一条可用数据，返回值标记是否有数据
            /// </summary>
            /// <param name="_tmpSocketData"></param>
            /// <returns></returns>
            public bool GetData(out byte[] _data)
            {
                _data = new byte[0];

                if (_buffLength <= 0)
                {
                    UpdateDataLength();
                }

                if (_buffLength > 0 && _curBuffPosition >= _buffLength)
                {
                    _data = new byte[_dataLength];
                    Array.Copy(_buff, Constant.HEAD_DATA_LEN, _data, 0, _dataLength);
                    _curBuffPosition -= _buffLength;
                    byte[] _tmpBuff = new byte[_curBuffPosition < _minBuffLen ? _minBuffLen : _curBuffPosition];
                    Array.Copy(_buff, _buffLength, _tmpBuff, 0, _curBuffPosition);
                    _buff = _tmpBuff;


                    _buffLength = 0;
                    _dataLength = 0;
                    return true;
                }
                return false;
            }

        }
    }

    
}