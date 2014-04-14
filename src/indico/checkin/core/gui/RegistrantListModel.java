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
	
	private class entry implements Comparable{
		private String lastname;
		private String firstname;
		private String affiliation;
		private long id;

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

		public entry(String lastname, String firstname, String affiliation, long l){
			this.lastname = lastname;
			this.firstname = firstname;
			this.affiliation = affiliation;
			this.id = l;
		}

		@Override
		public int compareTo(Object o) {
			/*
			 * Comparison method
			 */
			if(o.getClass() != entry.class) return 1;
			entry c = (entry)o;
			if(lastname.toLowerCase().compareTo(c.lastname.toLowerCase()) == 0)
				return firstname.toLowerCase().compareTo(c.firstname.toLowerCase());
			return lastname.toLowerCase().compareTo(c.lastname.toLowerCase());
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
		return data.getNumberOfRegistrants();
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
		entry en = entrylist.get(rowIndex);
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

	public void setData(IndicoEventRegistrantList data){
		this.data = data;
		buildEntryList();
	}
	
	@SuppressWarnings("unchecked")
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
}
