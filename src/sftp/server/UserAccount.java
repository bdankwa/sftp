package sftp.server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class UserAccount {
	private BufferedReader in ;
	private PrintWriter out ;
	
	public UserAccount(){    
		
	}
	
	boolean addUser(String username, String password){
		boolean returnVal = true;
		
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream("system")));
			out = new PrintWriter(new FileOutputStream("system"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		synchronized(this){			
			out.println(username + " " + password);			
			closeIO(); 
		}
		
		return returnVal;
	}
	
	boolean removeUser(String username){
		boolean returnVal = false;
		
		return returnVal;
	}
	
	boolean authenticateUser(String username, String password){
		boolean returnVal = false;
        String line;
        String[] credentials;
        
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream("system")));
			out = new PrintWriter(new FileOutputStream("system"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
        
        try {
			while((line = in.readLine()) != null){
				credentials = line.split(" "); 
				if(credentials[0].equals(username) && credentials[1].equals(password)){
					returnVal = true;
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        closeIO();            

		return returnVal;
	}
	
	private void closeIO(){
        try {
			in.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}
	
	private boolean findUser(String username){
		
		return true;
	}

}
