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
import java.util.Arrays;
import java.util.List;
import java.util.Random;



public class SFTPFile {
	
	boolean foundFile = false;
	
	public final static int MAX_FILE_SIZE = 5120000; // 5MB
	public final static byte FILE_ON_DISK = (byte) 0xAA; 
	public final static byte FILE_NOT_ON_DISK = (byte) 0xDD; 
	
	public SFTPFile(){

	}
	
	public boolean transmit(Socket socket, String fileName, boolean curruptFile){
		
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
				
				byte[] cypherText = SFTPCrypto.encrypt(fileBytes);
				
				if(curruptFile){
					cypherText[1] = 0x25;
				}
				
				//TODO encrypt file
				out.write(cypherText);
				
				out.flush();
				fis.close();
				bis.close();

				//System.out.println("Server: sent : " + fileName + "of length " + f.length() + " to client" );
				
				status = true;
				
			}
			else{
				byte[] fileBytes = new byte[1];
				for(int i=0; i < 1; i++){
					fileBytes[i] = FILE_NOT_ON_DISK;
				}
				out.write(SFTPCrypto.encrypt(fileBytes));
				status = false;
			}
			
		}catch (Exception e) {
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
			
			byte[] tempBytes = new byte[MAX_FILE_SIZE];
						
			bis = new BufferedInputStream(is);
			bytesRead = bis.read(tempBytes, offset, tempBytes.length);
			offset = bytesRead;
						
			//System.out.println("offset : " + offset);
			
			//TODO dycrypt file and set decryptionStatus
			byte[] cypherBytes = Arrays.copyOf(tempBytes, bytesRead);			
						
			byte[] fileBytes = SFTPCrypto.decrypt(cypherBytes);
			
			if(fileBytes[0] == FILE_ON_DISK){
				fos = new FileOutputStream(fileName);
				bos = new BufferedOutputStream(fos);
				//System.out.println("fileBytes len : " + fileBytes.length);
				bos.write(fileBytes, 1, fileBytes.length -1);
				bos.flush();
				//System.out.println("Cleint received : " + fileName);
				foundFile = true;
				decryptionStatus = true;
				fos.close();
				bos.close();
			}
			else if(fileBytes[0] == FILE_NOT_ON_DISK){
				//System.out.println("Cleint : server couldn't fine " + fileName);
				foundFile = false;
				decryptionStatus = true;
			}
			else{
				decryptionStatus = false;
			}
		
		} catch (Exception e) {
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
