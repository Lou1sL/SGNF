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
            public int Tag = -1;
            public _AsyncType AsyncType = _AsyncType.XYZ;
        }

        public int Command = 0x1001;

        public NetVec3 NetPosition = new NetVec3();
        public NetVec3 NetRotation = new NetVec3();
        public NetVec3 NetScale = new NetVec3();

        private void Start()
        {
            MessageCenter.Instance.ClientTransformCall += delegate (ref SSSocketModel data)
            {
                if (data.Command != Command) return;

                if (NetPosition.AsyncThis)
                {
                    data.Vector[NetPosition.MessagePkgVecPointer] = new Vec(NetPosition.Tag,transform.position);
                }
                if (NetRotation.AsyncThis)
                {
                    data.Vector[NetRotation.MessagePkgVecPointer] = new Vec(NetRotation.Tag, transform.eulerAngles);
                }
                if (NetScale.AsyncThis)
                {
                    data.Vector[NetScale.MessagePkgVecPointer] = new Vec(NetScale.Tag, transform.localScale);
                }


            };
        }


    }
}
