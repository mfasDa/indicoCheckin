package indico.checkin.core.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
		return null;
	}
	
	public IndicoRegistrantInfoField createInfoField(String caption){
		IndicoRegistrantInfoField field = new IndicoRegistrantInfoField();
		field.setCaption(caption);
		setInfoField(field);
		return field;
	}
	
	public double getTotalPrice(){
		/*
		 * Get the full price of the group
		 */
		double price = 0;
		Iterator<IndicoRegistrantInfoField> fieldIter = data.iterator();
		while(fieldIter.hasNext()){
			IndicoRegistrantInfoField tmpField = fieldIter.next();
			if(tmpField.hasPrice() && tmpField.getQuantity() > 0){
				price += tmpField.getPrice()*tmpField.getQuantity();
			}
		}
		return price;
	}
}
