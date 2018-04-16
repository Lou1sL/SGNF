package com.ryubai.sgnf.scenarioserver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.socket.SocketChannel;

public class PlayerPool {

	private static Map<String, SocketChannel> map = new ConcurrentHashMap<>();

	public static void addPlayerChannel(String id, SocketChannel gateway_channel) {
		map.put(id, gateway_channel);
	}

	public static Map<String, SocketChannel> getChannels() {
		return map;
	}

	public static SocketChannel getPlayerChannel(String id) {
		return map.get(id);
	}

	public static void removePlayerChannel(String id) {
		map.remove(id);
	}
}
