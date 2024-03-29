package com.ryubai.sgnf.infoserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;   
import io.netty.channel.ChannelInitializer;   
import io.netty.channel.ChannelOption;   
import io.netty.channel.EventLoopGroup;   
import io.netty.channel.nio.NioEventLoopGroup;   
import io.netty.channel.socket.SocketChannel;   
import io.netty.channel.socket.nio.NioServerSocketChannel;  
  
public class InfoServer {
	
	private int port = 23345;
	private ArrayList<SSInfo> ssinfoList = new ArrayList<SSInfo>();
	private ISCallHandler callHandler = new ISCallHandler();
	private int maxConn = 1024;
	
	private boolean isRunning = false;
	
	
	public void setSSInfo(ArrayList<SSInfo> info){
		ssinfoList = info;
	}
	public void setCallHandler(ISCallHandler ch){
		callHandler = ch;
	}
	public void setMaxConn(int c){
		maxConn = c;
	}
	public void setPort(int p){
		port = p;
	}
	
	public void sendMessage(String id,ISSocketModel msg){
		if(callHandler.PlayerPool.containsKey(id) && msg!=null)callHandler.PlayerPool.get(id).writeAndFlush(msg);
		else ISOUT.WriteConsole("Unable to sendMessage(),id not exist");
	}
	
	public void start()
    {
        if(!isRunning){
        	isRunning = true;
        	_process();
        }
    }
	
	_processThread th;
	public void startThread(){
		if(!isRunning){
			ISOUT.WriteConsole("Starting thread");
        	isRunning = true;
        	th = new _processThread();
        	th.start();
        }else ISOUT.WriteConsole("Server is already running!");
	}
	@SuppressWarnings("deprecation")
	public void shut(){
		if(isRunning){
			ISOUT.WriteConsole("Closing");
        	isRunning = false;
        	th.stop();
        }else ISOUT.WriteConsole("Server is not running!");
	}
	
	
	
	public class _processThread extends Thread{
		@Override
        public void run() {
			_process();
		}
	}
	private void _process(){
		
		callHandler.ssinfoList = ssinfoList;
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();//线程组
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();//server启动管理配置
            b.group(bossGroup, workGroup)
            .channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, maxConn)//最大客户端连接数
            .option(ChannelOption.SO_REUSEADDR,true)
            //.option(ChannelOption.TCP_NODELAY,true)
            //.option(ChannelOption.SO_REUSEPORT,true)
            .childHandler(new ChannelInitializer<SocketChannel>() {
            	@Override
                protected void initChannel(SocketChannel ch) throws Exception {
            		ch.pipeline().addLast(new LengthDecoder(1024,0,4,0,4));
            		ch.pipeline().addLast(new MessageDecoder());
            		ch.pipeline().addLast(new MessageEncoder());
            		ch.pipeline().addLast(callHandler);
            		}
            	});
            ChannelFuture f = b.bind(port).sync();
            if (f.isSuccess())
            {
            	ISOUT.WriteConsole("Server starts success at port:" + port);
            }
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
	}
}  