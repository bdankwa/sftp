package sftp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import sftp.SFTPFile;
import sftp.SFTPState;
import sftp.SFTPState.sftp_state;

public class SFTPShell {
	
	private Socket socket;
	private SFTPState st;
	private BufferedReader   in;
	private PrintWriter   out;
	private SFTPFile downloadFiles;
	
	public SFTPShell(Socket sock){
		this.socket = sock;
		st = new SFTPState();
		downloadFiles = new SFTPFile();
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
				stdinReader = new BufferedReader(new InputStreamReader(System.in));
				String command = stdinReader.readLine();
				
				if(!(command.contains("q"))){
					if(command.contains("register")){
						//String[] arguments = command.split(" ");
						processCommand(command);					
					}
					else if(command.contains("login")){
						String[] arguments = command.split(" ");
						if(arguments.length != 3){
							System.out.println("Usage:");
						}
						else{
							processCommand(command);
						}							
					}
					else if(command.contains("download")){
						String[] arguments = command.split(" ");
						if(arguments.length <= 1){
							System.out.println("Usage:");
						}
						else{
							if(processCommand(command)){
								// files downloaded successfully, terminate.
								closeIOs();
								break;
							}
						}							
					}
					else{
						System.out.println("Unrecognized command");
					}
					
				}
				else{
					break;
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}		
	
	}
	
	private boolean processCommand(String command){
		boolean status = false;	
		
		switch(st.state){
		case REGISTER :
			String[] arguments = command.split(" ");
			if(command.contains("register") && (arguments.length == 3)){
				//TODO send command to server
				// Receive ACK
				// Take action based on ACK
				// if ACK move to login, set status to true
				out.println(command);
				System.out.println("Client sent register command..");
				try {
					if(in.readLine().contains("LOGIN")){
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
			if(command.contains("login")){
				//TODO send command to server
				// Receive ACK
				// Take action based on ACK
				// if ACK move to download, set status to true
				out.println(command);
				try {
					System.out.println("Waiting for DOWNLOAD from server");
					if(in.readLine().contains("DOWNLOAD")){
						st.state = sftp_state.DOWNLOAD;
						status = true;
						System.out.println("Received DOWNLOAD command");
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
			else if(command.contains("register")){
				st.state = sftp_state.REGISTER;
			}
			else{
				System.out.println("Please log in to the system to download files");
				status = false;
			}
			break;
		case DOWNLOAD :
			System.out.println("In DOWNLOAD state");
			if(command.contains("download")){
				List<String> validFiles = null;
				List<String> invalidFiles = null;
				List<String> corruptFiles = null;
				
				//TODO send command to server
				// Receive ACK
				// Take action based on ACK
				// if all good set status to true
				// stay in download until quit.
				out.println(command);				

				if(downloadFiles.receive(socket)){
					//TODO 
					validFiles = downloadFiles.getValidFileNames(command.substring(9));
					invalidFiles = downloadFiles.getInvalidFileNames();
					if(validFiles == null || invalidFiles != null){
						// invalid file, print
						System.out.println("Error: the following files do not exist on server : ");
						for(String s : invalidFiles){
							System.out.print(s + " ");
						}
						status = false;
					}
					status = true;
				}
				else{ // Corrupt file
					corruptFiles = downloadFiles.getCorruptFileNames();
					// TODO Retry for a couple of times
					//st.state = sftp_state.DOWNLOAD;
					
				}				
			}
			else{
				System.out.println("You're already logged in, you can download files now.");
				status = false;
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
