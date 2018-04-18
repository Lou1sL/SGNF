package com.ryubai.sgnf.scenarioserver;

import java.util.ArrayList;
import java.util.List;

public class SSSocketModel {
	public int command;
	public int currentTick;
	public List<Integer> message = new ArrayList<Integer>();
	public List<Vector> vector = new ArrayList<Vector>();
	
	public SSSocketModel() {

	}

	public SSSocketModel(SSSocketModel bla) {
		command = bla.command;
		currentTick = bla.currentTick;
		message = bla.message;
		vector = bla.vector;
	}
}