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
package indico.checkin.core.data;

/**
 * Data structure for the social event
 *
 * @author: Markus Fasel
 */
public class IndicoRegistrantSocialEvent {
		
	private String type;
	private String fossil;
	private int id;
	private double price;
	private long numberPlaces;
	private String currency;
	private String caption;
	
	public IndicoRegistrantSocialEvent(){
		this.type = "";
		this.fossil = "";
		this.id = -1;
		this.price = 0.0;
		this.numberPlaces = 0;
		this.currency = "";
		this.caption = "";
	}
	
	public IndicoRegistrantSocialEvent(String type, String fossil, 
			int id, double price, long numberPlaces, String currency, String caption){
		this.type = type;
		this.fossil = fossil;
		this.id = id;
		this.price = price;
		this.numberPlaces = numberPlaces;
		this.currency = currency;
		this.caption = caption;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFossil() {
		return fossil;
	}

	public void setFossil(String fossil) {
		this.fossil = fossil;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public long getNumberPlaces() {
		return numberPlaces;
	}

	public void setNumberPlaces(long numberPlaces) {
		this.numberPlaces = numberPlaces;
	}

	public double getTotalPrice(){
		return numberPlaces * price;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
}
