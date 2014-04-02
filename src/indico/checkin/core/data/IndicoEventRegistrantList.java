package indico.checkin.core.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class IndicoEventRegistrantList{
	private class RegListMetadata{
		Map<String,String> metadata;
		
		public RegListMetadata(){
			this.metadata = new HashMap<String,String>();
		}
		
		public void AddMetaInformation(String key, String value){
			/*
			 * Set meta information
			 */
			this.metadata.put(key, value);
		}
		
		public String getMetadata(String key){
			String value = this.metadata.get(key);
			if(value != null) return value;
			return "";
		}
	}
	
	List<IndicoRegistrant> reglist;
	RegListMetadata metadata;
	
	
	public IndicoEventRegistrantList(){
		this.reglist = new ArrayList<IndicoRegistrant>();
		this.metadata = new RegListMetadata();
	}
	
	public void setMetadata(String key, String value){
		/*
		 * Add meta information
		 */
		this.metadata.AddMetaInformation(key, value);
	}
	
	public boolean isComplete(){
		/*
		 * Check whether transfered list is complete
		 */
		String meta = metadata.getMetadata("complete");
		if(meta.isEmpty()) return false;
		if(meta.equals("true")) return true;
		else return false;
 	}
	
	public IndicoRegistrant FindRegistrant(IndicoParsedETicket ticket){
		return FindRegistrantById(ticket.getRegistrantID());
	}
	
	public IndicoRegistrant FindRegistrantById(long id){
		/*
		 * Find the registrant inside the registrant list
		 */
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
	
	public void addRegistrant(IndicoRegistrant reg){
		/*
		 * Add registrant to list of registrants if not already there
		 */
		IndicoRegistrant tmp = FindRegistrantById(reg.getID());
		if(tmp == null){
			this.reglist.add(reg);
		}
	}

}
