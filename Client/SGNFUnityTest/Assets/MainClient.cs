﻿using System.Collections.Generic;
using UnityEngine;
using SGNFClient;
using UnityEngine.UI;
using System;

public class MainClient : MonoBehaviour
{
    public InputField isip;
    public InputField isport;
    
    public InputField inputUserName;
    public InputField inputPass;

    public Text result;

    public Text testRes;

    public Text ping;

    public InputField sstag;


    public Transform playerA;
    public Transform playerB;

    public Text ssinfo;

    public Button conn;
    public Button disconn;
    public Button testLogin;

    public Button connss;
    public Button disconnss;


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

        //每tick调用的SSUpdate函数
        Client.SSUpdate(delegate (SSSocketModel rcv)
        {
            //if (rcv.Command == (int)ProtocalCommand.TEST_PLAYER)
            //{
            //    local = playerB.position;
            //    newb = rcv.Vector[0].ToVector3();
            //}

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
        Client.Disconnect();
    }
    

    public void InfoServerConnect()
    {
        Client.Connect(isip.text,Convert.ToInt32(isport.text));
    }
    public void InfoServerDisconnect()
    {
        Client.Disconnect();
    }

    public void ScenarioServerJoin()
    {
        Client.SSJoin(sstag.text);
    }
    public void ScenarioServerLeave()
    {
        Client.SSLeave();
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

    /// <summary>
    /// 发送后的回调
    /// </summary>
    /// <param name="_msgData"></param>
    private void ISCallBack_Test(ISSocketModel _msgData)
    {
        result.text = _msgData.Message[0];
    }

    

    private float speed = 4;


    private Vector3 local = new Vector3();
    private Vector3 newb = new Vector3();

    private void Update()
    {
        

        //插值优化流畅度
        //playerB.position = SGNFUtils.SmoothVec(local,newb);
        //不优化
        //playerB.position = newb;



        //按钮启用
        conn.interactable = !Client.IsConnected;
        disconn.interactable = Client.IsConnected;
        testLogin.interactable = Client.IsConnected;

        connss.interactable = !Client.IsJoined && Client.IsConnected;
        disconnss.interactable = Client.IsJoined && Client.IsConnected;

        ping.text = "PING "+Client.Latency + "";

        //控制
        if (Client.AllSSInfo.Count > 0) ssinfo.text = Client.AllSSInfo[0].ToString();

        if (Input.GetKey(KeyCode.W)) playerA.position += Vector3.forward * Time.deltaTime * speed;
        if (Input.GetKey(KeyCode.S)) playerA.position -= Vector3.forward * Time.deltaTime * speed;

        if (Input.GetKey(KeyCode.A)) playerA.position += Vector3.left * Time.deltaTime * speed;
        if (Input.GetKey(KeyCode.D)) playerA.position -= Vector3.left * Time.deltaTime * speed;
    }
}