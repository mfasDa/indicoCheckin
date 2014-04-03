package indico.checkin.core.api;

public class ETicketDecodingException extends Exception {

	/**
	 * Exception from the json decoding
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public ETicketDecodingException(){
		this.message = "";
	}

	public ETicketDecodingException(String msg){
		this.message = msg;
	}
	
	@Override
	public String getMessage(){
		return this.message;
	}
}
