package com.ryubai.sgnf.scenarioserver;

import java.util.HashMap;
import java.util.Map;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;


//�����б�Ҫ˵�£������id���صĲ���asShortText����Ϊ���ݹٷ��ĵ���asShortText��non-unique��
//asLongText����
@ChannelInboundHandlerAdapter.Sharable
public abstract class SSCallHandler extends ChannelInboundHandlerAdapter {
	
	private int currentTick = 0;
	Map<String,Channel> PlayerPool = new HashMap<String,Channel>();
	
	@Override
	public final void channelActive(ChannelHandlerContext ctx) throws Exception // ���ͻ������Ϸ�������ʱ��ᴥ���˺���
	{
		PlayerPool.put(ctx.channel().id().asLongText(), ctx.channel());
		clientJoin(ctx.channel().id().asLongText());
	}
	@Override
	public final void channelInactive(ChannelHandlerContext ctx) throws Exception// ���ͻ��˶Ͽ����ӵ�ʱ�򴥷�����
	{
		PlayerPool.remove(ctx.channel().id().asLongText());
		clientDrop(ctx.channel().id().asLongText());
	}
	@Override
	// ���ͻ��˷������ݵ��������ᴥ���˺���
	public final void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
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
			//SSRecorder.RecordRcvData.put(message.currentTick, message);
			tickRcv(ctx.channel().id().asLongText(),message);
		}
	}
	@Override
    public final void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {  
        if (evt instanceof IdleStateEvent) {
        	
        	SSSocketModel snd = tickSend(ctx.channel().id().asLongText());
        	snd.currentTick = currentTick;
        	currentTick++;
        	//SSRecorder.RecordSendData.put(currentTick, snd);
            ctx.writeAndFlush(snd);
            
        }else{
        	super.userEventTriggered(ctx, evt); 
        }
    }  
	@Override
	public final void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		SSOUT.WriteConsole("FUCKED UP");
		cause.printStackTrace();
	}

	public abstract void clientJoin(String id);
	public abstract void clientDrop(String id) ;
	public abstract void tickRcv(String id,SSSocketModel msg) ;
	public abstract SSSocketModel tickSend(String id);
}