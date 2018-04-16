package com.ryubai.sgnf.infoserver;
import java.util.ArrayList;
import java.util.List;

import io.netty.channel.ChannelId;

public class PlayerPool {
	
	
	public List<PlayerInfo> POOL = new ArrayList<PlayerInfo>();
	
	
	
	
	public class PlayerInfo{
		public int PlayerID;
		public int AccessCode;
		public ChannelId ISChannel;
		public ChannelId SSChannel;
	}
	
	
	public void AddPlayer(PlayerInfo p){
		
	}
	
}
