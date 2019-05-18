using System.Collections.Generic;
using UnityEngine;
using SGNFClient;
using UnityEngine.UI;
using System;
using SGNFClient.UnityScript;

public class MainClient : MonoBehaviour
{

    public InputField testUserName;
    public InputField testPass;
    public Text testResult;
    public Text ping;

    /// <summary>
    /// 0xF000-0xFFFF是保留报文格式，仅内部使用。
    /// </summary>
    public enum ProtocalCommand
    {
        TEST_LOGIN = 0x1000,
        TEST_PLAYER = 0x1001,
    }
    
    private void Start()
    {
        //绑定数据包发送后的服务器回调处理函数
        NetManager.Instance.ISMsgAddRcver(ProtocalCommand.TEST_LOGIN, ISCallBack_Test);

        //每tick调用的SSUpdate函数,不一定非得用
        NetManager.Instance.SSUpdate(delegate (SSSocketModel rcv)
        {
            return new SSSocketModel(){};
        });
    }

    
    /// <summary>
    /// 发送数据包
    /// </summary>
    public void SendMsg2IS()
    {
        ISSocketModel model = new ISSocketModel()
        {
            Command = (int)ProtocalCommand.TEST_LOGIN,
            Message = new List<string>()
            {
                testUserName.text,
                testPass.text,
            }
        };
        NetManager.Instance.ISMsgSend(model);
    }

    /// <summary>
    /// 发送后的回调
    /// </summary>
    /// <param name="_msgData"></param>
    private void ISCallBack_Test(ISSocketModel _msgData)
    {
        testResult.text = _msgData.Message[0];
    }
    
    
    private void Update()
    {
        ping.text = "Lat: "+ NetManager.Instance.Latency + "";
    }
}