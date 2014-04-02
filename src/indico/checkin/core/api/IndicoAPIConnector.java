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
	
	private String apikey;
	private String apisecret;
	
	public IndicoAPIConnector(){
		this.setApikey("");
		this.setApisecret("");
	}

	public IndicoAPIConnector(String key, String secret){
		this.setApikey(key);
		this.setApisecret(secret);
	}

				
	public String CreateURLRegistrantList(IndicoParsedETicket ticket) throws EncryptionException {
		/*
		 * Build URL for registrant GET request
		 */
		if(this.apikey.isEmpty() || this.apisecret.isEmpty()) 
			throw new EncryptionException("API key or secret not specified");
		Long timestamp = new Long(System.currentTimeMillis()/1000);
		String contenturl = String.format("/export/event/%d/registrants.json?ak=%s&timestamp=%d", ticket.getEventID(), this.apikey, timestamp);
		
		// Priduce signature
		String signature = createSignature(contenturl, this.apisecret);
		
		// Put together
		StringBuilder apiUrl = new StringBuilder(ticket.getUrl());
		apiUrl.append(contenturl);
		apiUrl.append(String.format("&signature=%s", signature));
		
		return apiUrl.toString();
	}

	public String createSignature(String signatureString, String secretKey){
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
		 * @TODO: implement this method
		 */
		IndicoRegistrant reg = new IndicoRegistrant();
		return reg;
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

	public IndicoEventRegistrantList FetchRegistrantList(
			IndicoParsedETicket ticket) throws RegistrantListFetchingException{
		// TODO Auto-generated method stub
		String jsonresult = "";
		try{
			String myurl = CreateURLRegistrantList(ticket);
			System.out.printf("URL: %s\n", myurl);
			URL indicourl = new URL(myurl);
			try{
				BufferedReader w  = new BufferedReader(new InputStreamReader(new BufferedInputStream(indicourl.openStream())));
				StringBuilder sb = new StringBuilder();
				for(String line; (line = w.readLine()) != null;){
					System.out.println("New string");
					System.out.println(line);
					sb.append(line);
					jsonresult = sb.toString();
				}
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
		return jsonparser.parseJSONRegistrantList(jsonresult);
	}
}
