using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace SGNFClient.UnityScript
{
    public class NetClientTransform : MonoBehaviour
    {


        public enum _AsyncType
        {
            //XY,
            //Z,
            XYZ
        }

        [System.Serializable]
        public class NetVec3
        {
            public bool AsyncThis = false;
            
            public int MessagePkgVecPointer = 0;
            public _AsyncType AsyncType = _AsyncType.XYZ;
        }

        public NetVec3 NetPosition = new NetVec3();
        public NetVec3 NetRotation = new NetVec3();
        public NetVec3 NetScale = new NetVec3();

        private void Start()
        {
            MessageCenter.Instance.ClientTransformCall += delegate (ref SSSocketModel data)
            {
                if (NetPosition.AsyncThis)
                {
                    while (data.Vector.Count < NetPosition.MessagePkgVecPointer+1) data.Vector.Add(new Vec());
                    data.Vector[NetPosition.MessagePkgVecPointer] = new Vec(-1,transform.position);
                }
                if (NetRotation.AsyncThis)
                {
                    while (data.Vector.Count < NetRotation.MessagePkgVecPointer+1) data.Vector.Add(new Vec());
                    data.Vector[NetRotation.MessagePkgVecPointer] = new Vec(-1, transform.eulerAngles);
                }
                if (NetScale.AsyncThis)
                {
                    while (data.Vector.Count < NetScale.MessagePkgVecPointer+1) data.Vector.Add(new Vec());
                    data.Vector[NetScale.MessagePkgVecPointer] = new Vec(-1, transform.localScale);
                }


            };
        }


    }
}
