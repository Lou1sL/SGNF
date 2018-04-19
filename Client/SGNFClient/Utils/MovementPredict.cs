using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace SGNFClient.Utils
{
    public static class MovementPredict
    {
        public static Vector3 Predictor(Vector3 local, Vector3 newone, float dt)
        {
            if (!Client.IsConnected || !Client.IsJoined) return local;

            if (dt >= 1.0f / Client.Tick)
            {
                local = newone;
                return newone;
            }

            Vector3 dist = newone - local;
            return local + (dt/(1.0f / Client.Tick)) * dist;
        }

    }
}
