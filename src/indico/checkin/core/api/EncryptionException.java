package indico.checkin.core.api;

public class EncryptionException extends Exception {

	/**
	 * 
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
