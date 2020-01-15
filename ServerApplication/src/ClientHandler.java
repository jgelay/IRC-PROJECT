import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ClientHandler implements Runnable {
	private Socket socket;
	private String serverPass = "TESTING";
	
	private String ERR_PASSWDMISMATCH = "464";
	private String ERR_NONICKNAMEGIVEN = "431";
	
	
	ChannelManager cm;
	Channel currChannel;
	
	private InputStream is;
	private BufferedReader br;
	private OutputStreamWriter wr;
	
	private String nickName;
	
	public ClientHandler(Socket clientSocket, ChannelManager cm) {
		this.socket = clientSocket;
		this.cm = cm;
	}
	
	public void run() {
		try 
		{
			String[] messageArray = null;
			is = socket.getInputStream();
			br  = new BufferedReader(new InputStreamReader(is));
			String r = "Hello from Server";
			
			// make sure to send something back ...
			
	        wr = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII);
	        System.out.println(r);
	        client_Registration(br,wr,serverPass);
	       
			while (!r.equals("end")) {
				if (br.ready()) {
					r = br.readLine();
	        		messageArray = r.split(" ");
	        		System.out.println(r);
	        		if (messageArray[1].equals("PRIVMSG")) {
	        			for (ClientHandler client : currChannel.getMemberList()) {
							//if (!messageArray[0].equals(client.getNickName())){
								System.out.println("Sending message to recepient " + client.getNickName());
								System.out.println("Message is: " + messageArray[2]);
								client.getOutPutStreamWriter().write(messageArray[2] + '\n');
								client.getOutPutStreamWriter().flush();
							//}
						}
	        		}
					
				}
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
	
	private OutputStreamWriter getOutPutStreamWriter() {
		return wr;
	}
	
	private void client_Registration(BufferedReader br, OutputStreamWriter wr, String serverPass) {
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
	            	if (!cm.getNickNameList().contains(messageArray[1]) || cm.getNickNameList().isEmpty()) {
	            		nickNameSet = true;
	            		cm.getNickNameList().add(messageArray[1]);
	            		for (String name : cm.getNickNameList()) {
	            			System.out.println(name);
	            			nickName = name;
	            		}
	            		wr.write(":" + "example.server " + "USER" + "\n");
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
				messageArray = resp.
						split(" ");
				
				if (messageArray[0].equals("USER")) {
	            	if (!cm.getUserList().contains(messageArray[1]) || cm.getUserList().isEmpty()) {
	            		nickNameSet = true;
	            		userRegistered = true;
	            		cm.getUserList().add(messageArray[1]);
	            		wr.write(":" + "example.server " + "001" + "\n");
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
			
			this.join_Channel("#default");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return;
	}
	
	private String getNickName() {
		return nickName;
	}
	
	private void join_Channel(String channelName) {
		System.out.println("Joining channel " + channelName);
		cm.join(channelName, this);
		currChannel = cm.getChannel(channelName);
		return;
	}
	
}
