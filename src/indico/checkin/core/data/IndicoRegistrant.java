package indico.checkin.core.data;

import java.util.HashMap;
import java.util.Map;

public class IndicoRegistrant {

	Map<String,String> personalData;
	Map<String,String> registrantInformation;
	IndicoRegistrantFullInformation fullInformation;
	
	public IndicoRegistrant(){
		this.personalData = new HashMap<String,String>();
		this.registrantInformation = new HashMap<String,String>();
	}
	
	public long getID(){
		/*
		 * get the registrants ID
		 */
		String inf = this.registrantInformation.get("registrant_id");
		if(inf != null){
			return Long.parseLong(inf);
		}
		return -1;
	}
	
	public void setFullInformation(IndicoRegistrantFullInformation info){
		this.fullInformation = info;
	}
	
	
	public void setPersonalInformation(String key, String value){
		/*
		 * Store personal data
		 */
		this.personalData.put(key, value);
	}
	
	public void setRegistrantInformation(String key, String value){
		/*
		 * Store registrant info
		 */
		this.registrantInformation.put(key, value);
	}
	
	public IndicoRegistrantFullInformation getFullInformation(){
		return this.fullInformation;
	}
	
	public void setCheckedIn(boolean result){
		setRegistrantInformation("checked_in", result ? "true" : "false");
		if(fullInformation != null) fullInformation.setCheckedin(result);
	}
	
	public String getPersonalInformation(String key){
		/*
		 * Return value for key in personal data
		 */
		String val = this.personalData.get(key);
		if(val != null) return val;
		return "";
	}
	
	public String getRegistrantInformation(String key){
		/*
		 * Return value for a given key stored in registration info
		 */
		String val = this.registrantInformation.get(key);
		if(val != null) return val;
		return "";
	}
	
	public boolean hasCheckedIn(){
		String val = getRegistrantInformation("checked_in");
		return val.equals("true");
	}
	
	public String getSecretKey(){
		/*
		 * Get secret key for http request
		 */
		return this.getRegistrantInformation("checkin_secret");
	}
	
	public boolean matchSecretKey(String key){
		/*
		 * Compare users secret key to another secret key
		 */
		return this.getSecretKey().equals(key);
	}
	
	public String getAuthKey(){
		/*
		 * Get authentification key for http request
		 */
		return this.getRegistrantInformation("auth_key");
	}
	
	public String getFullName(){
		return this.getRegistrantInformation("full_name");
	}
	
	public String getFirstName(){
		return this.getPersonalInformation("firstName");
	}
	
	public String getSurname(){
		return this.getPersonalInformation("surname");
	}
	
	public String getInstitution(){
		return this.getPersonalInformation("institution");
	}
	
	public String getCountry(){
		return this.getPersonalInformation("country");
	}
	
	public String getCity(){
		return this.getPersonalInformation("city");
	}
	
	public String getTitle(){
		return this.getPersonalInformation("title");
	}
	
	public String getPosition(){
		return this.getPersonalInformation("position");
	}
	
	public String getPhone(){
		return this.getPersonalInformation("phone");
	}
	
	public String getFax(){
		return this.getPersonalInformation("fax");
	}
	
	public String getEmail(){
		return this.getPersonalInformation("email");
	}
	
	public String getWebpage(){
		return this.getPersonalInformation("address");
	}
	
	public boolean hasPaid(){
		/*
		 * check whether registrant has already payed
		 */
		if(fullInformation != null)
			return fullInformation.isPaid();
		else return false;
	}

	public boolean isTicketValid(IndicoParsedETicket ticket){
		/*
		 * check whether ticket is valid:
		 *  * secret key has to be correct
		 */
		return this.getSecretKey().equals(ticket.getSecret());
	}
	
	public void  setCheckinDate(String checkinDate){
		if(fullInformation != null)
			fullInformation.setCheckinDate(checkinDate);
	}
	
	public double getFullPrice(){
		/*
		 * Get the total price for a registrant
		 */
		if(fullInformation != null) return fullInformation.getFullPrice();
		return 0.;
	}
}
