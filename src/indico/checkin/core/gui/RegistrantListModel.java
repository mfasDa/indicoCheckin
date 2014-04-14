package indico.checkin.core.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import indico.checkin.core.data.IndicoEventRegistrantList;
import indico.checkin.core.data.IndicoRegistrant;

import javax.swing.table.AbstractTableModel;

public class RegistrantListModel extends AbstractTableModel {
	
	/**
	 * Model for table used in the manual search dialog
	 * License: GPLv3 (a copy of the license is provided with the package)
	 * 
	 * @author Markus Fasel
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
		return data.getRegistrantById(entrylist.get(row).getId());
	}
	
	public IndicoRegistrant getSelectedRegistrantForRow(int row) throws EntryListBoundaryException{
		/*
		 * get the matching registrant, which is also marked as selected, 
		 * for a given row
		 */
		IndicoRegistrant reg = null;
		reg = data.getRegistrantById(getSelectedEntryAt(row).getId());
		return reg;
	}
}
