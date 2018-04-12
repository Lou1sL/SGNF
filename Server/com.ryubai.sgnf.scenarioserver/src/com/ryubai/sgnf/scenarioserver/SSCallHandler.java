package com.ryubai.sgnf.scenarioserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelInboundHandlerAdapter.Sharable
public class SSCallHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception // 当客户端连上服务器的时候会触发此函数
	{
		clientJoin(ctx.channel().id());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception// 当客户端断开连接的时候触发函数
	{
		clientDrop(ctx.channel().id());
	}

	@Override
	// 当客户端发送数据到服务器会触发此函数
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		SSSocketModel message = (SSSocketModel)msg;
		if(message.command == SocketUtil.internalCommand.PING.val())
		{
			ctx.writeAndFlush(message);
		}else
		{
			SSSocketModel response = dealMsg(message);
			if (response != null)ctx.writeAndFlush(response);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		SGNFOUT.WriteConsole("FUCKED UP");
		cause.printStackTrace();
	}

	public void clientJoin(ChannelId id) {
		SGNFOUT.WriteConsole("Client join ID:" + id);
	}

	public void clientDrop(ChannelId id) {
		SGNFOUT.WriteConsole("Client drop ID:" + id);
	}

	public SSSocketModel dealMsg(SSSocketModel msg) {
		//System.out.println("Client send:" + msg.message.get(0));
		return msg;
		//return null;
	}
}