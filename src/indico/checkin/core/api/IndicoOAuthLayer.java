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

import indico.checkin.core.data.IndicoCernOauthData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import oauth.signpost.http.HttpParameters;

/**
 * Class implementing http access to the indico api using oauth authentification
 * 
 * @author Markus Fasel
 */
public class IndicoOAuthLayer implements IndicoAuthentificationLayer {
	OAuthProvider serviceProvider;
	OAuthConsumer consumerTask;
	String verifier;
	boolean hasAccessToken;
	
	public IndicoOAuthLayer(){
		serviceProvider = null;
		consumerTask = null;
		verifier = "";
		hasAccessToken = false;
	}
	
	/**
	 * Login function obtaining a request token and a secret verifier
	 * User is redirected to a webbrowser where the login data have to be
	 * put in
	 * 
	 * @param server: Server URL
	 * @throws IndicoAuthException
	 */
	public void login(String server) throws IndicoAuthException {
		String consumerKey = "";
		String consumerSecret = "";
		String requestTokenUrl = "";
		String accessTokenUrl = "";
		String authorizeUrl = "";
		if(server.equals("https://indico.cern.ch")){
			consumerKey = IndicoCernOauthData.getConsumerkey();
			consumerSecret = IndicoCernOauthData.getConsumersecret();
			requestTokenUrl = IndicoCernOauthData.getRequesttokenurl();
			accessTokenUrl = IndicoCernOauthData.getAccesstokenurl();
			authorizeUrl = IndicoCernOauthData.getAuthorisationurl();
		} else {
			throw new IndicoAuthException(String.format("OAuth settings for server %s unknown", server));
		}
		consumerTask = new DefaultOAuthConsumer(consumerKey, consumerSecret);
		serviceProvider = new DefaultOAuthProvider(requestTokenUrl, accessTokenUrl, authorizeUrl);
		
		try {
			String url = serviceProvider.retrieveRequestToken(consumerTask, "http://localhost:8000");

			//Set your page url in this string. For eg, I m using URL for Google Search engine
			OAuthVerifyCatcher verifierretriever = new OAuthVerifyCatcher();
			Thread t = new Thread(verifierretriever);
			t.start();
			java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
			while(!verifierretriever.isTerminate()){
				// hold process untill servlet finished;
			}
			t.interrupt();
			verifier = verifierretriever.getVerifier();
		} catch (OAuthMessageSignerException | OAuthNotAuthorizedException
				| OAuthExpectationFailedException | java.io.IOException |OAuthCommunicationException e) {
			throw new IndicoAuthException("Failed retreiving verification key from the server");
		}
	}

	@Override
	public String doGetRequest(String geturl, Map<String, String> params) throws IndicoAuthException {
		String result = "";
		if(verifier.length() == 0) throw new IndicoAuthException("verifier missing");
		try {
			if(!hasAccessToken){
				serviceProvider.retrieveAccessToken(consumerTask, verifier);
				hasAccessToken = true;
			}
			String urlstring = geturl;
			if(params != null && !params.isEmpty()){
				// in case we have get params add them to the url
				urlstring += "?";
				TreeMap<String,String> tmp = new TreeMap<String,String>(params);	// Sorted
				Iterator<Map.Entry<String, String>> paramIter = tmp.entrySet().iterator();
				while(paramIter.hasNext()){
					Map.Entry<String, String> en = paramIter.next();
					urlstring += String.format("%s=%s", en.getKey(), en.getValue());
					if(paramIter.hasNext()) urlstring += "&";
				}
			}
			System.out.printf("URL: %s\n", urlstring);
			// Now run the http request:
			URL indicoURL = new URL(urlstring);
			HttpURLConnection request = (HttpURLConnection)indicoURL.openConnection();
			consumerTask.sign(request);
			request.connect();
			BufferedReader outreader = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String line = "";
			StringBuilder resultBuilder = new StringBuilder();
			while((line = outreader.readLine()) != null)
				resultBuilder.append(line);
			result = resultBuilder.toString();
			request.disconnect();
			outreader.close();
		} catch (OAuthMessageSignerException | OAuthNotAuthorizedException
				| OAuthExpectationFailedException | OAuthCommunicationException e) {
			System.out.println("iamhere");
			throw new IndicoAuthException("OAuth failure");
		} catch (MalformedURLException e) {
			throw new IndicoAuthException("URL invalid");
		} catch (IOException e) {
			throw new IndicoAuthException("IO error");
		}
		return result;
	}

	@Override
	public String doPostRequest(String posturl, Map<String, String> params)
			throws IndicoAuthException {
		String result = "";
		if(verifier.length() == 0) throw new IndicoAuthException("verifier missing");
		try {
			if(!hasAccessToken){
				serviceProvider.retrieveAccessToken(consumerTask, verifier);
				hasAccessToken = true;
			}
			String poststring = "";
			HttpParameters paramsIn = new HttpParameters();
			if(params != null && !params.isEmpty()){
				// in case we have get params add them to the url
				TreeMap<String,String> tmp = new TreeMap<String,String>(params);	// Sorted
				Iterator<Map.Entry<String, String>> paramIter = tmp.entrySet().iterator();
				while(paramIter.hasNext()){
					Map.Entry<String, String> en = paramIter.next();
					paramsIn.put(en.getKey(),en.getValue());
					poststring += String.format("%s=%s", en.getKey(), en.getValue());
					if(paramIter.hasNext()) poststring += "&";
				}
			}
			// Sign the request
			HttpParameters doubleEncodedParams =  new HttpParameters();
	        Iterator<String> iter = paramsIn.keySet().iterator();
	        while (iter.hasNext()) {
	            String key = iter.next();
	            doubleEncodedParams.put(key, OAuth.percentEncode(paramsIn.getFirst(key)));
	        }
	        doubleEncodedParams.put("realm", posturl.toString());
	        OAuthConsumer cons = new DefaultOAuthConsumer(consumerTask.getConsumerKey(), consumerTask.getConsumerSecret());
	        cons.setTokenWithSecret(consumerTask.getToken(), consumerTask.getTokenSecret());
	        cons.setAdditionalParameters(doubleEncodedParams);
			
			// Now run the http request:
			URL indicoURL = new URL(posturl);
			HttpURLConnection request = (HttpURLConnection)indicoURL.openConnection();
			request.setRequestMethod("POST");
	        request.setRequestProperty("Accept", "application/json");
			request.setDoInput(true);
			request.setDoOutput(true);
			request.setUseCaches(false);
			cons.sign(request);
			request.connect();
			
			// Send request to indico
			OutputStreamWriter sender = new OutputStreamWriter(request.getOutputStream());
			sender.write(poststring);
			sender.flush();

			// Read answer from indico
			BufferedReader outreader = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String line = "";
			StringBuilder resultBuilder = new StringBuilder();
			while((line = outreader.readLine()) != null)
				resultBuilder.append(line);
			result = resultBuilder.toString();
			request.disconnect();
			outreader.close();
		} catch (OAuthMessageSignerException | OAuthNotAuthorizedException
				| OAuthExpectationFailedException | OAuthCommunicationException e) {
			throw new IndicoAuthException("OAuth failure");
		} catch (MalformedURLException e) {
			throw new IndicoAuthException("URL invalid");
		} catch (IOException e) {
			throw new IndicoAuthException("IO error");
		}
		return result;
	}
}
