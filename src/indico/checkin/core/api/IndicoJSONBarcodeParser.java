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


public class IndicoJSONBarcodeParser {
	/**
	 * Class parsing the JSon result from the barcode scan
	 */

	public IndicoParsedETicket parse(String regjson) throws ETicketDecodingException{
		/*
		 * Parsing JSON type barcode
		 */
		IndicoParsedETicket reg = null;
		if(!regjson.isEmpty()){
			// string not empty - start parsing
			reg = new IndicoParsedETicket();
			
			// run parsing
			JSONParser parser = new JSONParser();
			ContainerFactory fact = new ContainerFactory(){

				@Override
				public List creatArrayContainer() {
					return new LinkedList();
				}

				@Override
				public Map createObjectContainer() {
					return new LinkedHashMap<>();
				}
				
			};
			try{
				Map parsed = (Map)parser.parse(regjson, fact);
				Iterator pIter = parsed.entrySet().iterator();
				while(pIter.hasNext()){
					Map.Entry en = (Map.Entry)pIter.next();
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
					} else if(key.equals("secret")){
						reg.setSecret((String)en.getValue());
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
