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

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.github.sarxos.webcam.Webcam;

/**
 * Model used in the display of the webcam selection table
 * 
 * @author: Markus Fasel
 */
public class WebcamSelectionModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	private List<Webcam> webcams;

	/**
	 * During construction get the list of webcams
	 */
	public WebcamSelectionModel(){
		webcams = Webcam.getWebcams();
	}

	/**
	 * Get the number of rows (equal to the number of webcams)
	 * @return: number of webcams
	 */
	@Override
	public int getRowCount() {
		return webcams.size();
	}

	/**
	 * Get the number of coloums in the table
	 * @return: number of coloums (2 - id and name)
	 */
	@Override
	public int getColumnCount() {
		return 2;
	}

	/**
	 * Provide names for columns
	 * @param columnIndex: index of the column
	 * @return the column name
	 */
	@Override
	public String getColumnName(int columnIndex) {
		String result = "";
		switch(columnIndex){
		case 0:
			result = "ID";
			break;
		case 1:
			result = "Webcam name";
			break;
		}
		return result;
	}

	/**
	 * Cells are not editable
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	/**
	 * Provide table content
	 * 
	 * @param rowIndex: the row index
	 * @param columnIndex: the coloum index
	 * @return: Content for the table
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Webcam mycam = webcams.get(rowIndex);
		String result = "";
		switch(columnIndex){
		case 0:
			result = String.format("%d", rowIndex);
			break;
		case 1:
			result = mycam.getName();
		}
		return result;
	}

	/**
	 * We don't set values
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		
	}
}
