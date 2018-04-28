using SGNFClient.UnityScript;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace SGNFClient
{
    public static class SGNFUtils
    {
        //插帧法
        public static Vector3 SmoothVec(Vector3 local, Vector3 newone)
        {
            if (!ISSocketManager.Instance.IsConnceted || !SSSocketManager.Instance.IsConnceted) return local;
            //服务器刷新频率与帧率相等或更低就不用平滑了
            if (MessageCenter.Instance.delatT >= 1.0f / NetManager.Instance.Tick)return newone;
            Vector3 dist = new Vector3();
            dist = newone - local;
            return local + (MessageCenter.Instance.timeFromLastTick / (1.0f / NetManager.Instance.Tick)) * dist;
        }

        //预测法（一卡一卡的先不用）
        public static Vector3 SmoothPredict(Vector3 position, Vector3 speed)
        {
            if (speed == Vector3.zero) return position;
            return position + speed * MessageCenter.Instance.timeFromLastTick;
        }


    }
}
