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
package indico.checkin.core.api;

import indico.checkin.core.data.IndicoRegistrantFullInformation;
import indico.checkin.core.data.IndicoRegistrantInfoField;
import indico.checkin.core.data.IndicoRegistrantInfoGroup;

import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Parser for JSON based indico registrant full information
 * 
 * Class depending on additional library
 *   json-simple, published under Apache License 2.0
 * 
 * @author: Markus Fasel
 */
public class IndicoFullRegistrantParser {
	
	/**
	 * Parse json result and convert to registrant full information
	 * 
	 * @param jsonstring: Input string from the server
	 * @return The registrant full information
	 * @throws ParseException
	 */
	public IndicoRegistrantFullInformation parseRegistrant(String jsonstring) throws ParseException {
		IndicoRegistrantFullInformation registrant = null;
		JSONParser parser = new JSONParser();
		JSONObject parsedRegistrant = (JSONObject)parser.parse(jsonstring);
		registrant = new IndicoRegistrantFullInformation();
		Iterator<?> keyIter = parsedRegistrant.entrySet().iterator();
		while(keyIter.hasNext()){
			Map.Entry<?,?> entry = (Map.Entry<?,?>)keyIter.next();
			if(entry.getKey().equals("complete"))
				registrant.setComplete(Boolean.parseBoolean(entry.getValue().toString()));
			if(entry.getKey().equals("results")){
				// Iterate over keys in the result section
				JSONObject registrantResults = (JSONObject)entry.getValue();
				Iterator<?> resultIter = registrantResults.entrySet().iterator();
				while(resultIter.hasNext())
					handleEntry(registrant, (Map.Entry<?,?>)resultIter.next());
			}
		}
		return registrant;
	}

	/**
	 * Process map entry
	 * 
	 * Unhandled:
	 * + sessionList
	 * + accommodation
	 * 
	 * Currently unhandled
	 * 
	 * @param registrant: The registrant
	 * @param entry: Entry of the registrant information
	 */
	private void handleEntry(IndicoRegistrantFullInformation registrant, Map.Entry<?,?> entry){
		String key = (String)entry.getKey();
		Object value = entry.getValue();
		if(key.equals("_type"))
			registrant.setType(value.toString());
		else if(key.equals("checked_in"))
			registrant.setCheckedin((boolean)value);
		else if(key.equals("amount_paid"))
			registrant.setAmountPaid(Double.parseDouble(value.toString()));
		else if(key.equals("registration_date"))
			registrant.setRegistrationDate(value.toString());
		else if(key.equals("reasonParticipation"))
			registrant.setReasonForParticipation(value.toString());
		else if(key.equals("paid"))
			registrant.setPaid((boolean)value);
		else if(key.equals("_fossil"))
			registrant.setFossil(value.toString());
		else if(key.equals("full_name"))
			registrant.setFullName(value.toString());
		else if(key.equals("registrant_id"))
			registrant.setRegistrantID(Long.parseLong(value.toString()));
		else if(key.equals("miscellaneousGroupList")){
			JSONArray groups = (JSONArray)value;
			Iterator<?> groupIter = groups.iterator();
			while(groupIter.hasNext()) handleGroup(registrant, (JSONObject)groupIter.next());
		} else if(key.equals("socialEvents")){
			JSONArray events = (JSONArray)value;
			Iterator<?> eventIter = events.iterator();
			while(eventIter.hasNext()){
				JSONObject event = (JSONObject)eventIter.next();
				registrant.createSocialEvent(
						event.get("_type").toString(), 
						event.get("_fossil").toString(), 
						Integer.parseInt(event.get("id").toString()), 
						Double.parseDouble(event.get("price").toString()), 
						(long)event.get("noPlaces"), 
						event.get("currency").toString(),
						event.get("caption").toString()
						);
			}
		}
	}
	
	/**
	 * Convert info group
	 * 
	 * @param registrant: The current registrant
	 * @param group: The current info group
	 */
	private void handleGroup(IndicoRegistrantFullInformation registrant, JSONObject group){
		try{
			IndicoRegistrantInfoGroup infogroup = registrant.createInfoGroup((String)group.get("title"), (String)group.get("_fossil"), 
				(String)group.get("_type"), Long.parseLong(group.get("id").toString()));
			JSONArray items = (JSONArray)group.get("responseItems");
			Iterator<?> itemIter = items.iterator();
			while(itemIter.hasNext()){
				infogroup.setInfoField(convertResponseItem((JSONObject)itemIter.next()));
			}
		} catch(NumberFormatException e){
			System.out.printf("Number format exception: %s\n", e.getMessage());
		}
	}
	
	/**
	 * Convert response item from JSON Object to field object
	 * @param item: current item to process
	 * @return info field used in the full registrant information
	 */
	private IndicoRegistrantInfoField convertResponseItem(JSONObject item){
		IndicoRegistrantInfoField field = new IndicoRegistrantInfoField();
		field.setCaption((String)item.get("caption"));
		field.setType((String)item.get("_type"));
		field.setFossil((String)item.get("_fossil"));
		field.setHtmlname((String)item.get("HTMLName"));
		if(item.get("value") != null)
			field.setValue((String)item.get("value").toString());
		else
			field.setValue("");
		if(!item.get("id").toString().isEmpty())
			field.setId(Long.parseLong(item.get("id").toString()));
		if(!item.get("price").toString().isEmpty())
			field.setPrice(Double.parseDouble(item.get("price").toString()));
		if(!item.get("quantity").toString().isEmpty())
			field.setQuantity(Integer.parseInt(item.get("quantity").toString()));
		field.setCurrency((String)item.get("currency"));
		return field;
	}
}
