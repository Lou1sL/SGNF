using System.Collections.Generic;
using ProtoBuf;

namespace SGNFClient
{
    [ProtoContract]
    public class SocketModel
    {
        [ProtoMember(1)]
        internal int type;//类型
        [ProtoMember(2)]
        public int area;//区域码
        [ProtoMember(3)]
        public int command;//指令
        [ProtoMember(4)]
        public List<string> message;//消息
    }
}