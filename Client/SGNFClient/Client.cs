using System.Collections.Generic;

namespace SGNFClient
{
    public static partial class Client
    {
        public static bool IsConnected => ISSocketManager.Instance.IsConnceted;
        internal static List<SocketUtil.SSInfo> allSSInfo = new List<SocketUtil.SSInfo>();
        public static List<SocketUtil.SSInfo> AllSSInfo => allSSInfo;

        public static bool IsJoined => SSSocketManager.Instance.IsConnceted;
        internal static int tick = -1;
        public static int Tick => tick;

        public static void Connect(string IP, int Port)
        {
            ISSocketManager.Instance.Connect(IP, Port);
        }
        public static void Disconnect()
        {
            SSSocketManager.Instance.Close();
            ISSocketManager.Instance.Close();
            allSSInfo.Clear();
            tick = -1;
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
        
        public static void AddISCallBackObserver<T>(T cmd, Callback_NetMessage_Handle callBack)
        {
            CheckEnum<T>();
            ISMessageCenter.Instance.addObserver((int)(object)cmd, callBack);
        }

        public static void RemoveISCallBackObserver<T>(T cmd, Callback_NetMessage_Handle callBack)
        {
            CheckEnum<T>();
            ISMessageCenter.Instance.removeObserver((int)(object)cmd, callBack);
        }


        private static void CheckEnum<T>()
        {
            if (!typeof(T).IsEnum)
                throw new System.ArgumentException("Please use Enum for Command !");
        }




        public static void JoinSS(SocketUtil.SSInfo ssinfo)
        {
            SSSocketManager.Instance.Connect(ssinfo.IP, ssinfo.Port);
        }

        public static void LeaveSS()
        {
            SSSocketManager.Instance.Close();
            tick = -1;
        }


        public static void SendSSMsg(SSSocketModel data)
        {
            if (!IsConnected) return;
            if (!SSSocketManager.Instance.IsConnceted) return;

            byte[] rawData = SocketUtil.SSSerial(data);
            SSSocketManager.Instance.SendMsgBase(rawData);
        }

        public static void AddSSCallBackObserver<T>(T cmd, Callback_SSMessage_Handle callBack)
        {
            CheckEnum<T>();
            SSMessageCenter.Instance.addObserver((int)(object)cmd, callBack);
        }

        public static void RemoveSSCallBackObserver<T>(T cmd, Callback_SSMessage_Handle callBack)
        {
            CheckEnum<T>();
            SSMessageCenter.Instance.removeObserver((int)(object)cmd, callBack);
        }
    }
}
