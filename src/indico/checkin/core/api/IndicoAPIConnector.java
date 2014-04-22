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

import indico.checkin.core.data.IndicoEventRegistrantList;
import indico.checkin.core.data.IndicoRegistrant;

import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class IndicoAPIConnector {
	/**
	 * Class handling communication with the indico server via http
	 * License: GPLv3 (a copy of the license is provided with the package)
	 * 
	 * Class depending on additional library:
	 *   json-simple, published under Apache License (attached to the package)
	 * 
	 * @author: Markus Fasel
	 */
	
	private String server;
	private int eventID;
	private IndicoAuthentificationLayer authentifier;
	
	public IndicoAPIConnector(){
		this.setServer("");
		this.setEventID(-1);
		this.authentifier = null;
	}

	public IndicoAPIConnector(String server, int event, IndicoAuthentificationLayer authentifier){
		this.setServer(server);
		this.setEventID(event);
		this.authentifier = authentifier;
	}
				
	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getEventID() {
		return eventID;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
	}
		
	public IndicoAuthentificationLayer getAuthentifier() {
		return authentifier;
	}

	public void setAuthentifier(IndicoAuthentificationLayer authentifier) {
		this.authentifier = authentifier;
	}

	/**
	 * Fetch full registrant information on request
	 * 
	 * @param reg the registrant
	 * @throws RegistrantBuilderException
	 */
	public void fetchFullRegistrantInformation(IndicoRegistrant reg) throws RegistrantBuilderException {
		String contenturl = String.format("%s/export/event/%d/registrant/%d.json", this.server, this.getEventID(), reg.getID());
		Map<String,String> params = new TreeMap<String,String>();
		params.put("detail", "full");
		String jsonresult;
		try {
			jsonresult = authentifier.doGetRequest(contenturl, params);
		} catch (IndicoAuthException e1) {
			throw new RegistrantBuilderException("Encryption invalid");
		}
		
		// Add values to the registrant which are not yet set
		IndicoFullRegistrantParser parser = new IndicoFullRegistrantParser();
		try {
			reg.setFullInformation(parser.parseRegistrant(jsonresult));
		} catch (ParseException e) {
			throw new RegistrantBuilderException("Failed parsing indico result");
		}
	}

	/**
	 * Fetch registrant list for a given event
	 * 
	 * @return List of registrants
	 * @throws RegistrantListFetchingException
	 */
	public IndicoEventRegistrantList fetchRegistrantList() throws RegistrantListFetchingException{
		String contenturl = String.format("%s/export/event/%d/registrants.json", this.server, this.getEventID());
		Map<String,String> params = new TreeMap<String,String>(); 
		IndicoEventRegistrantList reglist = null;
		try {
			String jsonresult = authentifier.doGetRequest(contenturl, params);
			IndicoJSONReglistParser jsonparser = new IndicoJSONReglistParser();
			reglist = jsonparser.parseJSONRegistrantList(jsonresult);
			reglist.setMetadata("eventID", String.format("%d", this.eventID));
		} catch (IndicoAuthException e1) {
			throw new RegistrantListFetchingException("Failure sending signed request");
		}
		return reglist;
	}
	
	/**
	 * Set registrant checkin status to true in indico.
	 * Updates information in the registrant object in case of success
	 * @param reg
	 * @return true if request was successfull, false otherwise
	 * @throws IndicoPostException
	 */
	public boolean pushCheckin(IndicoRegistrant reg) throws IndicoPostException{
		boolean status = false;
		Map<String,String> params = new TreeMap<String,String>();
		params.put("checked_in", "yes");
		params.put("secret", reg.getSecretKey());
		String contentUrl = String.format("%s/api/event/%d/registrant/%d/checkin.json", server, eventID, reg.getID());
		try {
			String resultJSON = authentifier.doPostRequest(contentUrl, params);
			// Evaluate answer from server
			if(!resultJSON.isEmpty()){
				JSONParser parser = new JSONParser();
				JSONObject answer = (JSONObject)parser.parse(resultJSON);
				boolean completed = (boolean)answer.get("complete");
				if(completed){
					JSONObject  checkinResult = (JSONObject)answer.get("results");
					boolean checkinStatus = (boolean)checkinResult.get("checked_in");
					if(checkinStatus){
						reg.setCheckinDate(checkinResult.get("checkin_date").toString());
						reg.setCheckedIn(true);
						status = true;
					}
				}
			} 
		} catch (IndicoAuthException e1) {
			throw new IndicoPostException("Failure in sending signed request");
		} catch (ParseException e) {
			throw new IndicoPostException("Unable to process anser from indico server");
		}

		return status;
	}
	
	/**
	 * Set registrant payment status to true in indico.
	 * Returns 
	 * Updates information in the registrant object in case of success
	 *
	 * @param reg
	 * @return true if request was successfull, false otherwise
	 * @throws IndicoPostException
	 */
	public boolean pushPayment(IndicoRegistrant reg) throws IndicoPostException{
		boolean status = false;	
		String contentUrl = String.format("%s/api/event/%d/registrant/%d/pay.json", server, eventID, reg.getID());
		Map<String, String> params = new TreeMap<String, String>();
		params.put("is_paid", "yes");
		try {
			String resultJSON = authentifier.doPostRequest(contentUrl, params);
			
			// Process answer from the server
			JSONParser parser = new JSONParser();
			JSONObject parsedAnswer = (JSONObject)parser.parse(resultJSON);
			boolean isComplete = (boolean)parsedAnswer.get("complete");
			if(isComplete){
				JSONObject resultIndico = (JSONObject)parsedAnswer.get("results");
				boolean isPaid = (boolean)resultIndico.get("paid");
				status = isPaid;
				if(isPaid){
					double amountPaid = Double.parseDouble(resultIndico.get("amount_paid").toString());
					reg.getFullInformation().setPaid(isPaid);
					reg.getFullInformation().setAmountPaid(amountPaid);
				}
			} else
				throw new IndicoPostException("Answer from indico server incomplete");
		} catch (IndicoAuthException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e) {
			throw new IndicoPostException("Failed decoding answer from indico server");
		}
		
		return status;
	}


}
