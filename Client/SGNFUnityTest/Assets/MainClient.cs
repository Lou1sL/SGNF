using ProtoBuf;
using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Net.Sockets;
using UnityEngine;
using SGNFClient;
using UnityEngine.UI;

public class MainClient : MonoBehaviour
{
    public Text PingText;


    /// <summary>
    /// 网络配置
    /// </summary>
    public class GameConst
    {
        public const string IP = "192.168.1.102";
        public const int Port = 9999;
    }


    /// <summary>
    /// 网络事件ID；
    /// 在服务器，它的长度为UInt16，
    /// 因此数值上不能大于0xFFFF。
    /// 同时，0xF000-0xFFFF是保留报文格式，仅内部使用。
    /// 因此，可用的事件ID范围为：
    /// 0x0000-0xEFFF
    /// </summary>
    public enum ProtocalCommand
    {
        test = 1234,
        player_position = 0x3000,
    }
    
    private void Start()
    {
        //绑定数据包发送后的服务器回调处理函数
        Client.AddCallBackObserver(ProtocalCommand.test, CallBack_Test);
        Client.Connect(GameConst.IP, GameConst.Port);
    }

    private void OnDisable()
    {
        //解绑
        Client.RemoveCallBackObserver(ProtocalCommand.test, CallBack_Test);
    }
    void OnApplicationQuit()
    {
        
        Client.Disconnect();
    }
    


    /// <summary>
    /// 发送数据包
    /// </summary>
    public void SendMsg2IS()
    {
        ISSocketModel model = new ISSocketModel()
        {
            Command = (int)ProtocalCommand.test,
            Message = new List<string>()
            {
                "HAHAHAHAHAAHAAAAAHHH!",
            }
        };
        Client.SendISMsg(model);
    }

    /// <summary>
    /// 发送后的回调
    /// </summary>
    /// <param name="_msgData"></param>
    private void CallBack_Test(ISSocketModel _msgData)
    {
        Debug.Log(_msgData.Command);
        Debug.Log(_msgData.Message[0]);
    }
    
    private void Update()
    {
        PingText.text = Client.Ping().ToString();
    }
}