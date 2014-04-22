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

/**
 * Data representation of the e-Ticket after reading in
 * 
 * @author Markus Fasel
 *
 */
public class IndicoParsedETicket {
	private String registrantID;
	private String eventID;
	private String secret;
	private String auth_key;
	private String url;
	
	/**
	 * Default constructor
	 */
	public IndicoParsedETicket(){
		registrantID = "";
		eventID = "";
		secret = "";
		setAuthKey("");
		url = "";
	}
	
	/**
	 * get the registrants ID
	 * 
	 * @return registrant ID
	 */
	public long getRegistrantID() {
		return Long.parseLong(registrantID);
	}
	
	/**
	 * Set the registrant ID
	 * 
	 * @param registrantID
	 */
	public void setRegistrantID(String registrantID) {
		this.registrantID = registrantID;
	}
	
	/**
	 * get the event ID
	 * 
	 * @return the event ID
	 */
	public int getEventID() {
		return Integer.parseInt(eventID);
	}
	
	/**
	 * Set the event ID
	 * 
	 * @param eventID
	 */
	public void setEventID(String eventID) {
		this.eventID = eventID;
	}
	
	/**
	 * Get the registrant secret key
	 * @return the secret key
	 */
	public String getSecret() {
		return secret;
	}
	
	/** 
	 * Set the registrant secret key
	 * 
	 * @param secret: The secret key
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	/**
	 * Get the request URL
	 * 
	 * @return the request URL
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * Set the request URL
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString(){
		return String.format("Registrant: %s\nEvent: %s\nBase URL: %s\nSecret: %s", this.registrantID, this.eventID, this.url, this.secret);
	}

	/**
	 * Get the authentification key
	 * 
	 * @return the authentification key
	 */
	public String getAuthKey() {
		return auth_key;
	}
	
	/**
	 * Set the authentification key
	 * 
	 * @param auth_key: The authentification key
	 */
	public void setAuthKey(String auth_key) {
		this.auth_key = auth_key;
	}
}
