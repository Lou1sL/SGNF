package com.ryubai.sgnfexample;
import java.util.ArrayList;
import java.util.Scanner;

import com.ryubai.sgnf.infoserver.*;
import com.ryubai.sgnf.scenarioserver.SSCallHandler;
import com.ryubai.sgnf.scenarioserver.SSSocketModel;
import com.ryubai.sgnf.scenarioserver.ScenarioServer;

import io.netty.channel.ChannelId;


public class ServerExample {

	//ģ���¼��ָ��
	private static int COMMAND_TEST_LOGIN = 0x1000;
	//ģ���¼������
	private static class USERIDENTITY{
		public static String Name = "MyName";
		public static String Pass = "12345678";
	}
	
	//��IS���õ�SS��Ϣ
	@SuppressWarnings("serial")
	private static ArrayList<SSInfo> ssinfo = new ArrayList<SSInfo>() {
		{
			add(new SSInfo("ss0", "192.68.1.102", 9876));
		}
	};

	public static void main(String[] args) throws Exception{
		
		
		//����һ�����Ϸ�����
		InfoServer is = new InfoServer();
		is.setSSInfo(ssinfo);
		is.setCallHandler(new ISCallHandler(){
			
			//�������������ҷ���������
			@Override
			public ISSocketModel dealMsg(ChannelId id, ISSocketModel message){
				ISSocketModel response = new ISSocketModel();
				response.message.add("");
				if(message.command == COMMAND_TEST_LOGIN){
					response.command = COMMAND_TEST_LOGIN;
					if(message.message.get(0).equals(ServerExample.USERIDENTITY.Name) && 
							message.message.get(1).equals(ServerExample.USERIDENTITY.Pass)){
						response.message.set(0, "OK!");
					}
					else response.message.set(0, "NOPE!");
					return response;
				}else return null;
			}
			
			//�������
			@Override
			public void clientJoin(ChannelId id) {
				SGNFOUT.WriteConsole("Client join ID:" + id);
			}
			//��ҶϿ�
			@Override
			public void clientDrop(ChannelId id) {
				SGNFOUT.WriteConsole("Client join ID:" + id);
			}
		});
		is.setMaxConn(1024);
		is.setPort(9999);
		//�����start()��������ͬ��ִ�У��ǳ���Ͷ����ˡ�����
		is.startThread();
		
		
		
		
		
		
		
		
		
		
		
		
		//����һ������ͬ��������
		ScenarioServer ss = new ScenarioServer();
		ss.setCallHandler(new SSCallHandler(){
			@Override
			public SSSocketModel dealMsg(SSSocketModel message){
				SSSocketModel response = new SSSocketModel();
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
		ss.setMaxConn(1024);
		ss.setTick(30);
		ss.setPort(9876);
		ss.startThread();
		
		
		
		
		
		
		
		
		
		//�������룬����һЩ����˼�Ĳ���
		Scanner scanner = new Scanner(System.in);
		while(true)
		{
			String str = scanner.nextLine();  
			if(str.equals("q")){
				is.shut();
				//ss.shut();
				break;
			}else{
				//���������޸�������
				ServerExample.USERIDENTITY.Pass = str;
			}
		}
		scanner.close();
	}

}
