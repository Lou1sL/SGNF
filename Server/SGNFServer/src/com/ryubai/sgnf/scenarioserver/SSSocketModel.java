package com.ryubai.sgnf.scenarioserver;

import java.util.ArrayList;
import java.util.List;

public class SSSocketModel {
	
	public int command;
	
	//��int32���͵����ֵ��˵����ÿ��60tick���ٶȣ�������֧�ֵ�һ����Ϸ414��
	//��ս����Ϸ��ȫ���ӣ�������������ҪС����
	//�������㿪��������Ҳ�����ܷ���������414��ɣ�
	//2333
	public int currentTick;
	public List<Integer> message = new ArrayList<Integer>();
	public List<NetVector> vector = new ArrayList<NetVector>();
	
	public SSSocketModel() {

	}

	public SSSocketModel(SSSocketModel bla) {
		command = bla.command;
		currentTick = bla.currentTick;
		message = bla.message;
		vector = bla.vector;
	}
}