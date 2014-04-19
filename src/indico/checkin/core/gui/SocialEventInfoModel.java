/****************************************************************************
 *  Copyright (C) 2014  Markus Fasel <markus.fasel@cern.ch>                 *
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

import indico.checkin.core.data.IndicoRegistrantSocialEvent;

import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * Model used in the table showing the social events in the info dialog
 * 
 * @author: Markus Fasel
 */
public class SocialEventInfoModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private List<IndicoRegistrantSocialEvent> eventList;
	
	SocialEventInfoModel(List<IndicoRegistrantSocialEvent> events){
		this.eventList = events;
	}

	@Override
	public int getRowCount() {
		return eventList.size();
	}

	@Override
	public int getColumnCount() {
		return 4;
	}
	
	@Override
	public String getColumnName(int colID){
		String colnames[] = {"Event name", "Price", "Places", "Total Price"};
		return colnames[colID];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		IndicoRegistrantSocialEvent event = eventList.get(rowIndex);
		String result = "";
		switch(columnIndex){
		case 0:
			result = event.getCaption();
			break;
		case 1:
			result = String.format("%.2f %s", event.getPrice(), event.getCurrency());
			break;
		case 2:
			result = String.format("%d", event.getNumberPlaces());
			break;
		case 3:
			result = String.format("%.2f %s", event.getPrice() * event.getNumberPlaces(), event.getCurrency());
		};
		return result;
	}

}
