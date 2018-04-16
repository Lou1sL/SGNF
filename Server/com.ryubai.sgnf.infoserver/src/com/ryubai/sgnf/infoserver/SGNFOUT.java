package com.ryubai.sgnf.infoserver;

public class SGNFOUT {

	public static void WriteConsole(String ip,String str){
		System.out.println(">IS "+ip+": "+str);
	}
	public static void WriteConsole(String str){
		System.out.println(">IS local: "+str);
	}
}
