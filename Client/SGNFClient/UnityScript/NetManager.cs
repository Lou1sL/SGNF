using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityStandardUtils;

namespace SGNFClient.UnityScript
{
    public class NetManager : MonoBehaviour
    {

        //-------------Singleton
        protected static NetManager sInstance = null;
        public static bool NeedDestroy = false;
        public static NetManager Instance
        {
            get
            {
                if (NeedDestroy)
                {
                    return null;
                }
                return sInstance;
            }
        }

        private void Awake()
        {
            sInstance = this;
            if (sInstance.DontDestoryOnLoad) DontDestroyOnLoad(sInstance.gameObject);
        }

        private void OnDestroy()
        {
            NeedDestroy = true;
        }

        private void OnApplicationQuit()
        {
            NeedDestroy = true;
        }
        //--------------------------



        [Tooltip("Keep this GameObject to next scene")]
        public bool DontDestoryOnLoad = true;

        public enum _LogLevel
        {
            EverythingIncludingBufferLog = 0,
            ErrorAndLog = 1,
            ErrorOnly = 2,
            DoNotBotherMe = 3,
        }

        [Tooltip("Which sort of Log would be shown in console")]
        public _LogLevel LogLevel = _LogLevel.ErrorAndLog;


        [System.Serializable]
        public class _NetworkInfo
        {
            public string InfoServerAddress = "127.0.0.1";
            public int InfoServerPort = 9999;
            public string ScenarioServerTag = "ss0";
        }

        [Tooltip("Connection settings")]
        public _NetworkInfo NetworkInfo = new _NetworkInfo();


        
        [System.Serializable]
        public class _SpawnInfo
        {
            public GameObject PlayerPrefeb;
        }

        [Tooltip("Useless now,just for future implementation")]
        public _SpawnInfo SpawnInfo = new _SpawnInfo();


        internal List<SocketUtil.SSInfo> allSSInfo = new List<SocketUtil.SSInfo>();
        internal int tick = -1;
        internal float latency = 0;

        
        public List<SocketUtil.SSInfo> AllSSInfo => allSSInfo;
        public bool IsConnected => ISSocketManager.Instance.IsConnceted;
        public bool IsJoined => SSSocketManager.Instance.IsConnceted;
        public int Tick => tick;
        public float Latency => latency;




        public void Connect()
        {
            ISSocketManager.Instance.Connect(NetworkInfo.InfoServerAddress, NetworkInfo.InfoServerPort);
        }
        public void Disconnect()
        {
            SSLeave();
            ISSocketManager.Instance.Close();
            allSSInfo.Clear();
        }

        /// <summary>
        /// 发送
        /// </summary>
        /// <param name="_protocalType"></param>
        /// <param name="data"></param>
        public void ISMsgSend(ISSocketModel data)
        {
            if (!IsConnected) return;
            byte[] rawData = SocketUtil.ISSerial(data);
            ISSocketManager.Instance.SendMsgBase(rawData);
        }

        public void ISMsgAddRcver<T>(T cmd, ISMessage_Callback_Handler callBack)
        {
            CheckEnum<T>();
            MessageCenter.Instance.addISObserver((int)(object)cmd, callBack);
        }

        public void ISMsgRemoveRcver<T>(T cmd, ISMessage_Callback_Handler callBack)
        {
            CheckEnum<T>();
            MessageCenter.Instance.removeISObserver((int)(object)cmd, callBack);
        }


        private void CheckEnum<T>()
        {
            if (!typeof(T).IsEnum)
                throw new System.ArgumentException("Please use Enum for Command !");
        }


        
        public void SSJoin()
        {
            foreach (SocketUtil.SSInfo inf in allSSInfo)
            {
                if (inf.Tag == NetworkInfo.ScenarioServerTag)
                {
                    SSSocketManager.Instance.Connect(inf.IP, inf.Port);
                    break;
                }
            }
        }

        public void SSLeave()
        {
            SSSocketManager.Instance.Close();
            tick = -1;
            SSRecorder.Instance.RecieveDataBuffer.Clear();
        }


        public void SSUpdate(SSTickFrame frameUpdater)
        {
            MessageCenter.Instance.setSSUpdater(frameUpdater);
        }
    }


}
