package com.ryubai.sgnfexample;
import java.util.ArrayList;
import java.util.Scanner;

import com.ryubai.sgnf.infoserver.*;
import com.ryubai.sgnf.scenarioserver.SSCallHandler;
import com.ryubai.sgnf.scenarioserver.SSOUT;
import com.ryubai.sgnf.scenarioserver.SSSocketModel;
import com.ryubai.sgnf.scenarioserver.SSSocketModel.Vec;
import com.ryubai.sgnf.scenarioserver.ScenarioServer;
import com.ryubai.sgnf.scenarioserver.TickSender;

import io.netty.channel.ChannelId;


public class ServerExample {

	
	//----------------------------------------------------------IS
	
	//模拟登录的指令
	private static int COMMAND_TEST_LOGIN = 0x1000;
	//模拟登录的数据
	private static class USERIDENTITY{
		public static String Name = "MyName";
		public static String Pass = "12345678";
	}
	
	//给IS配置的SS信息
		@SuppressWarnings("serial")
		private static ArrayList<SSInfo> ssinfo = new ArrayList<SSInfo>() {
			{
				add(new SSInfo("ss0", "127.0.0.1", 9876));
			}
		};
	
	
	//----------------------------------------------------------SS
	
	//模拟更新玩家位置的指令
	private static int COMMAND_UPDATE_PLAYER = 0x1001;
	//模拟的战场
	private static class BattleFiled {
		public static SSSocketModel.Vec playerA;
		public static SSSocketModel.Vec playerB;
	}
	

	public static void main(String[] args) throws Exception{
		
		
		//创建一个资料服务器
		InfoServer is = new InfoServer();
		is.setSSInfo(ssinfo);
		is.setCallHandler(new ISCallHandler(){
			
			//这个函数处理玩家发来的数据
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
					else {
						response.message.set(0, "NOPE!");
					}
					return response;
				}else return null;
			}
			
			//玩家连接
			@Override
			public void clientJoin(ChannelId id) {
				ISOUT.WriteConsole("Client join ID:" + id);
			}
			//玩家断开
			@Override
			public void clientDrop(ChannelId id) {
				ISOUT.WriteConsole("Client join ID:" + id);
			}
			
			
			
			
			
			
			
		});
		is.setMaxConn(1024);
		is.setPort(9999);
		//如果是start()函数则是同步执行，那程序就堵塞了。。。
		is.startThread();
		
		
		
		
		
		
		
		
		
		
		
		
		//创建一个场景同步服务器
		ScenarioServer ss = new ScenarioServer();
		ss.setCallHandler(new SSCallHandler(){
			@Override
			public SSSocketModel dealMsg(SSSocketModel message){
				
				
				
				
				return null;
			}
			//玩家连接
			@Override
			public void clientJoin(ChannelId id) {
				SSOUT.WriteConsole("Client join ID:" + id);
			}
			//玩家断开
			@Override
			public void clientDrop(ChannelId id) {
				SSOUT.WriteConsole("Client join ID:" + id);
			}
			
		});
		
		
		ss.setTickSender(new TickSender(){
			@Override
			public SSSocketModel dealSend(String channelId){
				
				SSSocketModel send = new SSSocketModel();
				send.command = COMMAND_UPDATE_PLAYER;
				send.vector.add(send.new Vec());
				return send;
			}
		});
		ss.setMaxConn(1024);
		ss.setTick(30);
		ss.setPort(ssinfo.get(0).Port);
		ss.startThread();
		
		
		
		
		
		
		
		
		
		//监听输入，进行一些有意思的测试
		Scanner scanner = new Scanner(System.in);
		while(true)
		{
			String str = scanner.nextLine();  
			if(str.equals("q")){
				is.shut();
				ss.shut();
				break;
			}else{
				//比如这里修改了密码
				ServerExample.USERIDENTITY.Pass = str;
			}
		}
		scanner.close();
	}

}
