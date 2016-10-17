package sftp;

public class SFTPState {
	
	public enum sftp_state{
		REGISTER,
		LOGIN,
		DOWNLOAD
	}
	
	public sftp_state state;
	
	public SFTPState(){
		state = sftp_state.LOGIN;
	}

}
