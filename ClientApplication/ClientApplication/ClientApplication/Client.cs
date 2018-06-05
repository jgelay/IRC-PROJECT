using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Sockets;
using System.IO;

namespace ClientApplication
{
    class Client
    {
        TcpClient client;
        public Client()
        {
            string SERVERIP = "";
            int PORT = 0;

            this.client = new TcpClient(SERVERIP, PORT);
        }

        public void ClientRegistration(NetworkStream stream)
        {
            passwordRegistration(stream);

        }
        public void passwordRegistration(NetworkStream stream)
        {
            int bytesRead;
            string password;
            string command;
            Console.WriteLine("PASS: ");
            password = "PASS " + Console.ReadLine();

            int byteCount = Encoding.ASCII.GetByteCount(password + 1);
            byte[] sendData = new byte[byteCount];
            byte[] respData = new byte[1];

            sendData = Encoding.ASCII.GetBytes(password + "\n");

            stream.Write(sendData, 0, sendData.Length);

            using (var memstream = new MemoryStream())
            {
                while (!Encoding.ASCII.GetString(respData).Equals("\n"))
                {
                    bytesRead = stream.Read(respData, 0, respData.Length);
                    memstream.Write(respData, 0, bytesRead);
                }
                byte[] result = memstream.GetBuffer();
                command = parseMessage(Encoding.ASCII.GetString(result));
            }
        }

        public void nickNameRegistration(NetworkStream stream)
        {

        }
        public string parseMessage(string message)
        {
            string[] messageArray = message.Split(' ');

            return null;
        }
        public TcpClient getClient()
        {
            return this.client;
        }
    }
}
