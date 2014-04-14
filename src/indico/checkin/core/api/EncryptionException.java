package indico.checkin.core.api;

public class EncryptionException extends Exception {

	/**
	 * Exception class handling signature problems for the communication with the 
	 * indico server
	 * License: GPLv3 (a copy of the license is provided with the package)
	 *
	 * @author: Markus Fasel
	 */
	
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public EncryptionException(){
		this.message = "";
	}

	public EncryptionException(String msg){
		this.message = msg;
	}

	@Override
	public String getMessage(){
		return this.message;
	}
}
