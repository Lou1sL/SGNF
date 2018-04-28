package com.ryubai.sgnf.scenarioserver;

public class SocketUtil {

	/**
	 * ���ֽ�ת������
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
	 * ������ת�����ֽ�
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
		NULL(0xF000), // ���������յ�����Ϣʱ������
		PING(0xF001), // �ͻ��˷���һ������������������ܴ�ָ��󷵻ظ������

		// Client-IS
		SSINFO(0xF002),// �ͻ��˴�IS��ȡȫ��SS��

		// Client-SS
		TICK(0xF003);//��ȡʱ��
		
		
		private int i;

		private internalCommand(int i) {
			this.i = i;
		}

		public int val() {
			return this.i;
		}
	}
}
