import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.*;

public class Server {
	ServerSocket serverSocket = null;
	Socket clientSocket = null;
	String serverPass = "TESTING"; 
	
	String ERR_PASSWDMISMATCH = "464";
	String ERR_NONICKNAMEGIVEN = "431";
	
	ArrayList<String> nickNamesList = new ArrayList<String>();
	ArrayList<String> userList = new ArrayList<String>();
	ChannelManager cm = new ChannelManager();
	
	public Server(int port, int numOfClients) {
		try 
		{
			InetAddress IP = InetAddress.getLocalHost();
			serverSocket = new ServerSocket(port, numOfClients, IP);
			
			System.out.println("IP of my system is := " + IP.getHostAddress());
			System.out.println("Port of server is := " + serverSocket.getLocalPort());
		}
		catch (IOException e){
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}
	
	public void accept_clients() throws IOException {
		while(true) {
			try 
			{
				clientSocket = serverSocket.accept();
				System.out.println("Client Connected from " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
			}	
			catch (Exception e) 
			{
				System.out.println("Can't accept client connection");
			}
			new ClientHandler(clientSocket, cm).start();
		}
		
	}
}
