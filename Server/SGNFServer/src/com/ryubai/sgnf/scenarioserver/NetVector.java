package com.ryubai.sgnf.scenarioserver;

public class NetVector {

	public int Tag = -1;
	public float x = 0;
	public float y = 0;
	public float z = 0;

	public NetVector() {
	}

	public NetVector(int tag ,float xx, float yy, float zz) {
		Tag = tag;
		x = xx;
		y = yy;
		z = zz;
	}
}
