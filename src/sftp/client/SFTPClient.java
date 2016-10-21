package sftp.client;

import java.io.IOException;
import java.net.Socket;


public class SFTPClient {
	
	public static void main (String[] args) throws Exception{
		System.out.println("Client Started...");
		String serverAddress = "127.0.0.1";
		int retries = 3;
		int corruptFile = 0;

		try{
			Socket socket = new Socket(serverAddress, 32011);
			SFTPShell shell = new SFTPShell(socket, retries, (corruptFile == 1));
			shell.run();			
		}catch(IOException  e){
			e.printStackTrace();
		}
	}

}
