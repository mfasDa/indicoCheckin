/****************************************************************************
 *  Copyright (C) 2014  Markus Fasel <markus.fasel@cern.ch>                 *
 *                      Steffen Weber <s.weber@gsi.de>                      *
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
package indico.checkin.core.data;

/**
 * Authentification data for this app at the CERN indico server
 * 
 * @author Markus Fasel
 *
 */
public class IndicoCernOauthData {
	private static final String consumerKey = "DZrlHPLoBkjsH1c7LkmxIg2uHkGAdsZ6Zh7etABw";
	private static final String consumerSecret = "EYWF0P9KoYIe51qmX0IM5TBYsWfysYfYYq7wyixI";
	private static final String authorisationURL = "https://indico.cern.ch/oauth/authorize";
	private static final String requestTokenURL = "https://indico.cern.ch/oauth/request_token";
	private static final String accessTokenURL = "https://indico.cern.ch/oauth/access_token";
	
	/**
	 * Get the consumer key
	 * @return the consumer key
	 */
	public static String getConsumerkey() {
		return consumerKey;
	}
	
	/**
	 * Get the consumer secret
	 * @return the consumer secret
	 */
	public static String getConsumersecret() {
		return consumerSecret;
	}
	
	/**
	 * Get the authorisation URL
	 * @return the authorisation URL
	 */
	public static String getAuthorisationurl() {
		return authorisationURL;
	}

	/**
	 * Get the request token URL
	 * @return the request token URL
	 */
	public static String getRequesttokenurl() {
		return requestTokenURL;
	}
	
	/**
	 * Get the access token URL
	 * @return the access token URL
	 */
	public static String getAccesstokenurl() {
		return accessTokenURL;
	}
}
