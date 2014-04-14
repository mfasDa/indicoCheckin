package indico.checkin.core.gui;

import indico.checkin.core.data.IndicoRegistrant;

import javax.swing.table.AbstractTableModel;

public class RegistrantInfoDisplayModel extends AbstractTableModel {

	/**
	 * Model for table display, defining the information which has to be shown in
	 * the registrant info display
	 * License: GPLv3 (a copy of the license is provided with the package)
	 * 
	 * @author: Markus Fasel
	 */
	private static final long serialVersionUID = 1L;
	private IndicoRegistrant registrant;
	
	public RegistrantInfoDisplayModel(){
		registrant = null;
	}

	@Override
	public int getRowCount() {
		return 6;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String rowTitles[] = {"Last Name:", "First Name:", "Registrant ID:", "Paid:", "Checked in:", "Price:"};
		if(columnIndex == 0){
			return rowTitles[rowIndex];
		} else {
			String entry = "";
			if(registrant != null){
				switch(rowIndex){
				case 0: 
					entry = registrant.getSurname();
					break;
				case 1:
					entry = registrant.getFirstName();
					break;
				case 2:
					entry = String.format("%d", registrant.getID());
					break;
				case 3:
					entry = registrant.hasPaid() ? "yes" : "no";
					break;
				case 4:
					entry = registrant.hasCheckedIn() ? "yes" : "no";
					break;
				case 5:
					entry = String.format("EUR %.2f", registrant.getFullPrice());
					break;
				}
			}
			return entry;
		}
	}
	
	public void SetRegistrant(IndicoRegistrant reg){
		registrant = reg;
		this.fireTableRowsUpdated(0, 4);
	}

	public void setInfoUpdated() {
		this.fireTableRowsUpdated(0, 4);
	}
}
