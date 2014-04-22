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

/**
 * Class for exception in the selection of the registrant via manual search
 * 
 * @author: Markus Fasel
 */
public class EntryListBoundaryException extends Exception {

	private static final long serialVersionUID = 1L;
	private int countSelected;
	private int rowRequested;
	
	public EntryListBoundaryException(int nsel, int rsel){
		countSelected = nsel;
		rowRequested = rsel;
	}
	
	@Override
	public String getMessage(){
		return String.format("Boundary exceeded: %d entries, but number %d requested", countSelected, rowRequested);
	}
}
