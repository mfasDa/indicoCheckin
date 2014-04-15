package indico.checkin.core.gui;

import indico.checkin.core.data.IndicoRegistrant;
import indico.checkin.core.data.IndicoRegistrantInfoField;
import indico.checkin.core.data.IndicoRegistrantInfoGroup;

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
		return 8;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String rowTitles[] = {"Last Name:", "First Name:", "Registrant ID:", "Paid:", "Checked in:", "Price:", "Amount Paid:", "Places for conference dinner;"};
		if(columnIndex == 0){
			return rowTitles[rowIndex];
		} else {
			Object entry = "";
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
					entry = new Boolean(registrant.hasPaid());
					break;
				case 4:
					entry = new Boolean(registrant.hasCheckedIn());
					break;
				case 5:
					entry = String.format("EUR %.2f", registrant.getFullPrice());
					break;
				case 6:
					entry = String.format("EUR %.2f", registrant.getFullInformation().getAmountPaid());
					break;
				case 7:
					// This is a special field for the quark matter conference (up to now)
					IndicoRegistrantInfoGroup dinnergroup = registrant.getFullInformation().findGroupByTitle("Lunch and conference dinner options");
					IndicoRegistrantInfoField selffield = dinnergroup.findFieldByCaption("Conference Dinner (included in the conference fee)");
					int nparticipants = selffield.getValue().contains("Yes") ? 1 : 0;
					// check also accompanying person
					IndicoRegistrantInfoField accompanyField = dinnergroup.findFieldByCaption("Accompanying person conference dinner");
					nparticipants += accompanyField.getValue().length() > 0 ? Integer.parseInt(accompanyField.getValue()) : 0;
					entry = String.format("%d", nparticipants);
				}
			}
			return entry;
		}
	}
	
	public IndicoRegistrant getData(){
		return registrant;
	}
	
	public void SetRegistrant(IndicoRegistrant reg){
		registrant = reg;
		this.fireTableRowsUpdated(0, 4);
	}

	public void setInfoUpdated() {
		this.fireTableRowsUpdated(0, 4);
	}
}
