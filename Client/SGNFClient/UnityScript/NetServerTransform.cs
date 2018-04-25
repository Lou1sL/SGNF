using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace SGNFClient.UnityScript
{
    //不可以Instantiate哦
    //一切以服务器为准哦
    public class NetServerTransform : MonoBehaviour
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

            public bool Smooth = false;
            public int MessagePkgVecPointer = 0;
            public _AsyncType AsyncType = _AsyncType.XYZ;
        }
        
        public NetVec3 NetPosition = new NetVec3();
        public NetVec3 NetRotation = new NetVec3();
        public NetVec3 NetScale = new NetVec3();




        private Vector3 newp = new Vector3();
        private Vector3 newr = new Vector3();
        private Vector3 news = new Vector3();

        private Vector3 oldp = new Vector3();
        private Vector3 oldr = new Vector3();
        private Vector3 olds = new Vector3();

        private void Start()
        {
             MessageCenter.Instance.ServerTransformCall+=(delegate(SSSocketModel data)
             {

                 if (NetPosition.AsyncThis)
                 {
                     newp = data.Vector[NetPosition.MessagePkgVecPointer].ToVector3();
                     oldp = transform.position;
                 }
                 if (NetRotation.AsyncThis)
                 {
                     newr = data.Vector[NetRotation.MessagePkgVecPointer].ToVector3();
                     oldr = transform.eulerAngles;
                 }
                 if (NetScale.AsyncThis)
                 {
                     news = data.Vector[NetScale.MessagePkgVecPointer].ToVector3();
                     olds = transform.localScale;
                 }

             });
        }

        private void Update()
        {
            if (!ISSocketManager.Instance.IsConnceted || !SSSocketManager.Instance.IsConnceted) return;

            

            if (NetPosition.AsyncThis)
            {
                if (NetPosition.Smooth)
                {
                    if (NetPosition.AsyncType == _AsyncType.XYZ)
                    {
                        transform.position = SGNFUtils.SmoothVec(oldp, newp);
                    }
                }
                else
                {
                    if (NetPosition.AsyncType == _AsyncType.XYZ)
                    {
                        transform.position = newp;
                    }
                }
            }
            if (NetRotation.AsyncThis)
            {
                if (NetRotation.Smooth)
                {
                    if (NetRotation.AsyncType == _AsyncType.XYZ)
                    {
                        transform.eulerAngles = SGNFUtils.SmoothVec(oldr, newr);
                    }
                }
                else
                {
                    if (NetRotation.AsyncType == _AsyncType.XYZ)
                    {
                        transform.eulerAngles = newr;
                    }
                }

            }
            if (NetScale.AsyncThis)
            {
                if (NetScale.Smooth)
                {
                    if (NetScale.AsyncType == _AsyncType.XYZ)
                    {
                        transform.localScale = SGNFUtils.SmoothVec(olds, news);
                    }
                }
                else
                {
                    if (NetScale.AsyncType == _AsyncType.XYZ)
                    {
                        transform.localScale = news;
                    }
                }
            }












        }







    }

}
