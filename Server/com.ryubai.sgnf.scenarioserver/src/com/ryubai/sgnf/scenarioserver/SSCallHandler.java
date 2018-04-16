package com.ryubai.sgnf.scenarioserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

@ChannelInboundHandlerAdapter.Sharable
public class SSCallHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception // ���ͻ������Ϸ�������ʱ��ᴥ���˺���
	{
		String uuid = ctx.channel().id().asLongText();
        PlayerPool.addPlayerChannel(uuid, (SocketChannel)ctx.channel());
		clientJoin(ctx.channel().id());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception// ���ͻ��˶Ͽ����ӵ�ʱ�򴥷�����
	{
		String uuid = ctx.channel().id().asLongText();
        PlayerPool.removePlayerChannel(uuid);
		clientDrop(ctx.channel().id());
	}

	@Override
	// ���ͻ��˷������ݵ��������ᴥ���˺���
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
		SSOUT.WriteConsole("FUCKED UP");
		cause.printStackTrace();
	}

	public void clientJoin(ChannelId id) {
		SSOUT.WriteConsole("Client join ID:" + id);
	}

	public void clientDrop(ChannelId id) {
		SSOUT.WriteConsole("Client drop ID:" + id);
	}

	public SSSocketModel dealMsg(SSSocketModel msg) {
		//System.out.println("Client send:" + msg.message.get(0));
		return msg;
		//return null;
	}
}