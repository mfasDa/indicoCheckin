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

import javax.swing.table.DefaultTableCellRenderer;

/**
 * Renderer for the registrant info display
 * For boolean values set the text color to green for true and to red for false.
 * 
 * @author: Markus Fasel
 */
public class RegistrantInfoRenderer extends DefaultTableCellRenderer {

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
