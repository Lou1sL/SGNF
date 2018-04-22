using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityStandardUtils;

namespace SGNFClient
{
    public class SSRecorder:Singleton<SSRecorder>
    {
        private Dictionary<int, SSSocketModel> recieveDataBuffer = new Dictionary<int, SSSocketModel>();

        internal Dictionary<int, SSSocketModel> RecieveDataBuffer
        {
            get
            {
                lock (recieveDataBuffer)
                {
                    return recieveDataBuffer;
                }
            }
        }

    }
}
