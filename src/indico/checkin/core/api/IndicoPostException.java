package indico.checkin.core.api;

public class IndicoPostException extends Exception {

	/**
	 * 
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
