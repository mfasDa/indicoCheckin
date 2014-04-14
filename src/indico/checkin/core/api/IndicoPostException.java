package indico.checkin.core.api;

public class IndicoPostException extends Exception {

	/**
	 * Exception handling post requests to the indico server which fail
	 * License: GPLv3 (a copy of the license is provided with the package)
	 * 
	 * @author: Markus Fasel
	 */
	private static final long serialVersionUID = 1L;
	private String message;

	public IndicoPostException(){
		message = "";
	}
	
	public IndicoPostException(String msg){
		message = msg;
	}
	
	@Override
	public String getMessage(){
		return message;
	}

}
