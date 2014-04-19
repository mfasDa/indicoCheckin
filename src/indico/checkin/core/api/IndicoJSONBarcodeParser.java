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

import indico.checkin.core.data.IndicoParsedETicket;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Class parsing the JSon result from the barcode scan
 * 
 * Class depending on additional library:
 *   json-simple, published under Apache License v2 (attached to the package)
 *   
 * @author Markus Fasel
 *
 */
public class IndicoJSONBarcodeParser {

	/**
	 * Parsing JSON type barcode
	 * 
	 * @param regjson: JSON string representing the barcode
	 * @return the parsed E-Ticket
	 * @throws ETicketDecodingException
	 */
	public IndicoParsedETicket parse(String regjson) throws ETicketDecodingException{
		IndicoParsedETicket reg = null;
		if(!regjson.isEmpty()){
			// string not empty - start parsing
			reg = new IndicoParsedETicket();
			
			// run parsing
			JSONParser parser = new JSONParser();
			ContainerFactory fact = new ContainerFactory(){

				@Override
				public List<?> creatArrayContainer() {
					return new LinkedList<>();
				}

				@Override
				public Map<?,?> createObjectContainer() {
					return new LinkedHashMap<>();
				}
				
			};
			try{
				Map<?,?> parsed = (Map<?,?>)parser.parse(regjson, fact);
				Iterator<?> pIter = parsed.entrySet().iterator();
				while(pIter.hasNext()){
					Map.Entry<?,?> en = (Map.Entry<?,?>)pIter.next();
					String key = (String)en.getKey();
					if(key.equals("event_id")){
						reg.setEventID((String)en.getValue());
					} else if(key.equals("registrant_id")){
						reg.setRegistrantID((String)en.getValue());
					} else if(key.equals("server_url")){
						// Replace \/ by /
						String myurl = (String)en.getValue();
						myurl.replace("\\", "");
						reg.setUrl(myurl);
					} else if(key.equals("secret") || key.equals("checkin_secret")){
						reg.setSecret((String)en.getValue());
					} else if(key.equals("auth_key")){
						reg.setAuthKey((String)en.getValue());
					} else {
						StringBuilder sb = new StringBuilder("Key ");
						sb.append(key);
						sb.append(" unknown.");
						throw new ETicketDecodingException(sb.toString());
					}
				}
			} catch(ParseException e){
				throw new ETicketDecodingException("Parsing json string failed");
			}
		}
		return reg;
	}
}
