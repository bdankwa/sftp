package sftp.server;

import java.net.ServerSocket;

public class SFTPServer {	
	
	public static void main (String[] args) throws Exception{
		System.out.println("Server Started...");
		ServerSocket listener = new ServerSocket(32011);
		try{
			while(true){
				new SFTPWorker(listener.accept()).start();
			}
			
		}finally{
			listener.close();
		}
	}

}
