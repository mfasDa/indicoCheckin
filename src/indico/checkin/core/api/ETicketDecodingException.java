package indico.checkin.core.api;

public class ETicketDecodingException extends Exception {

	/**
	 * Exception from the json decoding of the indico e-Ticket
	 * License: GPLv3 (a copy of the license is provided with the package)
	 * 
	 * @author: Markus Fasel
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
