package com.ryubai.sgnf.infoserver;

import io.netty.bootstrap.ServerBootstrap;  
import io.netty.channel.ChannelFuture;   
import io.netty.channel.ChannelInitializer;   
import io.netty.channel.ChannelOption;   
import io.netty.channel.EventLoopGroup;   
import io.netty.channel.nio.NioEventLoopGroup;   
import io.netty.channel.socket.SocketChannel;   
import io.netty.channel.socket.nio.NioServerSocketChannel;  
  
public class Server {
	
	public void bind(int port) throws Exception
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup();//线程组
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();//server启动管理配置
            b.group(bossGroup, workGroup)
            .channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, 1024)//最大客户端连接数为1024
            .childHandler(new ChannelInitializer<SocketChannel>() {
            	@Override
                protected void initChannel(SocketChannel ch) throws Exception {
            		ch.pipeline().addLast(new LengthDecoder(1024,0,4,0,4));
            		ch.pipeline().addLast(new MessageDecoder());
            		ch.pipeline().addLast(new MessageEncoder());
            		ch.pipeline().addLast(new ISCallHandler());
            		}
            	});
            ChannelFuture f = b.bind(port).sync();
            if (f.isSuccess())
            {
                System.out.println("Server starts success at port:" + port);
            }
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
	
	
    public static void main(String[] args) throws Exception
    {
        int port = 9999;
        new Server().bind(port);
    }
}  