using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityStandardUtils;

namespace SGNFClient
{
    public delegate void Callback_SSMessage_Handle(SSSocketModel data);

    public class SSMessageCenter : SingletonMonoBehaviour<SSMessageCenter>
    {
        private Dictionary<int, Callback_SSMessage_Handle> _netMessage_EventList = new Dictionary<int, Callback_SSMessage_Handle>();
        internal Queue<SSSocketModel> _netMessageDataQueue = new Queue<SSSocketModel>();

        //添加网络事件观察者
        internal void addObserver(int protocalType, Callback_SSMessage_Handle callback)
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
        internal void removeObserver(int protocalType, Callback_SSMessage_Handle callback)
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
                    SSSocketModel tmpNetMessageData = _netMessageDataQueue.Dequeue();
                    if (_netMessage_EventList.ContainsKey(tmpNetMessageData.Command))
                    {
                        _netMessage_EventList[tmpNetMessageData.Command](tmpNetMessageData);
                    }
                }
            }
        }


    }

    
}
