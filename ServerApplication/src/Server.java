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
	
	public void relay_message() throws IOException {
		try 
		{
			clientSocket = serverSocket.accept();
			System.out.println("Client Connected from " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
		}	
		catch (Exception e) 
		{
			System.out.println("Can't accept client connection");
		}
		try 
		{
			InputStream is = clientSocket.getInputStream();
			BufferedReader br  = new BufferedReader(new InputStreamReader(is));
			String r = "Hello from Server";
			
			// make sure to send something back ...
			
	        OutputStreamWriter wr = new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.US_ASCII);
	        System.out.println(r);
	        client_registration(br,wr,serverPass);
	       
			while (!r.equals("end")) {
				
			}
		
		}
		catch (Exception e)
		{
			
		}
		
		serverSocket.close();
			
	}
	
	public void client_registration(BufferedReader br, OutputStreamWriter wr, String serverPass) {
		String[] messageArray = null;
		String resp;
		boolean passwordMatched = false;
		boolean nickNameSet = false;
		try {
			do {
				resp = br.readLine();
				messageArray = resp.split(" ");
				
				if (messageArray[0].equals("PASS")) {
	            	if (messageArray[1].equals(serverPass)) {
	            		passwordMatched = true;
	            		wr.write(":" + "example.server " + "NICK" + "\n");
	            		wr.flush();
	            	} else if (messageArray.length < 2) {
	            		
					} else {
	            		wr.write(":" + "example.server " + ERR_PASSWDMISMATCH + "\n");
	            		wr.flush();
	            	}
	            } else {
	            	throw new IOException();
	            }
			} while (passwordMatched == true);
			
			do {
				resp = br.readLine();
				messageArray = resp.split(" ");
				
				if (messageArray[0].equals("NICK")) {
	            	if (nickNamesList.contains(messageArray[1]) || nickNamesList.isEmpty()) {
	            		nickNameSet = true;
	            		wr.write(":" + "example.server " + "NICK" + "\n");
	            		wr.flush();
	            	} else if (messageArray.length < 2) {
	            		
					} else {
	            		wr.write(":" + "example.server " + ERR_PASSWDMISMATCH + "\n");
	            		wr.flush();
	            	}
	            } else {
	            	throw new IOException();
	            }
			} while (nickNameSet == true);
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return;
	}

}
