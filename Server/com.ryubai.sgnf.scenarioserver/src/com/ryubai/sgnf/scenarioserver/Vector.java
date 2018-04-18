package com.ryubai.sgnf.scenarioserver;

public class Vector {

	public int Tag = -1;
	public float x = 0;
	public float y = 0;
	public float z = 0;

	public Vector() {
	}

	public Vector(int tag ,float xx, float yy, float zz) {
		Tag = tag;
		x = xx;
		y = yy;
		z = zz;
	}
}
