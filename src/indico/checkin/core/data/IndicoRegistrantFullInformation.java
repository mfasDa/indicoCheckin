package indico.checkin.core.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class IndicoRegistrantFullInformation {
	/**
	 * Container object for full registrant information
	 * License: GPLv3 (a copy of the license is provided with the package)
	 * 
	 * @author: Markus Fasel
	 */
	List<IndicoRegistrantInfoGroup> miscallaneousGroups;
	List<IndicoRegistrantSocialEvent> socialEvents;
	private String type;
	private String fossil;
	private String fullName;
	private long registrantID;
	private boolean complete;
	private boolean checkedin;
	private boolean paid;
	private double amountPaid;
	private String reasonForParticipation;
	private Date registrationDate;
	private Date checkinDate;
	
	public IndicoRegistrantFullInformation(){
		miscallaneousGroups = new LinkedList<IndicoRegistrantInfoGroup>();
		socialEvents = new LinkedList<IndicoRegistrantSocialEvent>();
		type = "";
		fossil = "";
		fullName = "";
		registrantID = 0;
		checkedin = false;
		paid = false;
		amountPaid = 0.;
		reasonForParticipation = "";
		registrationDate = null;
		checkinDate = null;
	}
	
	public void AddInfoGroup(IndicoRegistrantInfoGroup group){
		/*
		 * Add info group to the list of groups
		 */
		miscallaneousGroups.add(group);
	}
	
	public void AddSocialEvent(IndicoRegistrantSocialEvent event){
		/*
		 * Add social event to the list of social events
		 */
		socialEvents.add(event);
	}
	
	public IndicoRegistrantInfoGroup createInfoGroup(String title){
		/*
		 * Create new info group with a title and add it to the list of 
		 * groups
		 */
		IndicoRegistrantInfoGroup group = new IndicoRegistrantInfoGroup();
		group.setTitle(title);
		AddInfoGroup(group);
		return group;
	}
	
	public IndicoRegistrantInfoGroup createInfoGroup(String title, String fossil, 
			String type, long id){
		/*
		 * Create new info group with a title and add it to the list of 
		 * groups
		 */
		IndicoRegistrantInfoGroup group = new IndicoRegistrantInfoGroup();
		group.setTitle(title);
		group.setFossil(fossil);
		group.setType(type);
		group.setId(id);
		AddInfoGroup(group);
		return group;
	}

	public IndicoRegistrantSocialEvent createSocialEvent(int id){
		/*
		 * Create a new social event with only id as information and add it to 
		 * the list of social events
		 */
		IndicoRegistrantSocialEvent event = new IndicoRegistrantSocialEvent();
		event.setId(id);
		AddSocialEvent(event);
		return event;
	}
	
	public IndicoRegistrantSocialEvent createSocialEvent(String type, String fossil, 
			int id, double price, long numberPlaces, String currency, String caption){
		/*
		 * Create a new social event with all informations and add it to the list 
		 * of social events
		 */
		IndicoRegistrantSocialEvent event = new IndicoRegistrantSocialEvent(type,fossil,id,price,numberPlaces,currency,caption);
		AddSocialEvent(event);
		return event;
	}
	
	public List<IndicoRegistrantInfoGroup> getListOfGroups(){
		return miscallaneousGroups;
	}
	
	public IndicoRegistrantInfoGroup findGroupByTitle(String title){
		/*
		 * Try to find info group according to the title
		 */
		Iterator<IndicoRegistrantInfoGroup> groupIter = miscallaneousGroups.iterator();
		IndicoRegistrantInfoGroup result = null;
		while(groupIter.hasNext()){
			IndicoRegistrantInfoGroup group = groupIter.next();
			if(group.getTitle().equals(title)){
				result = group;
				break;
			}
		}
		return result;
	}
	
	public List<IndicoRegistrantSocialEvent> getSocialEvents(){
		return socialEvents;
	}
	
	public IndicoRegistrantSocialEvent findSocialEventById(int id){
		/*
		 * Find the social event by the event id
		 * returns the event, or null if not found
		 */
		IndicoRegistrantSocialEvent event = null;
		Iterator<IndicoRegistrantSocialEvent> eventIter = socialEvents.iterator();
		while(eventIter.hasNext()){
			IndicoRegistrantSocialEvent tmpEvent = eventIter.next();
			if(tmpEvent.getId() == id){
				event = tmpEvent;
				break;
			}
		}
		return event;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isCheckedin() {
		return checkedin;
	}

	public void setCheckedin(boolean checkedin) {
		this.checkedin = checkedin;
	}

	public double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(double amountPaied) {
		this.amountPaid = amountPaied;
	}

	public String getReasonForParticipation() {
		return reasonForParticipation;
	}

	public void setReasonForParticipation(String reasonForParticipation) {
		this.reasonForParticipation = reasonForParticipation;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public String getFossil() {
		return fossil;
	}

	public void setFossil(String fossil) {
		this.fossil = fossil;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public long getRegistrantID() {
		return registrantID;
	}

	public void setRegistrantID(long registrantID) {
		this.registrantID = registrantID;
	}

	public void setRegistrationDate(String datestring){
		if(!datestring.isEmpty()){
			DateFormat formatter = new SimpleDateFormat("dd/mm/yy HH:MM");
			try {
				this.registrationDate = formatter.parse(datestring);
			} catch (ParseException e) {
				System.out.println("Invalid date format");
			}
		}
	}
	
	public void setCheckinDate(long timeinmilliseconds){
		this.checkinDate = new Date(timeinmilliseconds);
	}
	
	public void setCheckinDate(String datestring){
		if(!datestring.isEmpty()){
			DateFormat formatter = new SimpleDateFormat("dd/mm/yy HH:MM");
			try {
				this.checkinDate = formatter.parse(datestring);
			} catch (ParseException e) {
				System.out.println("Invalid date format");
			}
		}
	}

	
	public Date getRegistrationDate(){
		return this.registrationDate;
	}
	
	public Date getCheckinDate(){
		return this.checkinDate;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	
	public double getFullPrice(){
		/*
		 * Calculate the full price of the registrant
		 */
		double fullPrice = 0.;
		Iterator<IndicoRegistrantInfoGroup> groupIter = miscallaneousGroups.iterator();
		while(groupIter.hasNext()){
			IndicoRegistrantInfoGroup tmpGroup = groupIter.next();
			double tmpprice = tmpGroup.getTotalPrice();
			fullPrice += tmpprice;
			if(tmpprice > 0)
				// TODO: Remove debug statement when code is finished
				System.out.printf("Group: %s, Price: %f\n", tmpGroup.getTitle(), tmpprice);
		}
		// Process social events
		Iterator<IndicoRegistrantSocialEvent> eventIter = socialEvents.iterator();
		while(eventIter.hasNext()){
			IndicoRegistrantSocialEvent event = eventIter.next();
			if(event.getPrice() > 0)
				// TODO: Remove debug statement when code is finished
				System.out.printf("Registrant booked event: %d - %s - with price %f %s\n", event.getId(), event.getCaption(), event.getPrice(), event.getCurrency());
			fullPrice += event.getTotalPrice();
		}
		// TODO: Remove debug statement when code is finished
		System.out.println("=================================");
		System.out.printf("Total price: %.2f\n", fullPrice);
		return fullPrice;
	}
}