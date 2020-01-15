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
            string SERVERIP;
            int PORT;

            while(true)
            {
                try
                {
                    Console.Write("SERVER IP: ");
                    SERVERIP = Console.ReadLine();

                    Console.Write("PORT: ");
                    PORT = int.Parse(Console.ReadLine());
                    
                    this.client = new TcpClient(SERVERIP, PORT);
                    break;
                }
                
                catch (SocketException e)
                {
                    Console.WriteLine("SocketExcept: {0}", e);
                    Console.WriteLine("Please try again");
                }
            }
            
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

            Console.WriteLine("Successfully sent message");
            return;
          
        }

        private void receiver (NetworkStream stream)
        {
            byte[] respData = new byte[1024];
            int bytesRead = 0;
            StringBuilder message = new StringBuilder();

            if (stream.CanRead)
            {
                do
                {
                    bytesRead = stream.Read(respData, 0, respData.Length);
                    message.AppendFormat("{0}", Encoding.ASCII.GetString(respData, 0, bytesRead));

                } while (stream.DataAvailable);

                Console.Write(message);
            }      
        
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
