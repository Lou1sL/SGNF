package com.ryubai.sgnf.scenarioserver;

public class SSOUT {

	public static void WriteConsole(String ip,String str){
		System.out.println(">SS "+ip+": "+str);
	}
	public static void WriteConsole(String str){
		System.out.println(">SS local: "+str);
	}
}
