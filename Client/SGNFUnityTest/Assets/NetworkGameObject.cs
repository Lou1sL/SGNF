using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class 同步位置格式
{
    public bool X = true;
    public bool Y = true;
    public bool Z = true;
}

[System.Serializable]
public class 同步旋转格式
{
    public bool X = true;
    public bool Y = true;
    public bool Z = true;
}

public class NetworkGameObject : MonoBehaviour {

    public 同步位置格式 同步位置 = new 同步位置格式();
    public 同步旋转格式 同步旋转 = new 同步旋转格式();

    
	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}
}
