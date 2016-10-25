package sftp.client;

import java.io.IOException;
import java.net.Socket;


public class SFTPClient {
	
	public static void main (String[] args) throws Exception{
		System.out.println("******************************************************");
		System.out.println("* Welcome to secure file transfer (SFTP) version 1.0 *");
		System.out.println("* To login, enter username and password              *");
		System.out.println("* To register, enter \"register\"                      *");
		System.out.println("* To download, enter file names separated by spaces  *");
		System.out.println("* To exit, enter \"q\"                                 *");
		System.out.println("*****************************************************");
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
