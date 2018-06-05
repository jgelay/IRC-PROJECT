import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;

public class Server {
	ServerSocket serverSocket = null;
	Socket clientSocket = null;
	String serverPass = "TESTING"; 
	
	String ERR_PASSWDMISMATCH = "464";
	
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
		ask_password(br,wr,serverPass);
	}
	
	public void ask_password(BufferedReader br, OutputStreamWriter wr, String serverPass) {
		String[] password = null;
		String resp;
		
		System.out.println("Asking for Password Now");
		try {
			
			
			resp = br.readLine();
			password = resp.split(" ");
			
			if (password[0].equals("PASS")) {
            	if (password[1].equals(serverPass)) {
            		System.out.println(":" + "example.server " + "NICK" + "\n");
            	} else {
            		wr.write(":" + "example.server " + ERR_PASSWDMISMATCH + "\n");
            		wr.flush();
            	}
            } else {
            	throw new IOException();
            }
			
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void set_nickName(BufferedReader br, OutputStreamWriter wr, String serverPass) {
		
	}
}
