package indico.checkin.core.data;

public class IndicoLoginData {
	private String server;
	private int event;
	private String apikey;
	private String apisecret;
	
	public IndicoLoginData(){
		this.server = "";
		this.event = -1;
		this.apikey = "";
		this.apisecret = "";
	}
	
	public IndicoLoginData(String server, int event, String apikey, String apisecret){
		this.server = server;
		this.event = event;
		this.apikey = apikey;
		this.apisecret = apisecret;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getEvent() {
		return event;
	}

	public void setEvent(int event) {
		this.event = event;
	}

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public String getApisecret() {
		return apisecret;
	}

	public void setApisecret(String apisecret) {
		this.apisecret = apisecret;
	}

}
