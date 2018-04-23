using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace SGNFClient
{
    //不可以Instantiate哦
    //一切以服务器为准哦
    public class NetTransform : MonoBehaviour
    {

        [System.Serializable]
        public class NetInfo
        {
            public int MessagePkgVecPointer = 0;
            //public int Tag = -1;
        }

        [System.Serializable]
        public class NetVec3Set
        {
            public bool X = false;
            public bool Y = false;
            public bool Z = false;
        }

        [System.Serializable]
        public class NetVec3
        {
            public bool AsyncThis = false;

            public bool Smooth = false;
            public NetInfo info;
            public NetVec3Set set;
        }

        public int Command = 0x1001;

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
             MessageCenter.Instance.NetTransformTickCall+=(delegate(SSSocketModel data)
             {
                 if (data.Command != Command) return;

                 if (NetPosition.AsyncThis)
                 {
                     //if (NetPosition.info.Tag == data.Vector[NetPosition.info.MessagePkgVecPointer].Tag)
                     newp = data.Vector[NetPosition.info.MessagePkgVecPointer].ToVector3();
                     oldp = transform.position;
                 }
                 if (NetRotation.AsyncThis)
                 {
                     //if (NetRotation.info.Tag == data.Vector[NetRotation.info.MessagePkgVecPointer].Tag)
                     newr = data.Vector[NetRotation.info.MessagePkgVecPointer].ToVector3();
                     oldr = transform.eulerAngles;
                 }
                 if (NetScale.AsyncThis)
                 {
                     //if (NetScale.info.Tag == data.Vector[NetScale.info.MessagePkgVecPointer].Tag)
                     news = data.Vector[NetScale.info.MessagePkgVecPointer].ToVector3();
                     olds = transform.localScale;
                 }

             });
        }


        Vector3[] buffer = new Vector3[3];

        private void Update()
        {
            if (!Client.IsConnected || !Client.IsJoined) return;


            


            if (NetPosition.AsyncThis)
            {
                buffer[0] = transform.position;

                if (NetPosition.Smooth)
                {
                    if (NetPosition.set.X) buffer[0].x = SGNFUtils.SmoothFloat(oldp.x, newp.x);
                    if (NetPosition.set.Y) buffer[0].y = SGNFUtils.SmoothFloat(oldp.y, newp.y);
                    if (NetPosition.set.Z) buffer[0].z = SGNFUtils.SmoothFloat(oldp.z, newp.z);
                }
                else
                {
                    if (NetPosition.set.X) buffer[0].x = newp.x;
                    if (NetPosition.set.Y) buffer[0].y = newp.y;
                    if (NetPosition.set.Z) buffer[0].z = newp.z;
                }

                transform.position = buffer[0];
            }
            if (NetRotation.AsyncThis)
            {
                buffer[1] = transform.eulerAngles;

                if (NetRotation.Smooth)
                {
                    if (NetRotation.set.X) buffer[1].x = SGNFUtils.SmoothFloat(oldr.x, newr.x);
                    if (NetRotation.set.Y) buffer[1].y = SGNFUtils.SmoothFloat(oldr.y, newr.y);
                    if (NetRotation.set.Z) buffer[1].z = SGNFUtils.SmoothFloat(oldr.z, newr.z);
                }
                else
                {
                    if (NetRotation.set.X) buffer[1].x = newr.x;
                    if (NetRotation.set.Y) buffer[1].y = newr.y;
                    if (NetRotation.set.Z) buffer[1].z = newr.z;
                }
                transform.eulerAngles = buffer[1];
            }
            if (NetScale.AsyncThis)
            {
                buffer[2] = transform.localScale;

                if (NetScale.Smooth)
                {
                    if (NetScale.set.X) buffer[2].x = SGNFUtils.SmoothFloat(olds.x, news.x);
                    if (NetScale.set.Y) buffer[2].y = SGNFUtils.SmoothFloat(olds.y, news.y);
                    if (NetScale.set.Z) buffer[2].z = SGNFUtils.SmoothFloat(olds.z, news.z);
                }
                else
                {
                    if (NetScale.set.X) buffer[2].x = news.x;
                    if (NetScale.set.Y) buffer[2].y = news.y;
                    if (NetScale.set.Z) buffer[2].z = news.z;
                }

                transform.localScale = buffer[2];
            }












        }







    }

}
