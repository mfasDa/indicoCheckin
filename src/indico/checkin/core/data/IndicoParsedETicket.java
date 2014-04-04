package indico.checkin.core.data;

public class IndicoParsedETicket {
	private String registrantID;
	private String eventID;
	private String secret;
	private String auth_key;
	private String url;
	
	public IndicoParsedETicket(){
		registrantID = "";
		eventID = "";
		secret = "";
		setAuthKey("");
		url = "";
	}
	
	public long getRegistrantID() {
		return Long.parseLong(registrantID);
	}
	public void setRegistrantID(String registrantID) {
		this.registrantID = registrantID;
	}
	public int getEventID() {
		return Integer.parseInt(eventID);
	}
	public void setEventID(String eventID) {
		this.eventID = eventID;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString(){
		return String.format("Registrant: %s\nEvent: %s\nBase URL: %s\nSecret: %s", this.registrantID, this.eventID, this.url, this.secret);
	}

	public String getAuthKey() {
		return auth_key;
	}

	public void setAuthKey(String auth_key) {
		this.auth_key = auth_key;
	}
}
