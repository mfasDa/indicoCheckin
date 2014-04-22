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
package indico.checkin.core.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import indico.checkin.core.api.IndicoAPIConnector;
import indico.checkin.core.api.IndicoPostException;
import indico.checkin.core.api.RegistrantBuilderException;
import indico.checkin.core.api.RegistrantListFetchingException;
import indico.checkin.core.data.IndicoEventRegistrantList;
import indico.checkin.core.data.IndicoLoginData;
import indico.checkin.core.data.IndicoParsedETicket;
import indico.checkin.core.data.IndicoRegistrant;
import indico.checkin.core.export.PdfExporter;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * Class respresenting the main window of the checkin app GUI
 * @author Markus Fasel
 *
 */
public class IndicoCheckinAppMainGui extends JFrame implements ActionListener, WindowListener {
	private static final long serialVersionUID = 1L;
	private JButton loginbutton;
	private JButton apiinfobutton;
	private JButton newUserButton;
	private JButton changePaymentButton;
	private JButton generateTicketButton;
	private JButton checkinButton;
	private JButton cancelButton;
	private JButton manualSearchButton;
	
	private InfoPanel infopanel;
	
	// Thread for new registrant
	private Thread newregthread;
	
	// Data
	private IndicoEventRegistrantList registrants;
	private IndicoParsedETicket eticket;
	private IndicoRegistrant current;
	private RegistrantListModel manualSearchModel;
	
	private boolean isLoggedIn;
	
	// connection to indico server
	private IndicoAPIConnector indicoConnection;
	
	
	// pdf export
	private PdfExporter pdfExporter = new PdfExporter();
	
	/**
	 * Default constructor: Create the gui and show it
	 */
	public IndicoCheckinAppMainGui(){
		
		try {
			// Set the look and feel to the system look and feel
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} 
		this.getContentPane().setLayout(null);
		this.initWindow();
		
		registrants = null;
		current = null;
		indicoConnection = new IndicoAPIConnector();
		newregthread = null;
		
		isLoggedIn = false;
	}

	/**
	 * Create main window
	 */
	protected void initWindow(){
		this.setTitle("Indico checkin");
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(this);
		
		infopanel = new InfoPanel(this);
		
		// Define Panel for admin buttons
		JPanel userButtonPanel = new JPanel(new GridBagLayout());
		
		this.loginbutton = new JButton("Login");
		this.loginbutton.addActionListener(this);
		this.loginbutton.setActionCommand("login");
		this.loginbutton.setEnabled(true);
		GridBagConstraints lbcs = new GridBagConstraints();
		lbcs.fill = GridBagConstraints.HORIZONTAL;
		lbcs.gridx = 0;
		lbcs.gridy = 0;
		userButtonPanel.add(this.loginbutton, lbcs);
		
		this.apiinfobutton = new JButton("API Info");
		this.apiinfobutton.setActionCommand("apiinfo");
		this.apiinfobutton.addActionListener(this);
		this.apiinfobutton.setEnabled(false);
		GridBagConstraints abcs = new GridBagConstraints();
		abcs.fill = GridBagConstraints.HORIZONTAL;
		abcs.gridx = 0;
		abcs.gridy = 1;
		userButtonPanel.add(this.apiinfobutton, abcs);

		JButton exitbutton = new JButton("Exit");
		exitbutton.setActionCommand("exit");
		exitbutton.addActionListener(this);
		GridBagConstraints ebcs = new GridBagConstraints();
		ebcs.fill = GridBagConstraints.HORIZONTAL;
		ebcs.gridx = 0;
		ebcs.gridy = 2;
		userButtonPanel.add(exitbutton, ebcs);

				
		// Define panel for new user checkin process
		JPanel processPanel = new JPanel(new GridBagLayout());
		
		this.newUserButton = new JButton("New registrant");
		this.newUserButton.addActionListener(this);
		this.newUserButton.setActionCommand("newUser");
		this.newUserButton.setEnabled(false);
		GridBagConstraints nbcs = new GridBagConstraints();
		nbcs.fill = GridBagConstraints.HORIZONTAL;
		nbcs.gridx = 0;
		nbcs.gridy = 0;
		nbcs.gridwidth = 3;
		processPanel.add(this.newUserButton, nbcs);
		
		this.manualSearchButton = new JButton("Search registrant");
		this.manualSearchButton.addActionListener(this);
		this.manualSearchButton.setActionCommand("manualSearch");
		this.manualSearchButton.setEnabled(false);
		GridBagConstraints mbcs = new GridBagConstraints();
		mbcs.fill = GridBagConstraints.HORIZONTAL;
		mbcs.gridx = 3;
		mbcs.gridy = 0;
		mbcs.gridwidth = 3;
		processPanel.add(this.manualSearchButton, mbcs);

		this.changePaymentButton = new JButton("Set payed");
		this.changePaymentButton.addActionListener(this);
		this.changePaymentButton.setActionCommand("changePayment");
		this.changePaymentButton.setEnabled(false);
		GridBagConstraints cpbcs = new GridBagConstraints();
		cpbcs.fill = GridBagConstraints.HORIZONTAL;
		cpbcs.gridx = 0;
		cpbcs.gridy = 1;
		cpbcs.gridwidth = 2;
		processPanel.add(this.changePaymentButton, cpbcs);
		
		this.generateTicketButton = new JButton("Generate Ticket");
		this.generateTicketButton.addActionListener(this);
		this.generateTicketButton.setActionCommand("generateTicket");
		this.generateTicketButton.setEnabled(false);
		GridBagConstraints gtbcs = new GridBagConstraints();
		gtbcs.fill = GridBagConstraints.HORIZONTAL;
		gtbcs.gridx = 2;
		gtbcs.gridy = 1;
		gtbcs.gridwidth = 2;
		processPanel.add(this.generateTicketButton, gtbcs);

		this.checkinButton = new JButton("Checkin");
		this.checkinButton.addActionListener(this);
		this.checkinButton.setActionCommand("checkin");
		this.checkinButton.setEnabled(false);
		GridBagConstraints cibcs = new GridBagConstraints();
		cibcs.fill = GridBagConstraints.HORIZONTAL;
		cibcs.gridx = 4;
		cibcs.gridy = 1;
		cibcs.gridwidth = 2;
		processPanel.add(this.checkinButton, cibcs);
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("cancel");
		GridBagConstraints cabcs = new GridBagConstraints();
		cabcs.fill = GridBagConstraints.HORIZONTAL;
		cabcs.gridx = 0;
		cabcs.gridy = 2;
		cabcs.gridwidth = 6;
		processPanel.add(cancelButton, cabcs);
		
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(userButtonPanel);
		buttonPanel.add(processPanel);
		
		this.getContentPane().add(infopanel, BorderLayout.NORTH);
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
	}
	
	/**
	 * Enable button generating a ticket
	 */
	public void EnableTicketButton(){
		 generateTicketButton.setEnabled(true);
	}
	
	/**
	 * Enable button generating a ticket
	 */
	public void DisableTicketButton(){
		generateTicketButton.setEnabled(false);
	}

	/**
	 * Handle click to the different buttons
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getActionCommand().equals("login")){
			handleLogin();
		} else if(arg0.getActionCommand().equals("apiinfo")){
			showApiInfoDialog();
		} else if(arg0.getActionCommand().equals("exit")){
			handleExit();
		}else if(arg0.getActionCommand().equals("newUser")){
			newUserClicked();
		}else if(arg0.getActionCommand().equals("changePayment")){
			changePaymentClicked();
		}else if(arg0.getActionCommand().equals("checkin")){
			handleCheckinButton();
		}else if(arg0.getActionCommand().equals("cancel")){
			handleCancel();
		} else if(arg0.getActionCommand().equals("manualSearch")){
			handleManualSearch();
		}else if(arg0.getActionCommand().equals("generateTicket")){
			handleGenerateTicket();
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		System.exit(0);
	}

	@Override
	public void windowClosing(WindowEvent e) {
		/* 
		 * Show closing dialog
		 * If user confirms closing, dispose window
		 */
		handleExit();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * show exit dialog
	 */
	private void handleExit(){
		if(newregthread != null){
			// Shut down new registrant thread before exiting the program
			newregthread.interrupt();
		}
		if(JOptionPane.showConfirmDialog(null, "Close Program?") == JOptionPane.OK_OPTION)
			this.dispose();
	}
	
	/**
	 * Show login dialog
	 * Login dialog will return to function processLoginReturn
	 */
	private void handleLogin(){
		IndicoAPILoginDialog loginDialog = new IndicoAPILoginDialog(this);
		loginDialog.setVisible(true);
	}
	
	/**
	 * return function for the login dialog: 
	 * - Connects to server and fetches registrant list
	 * - In case the registrant list was transferred successfully,
	 *   enable button for user processing
	 * - Inform the user with message dialog about the result of the transfer
	 * 
	 * @param data: Login information
	 */
	public void processLoginReturn(IndicoLoginData data){
		this.indicoConnection.setAuthentifier(data.getAuthentifier());
		this.indicoConnection.setServer(data.getServer());
		this.indicoConnection.setEventID(data.getEvent());
		boolean success = false;
		try {
			this.registrants = this.indicoConnection.fetchRegistrantList();
			success = true;
		} catch (RegistrantListFetchingException e) {
			// Failed getting registrant list from the server 
			JOptionPane.showMessageDialog(this, String.format("Failed loading registrant list: %s", e.getMessage()));
		}
		if(success){
			/*
			 * Registrants correctly read:
			 * Show message dialog with the number of registrants
			 * Enable buttons for registrant processing and disable login
			 * button
			 */
			if(this.registrants.getNumberOfRegistrants() > 0){
				JOptionPane.showMessageDialog(this, 
						String.format("Found %d registrants for event %s", 
								this.registrants.getNumberOfRegistrants(), 
								this.indicoConnection.getEventID()));
				this.newUserButton.setEnabled(true);
				this.loginbutton.setEnabled(false);
				this.manualSearchButton.setEnabled(true);
				this.isLoggedIn = true;
				manualSearchModel = new RegistrantListModel(registrants);
			} else {
				JOptionPane.showMessageDialog(this, 
						String.format("No registrants for event %s", 
								this.indicoConnection.getEventID()));
			}
			this.apiinfobutton.setEnabled(true);
		}

	}
	
	/**
	 * Show Dialog with API connection details
	 */
	private void showApiInfoDialog(){
		IndicoAPIInfoDialog infoDialog = new IndicoAPIInfoDialog(this, 
				this.indicoConnection.getServer(),
				this.indicoConnection.getEventID(),
				this.indicoConnection.getAuthentifier());
		infoDialog.setVisible(true);
	}
	
	/**
	 * Handle click to newUserButton
	 */
	public void newUserClicked(){
		this.newUserButton.setEnabled(false);
		this.changePaymentButton.setEnabled(false);
		this.checkinButton.setEnabled(false);
		this.generateTicketButton.setEnabled(false);
		eticket = null;
		current = null;
		newregthread = new Thread(new Webcamcatcher(this));
		newregthread.start();
	}
	
	/**
	 * Registrant has paid at the conference desk:
	 * Enable buttons for checkin and ticket generation
	 * Put information to indico
	 */
	public void changePaymentClicked(){
		boolean success = false;
		try {
			success = this.indicoConnection.pushPayment(current);
		} catch (IndicoPostException e) {
			JOptionPane.showMessageDialog(this, String.format("Payment setting failed: %s", e.getMessage()));
		}
		if(success){
			this.changePaymentButton.setEnabled(false);
			this.generateTicketButton.setEnabled(true);
			if(!current.hasCheckedIn())
				this.checkinButton.setEnabled(true);
			infopanel.UpdateRegistrantDisplay();
		}
	}
	
	public void printTicketClicked(){
		/*
		 * Handle click to printTicket button
		 */
	}

	/**
	 * Open dialog with a list of all registrants where the user can search
	 * a given registrant by the name. In case a registrant is selected, the same
	 * procedure as for e-tickets is applied
	 */
	public void handleManualSearch(){
		ManualSearchDialog searchDialog = new ManualSearchDialog(this, manualSearchModel);
		searchDialog.setVisible(true);
		if(searchDialog.isEntrySelected()){
			current = searchDialog.getSelectedRegistrant();
			//current = manualSearchModel.getRegistrantForRow(searchDialog.getSelectedRow());
			if(current != null){
				try{
					this.indicoConnection.fetchFullRegistrantInformation(current);
					this.infopanel.UpdateRegistrantData(current);

					if(current.hasPaid()){
						// if the registrant has already payed, allow checkin
						// TODO: remove printout when debugging finished
						System.out.println("Registrant has already paid");
						this.generateTicketButton.setEnabled(true);
						if(!current.hasCheckedIn())
							this.checkinButton.setEnabled(true);
						this.changePaymentButton.setEnabled(false);
					} else {
						// Only enable button that the changes payment
						// TODO: remove printout when debugging finished
						System.out.println("Registrant did not yet pay");
						this.generateTicketButton.setEnabled(false);
						this.checkinButton.setEnabled(false);
						this.changePaymentButton.setEnabled(true);
					}

				} catch(RegistrantBuilderException e){
					JOptionPane.showMessageDialog(this, String.format("Error accessing registration information for registrant %d", current.getID()));
				}
			}
		}
	}
	
	/**
	 * Registrant checked in: 
	 * Post checkin status to indico, update registrant and registrant display, 
	 * and disable checkin button 
	 */
	public void handleCheckinButton(){
		if(current != null){
			try {
				boolean status = indicoConnection.pushCheckin(current);
				if(status){
					// checkin successfull
					JOptionPane.showMessageDialog(this, String.format("Registrant %s successfully checked in", current.getFullName()));
					infopanel.UpdateRegistrantDisplay();
					this.checkinButton.setEnabled(false);					
				} else
					JOptionPane.showMessageDialog(this, String.format("Checkin of registrant %s was not successfull", current.getFullName()));
			} catch (IndicoPostException e) {
				JOptionPane.showMessageDialog(this, String.format("Checkin failure: %s", e.getMessage()));
			}
		}
	}

	/**
	 * Ticket was parsed
	 */
	public void handleEticketParsed(){
		if(registrants.isTicketValid(eticket)){
			// check ticket validity
			current = registrants.FindRegistrant(eticket);
			if(current != null){
				System.out.printf("Fetch full information for registrant %d\n", eticket.getRegistrantID());
				try{
					this.indicoConnection.fetchFullRegistrantInformation(current);
					this.infopanel.UpdateRegistrantData(current);
				} catch (RegistrantBuilderException e){
					JOptionPane.showMessageDialog(this, String.format("Failed reading registrant: %s", e.getMessage()));
				}
				JOptionPane.showMessageDialog(this, String.format("ETicket read successfully and valid: %s", current.getFullName()));				
				if(current.hasPaid()){
					// if the registrant has already payed, allow checkin
					// TODO: remove printout when debugging finished
					System.out.println("Registrant has already paid");
					this.generateTicketButton.setEnabled(true);
					if(!current.hasCheckedIn())
						this.checkinButton.setEnabled(true);
					this.changePaymentButton.setEnabled(false);
				} else {
					// Only enable button that the changes payment
					// TODO: remove printout when debugging finished
					System.out.println("Registrant did not yet pay");
					this.generateTicketButton.setEnabled(false);
					this.checkinButton.setEnabled(false);
					this.changePaymentButton.setEnabled(true);
				}
			} else {
				JOptionPane.showMessageDialog(this, "ETicket read successfully, but registrant not found");
			}
		} else {
			JOptionPane.showMessageDialog(this, "Invalid ETicket");
		}
	}

	/**
	 * User pressed cancel:
	 * Finish registrant thread, deactivate user buttons, and remove link for current user
	 */
	public void handleCancel(){
		this.current = null;
		
		this.generateTicketButton.setEnabled(false);
		this.checkinButton.setEnabled(false);
		this.changePaymentButton.setEnabled(false);
		
		finishBarcodeThread();
	}

	public void handleGenerateTicket(){
		pdfExporter.setRegistrant(current);
		pdfExporter.exportPdf();
	}
	
	/**
	 * enable new user button
	 */
	public void finishBarcodeThread(){
		if(isLoggedIn) this.newUserButton.setEnabled(true);
		if(newregthread != null){
			newregthread.interrupt();
			newregthread = null;
		}
	}
	
	public void setTicket(IndicoParsedETicket ticket){
		eticket = ticket;
		if(eticket != null)
			handleEticketParsed();
		else
			JOptionPane.showMessageDialog(this, "Failed reading eticket");
		if(isLoggedIn) this.newUserButton.setEnabled(true);
	}
	
	public InfoPanel getInfoPanel(){
		return infopanel;
	}
}
