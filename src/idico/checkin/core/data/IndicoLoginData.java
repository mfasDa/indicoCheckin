package idico.checkin.core.data;

public class IndicoLoginData {
	private String fUser;
	private String fPassword;
	private String fIndicoURL;

	public IndicoLoginData(){
		this.fUser = "";
		this.fPassword = "";
		this.fIndicoURL = "";
	}
	
	public IndicoLoginData(String user, String passwd, String url){
		this.fUser = user;
		this.fPassword = passwd;
		this.fIndicoURL = url;
	}
	
	public void setUser(String user){
		this.fUser = user;
	}
	
	public void setPasswd(String passwd){
		this.fPassword = passwd;
	}
	
	public void setIndicoUrl(String url){
		this.fIndicoURL = url;
	}
	
	public String getUser(){
		return this.fUser;
	}
	
	public String getPassword(){
		return this.fPassword;
	}
	
	public String getIndicoUrl(){
		return this.fIndicoURL;
	}
}
