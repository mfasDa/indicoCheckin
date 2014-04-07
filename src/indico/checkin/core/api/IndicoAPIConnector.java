package indico.checkin.core.api;

import indico.checkin.core.data.IndicoEventRegistrantList;
import indico.checkin.core.data.IndicoParsedETicket;
import indico.checkin.core.data.IndicoRegistrant;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class IndicoAPIConnector {
	
	private String server;
	private int eventID;
	private String apikey;
	private String apisecret;
	
	public IndicoAPIConnector(){
		this.setServer("");
		this.setEventID(-1);
		this.setApikey("");
		this.setApisecret("");
	}

	public IndicoAPIConnector(String server, int event, String key, String secret){
		this.setServer(server);
		this.setEventID(event);
		this.setApikey(key);
		this.setApisecret(secret);
	}
				
	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getEventID() {
		return eventID;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
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
	
	private String CreateURLRegistrantList() throws EncryptionException {
		/*
		 * Build URL for registrant list GET request
		 */
		if(this.eventID < 0 || this.server.isEmpty()) 
			throw new EncryptionException("Indico server or Event ID not specified");
		if(this.apikey.isEmpty() || this.apisecret.isEmpty()) 
			throw new EncryptionException("API key or secret not specified");
		Long timestamp = new Long(System.currentTimeMillis()/1000);
		String contenturl = String.format("/export/event/%d/registrants.json?ak=%s&timestamp=%d", this.getEventID(), this.apikey, timestamp);
		
		// Priduce signature
		String signature = createSignature(contenturl, this.apisecret);
		
		// Put together
		StringBuilder apiUrl = new StringBuilder(this.getServer());
		apiUrl.append(contenturl);
		apiUrl.append(String.format("&signature=%s", signature));
		
		return apiUrl.toString();
	}
	
	private String CreateURLRegistrant(IndicoParsedETicket ticket) throws EncryptionException{
		/*
		 * Build URL for registrant GET request
		 */
		if(this.eventID < 0 || this.server.isEmpty()) 
			throw new EncryptionException("Indico server or Event ID not specified");
		if(this.apikey.isEmpty() || this.apisecret.isEmpty()) 
			throw new EncryptionException("API key or secret not specified");
		Long timestamp = new Long(System.currentTimeMillis()/1000);
		String contenturl = String.format("/export/event/%d/registrant/%d.json?ak=%s&authl_key=%s&details=full&timestamp=%d", this.getEventID(), ticket.getRegistrantID(), this.apikey, ticket.getAuthKey(), timestamp);
		
		// Priduce signature
		String signature = createSignature(contenturl, this.apisecret);
		
		// Put together
		StringBuilder apiUrl = new StringBuilder(this.getServer());
		apiUrl.append(contenturl);
		apiUrl.append(String.format("&signature=%s", signature));
		
		return apiUrl.toString();
		
	}

	private String createSignature(String signatureString, String secretKey){
		// Method found in stackoverflow
		SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), "HmacSHA1");

		Mac mac = null;
		String form = "";
		try{
			mac = Mac.getInstance("HmacSHA1");
			mac.init(signingKey);

			byte[] bytes = mac.doFinal(signatureString.getBytes());

			for (int i = 0; i < bytes.length; i++){
				String str = Integer.toHexString(((int)bytes[i]) & 0xff);
				if (str.length() == 1){
					str = "0" + str;
				}

				form = form + str;
			}
		} catch (NoSuchAlgorithmException e){
			System.out.printf("Wrong algorithm: %s\n", e.getMessage());
		} catch (InvalidKeyException e) {
			System.out.printf("Invalid key: %s\n", e.getMessage());
		}
		return form;
	}
	
	public IndicoRegistrant convertJSONtoRegistrant(String jsonstring){
		/*
		 * Convert json representation of indico registrant to java object
		 * TODO: implement this method
		 */
		IndicoRegistrant reg = new IndicoRegistrant();
		return reg;
	}
	
	public void FetchFullRegistrantInformation(IndicoRegistrant reg, IndicoParsedETicket ticket){
		/*
		 * Fetch full registrant information
		 */
		String jsonresult = "";
		try{
			String myurl = CreateURLRegistrant(ticket);
			System.out.printf("URL: %s\n", myurl);
			URL indicourl = new URL(myurl);
			
			BufferedReader w  = new BufferedReader(new InputStreamReader(new BufferedInputStream(indicourl.openStream())));
			StringBuilder sb = new StringBuilder();
			for(String line; (line = w.readLine()) != null;){
				sb.append(line);
				jsonresult = sb.toString();
			}
			// TODO: remove output after finish debugging 
			System.out.println("Final string:");
			System.out.println(sb.toString());
		} catch(EncryptionException e){
			
		} catch(MalformedURLException e){
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Add values to the registrant which are not yet set
		IndicoJSONReglistParser parser = new IndicoJSONReglistParser();
		parser.parseFullRegistrantInformation(reg, jsonresult);
	}

	public IndicoEventRegistrantList FetchRegistrantList() throws RegistrantListFetchingException{
		/*
		 * Fetch registrant list for a given event
		 */
		String jsonresult = "";
		try{
			String myurl = CreateURLRegistrantList();
			// TODO: remove output after finish debugging 
			System.out.printf("URL: %s\n", myurl);
			URL indicourl = new URL(myurl);
			try{
				BufferedReader w  = new BufferedReader(new InputStreamReader(new BufferedInputStream(indicourl.openStream())));
				StringBuilder sb = new StringBuilder();
				for(String line; (line = w.readLine()) != null;){
					sb.append(line);
					jsonresult = sb.toString();
				}
				// TODO: remove output after finish debugging 
				System.out.println("Final string:");
				System.out.println(sb.toString());
			} catch (IOException e){
				System.out.printf("IO Exception: %s\n", e.getMessage());
				throw new RegistrantListFetchingException("Failed obtaining registrant list");
			}		
		} catch (MalformedURLException e){ 
			System.out.printf("URL malformed: %s", e.getMessage());
			throw new RegistrantListFetchingException("Failed obtaining registrant list");
		} catch (Exception e){
			System.out.println("an unexpected error occurred.");
			throw new RegistrantListFetchingException("Failed obtaining registrant list");
		}
		IndicoJSONReglistParser jsonparser = new IndicoJSONReglistParser();
		IndicoEventRegistrantList reglist = jsonparser.parseJSONRegistrantList(jsonresult);
		reglist.setMetadata("eventID", String.format("%d", this.eventID));
		return reglist;
	}

}
