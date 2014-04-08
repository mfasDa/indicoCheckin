package indico.checkin.core.data;

public class IndicoRegistrantInfoField {
	/**
	 * Java representation of registrant info field
	 */
	
	private String type;
	private String htmlname;
	private String caption;
	private String value;
	private int price;
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
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

}
