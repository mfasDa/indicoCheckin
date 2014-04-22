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

import indico.checkin.core.data.IndicoEventRegistrantList;
import indico.checkin.core.data.IndicoRegistrant;

import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Parser class for the registrant list obtained as JSON string from the 
 * indico server. Converts registrant list in the program data structure.
 * 
 * Clas depending on additional library:
 *   json-simple, published under Apache License 2.0 (attached to the package)
 *
 * @author Markus Fasel
 *
 */
public class IndicoJSONReglistParser {
	/**
	 * Parse JSON string representation of the registrant list
	 * 
	 * @param str: JSON representation of the reglist
	 * @return The registrant list
	 */
	public IndicoEventRegistrantList parseJSONRegistrantList(String str){
		IndicoEventRegistrantList res = null;
		
		JSONParser parser = new JSONParser();
		try {
			res = new IndicoEventRegistrantList();
			Map<?,?> parsed = (Map<?,?>)parser.parse(str);
			Iterator<?> piter = parsed.entrySet().iterator();
			while(piter.hasNext()){
				Map.Entry<?,?> en = (Map.Entry<?,?>)piter.next();
				if(!en.getKey().equals("results")){
					// Metadata
					res.setMetadata(en.getKey().toString(), en.getValue().toString());
				} else {
					// Split registrant string
					JSONObject obj = (JSONObject)en.getValue();
					JSONArray regarray = (JSONArray)obj.get("registrants");
					
					Iterator<?> regiter = regarray.iterator();
					while(regiter.hasNext()){
						JSONObject jsonreg = (JSONObject) regiter.next();
						IndicoRegistrant reg = parseRegistrant(jsonreg);
						res.addRegistrant(reg);
					}
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * Convert JSON registrant information into a registrant object
	 * 
	 * @param jsonreg: JSON object of a registrant
	 * @return The registrant (with basic information only)
	 */
	public IndicoRegistrant parseRegistrant(JSONObject jsonreg){
		IndicoRegistrant reg = new IndicoRegistrant();
		Iterator<?> keyIter = jsonreg.entrySet().iterator();
		while(keyIter.hasNext()){
			// Process keys and insert them into the registrant
			Map.Entry<?,?> en = (Map.Entry<?,?>)keyIter.next();
			if(!en.getKey().equals("personal_data")){
				reg.setRegistrantInformation(en.getKey().toString(), en.getValue().toString());
			} else {
				// Split personal data
				JSONParser parser = new JSONParser();
				JSONObject pobj;
				try {
					pobj = (JSONObject)parser.parse(en.getValue().toString());
					Iterator<?> pIter = pobj.entrySet().iterator();
					while(pIter.hasNext()){
						Map.Entry<?,?> pe = (Map.Entry<?,?>)pIter.next();
						reg.setPersonalInformation(pe.getKey().toString(), pe.getValue().toString());
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return reg;
	}
	
	/**
	 * Parse json string and add values to the registrant
	 * 
	 * @param reg: the registrant
	 * @param jsonstring: json representation of the information
	 */
	public void parseFullRegistrantInformation(IndicoRegistrant reg, String jsonstring){
		JSONParser parser = new JSONParser();
		try {
			JSONObject regfull = (JSONObject)parser.parse(jsonstring);
			JSONObject res = (JSONObject)regfull.get("results");
			if(res != null){
				Iterator<?> keyIter = res.entrySet().iterator();
				while(keyIter.hasNext()){
					Map.Entry<?,?> en = (Map.Entry<?,?>)keyIter.next();
					String key = (String) en.getKey();
					System.out.println("Key: "+ key);
					if(en.getValue() != null)
						System.out.printf("Key: %s,  value type:%s\n", key, en.getValue().getClass().getName());
					if(key.equals("personal_data")){
						// information does not need an update
						continue;
					} else if(key.equals("_type")){
						continue;
					} else if(key.equals("_fossil")){
						continue;
					} else {
						if(en.getValue() != null){
							if(en.getValue().getClass().equals(JSONObject.class)){
								// JSON Object
								System.out.println("Value is a json object");
							} else {
								reg.setRegistrantInformation(en.getKey().toString(), en.getValue().toString());
							}
						} else {
							/*
							 * Set empty string for the key
							 */
							reg.setRegistrantInformation((String)en.getKey(), "");
						}
					}
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
