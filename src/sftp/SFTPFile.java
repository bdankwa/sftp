package sftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;



public class SFTPFile {
	
	List<String> validFileNames;
	List<String> invalidFileNames;
	List<String> curruptFiles;
	
	boolean foundFile = false;
	
	public final static int MAX_FILE_SIZE = 5120000; // 5MB
	public final static byte FILE_ON_DISK = (byte) 0xAA; 
	public final static byte FILE_NOT_ON_DISK = (byte) 0xDD; 
	
	public SFTPFile(){
		validFileNames = new ArrayList<String>();
		invalidFileNames = new ArrayList<String>();;
		curruptFiles = new ArrayList<String>();;
	}
	
	public boolean transmit(Socket socket, String fileName){
		
		boolean status = false;
		
		FileInputStream fis;
		BufferedInputStream bis;
		OutputStream out;
		
		try {
			out = socket.getOutputStream();
			
			if(fileExists(fileName)){
				File f = new File(fileName);
				byte[] fileBytes = new byte[(int)f.length() + 1];
				fis = new FileInputStream(f);
				bis = new BufferedInputStream(fis);
				
				fileBytes[0] = FILE_ON_DISK;
				bis.read(fileBytes, 1, (int)f.length());
				
				//TODO encrypt file
				out.write(fileBytes);
				
				out.flush();
				fis.close();
				bis.close();

				System.out.println("Server: sent : " + fileName + "of length " + f.length() + " to client" );
				
				status = true;
				
			}
			else{
				byte fileBytes = FILE_NOT_ON_DISK;
				out.write(fileBytes);
				status = false;
			}
			
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return status;
	}
	
	public boolean receive(Socket socket, String fileName){
		
		boolean decryptionStatus = false;
		
	    FileOutputStream fos;
	    BufferedOutputStream bos;
	    BufferedInputStream bis;
	    InputStream is;
	    int bytesRead;
	    int offset = 0;
		
		// Read file from socket and decrypt
		
		try {
			is = socket.getInputStream();
			
			byte[] fileBytes = new byte[MAX_FILE_SIZE];
			
			bis = new BufferedInputStream(is);
			bytesRead = bis.read(fileBytes, offset, fileBytes.length);
			offset = bytesRead;
						
			System.out.println("offset : " + offset);
			
			//TODO dycrypt file and set decryptionStatus
			
			if(fileBytes[0] == FILE_ON_DISK){
				fos = new FileOutputStream(fileName);
				bos = new BufferedOutputStream(fos);
				bos.write(fileBytes, 1, offset-1);
				bos.flush();
				System.out.println("Cleint received : " + fileName);
				foundFile = true;
				fos.close();
				bos.close();
			}
			else if(fileBytes[0] == FILE_NOT_ON_DISK){
				System.out.println("Cleint : server couldn't fine " + fileName);
				foundFile = false;
			}			
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return decryptionStatus;
	}
	
	private boolean fileExists(String fileName ){
		
		File f = new File(fileName);
		if(f.exists() && !f.isDirectory()){
			System.out.println("Server found file (" + fileName + ") on disk");
			return true;
		}
		System.out.println("Server did not find file (" + fileName + ") on disk");
		return false;

	}
	
	public boolean receivedFile(){
		
		return foundFile;
	}
	
	
	private boolean encrypt(){
		
		return true;
	}
	
	private boolean decrypt(){
		
		return true;
	}

}
