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
package indico.checkin.core.api;

/**
 * Exception handling post requests to the indico server which fail
 * 
 * @author: Markus Fasel
 */
public class IndicoPostException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message;

	public IndicoPostException(){
		message = "";
	}
	
	public IndicoPostException(String msg){
		message = msg;
	}
	
	@Override
	public String getMessage(){
		return message;
	}

}
