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


public class IndicoFullRegistrantParser {
	/**
	 * Parser for JSON based indico registrant full information
	 */
	
	public IndicoRegistrantFullInformation parseRegistrant(String jsonstring) throws ParseException {
		/*
		 * Parse json result and convert to registrant full information
		 */
		IndicoRegistrantFullInformation registrant = null;
		JSONParser parser = new JSONParser();
		JSONObject parsedRegistrant = (JSONObject)parser.parse(jsonstring);
		registrant = new IndicoRegistrantFullInformation();
		Iterator keyIter = parsedRegistrant.entrySet().iterator();
		while(keyIter.hasNext()){
			Map.Entry entry = (Map.Entry)keyIter.next();
			if(entry.getKey().equals("complete"))
				registrant.setComplete((boolean)entry.getValue());
			if(entry.getKey().equals("results")){
				// Iterate over keys in the result section
				JSONObject registrantResults = (JSONObject)entry.getValue();
				Iterator resultIter = registrantResults.entrySet().iterator();
				while(resultIter.hasNext())
					handleEntry(registrant, (Map.Entry)resultIter.next());
			}
		}
		return registrant;
	}
	
	private void handleEntry(IndicoRegistrantFullInformation registrant, Map.Entry entry){
		/*
		 * Process map entry
		 * 
		 * Unhandled:
		 * + sessionList
		 * + accommodation
		 * 
		 * Currently unhandled
		 * + checkin_date
		 * + socialEvents
		 */
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
			Iterator groupIter = groups.iterator();
			while(groupIter.hasNext()) handleGroup(registrant, (JSONObject)groupIter.next());
		}
	}
		
	private void handleGroup(IndicoRegistrantFullInformation registrant, JSONObject group){
		/*
		 * Convert info group
		 */
		try{
			IndicoRegistrantInfoGroup infogroup = registrant.createInfoGroup((String)group.get("title"), (String)group.get("_fossil"), 
				(String)group.get("_type"), Long.parseLong(group.get("id").toString()));
			JSONArray items = (JSONArray)group.get("responseItems");
			Iterator itemIter = items.iterator();
			while(itemIter.hasNext()){
				infogroup.setInfoField(convertResponseItem((JSONObject)itemIter.next()));
			}
		} catch(NumberFormatException e){
			System.out.printf("Number format exception: %s\n", e.getMessage());
		}
	}
	
	private IndicoRegistrantInfoField convertResponseItem(JSONObject item){
		/*
		 * Convert response item from JSON Object to field object
		 */
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
			field.setPrice(Integer.parseInt(item.get("price").toString()));
		if(!item.get("quantity").toString().isEmpty())
			field.setQuantity(Integer.parseInt(item.get("quantity").toString()));
		field.setCurrency((String)item.get("currency"));
		return field;
	}
}
