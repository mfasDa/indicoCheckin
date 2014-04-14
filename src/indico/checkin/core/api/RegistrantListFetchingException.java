package indico.checkin.core.api;

public class RegistrantListFetchingException extends Exception {

	/**
	 * Absorbs all exceptions in fetching the event's registrant list
	 * License: GPLv3 (a copy of the license is provided with the package)
	 * 
	 *  @author: Markus Fasel
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
