package com.ryubai.sgnfexample;

import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;

import com.ryubai.sgnf.dbmanager.*;
import com.ryubai.sgnf.infoserver.*;
import com.ryubai.sgnf.scenarioserver.*;
import com.ryubai.sgnf.scenarioserver.UnityType.Vector3;

public class ServerExample {

	// 模拟登录的指令
	private static int COMMAND_TEST_LOGIN = 0x1000;

	// 模拟登录的数据
	public static String UserName = "MyName";
	public static String Pass = "12345678";

	// 给IS配置的SS信息
	private static ArrayList<SSInfo> ssinfo = new ArrayList<SSInfo>() {
		{
			add(new SSInfo("ss0", "127.0.0.1", 9876));
		}
	};

	// 模拟更新玩家位置的指令
	private static int COMMAND_UPDATE_PLAYER = 0x1001;

	// 模拟的战场
	public static class BattleField {
		public static String AID = "";
		public static String BID = "";

		public static Vector3 playerA = new Vector3(-2f, 0.5f, 0f);
		public static Vector3 playerB = new Vector3(2f, 0.5f, 0f);

		// 以下是错误用法！！！因为protobuf在传输时会把null压缩没，导致List后面的部分错位
		// public static Vector playerA = null;
		// public static Vector playerB = null;
	}

	public static void main(String[] args) throws Exception {

		// 创建数据库连接池
		new DBConnectionManager();
		DBConnectionManager db = DBConnectionManager.getInstance();
		Connection con = db.getConnection("db");// 获取一个连接对象

		// 创建一个资料服务器
		InfoServer is = new InfoServer();

		is.setCallHandler(new ISCallHandler() {
			// 处理接收的数据
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
					// 调用该函数发送数据
					is.sendMessage(id, response);
				}
			}

			// 玩家连接
			@Override
			public void clientJoin(String id) {
				ISOUT.WriteConsole("Client join ID:" + id);
			}

			// 玩家断开
			@Override
			public void clientDrop(String id) {
				ISOUT.WriteConsole("Client drop ID:" + id);
			}
		});
		// 绑定场景服务器
		is.setSSInfo(ssinfo);
		// 配置最大连接数
		is.setMaxConn(1024);
		// 配置服务器端口
		is.setPort(9999);
		// 如果是start()函数则是同步执行堵塞
		//is.startThread();

		// 创建一个场景同步服务器
		ScenarioServer ss = new ScenarioServer();
		ss.setCallHandler(new SSCallHandler() {

			// 每tick发送
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

			// tick接收
			@Override
			public void tickRcv(String id, SSSocketModel message) {

				if (id.equals(BattleField.AID))
					BattleField.playerA = message.vector.get(0).toVector3();
				if (id.equals(BattleField.BID))
					BattleField.playerB = message.vector.get(0).toVector3();

				// SSOUT.WriteConsole("x:"+message.vector.get(0).x+"
				// z:"+message.vector.get(0).z);
			}

			// 玩家连接
			@Override
			public void clientJoin(String id) {
				if (BattleField.AID.isEmpty())
					BattleField.AID = id;
				else if (BattleField.BID.isEmpty())
					BattleField.BID = id;

				SSOUT.WriteConsole("Client join ID:" + id);
			}

			// 玩家断开
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
		// 设定帧率
		ss.setTick(60);
		ss.setPort(ssinfo.get(0).Port);
		//ss.startThread();


		new UIExample(is,ss).setBounds(500,500,400,500);


	}

}
