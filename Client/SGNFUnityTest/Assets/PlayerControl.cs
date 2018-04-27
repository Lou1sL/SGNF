using SGNFClient.UnityScript;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerControl : MonoBehaviour {

    private float speed = 8;
    private Rigidbody rigid;

    private void Start()
    {
        rigid = GetComponent<Rigidbody>();
    }
    private void Update()
    {
        
        
    }
    private void FixedUpdate()
    {

        //控制
        Vector3 targetp = transform.position;


        if (Input.GetKey(KeyCode.W)) targetp += Vector3.forward * Time.deltaTime * speed;
        if (Input.GetKey(KeyCode.S)) targetp -= Vector3.forward * Time.deltaTime * speed;

        if (Input.GetKey(KeyCode.A)) targetp += Vector3.left * Time.deltaTime * speed;
        if (Input.GetKey(KeyCode.D)) targetp -= Vector3.left * Time.deltaTime * speed;

        rigid.MovePosition(targetp);
    }
}
