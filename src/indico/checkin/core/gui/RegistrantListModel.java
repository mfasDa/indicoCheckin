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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import indico.checkin.core.data.IndicoEventRegistrantList;
import indico.checkin.core.data.IndicoRegistrant;

import javax.swing.table.AbstractTableModel;

/**
 * Model for table used in the manual search dialog
 * License: GPLv3 (a copy of the license is provided with the package)
 * 
 * @author Markus Fasel
 */
public class RegistrantListModel extends AbstractTableModel {
	/**
	 * Helper class storing the entries in alphabetical order according to
	 * the second name
	 * 
	 * @author Markus Fasel
	 *
	 */
	private class entry implements Comparable<entry>{
		private String lastname;
		private String firstname;
		private String affiliation;
		private long id;
		private boolean selected;

		public String getLastname() {
			return lastname;
		}

		public String getFirstname() {
			return firstname;
		}

		public String getAffiliation() {
			return affiliation;
		}

		public long getId() {
			return id;
		}

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		public entry(String lastname, String firstname, String affiliation, long l){
			this.lastname = lastname;
			this.firstname = firstname;
			this.affiliation = affiliation;
			this.id = l;
			this.selected = true;
		}

		@Override
		public int compareTo(entry o) {
			/*
			 * Comparison method
			 */
			if(lastname.toLowerCase().compareTo(o.lastname.toLowerCase()) == 0)
				return firstname.toLowerCase().compareTo(o.firstname.toLowerCase());
			return lastname.toLowerCase().compareTo(o.lastname.toLowerCase());
		}

		public boolean MatchBeginningLastName(String str){
			/*
			 * checks if the last name starts with the given string
			 */
			String lastnamesub = lastname.substring(0, str.length()).toLowerCase();
			if(lastnamesub.equals(str.toLowerCase())) return true;
			else return false;
		}

	}

	private static final long serialVersionUID = 1L;
	private IndicoEventRegistrantList data;
	private List<entry> entrylist;
	
	public RegistrantListModel(IndicoEventRegistrantList data){
		this.data = data;
		this.entrylist = new ArrayList<entry>();
		buildEntryList();
	}

	@Override
	public int getRowCount() {
		// count number of selected entries
		int nrow = getNumberOfSelectedEntries();
		return nrow;
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		/*
		 * Selects registrant for a given row
		 * returns information for the given col
		 */
		entry en = null;
		try{
			en = getSelectedEntryAt(rowIndex);
		} catch (EntryListBoundaryException e){
			return "";
		}
		String result = "";
		switch(columnIndex){
		case 0:
			result = en.getLastname();
			break;
		case 1:
			result = en.getFirstname();
			break;
		case 2:
			result = en.getAffiliation();
			break;
		case 3:
			result = String.format("%d", en.getId());
			break;
		}
		return result;
	}
	
	@Override
	public String getColumnName(int colID){
		/*
		 * Get the name of the column
		 */
		String result = "";
		switch(colID){
		case 0:
			result = "Last name";
			break;
		case 1:
			result = "First name";
			break;
		case 2:
			result = "Affiliation";
			break;
		case 3:
			result = "ID";
			break;
		}
		return result;
	}
	
	public entry getSelectedEntryAt(int index) throws EntryListBoundaryException{
		/*
		 * returns the entry at index index in the list of selected entries
		 */
		int nsel = getNumberOfSelectedEntries();
		if(index >= nsel)
			throw new EntryListBoundaryException(nsel, index);
		entry result = null;
		int count = 0;
		Iterator<entry> entryIter = entrylist.iterator();
		while(entryIter.hasNext()){
			entry tmp = entryIter.next();
			if(tmp.isSelected()){
				if(index == count){
					result = tmp;
					break;
				} else count++;
			}
		}
		return result;
	}
	
	public int getNumberOfSelectedEntries(){
		/*
		 * returns the number of selected entries
		 */
		int nentries = 0;
		Iterator<entry> it = entrylist.iterator();
		while(it.hasNext()){
			if(it.next().isSelected()) nentries++;
		}
		return nentries;
	}

	public void setData(IndicoEventRegistrantList data){
		this.data = data;
		buildEntryList();
	}
	
	public void selectAll(){
		/*
		 * Mark all entries as selected
		 */
		Iterator<entry> it = entrylist.iterator();
		while(it.hasNext())
			it.next().setSelected(true);
	}
	
	void UpdateSelected(String strtomatch){
		/*
		 * Selects all entries where the lastname starts with the given substring
		 */
		Iterator<entry> it = entrylist.iterator();
		while(it.hasNext()){
			entry en = it.next();
			if(en.MatchBeginningLastName(strtomatch)){
				en.setSelected(true);
			}
			else en.setSelected(false);
		}
	}
	
	private void buildEntryList(){
		entrylist.clear();
		Iterator<IndicoRegistrant> regIter = data.getRegistrantList().iterator();
		while(regIter.hasNext()){
			IndicoRegistrant reg = regIter.next();
			entrylist.add(new entry(reg.getSurname(), reg.getFirstName(), reg.getInstitution(), reg.getID()));
		}
		Collections.sort(entrylist);
	}
	
	public IndicoRegistrant getRegistrantForRow(int row){
		/*
		 * Get the matching registrant for a given row
		 */
		return data.FindRegistrantById(entrylist.get(row).getId());
	}
	
	public IndicoRegistrant getSelectedRegistrantForRow(int row) throws EntryListBoundaryException{
		/*
		 * get the matching registrant, which is also marked as selected, 
		 * for a given row
		 */
		IndicoRegistrant reg = null;
		reg = data.FindRegistrantById(getSelectedEntryAt(row).getId());
		return reg;
	}
}
