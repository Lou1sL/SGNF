package com.ryubai.sgnf.scenarioserver;

import java.util.ArrayList;
import java.util.List;

public class SSSocketModel {
	
	public int command;
	
	//按int32类型的最大值来说，以每秒60tick的速度，最大可以支持到一局游戏414天
	//对战类游戏完全足矣，开放世界类则要小心了
	//不过就算开放世界类也不可能服务器连开414天吧？
	//2333
	public int currentTick;
	public List<Integer> message = new ArrayList<Integer>();
	public List<NetVector> vector = new ArrayList<NetVector>();
	
	public SSSocketModel() {

	}

	public SSSocketModel(SSSocketModel bla) {
		command = bla.command;
		currentTick = bla.currentTick;
		message = bla.message;
		vector = bla.vector;
	}
}