using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace SGNFClient.Utils
{
    public static class MovementPredict
    {

        public static Vector3 Predictor(Vector3 local, Vector3 newone)
        {
            if (!Client.IsConnected || !Client.IsJoined) return local;

            //服务器刷新频率与帧率相等或更低就不用平滑了
            if (MessageCenter.Instance.delatT >= 1.0f / Client.Tick)
            {
                local = newone;
                return newone;
            }

            Vector3 dist = newone - local;
            return local + (MessageCenter.Instance.delatT / (1.0f / Client.Tick)) * dist;
        }

    }
}
