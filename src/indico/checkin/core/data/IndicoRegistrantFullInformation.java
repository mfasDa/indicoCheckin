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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Container object for full registrant information.
 * Used inside the indico registrant:
 * Full information consists of a list of groups, which themselves 
 * contain a list of fields. Groups are build as in the indico
 * registration form.
 * 
 * @author: Markus Fasel
 */
public class IndicoRegistrantFullInformation {
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
	
	/**
	 * Default constructor
	 */
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
	
	/**
	 * Add info group to the list of groups
	 * 
	 * @param group
	 */
	public void AddInfoGroup(IndicoRegistrantInfoGroup group){
		miscallaneousGroups.add(group);
	}
	
	/**
	 * Add social event to the list of social events
	 * 
	 * @param event
	 */
	public void AddSocialEvent(IndicoRegistrantSocialEvent event){
		socialEvents.add(event);
	}
	
	/**
	 * Create new info group with a title and add it to the list of 
	 * groups
	 * 
	 * @param title
	 * @return the new info group
	 */
	public IndicoRegistrantInfoGroup createInfoGroup(String title){
		IndicoRegistrantInfoGroup group = new IndicoRegistrantInfoGroup();
		group.setTitle(title);
		AddInfoGroup(group);
		return group;
	}
	
	/**
	  * Create new info group with a title and add it to the list of 
	 * groups
	 *
	 * @param title
	 * @param fossil
	 * @param type
	 * @param id
	 * @return the new info group
	 */
	public IndicoRegistrantInfoGroup createInfoGroup(String title, String fossil, 
			String type, long id){
		IndicoRegistrantInfoGroup group = new IndicoRegistrantInfoGroup();
		group.setTitle(title);
		group.setFossil(fossil);
		group.setType(type);
		group.setId(id);
		AddInfoGroup(group);
		return group;
	}

	/**
	 * Create a new social event with only id as information and add it to 
	 * the list of social events
	 * 
	 * @param id
	 * @return a social event  with only a given ID
	 */
	public IndicoRegistrantSocialEvent createSocialEvent(int id){
		IndicoRegistrantSocialEvent event = new IndicoRegistrantSocialEvent();
		event.setId(id);
		AddSocialEvent(event);
		return event;
	}
	
	/**
	 * Create a new social event with all informations and add it to the list 
	 * of social events
	 *
	 * @param type
	 * @param fossil
	 * @param id
	 * @param price
	 * @param numberPlaces
	 * @param currency
	 * @param caption
	 * @return the new social event
	 */
	public IndicoRegistrantSocialEvent createSocialEvent(String type, String fossil, 
			int id, double price, long numberPlaces, String currency, String caption){
		IndicoRegistrantSocialEvent event = new IndicoRegistrantSocialEvent(type,fossil,id,price,numberPlaces,currency,caption);
		AddSocialEvent(event);
		return event;
	}
	
	/**
	 * Get a list of info groups
	 * 
	 * @return the list of info groups
	 */
	public List<IndicoRegistrantInfoGroup> getListOfGroups(){
		return miscallaneousGroups;
	}
	
	/**
	 * Try to find info group according to the title
	 * 
	 * @param title: Group title
	 * @return the group object (null if not found)
	 */
	public IndicoRegistrantInfoGroup findGroupByTitle(String title){
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
	
	/**
	 * Get a list of social events
	 * 
	 * @return List of social events
	 */
	public List<IndicoRegistrantSocialEvent> getSocialEvents(){
		return socialEvents;
	}
	
	/**
	 * Find the social event by the event id
	 * 
	 * @param id: the event ID 
	 * @return a social event object (null if not found)
	 */
	public IndicoRegistrantSocialEvent findSocialEventById(int id){
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
	
	/**
	 * Calculate the full price of the registrant
	 * 
	 * @return the full price
	 */
	public double getFullPrice(){
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