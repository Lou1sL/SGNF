package com.ryubai.sgnf.scenarioserver;

import com.ryubai.sgnf.scenarioserver.UnityType.Vector3;

public class NetVector {

	public int Tag = -1;
	public float x = 0;
	public float y = 0;
	public float z = 0;

	public NetVector() {
	}

	public NetVector(int tag, float xx, float yy, float zz) {
		Tag = tag;
		x = xx;
		y = yy;
		z = zz;
	}

	public NetVector(int tag, Vector3 v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}

	public Vector3 toVector3() {
		return new Vector3(x, y, z);
	}
}
