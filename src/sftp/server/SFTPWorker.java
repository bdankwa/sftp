package sftp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SFTPWorker extends Thread{
	private Socket socket;
	private BufferedReader   in;
	private PrintWriter   out;
	
	public SFTPWorker(Socket sock){
		this.socket = sock;
		try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void run(){
		System.out.println("Some client connected...");
		// TODO Read parameters from socket stream
		// Read files from disk
		// Encrypt files
		// Send files over socket stream to client.
		try {
			while(true){
				String clientCommand = in.readLine();
				System.out.print(clientCommand);
				if(clientCommand.contains("register")){
					//process register
					// if successfull, send LOGIN
					out.println("LOGIN");
					System.out.println("SENT LOGIN to client.");
				}
				else if(clientCommand.contains("login")){
					//process login
					// if successfull, send DOWNLOAD
					out.println("DOWNLOAD");
					System.out.println("SENT DOWNLOAD to client.");
				}
				else if(clientCommand.contains("download")){
					// process download
					// if successfull, send SUCCESS
					out.println("SUCCESS");
					System.out.println("SENT SUCCESS to client.");
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
