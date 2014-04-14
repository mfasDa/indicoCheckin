package indico.checkin.core.gui;

public class EntryListBoundaryException extends Exception {

	/**
	 * Class for exception in the selection of the registrant via manual search
	 * License: GPLv3 (a copy of the license is provided with the package)
	 * 
	 * @author: Markus Fasel
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
