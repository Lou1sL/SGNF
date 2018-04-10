using System;
using UnityEngine;

namespace SGNFClient
{
    public static class SGNFDebug
    {
        public static void ExceptionCaught(Exception e)
        {
#if DEBUG
            Debug.LogError("SGNF EX:" + e.Message + " STACKTRACE:" + e.StackTrace);
            throw e;
#endif
        }
        public static void Log(object o)
        {
#if DEBUG
            Debug.Log("SGNF LOG: " + o.ToString());
#endif
        }
        public static void HEXLog(object desc,byte[] b, int len)
        {
#if DEBUG
            byte[] tmp = new byte[len];
            Array.Copy(b, tmp, len);

            var hex = BitConverter.ToString(tmp, 0).Replace("-", string.Empty).ToLower();
            Debug.Log("SGNF HEX: DESC:" + desc + " HEX: " + hex);
#endif
        }
    }
}
