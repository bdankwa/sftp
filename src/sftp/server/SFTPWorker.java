package sftp.server;

import java.net.Socket;

public class SFTPWorker extends Thread{
	private Socket socket;
	
	public SFTPWorker(Socket socket){
		this.socket = socket;
	}
	
	
	public void run(){
		System.out.println("Some client connected...");
		// TODO Read parameters from socket stream
		// Read files from disk
		// Encrypt files
		// Send files over socket stream to client.
	}

}
