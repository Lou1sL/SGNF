using System.Collections.Generic;
using UnityEngine;
using SGNFClient;
using UnityEngine.UI;

public class MainClient : MonoBehaviour
{
    public Transform playerA;
    public Transform playerB;

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
        Client.AddISMsgRcver(ProtocalCommand.TEST_LOGIN, ISCallBack_Test);
        Client.Connect("192.168.1.102", 9999);
        Client.SSUpdate(SSUpdate);
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
        Client.SSJoin(Client.AllSSInfo[0]);
    }

    /// <summary>
    /// 发送后的回调
    /// </summary>
    /// <param name="_msgData"></param>
    private void ISCallBack_Test(ISSocketModel _msgData)
    {
        result.text = _msgData.Message[0];
    }

    private SSSocketModel SSUpdate(SSSocketModel rcv)
    {
        if(rcv.Command == (int)ProtocalCommand.TEST_PLAYER)
            playerB.position = new Vector3(rcv.Vector[0].X, rcv.Vector[0].Y, rcv.Vector[0].Z);

        return new SSSocketModel()
        {
            Command = (int)ProtocalCommand.TEST_PLAYER,
            Vector = new List<Vec>()
            {
                new Vec()
                {
                    X = playerA.position.x,
                    Y = playerA.position.y,
                    Z = playerA.position.z,
                }
            }
        };
    }

    private float speed = 4;

    private void Update()
    {
        if (Input.GetKey(KeyCode.W)) playerA.position += Vector3.forward * Time.deltaTime * speed;
        if (Input.GetKey(KeyCode.S)) playerA.position -= Vector3.forward * Time.deltaTime * speed;

        if (Input.GetKey(KeyCode.A)) playerA.position += Vector3.left * Time.deltaTime * speed;
        if (Input.GetKey(KeyCode.D)) playerA.position -= Vector3.left * Time.deltaTime * speed;
    }
}