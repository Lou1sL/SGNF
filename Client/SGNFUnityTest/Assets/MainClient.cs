using System.Collections.Generic;
using UnityEngine;
using SGNFClient;
using UnityEngine.UI;
using System;
using SGNFClient.UnityScript;

public class MainClient : MonoBehaviour
{

    public InputField inputUserName;
    public InputField inputPass;

    public Text result;

    public Text ping;

    public Transform playerA;

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

        //每tick调用的SSUpdate函数,不一定非得有用
        NetManager.Instance.SSUpdate(delegate (SSSocketModel rcv)
        {
            return new SSSocketModel()
            {
                Command = (int)ProtocalCommand.TEST_PLAYER,
                Vector = new List<Vec>()
                {
                    new Vec(-1,playerA.position),
                }
            };
        });
    }

    void OnApplicationQuit()
    {
        NetManager.Instance.Disconnect();
    }
    

    public void InfoServerConnect()
    {
        NetManager.Instance.Connect();
    }
    public void InfoServerDisconnect()
    {
        NetManager.Instance.Disconnect();
    }

    public void ScenarioServerJoin()
    {
        NetManager.Instance.SSJoin();
    }
    public void ScenarioServerLeave()
    {
        NetManager.Instance.SSLeave();
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
        NetManager.Instance.ISMsgSend(model);
    }

    /// <summary>
    /// 发送后的回调
    /// </summary>
    /// <param name="_msgData"></param>
    private void ISCallBack_Test(ISSocketModel _msgData)
    {
        result.text = _msgData.Message[0];
    }
    
    private float speed = 4;
    
    private void Update()
    {
        ping.text = "PING "+ NetManager.Instance.Latency + "";

        //控制
        if (Input.GetKey(KeyCode.W)) playerA.position += Vector3.forward * Time.deltaTime * speed;
        if (Input.GetKey(KeyCode.S)) playerA.position -= Vector3.forward * Time.deltaTime * speed;

        if (Input.GetKey(KeyCode.A)) playerA.position += Vector3.left * Time.deltaTime * speed;
        if (Input.GetKey(KeyCode.D)) playerA.position -= Vector3.left * Time.deltaTime * speed;
    }
}