package sftp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import sftp.SFTPFile;

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
		SFTPFile file = null;
		System.out.println("Some client connected...");
		// TODO Read parameters from socket stream
		// Read files from disk
		// Encrypt files
		// Send files over socket stream to client.
		UserAccount account = new UserAccount();
		try {
			while(true){
				String clientCommand = in.readLine();
				System.out.print(clientCommand);
				if(clientCommand.contains("register")){
					String[] registrationInfo = clientCommand.split(" ");
					//process register
					// if successful, send LOGIN
					if(account.addUser(registrationInfo[1], registrationInfo[2])){
						out.println("LOGIN");
					}					
					System.out.println("SENT LOGIN to client.");
				}
				else if(clientCommand.contains("login")){
					System.out.println("about to authenticate");
					String[] userPass = clientCommand.split(" ");
					//process login
					// if successful, send DOWNLOAD
					if(account.authenticateUser(userPass[1], userPass[2])){
						out.println("DOWNLOAD");
						
						file = new SFTPFile();
						System.out.println("SENT DOWNLOAD to client.");
					}
					else{
						out.println("AUTH_FAILED");
					}

				}
				else if(clientCommand.contains("download")){
					// process download
					// if successful, send SUCCESS	
					List<String> fileNames = null;
					if(file != null){
						if((fileNames = file.getValidFileNames(clientCommand.substring(9))) != null){
							if(file.transmit(socket, fileNames)){
								out.println("SUCCESS");
							}
						}
						if((fileNames = file.getInvalidFileNames()) != null){
							//TODO tell client these files don't exist
						}
					}
					
					System.out.println("SENT SUCCESS to client.");
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
