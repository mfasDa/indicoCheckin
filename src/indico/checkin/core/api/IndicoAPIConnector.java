package indico.checkin.core.api;

import indico.checkin.core.data.IndicoEventRegistrantList;
import indico.checkin.core.data.IndicoRegistrant;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
	
	private String CreateURLRegistrantList() throws IndicoURLBuilderException {
		/*
		 * Build URL for registrant list GET request
		 */
		if(this.eventID < 0 || this.server.isEmpty()) 
			throw new IndicoURLBuilderException("Indico server or Event ID not specified");
		if(this.apikey.isEmpty() || this.apisecret.isEmpty()) 
			throw new IndicoURLBuilderException("API key or secret not specified");
		Long timestamp = new Long(System.currentTimeMillis()/1000);
		String contenturl = String.format("/export/event/%d/registrants.json?ak=%s&timestamp=%d", this.getEventID(), this.apikey, timestamp);
		
		// Priduce signature
		String signature = "";
		try{
			signature = createSignature(contenturl, this.apisecret);
		} catch(EncryptionException e){
			throw new IndicoURLBuilderException(String.format("Failure in creating signature: %s", e.getMessage()));
		}
		// Put together
		StringBuilder apiUrl = new StringBuilder(this.getServer());
		apiUrl.append(contenturl);
		apiUrl.append(String.format("&signature=%s", signature));
		
		return apiUrl.toString();
	}
	
	private String CreateURLRegistrant(IndicoRegistrant reg) throws IndicoURLBuilderException{
		/*
		 * Build URL for registrant GET request
		 */
		if(this.eventID < 0 || this.server.isEmpty()) 
			throw new IndicoURLBuilderException("Indico server or Event ID not specified");
		if(this.apikey.isEmpty() || this.apisecret.isEmpty()) 
			throw new IndicoURLBuilderException("API key or secret not specified");
		Long timestamp = new Long(System.currentTimeMillis()/1000);
		String contenturl = String.format("/export/event/%d/registrant/%d.json?ak=%s", this.getEventID(), reg.getID(), this.apikey);
		contenturl += String.format("&detail=full&timestamp=%d", timestamp);
		
		// Priduce signature
		String signature = "";
		try{
			 signature = createSignature(contenturl, this.apisecret);
		} catch (EncryptionException e){
			throw new IndicoURLBuilderException(String.format("Failure in creating signature: %s", e.getMessage()));
		}
		
		// Put together
		StringBuilder apiUrl = new StringBuilder(this.getServer());
		apiUrl.append(contenturl);
		apiUrl.append(String.format("&signature=%s", signature));
		
		return apiUrl.toString();
		
	}

	private String createSignature(String signatureString, String secretKey) throws EncryptionException {
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
			throw new EncryptionException("Encryption algorithm does not exist");
		} catch (InvalidKeyException e) {
			throw new EncryptionException("Key invalid");
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
	
	public void fetchFullRegistrantInformation(IndicoRegistrant reg) throws RegistrantBuilderException {
		/*
		 * Fetch full registrant information
		 */
		String jsonresult = "";
		try{
			String myurl = CreateURLRegistrant(reg);
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
		} catch(IndicoURLBuilderException e){
			throw new RegistrantBuilderException("Encryption invalid");
		} catch(MalformedURLException e){
			throw new RegistrantBuilderException("Indico URL invalid");
		} catch (IOException e) {
			throw new RegistrantBuilderException("Failure in reading indico data stream");
		}
		
		// Add values to the registrant which are not yet set
		IndicoFullRegistrantParser parser = new IndicoFullRegistrantParser();
		try {
			reg.setFullInformation(parser.parseRegistrant(jsonresult));
		} catch (ParseException e) {
			throw new RegistrantBuilderException("Failed parsing indico result");
		}
	}

	public IndicoEventRegistrantList fetchRegistrantList() throws RegistrantListFetchingException{
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
	
	public boolean pushCheckin(IndicoRegistrant reg) throws IndicoPostException{
		/*
		 * Set registrant checkin status to true in indico.
		 * Returns true if successfull, false otherwise
		 * Updates information in the registrant object in case of success
		 */
		
		// Build URL and POST body
		if(this.eventID < 0 || this.server.isEmpty()) 
			throw new IndicoPostException("Indico server or Event ID not specified");
		if(this.apikey.isEmpty() || this.apisecret.isEmpty()) 
			throw new IndicoPostException("API key or secret not specified");
		Long timestamp = new Long(System.currentTimeMillis()/1000);
		String urlappendix = String.format("/api/event/%d/registrant/%d/checkin.json",eventID, reg.getID());
		String poststring = String.format("ak=%s&checked_in=yes&secret=%s&timestamp=%d", apikey, reg.getSecretKey(), timestamp);
		String signature = "";
		try {
			signature = createSignature(String.format("%s?%s", urlappendix,poststring), apisecret);
		} catch (EncryptionException e) {
			throw new IndicoPostException(String.format("Encryption error: %s", e.getMessage()));
		}
		poststring += String.format("&signature=%s", signature);
		String myurl = String.format("%s%s", server, urlappendix);
		// TODO: remove output after finish debugging 
		System.out.printf("POST URL: %s\n", myurl);
		System.out.printf("POST arguments: %s\n", poststring);
		
		// Perform POST
		String resultJSON = "";
		try {
			URL indicoUrl = new URL(myurl);
			HttpURLConnection con = (HttpURLConnection) indicoUrl.openConnection();
			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			
			// Send request to indico
			OutputStreamWriter sender = new OutputStreamWriter(con.getOutputStream());
			sender.write(poststring);
			sender.flush();
			
			// Process answer from the server
			BufferedReader receiver = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String tmpstring = "";
			while((tmpstring = receiver.readLine()) != null)
				sb.append(tmpstring);
			resultJSON = sb.toString();
			// TODO: remove output after finish debugging 
			System.out.printf("Answer from server: %s\n", resultJSON);
		} catch (MalformedURLException e) {
			throw new IndicoPostException(String.format("URL %s invalid", myurl));
		} catch (IOException e) {
			throw new IndicoPostException("Failed receiving answer from indico server");
		}
		
		// Evaluate answer from server
		boolean status = false;
		if(!resultJSON.isEmpty()){
			JSONParser parser = new JSONParser();
			try {
				JSONObject answer = (JSONObject)parser.parse(resultJSON);
				boolean completed = (boolean)answer.get("complete");
				if(completed){
					JSONObject  checkinResult = (JSONObject)answer.get("results");
					boolean checkinStatus = (boolean)checkinResult.get("checked_in");
					if(checkinStatus){
						reg.setCheckinDate(checkinResult.get("checkin_date").toString());
						reg.setCheckedIn(true);
						status = true;
					}
				}
			} catch (ParseException e) {
				throw new IndicoPostException("Unable to process anser from indico server");
			}
		} 
		return status;
	}
	
	public boolean pushPayment(IndicoRegistrant reg) throws IndicoPostException{
		/*
		 * Set registrant payment status to true in indico.
		 * Returns true if successfull, false otherwise
		 * Updates information in the registrant object in case of success
		 */
		
		// Build URL and POST body
		if(this.eventID < 0 || this.server.isEmpty()) 
			throw new IndicoPostException("Indico server or Event ID not specified");
		if(this.apikey.isEmpty() || this.apisecret.isEmpty()) 
			throw new IndicoPostException("API key or secret not specified");
		Long timestamp = new Long(System.currentTimeMillis()/1000);
		String urlappendix = String.format("/api/event/%d/registrant/%d/pay.json", eventID, reg.getID());
		String poststring = String.format("ak=%s&is_paid=yes&timestamp=%d", apikey, timestamp);
		String signature = "";
		try {
			signature = createSignature(String.format("%s?%s", urlappendix,poststring), apisecret);
		} catch (EncryptionException e) {
			throw new IndicoPostException(String.format("Encryption error: %s", e.getMessage()));
		}
		poststring += String.format("&signature=%s", signature);
		String myurl = String.format("%s%s", server, urlappendix);
		// TODO: remove output after finish debugging 
		System.out.printf("POST URL: %s\n", myurl);
		System.out.printf("POST arguments: %s\n", poststring);
		
		// Perform POST
		String resultJSON = "";
		try {
			URL indicoUrl = new URL(myurl);
			HttpURLConnection con = (HttpURLConnection) indicoUrl.openConnection();
			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			
			// Send request to indico
			OutputStreamWriter sender = new OutputStreamWriter(con.getOutputStream());
			sender.write(poststring);
			sender.flush();
			
			// Process answer from the server
			BufferedReader receiver = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String tmpstring = "";
			while((tmpstring = receiver.readLine()) != null)
				sb.append(tmpstring);
			resultJSON = sb.toString();
			// TODO: remove output after finish debugging 
			System.out.printf("Answer from server: %s\n", resultJSON);
		} catch (MalformedURLException e) {
			throw new IndicoPostException(String.format("URL %s invalid", myurl));
		} catch (IOException e) {
			throw new IndicoPostException("Failed receiving answer from indico server");
		}
		
		// Process answer from the server
		JSONParser parser = new JSONParser();
		boolean status = false;
		try {
			JSONObject parsedAnswer = (JSONObject)parser.parse(resultJSON);
			boolean isComplete = (boolean)parsedAnswer.get("complete");
			if(isComplete){
				JSONObject resultIndico = (JSONObject)parsedAnswer.get("results");
				boolean isPaid = (boolean)resultIndico.get("paid");
				status = isPaid;
				if(isPaid){
					double amountPaid = Double.parseDouble(resultIndico.get("amount_paid").toString());
					reg.getFullInformation().setPaid(isPaid);
					reg.getFullInformation().setAmountPaid(amountPaid);
				}
			} else
				throw new IndicoPostException("Answer from indico server incomplete");
		} catch (ParseException e) {
			throw new IndicoPostException("Failed decoding answer from indico server");
		}
		return status;
	}


}
