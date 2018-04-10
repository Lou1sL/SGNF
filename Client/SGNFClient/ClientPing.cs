using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.NetworkInformation;
using System.Text;
using System.Threading;

namespace SGNFClient
{
    public static partial class Client
    {
        private static int ping = 1000;

        private static Thread PingThread = null;
        public static int Ping()
        {
            if (IsConnected)
            {
                if (PingThread == null)
                {
                    PingThread = new Thread(new ThreadStart(sendPingPackage));
                    PingThread.IsBackground = true;
                    PingThread.Start();
                }

            }
            return ping;
        }
        private static void sendPingPackage()
        {
            while (true)
            {

                Ping pingSender = new Ping();
                PingReply reply = pingSender.Send(ip);
                if (reply.Status == IPStatus.Success)
                {
                    ping = Convert.ToInt32(reply.RoundtripTime);
                    SGNFDebug.Log(reply.Status.ToString());
                }
                else
                {
                    SGNFDebug.Log(reply.Status.ToString());
                }

                Thread.Sleep(1000);
            }
        }
    }

}
