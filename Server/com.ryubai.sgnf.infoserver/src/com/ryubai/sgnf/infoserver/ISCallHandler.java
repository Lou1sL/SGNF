package com.ryubai.sgnf.infoserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ISCallHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception // ���ͻ������Ϸ�������ʱ��ᴥ���˺���
	{
		clientJoin(ctx.channel().id());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception// ���ͻ��˶Ͽ����ӵ�ʱ�򴥷�����
	{
		clientDrop(ctx.channel().id());
	}

	@Override
	// ���ͻ��˷������ݵ��������ᴥ���˺���
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ISSocketModel response = dealMsg((ISSocketModel)msg);
		if (response != null)
			ctx.writeAndFlush(response);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("FUCKED UP");
		cause.printStackTrace();
	}

	public void clientJoin(ChannelId id) {
		System.out.println("Client join ID:" + id);
	}

	public void clientDrop(ChannelId id) {
		System.out.println("Client drop ID:" + id);
	}

	public ISSocketModel dealMsg(ISSocketModel msg) {
		System.out.println("Client send:" + msg.message.get(0));
		return msg;
		//return null;
	}
}