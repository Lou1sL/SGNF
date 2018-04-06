package com.ryubai.sgnf.infoserver;

import io.netty.channel.ChannelHandlerContext;  
import io.netty.channel.ChannelInboundHandlerAdapter;  
  
public class ServerHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception  //当客户端连上服务器的时候会触发此函数
    {
        System.out.println("client:" + ctx.channel().id() + " join server");
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception//当客户端断开连接的时候触发函数
    {
        System.out.println("client:" + ctx.channel().id() + " leave server");
        //User.onlineUser.remove(LoginDispatch.getInstance().user);
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception//当客户端发送数据到服务器会触发此函数
    {
    	
        SocketModel message = (SocketModel) msg;
        switch (message.getType()) {
        case TypeProtocol.TYPE_LOGIN:
            LoginDispatch.getInstance().dispatch(ctx, message);
            break;
        case TypeProtocol.TYPE_WIZARD:
            //WizardDispatch.getInstance().dispatch(ctx, message);
            break;
        case TypeProtocol.TYPE_USER:
            //UserDispatch.getInstance().dispatch(ctx, message);
            break;
        case TypeProtocol.TYPE_BATTLE:
            //BattleDispatch.getInstance().dispatch(ctx, message);
        default:
            break;
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        cause.printStackTrace();
    }
}