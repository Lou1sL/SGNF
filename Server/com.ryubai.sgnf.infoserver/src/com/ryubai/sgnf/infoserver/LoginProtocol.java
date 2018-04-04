package com.ryubai.sgnf.infoserver;

public class LoginProtocol {
    
    /*
     * Login_Area
     * **/
    public static final int Area_LoginRequest = 0; // 登陆请求
    public static final int Area_LoginResponse = 1; //登录应答
     
    /*
     * Login_Command
     * **/
    public static final int Login_InvalidMessage = 0;//无效消息
    public static final int Login_InvalidUsername = 1;//无效用户名
    public static final int Login_InvalidPassword = 2;//密码错误
     
    public static final int Login_Succeed = 10;//登陆成功
}