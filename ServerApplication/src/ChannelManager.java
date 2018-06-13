import java.util.concurrent.CopyOnWriteArrayList;

public class ChannelManager {
	private CopyOnWriteArrayList<String> nickNamesList = new CopyOnWriteArrayList<String>();
	private CopyOnWriteArrayList<String> userList = new CopyOnWriteArrayList<String>();
	
	public CopyOnWriteArrayList<String> getNickNameList() {
		return nickNamesList;
	}
	
	public CopyOnWriteArrayList<String> getUserList() {
		return userList;
	}
}
