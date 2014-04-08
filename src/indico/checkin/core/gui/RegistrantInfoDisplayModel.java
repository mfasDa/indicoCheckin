package indico.checkin.core.gui;

import indico.checkin.core.data.IndicoRegistrant;

import javax.swing.table.AbstractTableModel;

public class RegistrantInfoDisplayModel extends AbstractTableModel {

	/**
	 * Model for table display
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		String rowTitles[] = {"Last Name:", "First Name:", "Registrant ID:", "Paid:", "Checked in:"};
		if(columnIndex == 0){
			return rowTitles[rowIndex];
		}
		return null;
	}
	
	public void SetRegistrant(IndicoRegistrant reg){
	
	}
}
