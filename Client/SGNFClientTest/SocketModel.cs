using System.Collections.Generic;
using ProtoBuf;

[ProtoContract]
public class SocketModel
{
    [ProtoMember(1)]
    public int type;//类型
    [ProtoMember(2)]
    public int area;//区域码
    [ProtoMember(3)]
    public int command;//指令
    [ProtoMember(4)]
    public List<string> message;//消息
}