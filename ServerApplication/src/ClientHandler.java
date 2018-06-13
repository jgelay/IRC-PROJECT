import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.*;

public class ClientHandler extends Thread {
	private Socket socket;
	private String serverPass = "TESTING";
	
	private String ERR_PASSWDMISMATCH = "464";
	private String ERR_NONICKNAMEGIVEN = "431";
	
	ChannelManager channel;
	public ClientHandler(Socket clientSocket, ChannelManager cm) {
		this.socket = clientSocket;
		channel = cm;
	}
	
	public void run() {
		try 
		{
			InputStream is = socket.getInputStream();
			BufferedReader br  = new BufferedReader(new InputStreamReader(is));
			String r = "Hello from Server";
			
			// make sure to send something back ...
			
	        OutputStreamWriter wr = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII);
	        System.out.println(r);
	        client_registration(br,wr,serverPass);
	       
			while (!r.equals("end")) {
				
			}
		
		}
		catch (Exception e)
		{
			
		}
		
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void client_registration(BufferedReader br, OutputStreamWriter wr, String serverPass) {
		String[] messageArray = null;
		String resp;
		boolean passwordMatched = false;
		boolean nickNameSet = false;
		boolean userRegistered = false;
		try {
			do {
				System.out.println("Asking for Password");
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
			} while (passwordMatched == false);
			
			do {
				resp = br.readLine();
				messageArray = resp.split(" ");
				
				if (messageArray[0].equals("NICK")) {
	            	if (!channel.getNickNameList().contains(messageArray[1]) || channel.getNickNameList().isEmpty()) {
	            		nickNameSet = true;
	            		channel.getNickNameList().add(messageArray[1]);
	            		for (String name : channel.getNickNameList()) {
	            			System.out.println(name);
	            		}
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
			} while (nickNameSet == false);
			
			do {
				resp = br.readLine();
				messageArray = resp.split(" ");
				
				if (messageArray[0].equals("USER")) {
	            	if (!channel.getUserList().contains(messageArray[1]) || channel.getUserList().isEmpty()) {
	            		nickNameSet = true;
	            		channel.getUserList().add(messageArray[1]);
	            		wr.write(":" + "example.server " + "NICK" + "\n");
	            		wr.flush();
	            	} else if (messageArray.length < 4) {
	            		
					} else {
	            		wr.write(":" + "example.server " + ERR_PASSWDMISMATCH + "\n");
	            		wr.flush();
	            	}
	            } else {
	            	throw new IOException();
	            }
			} while (userRegistered == false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return;
	}
	
}
