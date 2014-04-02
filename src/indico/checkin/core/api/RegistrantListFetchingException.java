package indico.checkin.core.api;

public class RegistrantListFetchingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;

	public RegistrantListFetchingException(){
		this.message = "";
	}
	
	public RegistrantListFetchingException(String msg){
		this.message = msg;
	}
	
	public String getMessage(){
		return this.message;
	}
}
