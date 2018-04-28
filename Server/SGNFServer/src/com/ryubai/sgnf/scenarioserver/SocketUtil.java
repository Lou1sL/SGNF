package com.ryubai.sgnf.scenarioserver;

public class SocketUtil {

	/**
	 * 将字节转成整形
	 * 
	 * @param data
	 * @param offset
	 * @return
	 */
	public static int bytesToInt(byte[] data, int offset) {
		int num = 0;
		for (int i = offset; i < offset + 4; i++) {
			num <<= 8;
			num |= (data[i] & 0xff);
		}
		return num;
	}

	/**
	 * 将整形转化成字节
	 * 
	 * @param num
	 * @return
	 */
	public static byte[] intToBytes(int num) {
		byte[] b = new byte[4];
		//for (int i = 0; i < 4; i++) {
		//	b[i] = (byte) (num >>> (24 - i * 8));
		//}
		b[0] = (byte) (num & 0xff); 
		b[1] = (byte) ((num >> 8) & 0xff); 
		b[2] = (byte) ((num >> 16) & 0xff); 
		b[3] = (byte) ((num >> 24) & 0xff); 
		return b;
	}

	public enum internalCommand {
		// Common
		NULL(0xF000), // 当服务器收到该消息时抛弃掉
		PING(0xF001), // 客户端发来一个随机数，服务器接受此指令后返回该随机数

		// Client-IS
		SSINFO(0xF002),// 客户端从IS获取全部SS列

		// Client-SS
		TICK(0xF003);//获取时钟
		
		
		private int i;

		private internalCommand(int i) {
			this.i = i;
		}

		public int val() {
			return this.i;
		}
	}
}
