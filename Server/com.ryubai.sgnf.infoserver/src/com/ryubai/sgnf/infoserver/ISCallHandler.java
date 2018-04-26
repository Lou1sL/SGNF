package com.ryubai.sgnf.infoserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelInboundHandlerAdapter.Sharable
public class ISCallHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception // 当客户端连上服务器的时候会触发此函数
	{
		clientJoin(ctx.channel().id().asLongText());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception// 当客户端断开连接的时候触发函数
	{
		clientDrop(ctx.channel().id().asLongText());
	}

	@Override
	// 当客户端发送数据到服务器会触发此函数
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		ISSocketModel message = (ISSocketModel)msg;
		if(message.command == SocketUtil.internalCommand.PING.val())
		{
			ctx.writeAndFlush(message);
		}else if(message.command == SocketUtil.internalCommand.SSINFO.val())
		{
			int sz = InfoServer.ssinfoList.size();
			ISSocketModel response = new ISSocketModel();
			response.command = SocketUtil.internalCommand.SSINFO.val();
			response.message.add(sz + "");
			
			if (sz > 0) {
				for (int i = 0; i < sz; i++) {
					response.message.add(InfoServer.ssinfoList.get(i).Tag);
					response.message.add(InfoServer.ssinfoList.get(i).IP);
					response.message.add(InfoServer.ssinfoList.get(i).Port+"");
				}
			}
			ctx.writeAndFlush(response);
			
		}else
		{
			ISSocketModel response = dealMsg(ctx.channel().id().asLongText(),message);
			if (response != null)ctx.writeAndFlush(response);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ISOUT.WriteConsole("FUCKED UP");
		cause.printStackTrace();
	}

	public void clientJoin(String id) {
		ISOUT.WriteConsole("Client join ID:" + id);
	}

	public void clientDrop(String id) {
		ISOUT.WriteConsole("Client drop ID:" + id);
	}

	public ISSocketModel dealMsg(String id,ISSocketModel msg) {
		//System.out.println("Client send:" + msg.message.get(0));
		return msg;
		//return null;
	}
}