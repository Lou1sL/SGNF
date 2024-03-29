package com.ryubai.sgnf.scenarioserver;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<SSSocketModel>{
    private Schema<SSSocketModel> schema = RuntimeSchema.getSchema(SSSocketModel.class);
    @Override
    protected void encode(ChannelHandlerContext ctx, SSSocketModel message,ByteBuf out) throws Exception {
        //System.out.println("encode");
        LinkedBuffer buffer = LinkedBuffer.allocate(1024);
        byte[] data = ProtobufIOUtil.toByteArray(message, schema, buffer);
        ByteBuf buf = Unpooled.copiedBuffer(SocketUtil.intToBytes(data.length),data);//在写消息之前需要把消息的长度添加到投4个字节
        out.writeBytes(buf);
    }
}