package com.ryubai.sgnf.scenarioserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;


//�����б�Ҫ˵�£������id���صĲ���asShortText����Ϊ���ݹٷ��ĵ���asShortText��non-unique��
//asLongText����
@ChannelInboundHandlerAdapter.Sharable
public class SSCallHandler extends ChannelInboundHandlerAdapter {
	
	private int currentTick = 0;
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception // ���ͻ������Ϸ�������ʱ��ᴥ���˺���
	{
		clientJoin(ctx.channel().id().asLongText());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception// ���ͻ��˶Ͽ����ӵ�ʱ�򴥷�����
	{
		clientDrop(ctx.channel().id().asLongText());
	}

	@Override
	// ���ͻ��˷������ݵ��������ᴥ���˺���
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
			//SSRecorder.RecordRcvData.put(message.currentTick, message);
			tickRcv(ctx.channel().id().asLongText(),message);
		}
	}
	
	
	@Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {  
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
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		SSOUT.WriteConsole("FUCKED UP");
		cause.printStackTrace();
	}

	public void clientJoin(String id) {
		SSOUT.WriteConsole("Client join ID:" + id);
	}

	public void clientDrop(String id) {
		SSOUT.WriteConsole("Client drop ID:" + id);
	}

	public void tickRcv(String id,SSSocketModel msg) {
		return;
	}
	public SSSocketModel tickSend(String id){
		return null;
	}
}