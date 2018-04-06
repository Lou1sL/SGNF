package com.ryubai.sgnf.infoserver;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;


public class LoginDispatch {
    private static LoginDispatch instance = new LoginDispatch();
    public static LoginDispatch getInstance()
    {
        return instance;
    }
    //public User user = null;
    //public Wizard wizard = null;
    public void dispatch(ChannelHandlerContext ctx, SocketModel message)
    {
        switch (message.getArea()) {
        case LoginProtocol.Area_LoginRequest:
            LoginResponse(ctx,message);
     
            break;
        default:
            break;
        }
    }
    /*
     * **检测用户登录是否密码错误，用户名不存在等，返回int对应的不同类型
     */
    public int LoginCheck(ChannelHandlerContext ctx,SocketModel request)
    {
        List<String> message = request.getMessage();
        String username = message.get(0);
        String password = message.get(1);
        System.out.println(username);
        System.out.println(password);
        /*if (message.isEmpty())
        {
            return LoginProtocol.Login_InvalidMessage;
        }else{
            if (UserMySQL.getInstance().usernameExit(username))
            {
                user = UserMySQL.getInstance().userExit(username, password,ctx.channel());
                if (user != null){
         
                    return LoginProtocol.Login_Succeed;
                }else{
                    return LoginProtocol.Login_InvalidPassword;
                }
            }else{
                return LoginProtocol.Login_InvalidUsername;
            }
        }
    */
        return LoginProtocol.Login_Succeed;
    }
    public void LoginResponse(ChannelHandlerContext ctx,SocketModel request)
    {
        SocketModel response = new SocketModel();
        int command = LoginCheck(ctx, request);
        response.setType(TypeProtocol.TYPE_LOGIN);
        response.setArea(LoginProtocol.Area_LoginResponse);
        response.setCommand(command);
        response.setMessage(request.getMessage());
        ctx.writeAndFlush(response);
        if (command == LoginProtocol.Login_Succeed)
        {
            LoginUser(ctx,request);//如果成功就登陆用户,并开始新手向导
        }
    }
    /**
     * 登陆用户，并开始新手向导
     * @param ctx
     */
    public void LoginUser(ChannelHandlerContext ctx,SocketModel socketModel)
    {/*
        user = UserMySQL.getInstance().initUser(User.getUserByChannel(ctx.channel()));
        user.setWizard(WizardMySQL.getInstance().initWizard(user.getUserID()));
        SocketModel message = new SocketModel();
        message.setType(TypeProtocol.TYPE_WIZARD);
        message.setArea(WizardProtocol.Wizard_Create_Request);
        message.setCommand(user.getWizard().getStepIndex());
        message.setMessage(null);
        ctx.writeAndFlush(message);*/
    }
}