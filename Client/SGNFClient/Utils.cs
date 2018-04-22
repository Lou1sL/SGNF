using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace SGNFClient
{
    public static class SGNFUtils
    {

        public static Vector3 SmoothVec(Vector3 local, Vector3 newone)//int newVecPointer)//,bool predict)
        {
            if (!Client.IsConnected || !Client.IsJoined) return local;

            //SSSocketModel sm = new SSSocketModel();
            //if (!SSRecorder.Instance.RecieveDataBuffer.TryGetValue(MessageCenter.Instance.currentTick, out sm)) return local;

            //Vector3 newone = sm.Vector[newVecPointer].ToVector3();

            //SSSocketModel smpre = new SSSocketModel();
            //if (!SSRecorder.Instance.RecieveDataBuffer.TryGetValue(MessageCenter.Instance.currentTick-1, out smpre)) return local;

            //Vector3 preone = smpre.Vector[newVecPointer].ToVector3();

            //服务器刷新频率与帧率相等或更低就不用平滑了
            if (MessageCenter.Instance.delatT >= 1.0f / Client.Tick)return newone;

            
            Vector3 dist = new Vector3();

            //if (predict)
            //{
            //    Vector3 predictnewone = (newone - preone) * (Client.Latency / (1.0f / Client.Tick)) + newone;
            //    dist = predictnewone - local;
            //}
            //else
            //{
                dist = newone - local;
            //}


            return local + (MessageCenter.Instance.timeFromLastTick / (1.0f / Client.Tick)) * dist;
            



        }
    }
}
