package sftp.server;

import java.util.HashMap;
import java.util.Map;

public class UserAccount {
	private Map<String, String> users = new HashMap<String, String>();
	
	public UserAccount(){
		
	}
	
	boolean addUser(String username, String password){
		boolean returnVal = false;
		
		synchronized(this){
			if(users.put(username, password) != null){
				returnVal = true;
			}	
		}	
		return returnVal;
	}
	
	boolean removeUser(String username){
		boolean returnVal = false;
		
		synchronized(this){
			if(users.remove(username) != null){
				returnVal = true;
			}
		}		
		return returnVal;
	}
	
	boolean authenticateUser(String username, String password){
		if(users.containsKey(username)){
			if(users.get(username).equals(password)){
				return true;
			}
		}		
		return false;
	}

}
