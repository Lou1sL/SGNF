package com.ryubai.sgnf.infoserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelInboundHandlerAdapter.Sharable
public class ISCallHandler extends ChannelInboundHandlerAdapter {
	
	ArrayList<SSInfo> ssinfoList = new ArrayList<SSInfo>();
	Map<String,Channel> PlayerPool = new HashMap<String,Channel>();
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception // 当客户端连上服务器的时候会触发此函数
	{
		PlayerPool.put(ctx.channel().id().asLongText(), ctx.channel());
		clientJoin(ctx.channel().id().asLongText());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception// 当客户端断开连接的时候触发函数
	{
		PlayerPool.remove(ctx.channel().id().asLongText());
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
			int sz = ssinfoList.size();
			ISSocketModel response = new ISSocketModel();
			response.command = SocketUtil.internalCommand.SSINFO.val();
			response.message.add(sz + "");
			
			if (sz > 0) {
				for (int i = 0; i < sz; i++) {
					response.message.add(ssinfoList.get(i).Tag);
					response.message.add(ssinfoList.get(i).IP);
					response.message.add(ssinfoList.get(i).Port+"");
				}
			}
			ctx.writeAndFlush(response);
			
		}else
		{
			recieveMessage(ctx.channel().id().asLongText(),message);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ISOUT.WriteConsole("FUCKED UP");
		cause.printStackTrace();
	}
	
	////
	public void clientJoin(String id) {
		ISOUT.WriteConsole("Client join ID:" + id);
	}

	public void clientDrop(String id) {
		ISOUT.WriteConsole("Client drop ID:" + id);
	}

	public void recieveMessage(String id,ISSocketModel msg) {
		//System.out.println("Client send:" + msg.message.get(0));
	}
	
	
}