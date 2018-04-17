package com.ryubai.sgnf.scenarioserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

@ChannelInboundHandlerAdapter.Sharable
public class SSCallHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception // 当客户端连上服务器的时候会触发此函数
	{
		String uuid = ctx.channel().id().asLongText();
        PlayerPool.addPlayerChannel(uuid, ctx.channel());
		clientJoin(ctx.channel().id());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception// 当客户端断开连接的时候触发函数
	{
		String uuid = ctx.channel().id().asLongText();
        PlayerPool.removePlayerChannel(uuid);
		clientDrop(ctx.channel().id());
	}

	@Override
	// 当客户端发送数据到服务器会触发此函数
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		SSSocketModel message = (SSSocketModel)msg;
		if(message.command == SocketUtil.internalCommand.PING.val())
		{
			ctx.writeAndFlush(message);
		}else if(message.command == SocketUtil.internalCommand.TICK.val())
		{
			SSSocketModel response = new SSSocketModel();
			response.command = message.command;
			response.message.add(ScenarioServer.tick);
			ctx.writeAndFlush(response);
		}else
		{
			SSSocketModel response = dealMsg(ctx.channel().id(),message);
			if (response != null)ctx.writeAndFlush(response);
		}
	}
	
	
	@Override  
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {  
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;  
            if (event.state() == IdleState.ALL_IDLE)  
                ctx.channel().write(tickSend(ctx.channel().id()));
        }
        super.userEventTriggered(ctx, evt); 
    }  

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		SSOUT.WriteConsole("FUCKED UP");
		cause.printStackTrace();
	}

	public void clientJoin(ChannelId id) {
		SSOUT.WriteConsole("Client join ID:" + id);
	}

	public void clientDrop(ChannelId id) {
		SSOUT.WriteConsole("Client drop ID:" + id);
	}

	public SSSocketModel dealMsg(ChannelId id,SSSocketModel msg) {
		//System.out.println("Client send:" + msg.message.get(0));
		return null;
		//return null;
	}
	
	
	public SSSocketModel tickSend(ChannelId id){
		return null;
	}
}