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
import indico.checkin.core.api.RegistrantListFetchingException;
import indico.checkin.core.data.IndicoEventRegistrantList;
import indico.checkin.core.data.IndicoParsedETicket;
import indico.checkin.core.data.IndicoRegistrant;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class IndicoCheckinAppMainGui extends JFrame implements ActionListener, WindowListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton loginbutton;
	private JButton apiinfobutton;
	private JButton newUserButton;
	private JButton changePaymentButton;
	private JButton generateTicketButton;
	private JButton checkinButton;
	private JButton cancelButton;
	
	private InfoPanel infopanel;
	
	// Thread for new registrant
	private Thread newregthread;
	
	// Data
	private IndicoEventRegistrantList registrants;
	private IndicoParsedETicket eticket;
	private IndicoRegistrant current;
	private IndicoBarcodeHandler barcodeHandler;
	
	private boolean isLoggedIn;
	
	// connection to indico server
	private IndicoAPIConnector indicoConnection;
	
	public IndicoCheckinAppMainGui(){
		// Default constructor
		this.getContentPane().setLayout(null);
		this.initWindow();
		
		registrants = null;
		current = null;
		indicoConnection = new IndicoAPIConnector();
		barcodeHandler = new IndicoBarcodeHandler();
		newregthread = null;
		
		isLoggedIn = false;
	}

	
	protected void initWindow(){
		/*
		 * Create main window
		 */
		this.setTitle("Indico checkin");
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(this);
		
		infopanel = new InfoPanel();
		
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

		this.changePaymentButton = new JButton("Set payed");
		this.changePaymentButton.addActionListener(this);
		this.changePaymentButton.setActionCommand("changePayment");
		this.changePaymentButton.setEnabled(false);
		GridBagConstraints cpbcs = new GridBagConstraints();
		cpbcs.fill = GridBagConstraints.HORIZONTAL;
		cpbcs.gridx = 0;
		cpbcs.gridy = 1;
		cpbcs.gridwidth = 1;
		processPanel.add(this.changePaymentButton, cpbcs);
		
		this.generateTicketButton = new JButton("Generate Ticket");
		this.generateTicketButton.addActionListener(this);
		this.generateTicketButton.setActionCommand("generateTicket");
		this.generateTicketButton.setEnabled(false);
		GridBagConstraints gtbcs = new GridBagConstraints();
		gtbcs.fill = GridBagConstraints.HORIZONTAL;
		gtbcs.gridx = 1;
		gtbcs.gridy = 1;
		gtbcs.gridwidth = 1;
		processPanel.add(this.generateTicketButton, gtbcs);

		this.checkinButton = new JButton("Checkin");
		this.checkinButton.addActionListener(this);
		this.checkinButton.setActionCommand("checkin");
		this.checkinButton.setEnabled(false);
		GridBagConstraints cibcs = new GridBagConstraints();
		cibcs.fill = GridBagConstraints.HORIZONTAL;
		cibcs.gridx = 2;
		cibcs.gridy = 1;
		cibcs.gridwidth = 1;
		processPanel.add(this.checkinButton, cibcs);
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("cancel");
		GridBagConstraints cabcs = new GridBagConstraints();
		cabcs.fill = GridBagConstraints.HORIZONTAL;
		cabcs.gridx = 0;
		cabcs.gridy = 2;
		cabcs.gridwidth = 3;
		processPanel.add(cancelButton, cabcs);
		
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(userButtonPanel);
		buttonPanel.add(processPanel);
		
		this.getContentPane().add(infopanel, BorderLayout.NORTH);
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
	}
	
	
	public void EnableTicketButton(){
		/*
		 * Enable button generating a ticket
		 */
		 generateTicketButton.setEnabled(true);
	}
	
	public void DisableTicketButton(){
		/*
		 * Enable button generating a ticket
		 */
		generateTicketButton.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		/*
		 * Hanslö
		 */
		if(arg0.getActionCommand().equals("login")){
			handleLogin();
		} else if(arg0.getActionCommand().equals("apiinfo")){
			showApiInfoDialog();
		} else if(arg0.getActionCommand().equals("exit"))
			handleExit();
		else if(arg0.getActionCommand().equals("newUser"))
			newUserClicked();
		else if(arg0.getActionCommand().equals("changePayment"))
			changePaymentClicked();
		else if(arg0.getActionCommand().equals("cancel")){
			handleCancel();
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
	
	private void handleExit(){
		/*
		 * show exit dialog
		 */
		if(newregthread != null){
			// Shut down new registrant thread before exiting the program
			newregthread.interrupt();
		}
		if(JOptionPane.showConfirmDialog(null, "Close Program?") == JOptionPane.OK_OPTION)
			this.dispose();
	}
	
	private void handleLogin(){
		/*
		 * Perform LoginDialog,
		 * enable new user button and API info button if successfull, read
		 * registrant list for the given event ID and disable login button
		 */
		IndicoAPILoginDialog loginDialog = new IndicoAPILoginDialog(this);
		loginDialog.setVisible(true);
		if(loginDialog.isInfoSet()){
			this.indicoConnection.setApikey(loginDialog.getAPIkey());
			this.indicoConnection.setApisecret(loginDialog.getAPIsecret());
			this.indicoConnection.setServer(loginDialog.getServerAddress());
			this.indicoConnection.setEventID(loginDialog.getEventID());
			boolean success = false;
			try {
				this.registrants = this.indicoConnection.FetchRegistrantList();
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
				JOptionPane.showMessageDialog(this, 
						String.format("Found %d registrants for event %s", 
								this.registrants.getNumberOfRegistrants(), 
								this.indicoConnection.getEventID()));
				this.newUserButton.setEnabled(true);
				this.loginbutton.setEnabled(false);
				this.apiinfobutton.setEnabled(true);
				this.isLoggedIn = true;
			}
		}
	}
	
	private void showApiInfoDialog(){
		/*
		 * Show Dialog with API connection details
		 */
		IndicoAPIInfoDialog infoDialog = new IndicoAPIInfoDialog(this, 
				this.indicoConnection.getApikey(), 
				this.indicoConnection.getApisecret(),
				this.indicoConnection.getServer(),
				this.indicoConnection.getEventID());
		infoDialog.setVisible(true);
	}
	
	public void newUserClicked(){
		/*
		 * Handle click to newUserButton
		 */
		this.newUserButton.setEnabled(false);
		this.changePaymentButton.setEnabled(false);
		this.checkinButton.setEnabled(false);
		this.generateTicketButton.setEnabled(false);
		eticket = null;
		current = null;
		newregthread = new Thread(){
			@Override
			public void run(){
				eticket = barcodeHandler.handleBarcode();
				if(eticket != null)
					handleEticketParsed();
				else
					handleParserFailure();
			}
		};
		newregthread.start();
	}
	
	public void changePaymentClicked(){
		/*
		 * Registrant has paid at the conference desk:
		 * Enable buttons for checkin and ticket generation
		 * Put information to indico
		 */
		this.generateTicketButton.setEnabled(true);
		this.checkinButton.setEnabled(true);
		this.changePaymentButton.setEnabled(false);
		// TODO: put information to indico
	}
	
	public void printTicketClicked(){
		/*
		 * Handle click to printTicket button
		 */
	}
	
	public void checkinClicked(){
		/*
		 * Registrant checked in: 
		 */
	}
	
	public void handleEticketParsed(){
		/*
		 * Ticket was parsed
		 */
		if(registrants.isTicketValid(eticket)){
			// check ticket validity
			current = registrants.FindRegistrant(eticket);
			if(current != null){
				System.out.printf("Fetch full information for registrant %d\n", eticket.getRegistrantID());
				this.indicoConnection.FetchFullRegistrantInformation(current, eticket);
				JOptionPane.showMessageDialog(this, String.format("ETicket read successfully and valid: %s", current.getFullName()));				
				if(current.hasPaid()){
					// if the registrant has already payed, allow checkin
					// TODO: remove printout when debugging finished
					System.out.println("Registrant has already paid");
					this.generateTicketButton.setEnabled(true);
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
		finishBarcodeThread();
	}
	
	public void handleParserFailure(){
		/*
		 * Parsing was not successfull:
		 * Kill thread and show an error message
		 */
		JOptionPane.showMessageDialog(this, "Failed scanning barcode");
		finishBarcodeThread();
	}
	
	public void handleCancel(){
		/*
		 * User pressed cancel:
		 * 
		 * Finish registrant thread, deactivate user buttons, and remove link for current user
		 * 
		 */
		this.current = null;
		
		this.generateTicketButton.setEnabled(false);
		this.checkinButton.setEnabled(false);
		this.changePaymentButton.setEnabled(false);
		
		finishBarcodeThread();
	}

	public void finishBarcodeThread(){
		/*
		 * enable new user button
		 */
		if(isLoggedIn) this.newUserButton.setEnabled(true);
		if(newregthread != null){
			newregthread.interrupt();
			newregthread = null;
		}
	}
}
