package com.ryubai.sgnf.infoserver;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<SocketModel>{
    private Schema<SocketModel> schema = RuntimeSchema.getSchema(SocketModel.class);
    @Override
    protected void encode(ChannelHandlerContext ctx, SocketModel message,
            ByteBuf out) throws Exception {
        //System.out.println("encode");
        LinkedBuffer buffer = LinkedBuffer.allocate(1024);
        byte[] data = ProtobufIOUtil.toByteArray(message, schema, buffer);
        ByteBuf buf = Unpooled.copiedBuffer(CoderUtil.intToBytes(data.length),data);//在写消息之前需要把消息的长度添加到投4个字节
        out.writeBytes(buf);
    }
}