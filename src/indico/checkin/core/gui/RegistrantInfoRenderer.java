package indico.checkin.core.gui;

import javax.swing.table.DefaultTableCellRenderer;

public class RegistrantInfoRenderer extends DefaultTableCellRenderer {

	/**
	 * Renderer for the registrant info display
	 * For boolean values set the text color to green for true and to red for false.
	 * License: GPLv3 (a copy of the license is provided with the package)
	 * 
	 * @author: Markus Fasel
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public void setValue(Object value){
		/*
		 * In case of boolean values 
		 */
		if(value instanceof Boolean){
			if(((Boolean) value).booleanValue()){
				// case true
				setText("<html><font color='green'>yes</font></html>");
			} else{
				setText("<html><font color='red'>no</font></html>");
			}
		} else {
			setText(value.toString());
		}
	}

}
