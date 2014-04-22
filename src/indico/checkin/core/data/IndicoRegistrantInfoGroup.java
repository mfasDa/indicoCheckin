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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Java representation of the info group
 * 
 * @author Markus Fasel
 *
 */
public class IndicoRegistrantInfoGroup {
	private List<IndicoRegistrantInfoField> data;
	private String fossil;
	private String type;
	private long id;
	private String title;
	
	public IndicoRegistrantInfoGroup(){
		data = new ArrayList<IndicoRegistrantInfoField>();
		this.fossil = "";
		this.type = "";
		this.id = -1;
		this.title = "";
	}

	public IndicoRegistrantInfoGroup(String fossil, String type, long id, String title){
		data = new ArrayList<IndicoRegistrantInfoField>();
		this.fossil = fossil;
		this.type = type;
		this.id = id;
		this.title = title;
	}
	
	public String getFossil() {
		return fossil;
	}

	public void setFossil(String fossil) {
		this.fossil = fossil;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setInfoField(IndicoRegistrantInfoField field){
		data.add(field);
	}
	
	public IndicoRegistrantInfoField findFieldByCaption(String caption){
		IndicoRegistrantInfoField result = null;
		Iterator <IndicoRegistrantInfoField> fieldIter = data.iterator();
		while(fieldIter.hasNext()){
			IndicoRegistrantInfoField tmp = fieldIter.next();
			if(tmp.getCaption().toLowerCase().equals(caption.toLowerCase())){
				result = tmp;
				break;
			}
		}
		return result;
	}
	
	public IndicoRegistrantInfoField createInfoField(String caption){
		IndicoRegistrantInfoField field = new IndicoRegistrantInfoField();
		field.setCaption(caption);
		setInfoField(field);
		return field;
	}
	
	/**
	 * Get the full price of the group
	 * 
	 * @return the full price of the group
	 */
	public double getTotalPrice(){
		double price = 0;
		Iterator<IndicoRegistrantInfoField> fieldIter = data.iterator();
		while(fieldIter.hasNext()){
			IndicoRegistrantInfoField tmpField = fieldIter.next();
			if(tmpField.hasPrice() && tmpField.getQuantity() > 0){
				System.out.printf("Field: %s, Price: %f, Quantity: %d\n",
						tmpField.getCaption(), tmpField.getPrice(), tmpField.getQuantity());
				price += tmpField.getPrice()*tmpField.getQuantity();
			}
		}
		return price;
	}
}
