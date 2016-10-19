package sftp;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SFTPFile {
	
	List<String> validFileNames;
	List<String> invalidFileNames;
	List<String> curruptFiles;
	
	public SFTPFile(){
		validFileNames = new ArrayList<String>();
		invalidFileNames = new ArrayList<String>();;
		curruptFiles = new ArrayList<String>();;
	}
	
	public boolean transmit(Socket socket, List<String> fileNames){
		
		// Encrypt file and transmit on socket
		for(String s: fileNames){
			
			System.out.println("Server: sent : " + s + " to client");
			//validFileNames.add(s);
			
		}
		
		return true;
	}
	
	public boolean receive(Socket socket){
		
		// Read file from socket and decrypt
		
		System.out.println("Cleint: recevived files");
		
		// Save the names of received files		
	
		return true;
	}
	
	public List<String> getValidFileNames(String fileNames ){
		
		String[] fnames = fileNames.split(" ");
		for(String s: fnames){
			// TODO check if files exists
			// if file exist save name
			validFileNames.add(s);
			
			// if doesn't exits save as well
			invalidFileNames.add(s);
		}
		
		return validFileNames;
	}
	
	public List<String> getInvalidFileNames(){
		
		return invalidFileNames;
	}
	
	public List<String> getCorruptFileNames(){
		
		return curruptFiles;
	}
	
	private boolean encrypt(){
		
		return true;
	}
	
	private boolean decrypt(){
		
		return true;
	}

}
