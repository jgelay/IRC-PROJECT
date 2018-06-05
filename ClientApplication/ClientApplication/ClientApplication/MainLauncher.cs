using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace ClientApplication
{
    class MainLauncher
    {
        static void Main(string[] args)
        {
            ClientApplication.Client client = new ClientApplication.Client();

            using (NetworkStream stream = client.getClient().GetStream())
            {
                client.passwordRegistration(stream);
            }
        }
    }
}
