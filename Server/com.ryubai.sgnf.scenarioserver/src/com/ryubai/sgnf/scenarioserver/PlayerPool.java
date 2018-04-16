package com.ryubai.sgnf.scenarioserver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;

public class PlayerPool {

	private static Map<String, Channel> map = new ConcurrentHashMap<>();

	public static void addPlayerChannel(String id, Channel gateway_channel) {
		map.put(id, gateway_channel);
	}

	public static Map<String, Channel> getChannels() {
		return map;
	}

	public static Channel getPlayerChannel(String id) {
		return map.get(id);
	}

	public static void removePlayerChannel(String id) {
		map.remove(id);
	}
}
