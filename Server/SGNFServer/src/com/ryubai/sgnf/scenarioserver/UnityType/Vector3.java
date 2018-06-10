package com.ryubai.sgnf.scenarioserver.UnityType;

public class Vector3 {


	public static final Vector3 zero = new Vector3(0,0,0);
	public static final Vector3 one = new Vector3(1,1,1);
	
	public static final Vector3 right = new Vector3(1,0,0);
	public static final Vector3 left = new Vector3(-1,0,0);
	public static final Vector3 up = new Vector3(0,1,0);
	public static final Vector3 down = new Vector3(0,-1,0);
	public static final Vector3 forward = new Vector3(0,0,1);
	public static final Vector3 back = new Vector3(0,0,-1);
	
	public static final float kEpsilon = 1e-005f;
	
	
	public float x = 0;
	public float y = 0;
	public float z = 0;
	
	
	public Vector3() {}
	public Vector3(float xx, float yy, float zz) {
		x = xx;
		y = yy;
		z = zz;
	}
	
	
	public static Vector3 add(Vector3 v1,Vector3 v2){
		return new Vector3(v1.x+v2.x,v1.y+v2.y,v1.z+v2.z);
	}
	public static Vector3 sub(Vector3 v1,Vector3 v2){
		return new Vector3(v1.x-v2.x,v1.y-v2.y,v1.z-v2.z);
	}
	public static float dot(Vector3 v1,Vector3 v2){
		return v1.x*v2.x+v1.y*v2.y+v1.z*v2.z;
	}
	public static Vector3 multi(Vector3 v,float f){
		return new Vector3(f*v.x,f*v.y,f*v.z);
	}
	public static Vector3 cross(Vector3 v1,Vector3 v2){
		return new Vector3(v1.y*v2.z-v1.z*v2.y,v1.z*v2.x-v1.x*v2.z,v1.x*v2.y-v1.y*v2.x);
	}
	public static double norm(Vector3 v1){
		return Math.sqrt(Math.pow((v1.x), 2)+Math.pow((v1.y), 2)+Math.pow((v1.z), 2));
	}
	public static double distance(Vector3 v1,Vector3 v2){
		return Math.sqrt(Math.pow((v1.x-v2.x), 2)+Math.pow((v1.y-v2.y), 2)+Math.pow((v1.z-v2.z), 2));
	}
	public static double angleCos(Vector3 v1,Vector3 v2){
		return Vector3.dot(v1, v2)/(Vector3.norm(v1)*Vector3.norm(v2));
	}
	
	@Override
	public String toString(){
		return "X:"+x+" Y:"+y+" Z:"+z;
	}
}
