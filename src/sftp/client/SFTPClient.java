package sftp.client;

import java.io.IOException;
import java.net.Socket;


public class SFTPClient {
	
	public static void main (String[] args) throws Exception{
		System.out.println("Client Started...");
		//String serverAddress = "127.0.0.1";
		String serverAddress = args[0];
		int retries = Integer.parseInt(args[1]);

		try{
			Socket socket = new Socket(serverAddress, 32011);
			SFTPShell shell = new SFTPShell(socket, retries);
			shell.run();			
		}catch(IOException  e){
			e.printStackTrace();
		}
	}

}
