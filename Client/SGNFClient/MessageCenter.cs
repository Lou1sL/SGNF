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

    public class MessageCenter : SingletonMonoBehaviour<MessageCenter>
    {
        private Dictionary<int, ISMessage_Callback_Handler> _netMessage_EventList = new Dictionary<int, ISMessage_Callback_Handler>();
        internal Queue<ISSocketModel> _netMessageDataQueue = new Queue<ISSocketModel>();

        internal SSTickFrame tickFrameUpdater = null;

        //添加网络事件观察者
        internal void addISObserver(int protocalType, ISMessage_Callback_Handler callback)
        {
            if (_netMessage_EventList.ContainsKey(protocalType))
            {
                //知识点
                //无返回值的委托，你给它注册多少个方法，它就执行多少个方法
                //而有返回值的委托，同样注册多少个方法就执行多少个方法，！！！！但返回的是最后一个方法的返回值！！！！
                _netMessage_EventList[protocalType] += callback;
            }
            else
            {
                _netMessage_EventList.Add(protocalType, callback);
            }
        }
        //删除网络事件观察者
        internal void removeISObserver(int protocalType, ISMessage_Callback_Handler callback)
        {
            if (_netMessage_EventList.ContainsKey(protocalType))
            {
                _netMessage_EventList[protocalType] -= callback;
                if (_netMessage_EventList[protocalType] == null)
                {
                    _netMessage_EventList.Remove(protocalType);
                }
            }
        }



        void Update()
        {
            while (_netMessageDataQueue.Count > 0)
            {
                lock (_netMessageDataQueue)
                {
                    ISSocketModel tmpNetMessageData = _netMessageDataQueue.Dequeue();
                    if (_netMessage_EventList.ContainsKey(tmpNetMessageData.Command))
                    {
                        _netMessage_EventList[tmpNetMessageData.Command](tmpNetMessageData);
                    }
                }
            }
        }


    }

    
}
