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
 * Java representation of registrant info field
 * 
 * @author Markus Fasel
 *
 */
public class IndicoRegistrantInfoField {
	
	private String type;
	private String htmlname;
	private String caption;
	private String value;
	private double price;
	private String currency;
	private String fossil;
	private long id;
	private int quantity;

	public IndicoRegistrantInfoField(String type, 
			String htmlname, String caption, String value, int price, 
			String currency, String fossil, long id, int quantity){
		this.type = type;
		this.htmlname = htmlname;
		this.caption = caption;
		this.value = value;
		this.price = price;
		this.currency = currency;
		this.fossil = fossil;
		this.id = id;
		this.quantity = quantity;
	}
	
	public IndicoRegistrantInfoField(){
		this.type = "";
		this.htmlname = "";
		this.caption = "";
		this.value = "";
		this.price = 0;
		this.currency = "";
		this.fossil = "";
		this.id = -1;
		this.quantity = 0;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHtmlname() {
		return htmlname;
	}

	public void setHtmlname(String htmlname) {
		this.htmlname = htmlname;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getFossil() {
		return fossil;
	}

	public void setFossil(String fossil) {
		this.fossil = fossil;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	/**
	 * Check if field has a positive, non-0 price
	 *
	 * @return the status
	 */
	public boolean hasPrice() {
		return price >= 0 && Math.abs(price) > 1e-5;
	}

}
