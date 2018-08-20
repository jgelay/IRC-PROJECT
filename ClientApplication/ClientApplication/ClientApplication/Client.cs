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
        private TcpClient client;
        private string nickName;

        public Client()
        {
            string SERVERIP = "";
            int PORT = 0;

            this.client = new TcpClient(SERVERIP, PORT);
        }

        public void ClientRegistration(NetworkStream stream)
        {
            string[] reply = null;
            string input;

            //password registration
            do
            {
                Console.Write("PASS" + ": ");
                input = "PASS" + " " + Console.ReadLine();

                reply = parsedMessage(stream, input);
            } while (!reply[1].Equals("NICK\n"));

            do
            {
                Console.Write("NICK" + ": ");
                input = "NICK" + " " + Console.ReadLine();
                nickName = input.Split(' ')[1];

                reply = parsedMessage(stream, input);
            } while (!reply[1].Equals("USER\n"));

            do
            {
                Console.Write("USER" + ": ");
                input = "USER" + " " + Console.ReadLine();

                reply = parsedMessage(stream, input);
            } while (!reply[1].Equals("001\n"));

            Console.WriteLine("You are now registered");
            ircRunning(stream);
      
            return;
        }
        public string[] parsedMessage(NetworkStream stream, string input)
        {
            int bytesRead;
            string[] messageArray;
            byte[] result;

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

        private async void sender(NetworkStream stream)
        {
            Task<string> consoleInput = ReadConsoleAsync();

            string input = nickName + " " + await consoleInput;

            int byteCount = Encoding.ASCII.GetByteCount(input + 1);
            byte[] sendData = new byte[byteCount];
            byte[] respData = new byte[1];

            sendData = Encoding.ASCII.GetBytes(input + "\n");

            await stream.WriteAsync(sendData, 0, sendData.Length);

            return;
          
        }

        private void receiver (NetworkStream stream)
        {

            String recMsg = parsedReceivedMessage(stream);

            Console.WriteLine(recMsg);
        
            return;
        }
        private void ircRunning(NetworkStream stream)
        {
            while (true)
            {
              
                sender(stream);
                receiver(stream);
            }
        }
       
        private string parsedReceivedMessage(NetworkStream stream)
        {
            byte[] respData = new byte[1];
            byte[] result;
            int bytesRead;
            string recMsg;


            using (var memstream = new MemoryStream())
            {
                while (!(Encoding.ASCII.GetChars(respData)[0] == '\n'))
                {
                    bytesRead = stream.Read(respData, 0, respData.Length);
                    memstream.Write(respData, 0, bytesRead);
                }

                result = memstream.GetBuffer();
                recMsg = Encoding.ASCII.GetString(result, 0, Convert.ToInt32(memstream.Length));
            }

            return recMsg;
        }

        public static Task<string> ReadConsoleAsync()
        {
            return Task.Run(() => Console.ReadLine());
        }

        public TcpClient getClient()
        {
            return this.client;
        }


    }
}
