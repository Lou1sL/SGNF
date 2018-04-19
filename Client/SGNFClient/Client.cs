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
        

        internal static float latency = 0;
        public static float Latency => latency;




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
        
        public static void AddISMsgRcver<T>(T cmd, ISMessage_Callback_Handler callBack)
        {
            CheckEnum<T>();
            MessageCenter.Instance.addISObserver((int)(object)cmd, callBack);
        }

        public static void RemoveISMsgRcver<T>(T cmd, ISMessage_Callback_Handler callBack)
        {
            CheckEnum<T>();
            MessageCenter.Instance.removeISObserver((int)(object)cmd, callBack);
        }


        private static void CheckEnum<T>()
        {
            if (!typeof(T).IsEnum)
                throw new System.ArgumentException("Please use Enum for Command !");
        }




        public static void SSJoin(SocketUtil.SSInfo ssinfo)
        {
            SSSocketManager.Instance.Connect(ssinfo.IP, ssinfo.Port);
        }

        public static void SSLeave()
        {
            SSSocketManager.Instance.Close();
            tick = -1;
        }


        public static void SSUpdate(SSTickFrame frameUpdater)
        {
            MessageCenter.Instance.setSSUpdater(frameUpdater);
        }
    }
}
