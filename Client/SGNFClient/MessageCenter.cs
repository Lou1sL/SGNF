using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityStandardUtils;

namespace SGNFClient
{
    public delegate void ISMessage_Callback_Handler(ISSocketModel data);
    public delegate void SSTickFrame(SSSocketModel data);

    public class MessageCenter : SingletonMonoBehaviour<MessageCenter>
    {
        private Dictionary<int, ISMessage_Callback_Handler> ISMessage_HandlerList = new Dictionary<int, ISMessage_Callback_Handler>();
        internal Queue<ISSocketModel> ISMessageDataQueue = new Queue<ISSocketModel>();

        internal SSTickFrame tickFrameUpdater = null;
        internal Queue<SSSocketModel> SSMessageDataQueue = new Queue<SSSocketModel>();

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

        //
        internal void setSSUpdater(SSTickFrame frameUpdater)
        {
            tickFrameUpdater = frameUpdater;
        }

        void Update()
        {
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

            while (SSMessageDataQueue.Count > 0)
            {
                lock (SSMessageDataQueue)
                {
                    SSSocketModel tmpSSMessageData = SSMessageDataQueue.Dequeue();
                    if (tickFrameUpdater != null)
                    {
                        tickFrameUpdater(tmpSSMessageData);
                    }
                }
            }

            
        }


    }

    
}
