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

        public static Vector3 SmoothVec(Vector3 local, Vector3 newone)
        {
            if (!ISSocketManager.Instance.IsConnceted || !SSSocketManager.Instance.IsConnceted) return local;
            //服务器刷新频率与帧率相等或更低就不用平滑了
            if (MessageCenter.Instance.delatT >= 1.0f / NetManager.Instance.Tick)return newone;
            Vector3 dist = new Vector3();
            dist = newone - local;
            return local + (MessageCenter.Instance.timeFromLastTick / (1.0f / NetManager.Instance.Tick)) * dist;
        }




        internal static float SmoothFloat(float local, float newone)
        {
            if (MessageCenter.Instance.delatT >= 1.0f / NetManager.Instance.Tick) return newone;
            float dist = newone - local;
            return local + (MessageCenter.Instance.timeFromLastTick / (1.0f / NetManager.Instance.Tick)) * dist;
        }


    }
}
