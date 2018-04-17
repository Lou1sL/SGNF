using System.Collections.Generic;
using UnityEngine;
using SGNFClient;
using UnityEngine.UI;

public class MainClient : MonoBehaviour
{
    public Transform player;

    public InputField inputUserName;
    public InputField inputPass;
    public Text result;

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
        Client.AddISCallBackObserver(ProtocalCommand.TEST_LOGIN, ISCallBack_Test);
        Client.AddSSCallBackObserver(ProtocalCommand.TEST_PLAYER, SSCallBack_Test);
        Client.Connect("192.168.1.102", 9999);
    }

    private void OnDisable()
    {
        //解绑
        Client.RemoveISCallBackObserver(ProtocalCommand.TEST_LOGIN, ISCallBack_Test);
        Client.RemoveSSCallBackObserver(ProtocalCommand.TEST_PLAYER, SSCallBack_Test);
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
            Command = (int)ProtocalCommand.TEST_LOGIN,
            Message = new List<string>()
            {
                inputUserName.text,
                inputPass.text,
            }
        };
        Client.SendISMsg(model);
    }

    public void JoinFirstSS()
    {
        Client.JoinSS(Client.AllSSInfo[0]);
    }

    /// <summary>
    /// 发送后的回调
    /// </summary>
    /// <param name="_msgData"></param>
    private void ISCallBack_Test(ISSocketModel _msgData)
    {
        result.text = _msgData.Message[0];
    }

    private void SSCallBack_Test(SSSocketModel _msgData)
    {
        Debug.Log("aaa"+ _msgData.Vector[0].Tag);
        player.position = new Vector3(_msgData.Vector[0].X, _msgData.Vector[0].Y, _msgData.Vector[0].Z);

        Client.SendSSMsg(new SSSocketModel()
        {
            Command = (int)ProtocalCommand.TEST_PLAYER,
        });
    }

}