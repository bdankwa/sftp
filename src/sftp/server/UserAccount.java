package sftp.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class UserAccount {
	private BufferedReader in ;
	private BufferedWriter out ;
	
	public UserAccount(){    
		
	}
	
	boolean addUser(String username, String password){
		boolean returnVal = true;
		
		openIOWrite();
		
		synchronized(this){			
			try {
				out.write(username + " " + password);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			closeIOWrite(); 
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
        
        openIORead();
        
        try {
			while((line = in.readLine()) != null){
				credentials = line.split(" "); 
				System.out.println("Account cred : " + credentials[0] + " " + credentials[1]);
				if(credentials[0].equals(username) && credentials[1].equals(password)){
					returnVal = true;
					break;
				}
			}
			System.out.println("Account input : " + username + " " + password);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        closeIORead();            

		return returnVal;
	}
	
	private void openIORead(){
		try {
			in = new BufferedReader(new FileReader("system"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private void openIOWrite(){
		
		try {
			out = new BufferedWriter(new FileWriter("system"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private void closeIORead(){
        try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}
	
	private void closeIOWrite(){
        try {
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
