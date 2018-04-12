using System.Collections.Generic;
using UnityEngine;
using SGNFClient;
using UnityEngine.UI;

public class MainClient : MonoBehaviour
{
    public InputField inputUserName;
    public InputField inputPass;
    public Text result;

    /// <summary>
    /// 0xF000-0xFFFF是保留报文格式，仅内部使用。
    /// </summary>
    public enum ProtocalCommand
    {
        TEST_LOGIN = 0x1000,
    }
    
    private void Start()
    {
        //绑定数据包发送后的服务器回调处理函数
        Client.AddCallBackObserver(ProtocalCommand.TEST_LOGIN, CallBack_Test);
        Client.Connect("192.168.1.102", 9999);
    }

    private void OnDisable()
    {
        //解绑
        Client.RemoveCallBackObserver(ProtocalCommand.TEST_LOGIN, CallBack_Test);
    }
    void OnApplicationQuit()
    {
        
        Client.Disconnect();
    }

    private System.DateTime sendT;

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
                inputUserName.text,
                inputPass.text,
            }
        };
        Client.SendISMsg(model);
        sendT = System.DateTime.Now;
    }

    /// <summary>
    /// 发送后的回调
    /// </summary>
    /// <param name="_msgData"></param>
    private void CallBack_Test(ISSocketModel _msgData)
    {
        result.text = _msgData.Message[0];
        Debug.Log("PING: "+(System.DateTime.Now - sendT).Milliseconds);
    }
    
}