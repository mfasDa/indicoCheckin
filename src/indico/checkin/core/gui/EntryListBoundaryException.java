package indico.checkin.core.gui;

public class EntryListBoundaryException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int countSelected;
	private int rowRequested;
	
	public EntryListBoundaryException(int nsel, int rsel){
		countSelected = nsel;
		rowRequested = rsel;
	}
	
	@Override
	public String getMessage(){
		return String.format("Boundary exceeded: %d entries, but number %d requested", countSelected, rowRequested);
	}
}
