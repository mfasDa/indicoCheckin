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
package indico.checkin.core.data;

import indico.checkin.core.api.IndicoAuthentificationLayer;

public class IndicoLoginData {
	private String server;
	private int event;
	private IndicoAuthentificationLayer authentifier;
	
	public IndicoLoginData(){
		this.server = "";
		this.event = -1;
		this.setAuthentifier(null);
	}
	
	public IndicoLoginData(String server, int event, IndicoAuthentificationLayer authentifier){
		this.server = server;
		this.event = event;
		this.authentifier = authentifier;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getEvent() {
		return event;
	}

	public void setEvent(int event) {
		this.event = event;
	}

	public IndicoAuthentificationLayer getAuthentifier() {
		return authentifier;
	}

	public void setAuthentifier(IndicoAuthentificationLayer authentifier) {
		this.authentifier = authentifier;
	}

}
