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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Server catching the answer from indico for a request token 
 * Class is running in a thread, providing a verification string to the 
 * application when the thread is done.
 * 
 * @author Markus Fasel
 *
 */
public class OAuthVerifyCatcher implements Runnable{

	/**
	 * Helper class for key/value representation of get parameters
	 * 
	 * @author Markus Fasel
	 */
	private class Pair{
		String first;
		String second;
		
		public Pair(String first, String second){
			this.first = first;
			this.second = second;
		}
		
		public String getFirst(){
			return first;
		}
		
		public String getSecond(){
			return second;
		}
	}
	
	private boolean isTerminate;
	private String verifier;
	
	public synchronized boolean isTerminate() {
		return isTerminate;
	}

	public void setTerminate(boolean isTerminate) {
		this.isTerminate = isTerminate;
	}

	public synchronized String getVerifier() {
		return verifier;
	}

	public void setVerifier(String verifier) {
		this.verifier = verifier;
	}


	@Override
	public void run() {
		isTerminate = false;
		verifier = "";
		try {
			ServerSocket server = new ServerSocket(8000);
			Socket client = server.accept();
			BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter writer = new PrintWriter(client.getOutputStream(),true);
			String line = "";
			while ((line = reader.readLine()) != null){
				if(line.length() != 0){
					if(line.contains("GET")){
						// read the verifier token
						Map<String,String> params = ExtractGetParamters(line);
						writer.write("<html><head><title>Finished</title></head><body>Please go back to your application</body></html>");
						String myVerifier = null;
						if((myVerifier = params.get("oauth_verifier")) != null){
							verifier = myVerifier;
						}	
						break;
					}
				}
			}
			reader.close();
			client.close();
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isTerminate=true;	
	}
	
	/**
	 * Converts a string of get parameters into a map
	 * 
	 * @param line: string containing get parameters
	 * @return Map with get parameters in key-value representation
	 */
	private Map<String,String> ExtractGetParamters(String line){
		Map<String,String> result = new HashMap<String,String>();
		StringTokenizer toks = new StringTokenizer(line, " ");
		while(toks.hasMoreTokens()){
			String token = toks.nextToken();
			if(token.contains("?")){
				// here are the get parameters
				int pos = token.indexOf("?");
				String paramsstring = token.substring(pos+1);
				if(paramsstring.contains("&")){
					StringTokenizer paramTokenizer = new StringTokenizer(paramsstring,"&");
					while(paramTokenizer.hasMoreElements()){
						String parstring = paramTokenizer.nextToken();
						Pair param = DecodeParam(parstring);
						result.put(param.getFirst(), param.getSecond());
					}
				} else {
					Pair param = DecodeParam(paramsstring);
					result.put(param.getFirst(), param.getSecond());
				}
			}
		}
		return result;
	}
	
	/**
	 * Decode parameters into key/value pair
	 * 
	 * @param paramstring: string-representation of the key/value pair
	 * @return key/value pair of the get param
	 */
	private Pair DecodeParam(String paramstring){
		int delim = paramstring.indexOf("=");
		Pair result = new Pair(paramstring.substring(0, delim),paramstring.substring(delim+1));
		return result;
	}

}
