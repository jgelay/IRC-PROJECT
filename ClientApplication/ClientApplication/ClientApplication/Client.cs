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
            string[] reply = null;

            //password registration
            do
            {
                reply = parsedMessage(stream, "PASS");
            } while (!reply[1].Equals("NICK\n"));

            do
            {
                reply = parsedMessage(stream, "NICK");
            } while (!reply[1].Equals("USER\n"));

            do
            {
                reply = parsedMessage(stream, "USER");
            } while (!reply[1].Equals("001\n"));

            Console.WriteLine("You are now registered");
            return;
        }
        public string[] parsedMessage(NetworkStream stream, string command)
        {
            int bytesRead;
            string input;
            string[] messageArray;
            byte[] result;

            Console.WriteLine(command + ": ");
            input = command + " " + Console.ReadLine();

            int byteCount = Encoding.ASCII.GetByteCount(input + 1);
            byte[] sendData = new byte[byteCount];
            byte[] respData = new byte[1];

            sendData = Encoding.ASCII.GetBytes(input + "\n");

            stream.Write(sendData, 0, sendData.Length);

            using (var memstream = new MemoryStream())
            {
                while (!(Encoding.ASCII.GetChars(respData)[0] == '\n'))
                {
                    bytesRead = stream.Read(respData, 0, respData.Length);
                    memstream.Write(respData, 0, bytesRead);
                }
                result = memstream.GetBuffer();
                messageArray = Encoding.ASCII.GetString(result, 0, Convert.ToInt32(memstream.Length)).Split(' ');
            }
            
            return messageArray;
        }

        public TcpClient getClient()
        {
            return this.client;
        }
    }
}
