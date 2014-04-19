/****************************************************************************
 *  Copyright (C) 2014  Markus Fasel <markus.fasel@cern.ch>                 *
 *                                                                          * 
 *  This program is free software: you can redistribute it and/or modify    *
 *  it under the terms of the GNU General Public License as published by    *
 *  the Free Software Foundation, either version 3 of the License, or       *
 *  (at your option) any later version.                                     *
 *                                                                          *
 *  This program is distributed in the hope that it will be useful,         *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of          *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           *
 *  GNU General Public License for more details.                            *
 *                                                                          *
 *  You should have received a copy of the GNU General Public License       *
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.   *
 ****************************************************************************/
package indico.checkin.core.api;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class implementing GET and POST request to indico using api key and secret
 * 
 * @author Markus Fasel <markus.fasel@cern.ch>
 */
public class IndicoKeyAuthLayer implements IndicoAuthentificationLayer {
	private String apikey;
	private String apisecret;
	
	public IndicoKeyAuthLayer(){
		this.apikey = "";
		this.apisecret = "";
	}
	
	public IndicoKeyAuthLayer(String apikey, String apisecret){
		this.apikey = apikey;
		this.apisecret = apisecret;
	}
	
	@Override
	public String doGetRequest(String geturl, Map<String, String> params) throws IndicoAuthException {
		String result = "";
		try {
			URL inputUrl = new URL(geturl);
			String path = inputUrl.getPath();
			Map<String,String> tmp;
			if(params == null){
				tmp = new TreeMap<String,String>();
			} else {
				tmp = new TreeMap<String,String>(params);
			}
			// Add authorisation key and timestamp
			Long timestamp = new Long(System.currentTimeMillis()/1000);
			tmp.put("ak", apikey);
			tmp.put("timestamp", timestamp.toString());
			Iterator<Map.Entry<String, String>> paramIter = tmp.entrySet().iterator();
			String pathstring = path;
			pathstring += "?";
			while(paramIter.hasNext()){
				Map.Entry<String, String> en = paramIter.next();
				pathstring += String.format("%s=%s", en.getKey(), en.getValue());
				if(paramIter.hasNext()) pathstring += "&";
			}
			String sign = createSignature(pathstring, apisecret);
			pathstring += String.format("&signature=%s", sign);
			URL indicoRequest = new URL(inputUrl.getProtocol() + "://" + inputUrl.getHost() + pathstring);
			BufferedReader w  = new BufferedReader(new InputStreamReader(new BufferedInputStream(indicoRequest.openStream())));
			StringBuilder sb = new StringBuilder();
			for(String line; (line = w.readLine()) != null;){
				sb.append(line);
				result = sb.toString();
			}

		} catch (MalformedURLException e) {
			throw new IndicoAuthException("Request URL malformed");
		}catch (EncryptionException e) {
			throw new IndicoAuthException("Encryption failure");
		} catch (IOException e) {
			throw new IndicoAuthException("Failure in IO");
		}
		return result;
	}

	@Override
	public String doPostRequest(String geturl, Map<String, String> params)
			throws IndicoAuthException {
		String answer = "";
		TreeMap<String,String> tmp; 
		if(params == null){
			tmp = new TreeMap<String,String>();
		} else {
			tmp = new TreeMap<String,String>(params);
		}
		// Add authorisation key and timestamp
		Long timestamp = new Long(System.currentTimeMillis()/1000);
		tmp.put("ak", apikey);
		tmp.put("timestamp", timestamp.toString());
		String poststring = "";
		Iterator<Map.Entry<String, String>> pariter = tmp.entrySet().iterator();
		while(pariter.hasNext()){
			Map.Entry<String, String> entry = pariter.next();
			poststring += String.format("%s=%s", entry.getKey(), entry.getValue());
			if(pariter.hasNext()) poststring += "&";
		}
		
		try {
			URL requestUrl = new URL(geturl);
			String sign = createSignature(String.format("%s?%s", requestUrl.getPath(), poststring), apisecret);
			poststring += String.format("&signature=%s", sign);
			
			HttpURLConnection con = (HttpURLConnection) requestUrl.openConnection();
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
			answer = sb.toString();

		} catch (EncryptionException e) {
			throw new IndicoAuthException("Creation of signature failed");
		} catch (MalformedURLException e) {
			throw new IndicoAuthException("Request URL malformed");
		} catch (ProtocolException e) {
			throw new IndicoAuthException("Protocoll error");
		} catch (IOException e) {
			throw new IndicoAuthException("I/O failure");
		}
		return answer;
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
	
	/**
	 * Function producing the signature for the http request
	 * 
	 * @param signatureString
	 * @param secretKey
	 * @return the signature
	 * @throws EncryptionException
	 */
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

}
