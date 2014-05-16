/****************************************************************************
 *  Copyright (C) 2014  Markus Fasel <markus.fasel@cern.ch>                 *
 *                      Steffen Weber <s.weber@gsi.de>                      *
 *                                                                          * 
 *  This program is free software: you can redistribute it and/or modify    *
 *  it under the terms of the GNU General Public License as published by    *
 *  the Free Software Foundation, either version 3 of the License, or       *
 *  (at your option) any later version.                                     *
 *                                                                          *
 *  This program is distributed in the hope that it will be useful,         *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of          *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           *
 *  GNU General Public License for more details.                            *
 *                                                                          *
 *  You should have received a copy of the GNU General Public License       *
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.   *
 ****************************************************************************/
package indico.checkin.core.gui;

import java.util.Iterator;

import indico.checkin.core.data.IndicoRegistrant;
import indico.checkin.core.data.IndicoRegistrantInfoField;
import indico.checkin.core.data.IndicoRegistrantInfoGroup;
import indico.checkin.core.data.IndicoRegistrantSocialEvent;

import javax.swing.table.AbstractTableModel;

/**
 * Model for table display, defining the information which has to be shown in
 * the registrant info display
 * 
 * @author: Markus Fasel
 */
public class RegistrantInfoDisplayModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private IndicoRegistrant registrant;
	
	public RegistrantInfoDisplayModel(){
		registrant = null;
	}

	@Override
	public int getRowCount() {
		return 9;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String rowTitles[] = {"Last Name:", "First Name:", "Registrant ID:", "Paid:", "Checked in:", "Price:", "Amount Paid:", "Places for conference dinner:", "Places in social events:"};
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
					// field wrongly set!!!
					//entry = String.format("EUR %.2f", registrant.getFullInformation().getAmountPaid());
					entry = "EUR " + registrant.getAmountPaid();
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
					break;
				case 8:
					// Shows the amount of social events booked
					int nplaces = 0;
					Iterator<IndicoRegistrantSocialEvent> eventIter = registrant.getFullInformation().getSocialEvents().iterator();
					while(eventIter.hasNext()){
						IndicoRegistrantSocialEvent event = eventIter.next();
						nplaces += event.getNumberPlaces();
					}
					entry = String.format("%d", nplaces);
					break;
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
		this.fireTableRowsUpdated(0, getRowCount());
	}

	public void setInfoUpdated() {
		this.fireTableRowsUpdated(0, getRowCount());
	}
}
