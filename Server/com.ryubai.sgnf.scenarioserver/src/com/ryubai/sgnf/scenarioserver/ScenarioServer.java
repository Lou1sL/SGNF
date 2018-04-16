package com.ryubai.sgnf.scenarioserver;

import java.util.ArrayList;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ScenarioServer {


	private int port = 34456;
	private SSFrameUpdater callHandler = new SSFrameUpdater();
	private int maxConn = 1024;
	int tick = 60;
	
	private boolean isRunning = false;
	
	
	public void setFrameUpdater(SSFrameUpdater ch){
		callHandler = ch;
	}
	public void setTick(int t){
		tick = t;
	}
	public void setMaxConn(int c){
		maxConn = c;
	}
	public void setPort(int p){
		port = p;
	}
	
	
	public void start()
    {
        if(!isRunning){
        	isRunning = true;
        	_process();
        }
    }
	
	_processThread th = new _processThread();
	public void startThread(){
		if(!isRunning){
			SGNFOUT.WriteConsole("Starting thread");
        	isRunning = true;
        	th.start();
        }else SGNFOUT.WriteConsole("Server is already running!");
	}
	@SuppressWarnings("deprecation")
	public void shut(){
		if(isRunning){
			SGNFOUT.WriteConsole("Closing");
        	isRunning = false;
        	th.stop();
        }else SGNFOUT.WriteConsole("Server is not running!");
	}
	
	
	
	public class _processThread extends Thread{
		@Override
        public void run() {
			_process();
		}
	}
	private void _process(){
		EventLoopGroup bossGroup = new NioEventLoopGroup();//�߳���
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();//server������������
            b.group(bossGroup, workGroup)
            .channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, maxConn)//���ͻ���������
            .option(ChannelOption.SO_REUSEADDR,true)
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
            	SGNFOUT.WriteConsole("Server starts success at port:" + port);
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
