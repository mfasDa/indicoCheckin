package indico.checkin.core.api;

public class IndicoURLBuilderException extends Exception {

	/**
	 * Exception for any kind of problems in creating the URL for the API accesss
	 */
	private static final long serialVersionUID = 1L;
	private String message;

	public IndicoURLBuilderException(){
		message = "";
	}
	
	public IndicoURLBuilderException(String msg){
		message = msg;
	}

	@Override
	public String getMessage(){
		return message;
	}
}
