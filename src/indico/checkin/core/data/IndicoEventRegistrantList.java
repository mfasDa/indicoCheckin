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
package indico.checkin.core.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Structure representing the registrant list for a given event
 * 
 * @author Markus Fasel
 *
 */
public class IndicoEventRegistrantList{
	/**
	 * Helper class containing the transfer meta information
	 * 
	 * @author Markus Fasel
	 */
	private class RegListMetadata{
		Map<String,String> metadata;
		
		public RegListMetadata(){
			this.metadata = new HashMap<String,String>();
		}
		
		/**
		 * Set meta information as key/value pair
		 * @param key
		 * @param value
		 */
		public void AddMetaInformation(String key, String value){
			this.metadata.put(key, value);
		}

		/**
		 * Retreive meta information for a given key
		 * 
		 * @param key
		 * @return meta infromation (empty string if key is not found)
		 */
		public String getMetadata(String key){
			String value = this.metadata.get(key);
			if(value != null) return value;
			return "";
		}
	}
	
	List<IndicoRegistrant> reglist;
	RegListMetadata metadata;
	
	/**
	 * Default constructor
	 */
	public IndicoEventRegistrantList(){
		this.reglist = new ArrayList<IndicoRegistrant>();
		this.metadata = new RegListMetadata();
	}
	
	/**
	 * Add meta information (as key/value pair)
	 * 
	 * @param key
	 * @param value
	 */
	public void setMetadata(String key, String value){
		this.metadata.AddMetaInformation(key, value);
	}
	
	/**
	 * Check whether transfered list is complete
	 * 
	 * @return completion status
	 */
	public boolean isComplete(){
		String meta = metadata.getMetadata("complete");
		if(meta.isEmpty()) return false;
		if(meta.equals("true")) return true;
		else return false;
 	}
	
	/**
	 * Find registrant for a given e-Ticket
	 * 
	 * @param ticket: parsed e-Ticket
	 * @return the registrant (null if not found)
	 */
	public IndicoRegistrant FindRegistrant(IndicoParsedETicket ticket){
		return FindRegistrantById(ticket.getRegistrantID());
	}
	
	/**
	 * Find the registrant inside the registrant list
	 * 
	 * @param id
	 * @return  the registrant (null if not found)
	 */
	public IndicoRegistrant FindRegistrantById(long id){
		IndicoRegistrant result = null;
		Iterator<IndicoRegistrant> regiter = this.reglist.iterator();
		while(regiter.hasNext()){
			IndicoRegistrant tmp = regiter.next();
			if(tmp.getID() == id){
				result = tmp;
				break;
			}
		}
		return result;
	}
	
	/**
	 * Add registrant to list of registrants if not already there
	 * 
	 * @param reg: registrant information
	 */
	public void addRegistrant(IndicoRegistrant reg){
		IndicoRegistrant tmp = FindRegistrantById(reg.getID());
		if(tmp == null){
			this.reglist.add(reg);
		}
	}

	/**
	 * Get the number of registrants in the list
	 * 
	 * @return the number of registrants
	 */
	public int getNumberOfRegistrants(){
		return reglist.size();
	}
	
	/**
	 * check validity of the e-Ticket
	 * 
	 * @param ticket: e-Ticket to be checked
	 * @return validity status
	 */
	public boolean isTicketValid(IndicoParsedETicket ticket){
		int eventID = Integer.parseInt(this.metadata.getMetadata("eventID"));
		if(eventID == ticket.getEventID()){
			IndicoRegistrant reg = FindRegistrant(ticket);
			if(reg.isTicketValid(ticket)) return true;
		}
		return false;
	}
	
	/**
	 * Get a list representation of the registrant list
	 * 
	 * @return the registrant list
	 */
	public List<IndicoRegistrant> getRegistrantList(){
		return reglist;
	}
	
	/**
	 * Build iterator of the registrant list
	 * 
	 * @return iterator over registrants
	 */
	public Iterator<IndicoRegistrant> iterator(){
		return reglist.iterator();
	}
}
