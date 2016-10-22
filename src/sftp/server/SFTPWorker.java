package sftp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import sftp.SFTPCrypto;
import sftp.SFTPFile;

public class SFTPWorker extends Thread{
	private Socket socket;
	private BufferedReader   in;
	private PrintWriter   out;
	private boolean corrupt;
	static int clientID;
	
	public SFTPWorker(Socket sock, boolean corrupt){
		this.socket = sock;
		this.corrupt = corrupt;
		//this.clientID = 0;
		
		try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public void run(){
		SFTPFile file = null;
		System.out.println("Client " + (++clientID) + " connected.");
		// TODO Read parameters from socket stream
		// Read files from disk
		// Encrypt files
		// Send files over socket stream to client.
		UserAccount account = new UserAccount();
		try {
			while(true){
				//String clientCommand = in.readLine();
				String clientCommand = SFTPCrypto.decrypt(in.readLine());
				//System.out.print(clientCommand);
				if(clientCommand.contains("register")){
					String[] registrationInfo = clientCommand.split(" ");
					//process register
					// if successful, send LOGIN
					if(account.addUser(SFTPCrypto.encrypt(registrationInfo[1]), 
							SFTPCrypto.encrypt(registrationInfo[2]))){
						String response = SFTPCrypto.encrypt("LOGIN");
						out.println(response);
					}					
					//System.out.println("SENT LOGIN to client.");
				}
				else if(clientCommand.contains("login")){
					//System.out.println("about to authenticate");
					String[] userPass = clientCommand.split(" ");
					//process login
					// if successful, send DOWNLOAD
					if(account.authenticateUser(SFTPCrypto.encrypt(userPass[1]),
							SFTPCrypto.encrypt(userPass[2]))){
						String response = SFTPCrypto.encrypt("DOWNLOAD");
						out.println(response);
						
						file = new SFTPFile();
						//System.out.println("SENT DOWNLOAD to client.");
					}
					else{
						String response = SFTPCrypto.encrypt("AUTH_FAILED");
						out.println(response);
					}

				}
				else if(clientCommand.contains("download")){
					// process download
					// if successful, send SUCCESS	
					String[] files = clientCommand.split(" ");
					String fileName = files[1];
					
					if(!fileName.contains(" ")){
						if(file.transmit(socket, fileName, corrupt)){
							//out.println("SUCCESS");
						}
					}
					
					//System.out.println("SENT SUCCESS to client.");					
				}
				else if(clientCommand.contains("quit")){
					System.out.println("quiting.");
					break;
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
