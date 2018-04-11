package com.ryubai.sgnfexample;
import java.util.ArrayList;
import java.util.Scanner;

import com.ryubai.sgnf.infoserver.*;

public class ServerExample {

	private static int COMMAND_TEST_LOGIN = 0x1000;
	
	@SuppressWarnings("serial")
	private static ArrayList<SSInfo> ssinfo = new ArrayList<SSInfo>() {
		{
			add(new SSInfo("ss0", "localhost", 9876));
		}
	};

	public static void main(String[] args) throws Exception{

		InfoServer is = new InfoServer();
		is.setSSInfo(ssinfo);
		is.setCallHandler(new ISCallHandler(){
			@Override
			public ISSocketModel dealMsg(ISSocketModel message){
				ISSocketModel response = new ISSocketModel();
				response.message.add("");
				if(message.command == COMMAND_TEST_LOGIN){
					response.command = COMMAND_TEST_LOGIN;
					if(message.message.get(0).equals("MyName") && message.message.get(1).equals("12345678")){
						response.message.set(0, "OK!");
					}
					else response.message.set(0, "NOPE!");
					return response;
				}else return null;
				
			}
		});
		is.setMaxConn(1024);
		is.setPort(9999);
		is.startThread();
		
		Scanner scanner = new Scanner(System.in);
		while(true)
		{
			String str = scanner.nextLine();  
			if(str.equals("q")){
				is.shut();
				break;
			}
		}
		scanner.close();
	}

}
