package com.ryubai.sgnf.dbmanager;

public class DBOUT {
	public static void WriteConsole(String str){
		System.out.println(">DB: "+str);
	}
	public static void WriteErr(String str){
		System.err.println(">DB ERROR: "+ str);
	}
}
