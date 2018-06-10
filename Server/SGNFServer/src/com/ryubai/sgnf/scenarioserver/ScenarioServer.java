package com.ryubai.sgnf.scenarioserver;

import java.util.concurrent.TimeUnit;

import com.ryubai.sgnf.infoserver.InfoServer._processThread;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class ScenarioServer {

	private SSCallHandler callHandler = new SSCallHandler();

	
	
	static int tick = 60;
	private int maxConn = 1024;
	private int port = 34456;

	private boolean isRunning = false;

	public void setCallHandler(SSCallHandler ch) {
		callHandler = ch;
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
		}
	}

	_processThread th;

	public void startThread() {
		if (!isRunning) {
			SSOUT.WriteConsole("Starting thread");
			isRunning = true;
			th = new _processThread();
			th.start();
		} else
			SSOUT.WriteConsole("Server is already running!");
	}

	@SuppressWarnings("deprecation")
	public void shut() {
		if (isRunning) {
			SSOUT.WriteConsole("Closing");
			isRunning = false;
			th.stop();
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
							ch.pipeline().addLast(new IdleStateHandler(0, 0, 1000 / tick, TimeUnit.MILLISECONDS));
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
}
