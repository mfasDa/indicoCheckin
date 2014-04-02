package indico.checkin.core.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import indico.checkin.core.api.IndicoAPIConnector;
import indico.checkin.core.data.IndicoEventRegistrantList;
import indico.checkin.core.data.IndicoRegistrant;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class IndicoCheckinAppMainGui extends JFrame implements ActionListener, WindowListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton newUserButton;
	private JButton changePaymentButton;
	private JButton generateTicketButton;
	private JButton checkinButton;
	private JButton cancelButton;
	
	// Data
	private IndicoEventRegistrantList registrants;
	private IndicoRegistrant current;
	
	// connection to indico server
	private IndicoAPIConnector indicoConnection;
	
	public IndicoCheckinAppMainGui(){
		// Default constructor
		this.getContentPane().setLayout(null);
		this.initWindow();
		
		registrants = null;
		current = null;
		indicoConnection = new IndicoAPIConnector();
	}

	public void newUserClicked(){
		/*
		 * Handle click to newUserButton
		 */
		
	}
	
	public void changePaymentClicked(){
		/*
		 * Handle click to changePayment button
		 */
	}
	
	public void printTicketClicked(){
		/*
		 * Handle click to printTicket button
		 */
	}
	
	public void checkinClicked(){
		/*
		 * Handle Click to checkin button
		 */
	}
	
	protected void initWindow(){
		/*
		 * Create main window
		 */
		this.setTitle("Indico checkin");
		this.setSize(600, 350);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
		this.addWindowListener(this);
		
		final int kButtonXSize = 130;
		final int kButtonYRow = 220;
		final int kButtonYSize = 40;
		final int kMarginY = 15;
		
		// Define button for new user
		this.newUserButton = new JButton("New registrant");
		this.newUserButton.setBounds(kMarginY, kButtonYRow, kButtonXSize, kButtonYSize);
		this.newUserButton.setActionCommand("newUser");

		this.changePaymentButton = new JButton("Set payed");
		this.changePaymentButton.setBounds(2*kMarginY + kButtonXSize, kButtonYRow, kButtonXSize, kButtonYSize);
		this.changePaymentButton.setActionCommand("changePayment");
		this.changePaymentButton.setEnabled(false);
		
		this.generateTicketButton = new JButton("Generate Ticket");
		this.generateTicketButton.setBounds(3*kMarginY + 2*kButtonXSize, kButtonYRow, kButtonXSize, kButtonYSize);
		this.generateTicketButton.setActionCommand("generateTicket");
		this.generateTicketButton.setEnabled(false);

		this.checkinButton = new JButton("Checkin");
		this.checkinButton.setBounds(4*kMarginY + 3*kButtonXSize, kButtonYRow, kButtonXSize, kButtonYSize);
		this.checkinButton.setActionCommand("checkin");
		this.checkinButton.setEnabled(false);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(15, 270, 280, 40);
		cancelButton.setActionCommand("cancel");
		
		// Add Buttons to pane
		this.getContentPane().add(this.newUserButton);
		this.getContentPane().add(this.changePaymentButton);
		this.getContentPane().add(this.generateTicketButton);
		this.getContentPane().add(this.checkinButton);
		this.getContentPane().add(this.cancelButton);
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
		// TODO Auto-generated method stub
		
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
		if(JOptionPane.showConfirmDialog(null, "Close Program?") == JOptionPane.OK_OPTION)
			e.getWindow().dispose();
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
}
