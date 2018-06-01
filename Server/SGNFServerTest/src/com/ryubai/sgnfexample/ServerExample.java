package com.ryubai.sgnfexample;

import java.sql.Connection;
import java.util.ArrayList;

import com.ryubai.sgnf.dbmanager.*;
import com.ryubai.sgnf.infoserver.*;
import com.ryubai.sgnf.scenarioserver.*;
import com.ryubai.sgnf.scenarioserver.UnityType.Vector3;

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
		public static String AID = "";
		public static String BID = "";

		public static Vector3 playerA = new Vector3(-2f, 0.5f, 0f);
		public static Vector3 playerB = new Vector3(2f, 0.5f, 0f);

		// �����Ǵ����÷���������Ϊprotobuf�ڴ���ʱ���nullѹ��û������List����Ĳ��ִ�λ
		// public static Vector playerA = null;
		// public static Vector playerB = null;
	}

	public static void main(String[] args) throws Exception {

		// �������ݿ����ӳ�
		new DBConnectionManager();
		DBConnectionManager db = DBConnectionManager.getInstance();
		Connection con = db.getConnection("db");// ��ȡһ�����Ӷ���

		// ����һ�����Ϸ�����
		InfoServer is = new InfoServer();

		is.setCallHandler(new ISCallHandler() {
			// ������յ�����
			@Override
			public void recieveMessage(String id, ISSocketModel message) {

				if (message.command == COMMAND_TEST_LOGIN) {

					ISSocketModel response = new ISSocketModel();
					response.message.add("");
					response.command = COMMAND_TEST_LOGIN;

					if (message.message.get(0).equals(UserName) && message.message.get(1).equals(Pass)) {
						response.message.set(0, "OK!");
					} else {
						response.message.set(0, "NOPE!");
					}
					// ���øú�����������
					is.sendMessage(id, response);
				}
			}

			// �������
			@Override
			public void clientJoin(String id) {
				ISOUT.WriteConsole("Client join ID:" + id);
			}

			// ��ҶϿ�
			@Override
			public void clientDrop(String id) {
				ISOUT.WriteConsole("Client drop ID:" + id);
			}
		});
		// �󶨳���������
		is.setSSInfo(ssinfo);
		// �������������
		is.setMaxConn(1024);
		// ���÷������˿�
		is.setPort(9999);
		// �����start()��������ͬ��ִ�ж���
		is.startThread();

		// ����һ������ͬ��������
		ScenarioServer ss = new ScenarioServer();
		ss.setCallHandler(new SSCallHandler() {

			// ÿtick����
			@Override
			public SSSocketModel tickSend(String id) {
				SSSocketModel sm = new SSSocketModel();

				sm.command = COMMAND_UPDATE_PLAYER;
				if (id.equals(BattleField.AID))
					sm.vector.add(new NetVector(-1,BattleField.playerB));
				else if (id.equals(BattleField.BID))
					sm.vector.add(new NetVector(-1,BattleField.playerA));
				sm.vector.add(new NetVector());
				return sm;
			}

			// tick����
			@Override
			public void tickRcv(String id, SSSocketModel message) {

				if (id.equals(BattleField.AID))
					BattleField.playerA = message.vector.get(0).toVector3();
				if (id.equals(BattleField.BID))
					BattleField.playerB = message.vector.get(0).toVector3();

				// SSOUT.WriteConsole("x:"+message.vector.get(0).x+"
				// z:"+message.vector.get(0).z);
			}

			// �������
			@Override
			public void clientJoin(String id) {
				if (BattleField.AID.isEmpty())
					BattleField.AID = id;
				else if (BattleField.BID.isEmpty())
					BattleField.BID = id;

				SSOUT.WriteConsole("Client join ID:" + id);
			}

			// ��ҶϿ�
			@Override
			public void clientDrop(String id) {
				if (BattleField.AID.equals(id))
					BattleField.AID = "";
				if (BattleField.BID.equals(id))
					BattleField.BID = "";

				SSOUT.WriteConsole("Client drop ID:" + id);
			}

		});

		ss.setMaxConn(1024);
		// �趨֡��
		ss.setTick(60);
		ss.setPort(ssinfo.get(0).Port);
		ss.startThread();


		while (true) {
		}
	}

}
