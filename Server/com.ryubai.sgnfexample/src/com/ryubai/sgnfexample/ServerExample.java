package com.ryubai.sgnfexample;

import java.util.ArrayList;
import java.util.Scanner;

import com.ryubai.sgnf.infoserver.*;
import com.ryubai.sgnf.scenarioserver.SSCallHandler;
import com.ryubai.sgnf.scenarioserver.SSOUT;
import com.ryubai.sgnf.scenarioserver.SSSocketModel;
import com.ryubai.sgnf.scenarioserver.ScenarioServer;
import com.ryubai.sgnf.scenarioserver.Vector;

import io.netty.channel.ChannelId;

public class ServerExample {
	
	// ģ���¼��ָ��
	private static int COMMAND_TEST_LOGIN = 0x1000;

	// ģ���¼������
	public static String UserName = "MyName";
	public static String Pass = "12345678";
	
	// ��IS���õ�SS��Ϣ
	private static ArrayList<SSInfo> ssinfo = new ArrayList<SSInfo>() {
		{
			add(new SSInfo("ss0", "192.168.1.102", 9876));
		}
	};

	// ģ��������λ�õ�ָ��
	private static int COMMAND_UPDATE_PLAYER = 0x1001;

	// ģ���ս��
	private static class BattleField {
		public static ChannelId AID = null;
		public static ChannelId BID = null;
		
		public static Vector playerA = new Vector();
		public static Vector playerB = new Vector();
		
		//�����Ǵ����÷���������Ϊprotobuf�ڴ���ʱ���nullѹ��û������List����Ĳ��ִ�λ
		//public static Vector playerA = null;
		//public static Vector playerB = null;
	}

	public static void main(String[] args) throws Exception {

		// ����һ�����Ϸ�����
		InfoServer is = new InfoServer();
		is.setSSInfo(ssinfo);
		is.setCallHandler(new ISCallHandler() {
			// �������������ҷ���������
			@Override
			public ISSocketModel dealMsg(ChannelId id, ISSocketModel message) {
				ISSocketModel response = new ISSocketModel();
				response.message.add("");
				if (message.command == COMMAND_TEST_LOGIN) {
					response.command = COMMAND_TEST_LOGIN;

					if (message.message.get(0).equals(UserName)
							&& message.message.get(1).equals(Pass)) {
						response.message.set(0, "OK!");
					} else {
						response.message.set(0, "NOPE!");
					}
					return response;
				} else
					return null;
			}

			// �������
			@Override
			public void clientJoin(ChannelId id) {
				ISOUT.WriteConsole("Client join ID:" + id);
			}

			// ��ҶϿ�
			@Override
			public void clientDrop(ChannelId id) {
				ISOUT.WriteConsole("Client drop ID:" + id);
			}
		});
		is.setMaxConn(1024);
		is.setPort(9999);
		// �����start()��������ͬ��ִ�ж���
		is.startThread();

		// ����һ������ͬ��������
		ScenarioServer ss = new ScenarioServer();
		ss.setCallHandler(new SSCallHandler() {
			
			//ÿtick����һ��
			@Override
			public SSSocketModel tickSend(ChannelId id){
				SSSocketModel sm = new SSSocketModel();
				
				
				sm.command = COMMAND_UPDATE_PLAYER;
				if(id==BattleField.AID)sm.vector.add(BattleField.playerB);
				else if(id==BattleField.BID)sm.vector.add(BattleField.playerA);
				sm.vector.add(new Vector());
				return sm;
			}
			//tick����
			@Override
			public void tickRcv(ChannelId id, SSSocketModel message) {
				
				
				if(id==BattleField.AID)BattleField.playerA = message.vector.get(0);
				if(id==BattleField.BID)BattleField.playerB = message.vector.get(0);
				
				//SSOUT.WriteConsole("x:"+message.vector.get(0).x+" z:"+message.vector.get(0).z);
			}
			
			// �������
			@Override
			public void clientJoin(ChannelId id) {
				if(BattleField.AID==null)BattleField.AID = id;
				else if(BattleField.BID==null)BattleField.BID = id;
				
				SSOUT.WriteConsole("Client join ID:" + id);
			}

			// ��ҶϿ�
			@Override
			public void clientDrop(ChannelId id) {
				if(BattleField.AID==id)BattleField.AID = null;
				if(BattleField.BID==id)BattleField.BID = null;
				
				SSOUT.WriteConsole("Client drop ID:" + id);
			}

		});

		ss.setMaxConn(1024);
		ss.setTick(10);
		ss.setPort(ssinfo.get(0).Port);
		ss.startThread();

		while(true){}
	}

}
