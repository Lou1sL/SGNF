package com.ryubai.sgnf.infoserver;

public class LoginProtocol {
    
    /*
     * Login_Area
     * **/
    public static final int Area_LoginRequest = 0; // ��½����
    public static final int Area_LoginResponse = 1; //��¼Ӧ��
     
    /*
     * Login_Command
     * **/
    public static final int Login_InvalidMessage = 0;//��Ч��Ϣ
    public static final int Login_InvalidUsername = 1;//��Ч�û���
    public static final int Login_InvalidPassword = 2;//�������
     
    public static final int Login_Succeed = 10;//��½�ɹ�
}