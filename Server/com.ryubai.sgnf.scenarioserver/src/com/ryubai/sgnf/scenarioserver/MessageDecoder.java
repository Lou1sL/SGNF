package com.ryubai.sgnf.scenarioserver;

import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MessageDecoder extends ByteToMessageDecoder {
	private Schema<ISSocketModel> schema = RuntimeSchema.getSchema(ISSocketModel.class);// protostuffµÄÐ´·¨

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> obj) throws Exception {

		byte[] data = new byte[in.readableBytes()];
		in.readBytes(data);
		ISSocketModel message = new ISSocketModel();
		ProtobufIOUtil.mergeFrom(data, message, schema);
		obj.add(message);

	}

}