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
package indico.checkin.core.api;

import java.util.Map;

/**
 * Interface defining methods for accessing the Indico api via the HTTP. Methods
 * for GET and POST request are implemented
 * 
 * @author Markus Fasel <markus.fasel@cern.ch>
 *
 */
public interface IndicoAuthentificationLayer {
	
	/**
	 * Definition for get access to the indico api
	 * @param geturl: URL for the get request
	 * @param params: Additional get params
	 * @return Answer from the server
	 * @throws IndicoAuthException
	 */
	public String doGetRequest(String geturl, Map<String, String> params) throws IndicoAuthException;
	
	/**
	 * Definition for get access to the indico api
	 * @param geturl
	 * @param params
	 * @return
	 * @throws IndicoAuthException
	 */
	public String doPostRequest(String geturl, Map<String, String> params) throws IndicoAuthException;
}
