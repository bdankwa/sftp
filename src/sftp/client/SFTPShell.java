package sftp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import sftp.SFTPCrypto;
//import sftp.SFTPCrypto;
import sftp.SFTPFile;
import sftp.SFTPState;
import sftp.SFTPState.sftp_state;

public class SFTPShell {
	
	private Socket socket;
	private SFTPState st;
	private BufferedReader   in;
	private PrintWriter   out;
	private SFTPFile downloadFile;
	int retries;
	
	public SFTPShell(Socket sock, int retries){
		this.socket = sock;
		this.retries = retries;	
		
		st = new SFTPState();
		downloadFile = new SFTPFile();
		try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
	
	public void run(){
		BufferedReader stdinReader = null;
		String base_prompt = "SecFTP:";
		String prompt = base_prompt;
		
		while(true){
			if(st.state == sftp_state.LOGIN){
				prompt = base_prompt + "Login>";
			}
			else if(st.state == sftp_state.REGISTER){
				prompt = base_prompt + "Register>";
			}
			else{
				prompt = base_prompt + "Download>";
			}
			System.out.print(prompt);
			try {
				/*byte[] test = SFTPCrypto.encrypt("plaintext".getBytes());
				System.out.println(new String(SFTPCrypto.decrypt(test)));*/

				stdinReader = new BufferedReader(new InputStreamReader(System.in));
				String command = stdinReader.readLine();
				
				if(!(command.equals("q"))){
					if(st.state == sftp_state.REGISTER){						
						String[] arguments = command.split(" ");
						if(arguments.length != 2){
							System.out.println("Usage: username password");
						}
						else{
							command = "#register" + " " + command;
							processCommand(command);
						}
					}
					else if(st.state == sftp_state.LOGIN){
						String[] arguments = command.split(" ");
						if(command.contains("register")){
							processCommand("#"+command);
						}
						else if(arguments.length != 2){
							System.out.println("Usage: username password");
						}
						else{
							command = "#login" + " " + command;
							processCommand(command);
						}
					}
					else if(st.state == sftp_state.DOWNLOAD){
						String[] arguments = command.split(" ");
						if(arguments.length == 0 || command.contains("register")){
							System.out.println("Usage: file1, file2....filen");
						}
						else{
							command = "#download" + " " + command;
							//System.out.println("Download " + command);
							if(processCommand(command)){
								// files downloaded successfully, terminate.
								//out.println(SFTPCrypto.encrypt("quit"));
								//closeIOs();
								//break;
							}
						}						
					}
					else{
						System.out.println("Unrecognized command");
					}					
				}
				else{
					out.println(SFTPCrypto.encrypt("#quit"));
					closeIOs();
					break;
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}		
	
	}
	
	private boolean processCommand(String command) throws Exception{
		boolean status = false;	
		
		switch(st.state){
		case REGISTER :
			String[] arguments = command.split(" ");
			if(command.contains("#register") && (arguments.length == 3)){
				//TODO send command to server
				// Receive ACK
				// Take action based on ACK
				// if ACK move to login, set status to true
				out.println(SFTPCrypto.encrypt(command));
				System.out.println("Client sent register command..");
				try {
					String response = SFTPCrypto.decrypt(in.readLine());
					if(response.contains("LOGIN")){
						st.state = sftp_state.LOGIN;
						status = true;
						System.out.println("Client received login command..");
					}
					else{
						st.state = sftp_state.REGISTER;
						status = false;
					}					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else{
				System.out.println("Usage:");
				st.state = sftp_state.REGISTER;
				status = false;
			}
			break;
		case LOGIN :
			if(command.contains("#login")){
				//TODO send command to server
				// Receive ACK
				// Take action based on ACK
				// if ACK move to download, set status to true
				out.println(SFTPCrypto.encrypt(command));
				try {
					//System.out.println("Waiting for DOWNLOAD from server");
					String response = SFTPCrypto.decrypt(in.readLine());
					if(response.contains("DOWNLOAD")){
						st.state = sftp_state.DOWNLOAD;
						status = true;
						//System.out.println("Received DOWNLOAD command");
					}
					else{
						st.state = sftp_state.LOGIN;
						System.out.println("Authentication failed on server, please try again");
						status = false;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			else if(command.contains("#register")){
				st.state = sftp_state.REGISTER;
			}
			else{
				System.out.println("Please log in to the system to download files");
				status = false;
			}
			break;
		case DOWNLOAD :
			//System.out.println("In DOWNLOAD state");
			if(command.contains("#download")){
				List<String> validFiles = null;
				List<String> invalidFiles = null;
				List<String> corruptFiles = null;
				
				//TODO send command to server
				// Receive ACK
				// Take action based on ACK
				// if all good set status to true
				// stay in download until quit.
					
				String files = command.substring(10);
				String[] fileNames = files.split(" ");
				
				for(String s : fileNames){
					//System.out.println("looping..");
					if(!s.contains(" ")){
						
						for(int i=0; i< retries; i++){
							out.println(SFTPCrypto.encrypt(("#download " + s)));
							
							if(downloadFile.receive(socket, s)){
								//TODO 
								if(!downloadFile.receivedFile()){
									System.out.println("Error: file " + s + " does not exist on server : ");
								}
								else{
									System.out.println("Client downloaded (" + s + ")." );
								}								
								break;
							}
							else{ // Corrupt file
								// TODO Retry for a couple of times
								//st.state = sftp_state.DOWNLOAD;
								if(i <= (retries - 2)){
									System.out.println("File :" + s + " was currupted in transit, retrying... " + (i+1) );
								}
								else{
									System.out.println("File :" + s + " was currupted in transit, retrying... " + (i+1) );
									System.out.println("Unable to download " + s + " after " + (i+1) + " tries." );
								}								
							}							
						}												
					}
				}
				status = true;
			}
			break;			
		
		}
		
		return status;
	}
	
	private void closeIOs(){
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
