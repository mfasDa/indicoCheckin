package indico.checkin.core.api;

public class RegistrantBuilderException extends Exception {

	/**
	 *  Absorbs all possible exception in the indico API connection
	 *  and forwards a message string to classes using the transfer
	 *  (i.e. GUI)
	 */
	private static final long serialVersionUID = 1L;
	private String message;

	public RegistrantBuilderException(){
		message = "";
	}
	
	public RegistrantBuilderException(String msg){
		message = msg;
	}
	
	@Override
	public String getMessage(){
		return message;
	}
}