package indico.checkin.core.api;

public class IndicoAuthException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;

	IndicoAuthException(String message){
		this.message = message;
	}
	
	@Override
	public String getMessage(){
		return message;
	}
}
