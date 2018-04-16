package com.ryubai.sgnf.scenarioserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ScenarioServer {

	private SSCallHandler callHandler = new SSCallHandler();
	private TickSender tickSender = new TickSender();
	
	int tick = 60;
	private int maxConn = 1024;
	private int port = 34456;

	private boolean isRunning = false;

	public void setCallHandler(SSCallHandler ch) {
		callHandler = ch;
	}
	public void setTickSender(TickSender ts){
		tickSender = ts;
	}

	public void setTick(int t) {
		tick = t;
	}

	public void setMaxConn(int c) {
		maxConn = c;
	}

	public void setPort(int p) {
		port = p;
	}

	public void start() {
		if (!isRunning) {
			isRunning = true;
			_process();
			sth.start();
		}
	}

	_processThread th = new _processThread();
	_sssendThread sth = new _sssendThread();
	
	public void startThread() {
		if (!isRunning) {
			SSOUT.WriteConsole("Starting thread");
			isRunning = true;
			th.start();
			sth.start();
		} else
			SSOUT.WriteConsole("Server is already running!");
	}

	@SuppressWarnings("deprecation")
	public void shut() {
		if (isRunning) {
			SSOUT.WriteConsole("Closing");
			isRunning = false;
			th.stop();
			sth.stop();
		} else
			SSOUT.WriteConsole("Server is not running!");
	}
	
	public class _processThread extends Thread {
		@Override
		public void run() {
			_process();
		}
	}

	private void _process() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();// 线程组
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();// server启动管理配置
			b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, maxConn)// 最大客户端连接数
					.option(ChannelOption.SO_REUSEADDR, true)
					// .option(ChannelOption.SO_REUSEPORT,true)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new LengthDecoder(1024, 0, 4, 0, 4));
							ch.pipeline().addLast(new MessageDecoder());
							ch.pipeline().addLast(new MessageEncoder());
							ch.pipeline().addLast(callHandler);
						}
					});
			ChannelFuture f = b.bind(port).sync();
			if (f.isSuccess()) {
				SSOUT.WriteConsole("Server starts success at port:" + port);
			}
			f.channel().closeFuture().sync();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}

	public class _sssendThread extends Thread {
		@Override
		public void run() {
			sendTaskLoop: for (;;) {
				//System.out.println("task is beginning...");
				try {
					Map<String, Channel> map = PlayerPool.getChannels();
					Iterator<String> it = map.keySet().iterator();
					while (it.hasNext()) {
						String key = it.next();
						Channel obj = map.get(key);
						//SSOUT.WriteConsole("SENDING"+key);
						if(obj.isActive())obj.writeAndFlush(tickSender.dealSend(key));						
						else PlayerPool.removePlayerChannel(key);
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(1000 / tick);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};
}
