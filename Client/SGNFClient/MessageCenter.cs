using SGNFClient.UnityScript;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityStandardUtils;

namespace SGNFClient
{
    public delegate void ISMessage_Callback_Handler(ISSocketModel data);
    public delegate SSSocketModel SSTickFrame(SSSocketModel data);

    public delegate void ServerTransformUpdate(SSSocketModel data);
    public delegate void ClientTransformUpdate(ref SSSocketModel data);


    public class MessageCenter : SingletonMonoBehaviour<MessageCenter>
    {
        
        //IS
        private Dictionary<int, ISMessage_Callback_Handler> ISMessage_HandlerList = new Dictionary<int, ISMessage_Callback_Handler>();
        internal Queue<ISSocketModel> ISMessageDataQueue = new Queue<ISSocketModel>();
        //SS
        internal SSTickFrame tickFrameUpdater = null;
        internal Queue<SSSocketModel> SSMessageDataQueue = new Queue<SSSocketModel>();
        

        internal int currentTick = 0;
        internal float timeFromLastTick = 0;

        internal float delatT = 0;

        //NetTransform
        internal ServerTransformUpdate ServerTransformCall = null;
        internal ClientTransformUpdate ClientTransformCall = null;


        //添加IS网络事件观察者
        internal void addISObserver(int protocalType, ISMessage_Callback_Handler callback)
        {
            if (ISMessage_HandlerList.ContainsKey(protocalType))
            {
                //知识点
                //无返回值的委托，你给它注册多少个方法，它就执行多少个方法
                //而有返回值的委托，同样注册多少个方法就执行多少个方法，！！！！但返回的是最后一个方法的返回值！！！！
                ISMessage_HandlerList[protocalType] += callback;
            }
            else
            {
                ISMessage_HandlerList.Add(protocalType, callback);
            }
        }
        //删除IS网络事件观察者
        internal void removeISObserver(int protocalType, ISMessage_Callback_Handler callback)
        {
            if (ISMessage_HandlerList.ContainsKey(protocalType))
            {
                ISMessage_HandlerList[protocalType] -= callback;
                if (ISMessage_HandlerList[protocalType] == null)
                {
                    ISMessage_HandlerList.Remove(protocalType);
                }
            }
        }

        //用户自定义SS帧消息处理器
        internal void setSSUpdater(SSTickFrame frameUpdater)
        {
            tickFrameUpdater = frameUpdater;
        }


        float ping = 0f;

        void Update()
        {
            delatT = Time.deltaTime;

            if(SSSocketManager.Instance.IsConnceted) timeFromLastTick += Time.deltaTime;

            while (ISMessageDataQueue.Count > 0)
            {
                lock (ISMessageDataQueue)
                {
                    ISSocketModel tmpNetMessageData = ISMessageDataQueue.Dequeue();
                    if (ISMessage_HandlerList.ContainsKey(tmpNetMessageData.Command))
                    {
                        ISMessage_HandlerList[tmpNetMessageData.Command](tmpNetMessageData);
                    }
                }
            }


            //计算延迟
            if (SSMessageDataQueue.Count <= 0) ping += delatT;
            else
            {
                NetManager.Instance.latency = ping;
                ping = 0f;
            }


            while (SSMessageDataQueue.Count > 0)
            {

                lock (SSMessageDataQueue)
                {
                    SSSocketModel tmpSSMessageData = SSMessageDataQueue.Dequeue();
                    currentTick = tmpSSMessageData.CurrentTick;


                    ServerTransformCall?.Invoke(tmpSSMessageData);


                    if (tickFrameUpdater != null)
                    {
                        timeFromLastTick = 0;

                        SSSocketModel snd = tickFrameUpdater(tmpSSMessageData);
                        snd.CurrentTick = currentTick;

                        ClientTransformCall?.Invoke(ref snd);

                        byte[] rawData = SocketUtil.SSSerial(snd);
                        SSSocketManager.Instance.SendMsgBase(rawData);
                    }
                }
            }


            




        }


    }

    
}
