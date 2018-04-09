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
    public static class Client
    {
        public static bool IsConnected => ISSocketManager.Instance.IsConnceted;
        

        public static void Connect(string IP, int Port)
        {
            ISSocketManager.Instance.Connect(IP, Port);
        }
        public static void Disconnect()
        {
            ISSocketManager.Instance.Close();
        }

        /// <summary>
        /// 发送
        /// </summary>
        /// <param name="_protocalType"></param>
        /// <param name="data"></param>
        public static void SendISMsg(ISSocketModel data)
        {
            if (!IsConnected) return;
            byte[] rawData = SocketUtil.ISSerial(data);
            ISSocketManager.Instance.SendMsgBase(rawData);
        }

        public static void AddCallBackObserver<T>(T cmd, Callback_NetMessage_Handle callBack)
        {
            CheckEnum<T>();
            ISMessageCenter.Instance.addObserver((int)(object)cmd, callBack);
        }

        public static void RemoveCallBackObserver<T>(T cmd, Callback_NetMessage_Handle callBack)
        {
            CheckEnum<T>();
            ISMessageCenter.Instance.removeObserver((int)(object)cmd, callBack);
        }

        private static void CheckEnum<T>()
        {
            if (!typeof(T).IsEnum)
                throw new System.ArgumentException("Please use Enum for Command !");
        }
    }
}
