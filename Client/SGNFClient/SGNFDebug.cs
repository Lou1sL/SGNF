using SGNFClient.UnityScript;
using System;
using System.Collections.Generic;
using UnityEngine;

namespace SGNFClient
{
    public static class SGNFDebug
    {
        
        public static void ExceptionCaught(Exception e)
        {

            if ((int)NetManager.Instance.LogLevel>=3) return;


            Debug.LogError("SGNF Exception:" + e.Message + " StackTrace: \n" + e.StackTrace);
            throw e;

        }
        public static void Log(object o)
        {
            if ((int)NetManager.Instance.LogLevel >= 2) return;

            Debug.Log("SGNF Log: " + o.ToString());

        }
        public static void HEXLog(object desc, byte[] b, int len)
        {
            if ((int)NetManager.Instance.LogLevel >= 1) return;

            byte[] tmp = new byte[len];
            Array.Copy(b, tmp, len);

            var hex = BitConverter.ToString(tmp, 0).Replace("-", string.Empty).ToLower();
            Debug.Log("SGNF Hex Log: Description:" + desc + " Hex are: " + hex);

        }
        public static void ListLog<T>(object desc, List<T> list)
        {
            if ((int)NetManager.Instance.LogLevel >= 2) return;

            string tmp = string.Empty;
            tmp += "Total: " + list.Count + "\n";

            foreach (T stuff in list) tmp += stuff.ToString() + "\n";

            Debug.Log("SGNF List Log: Description:" + desc + " List is: " + tmp);

        }
    }
}
