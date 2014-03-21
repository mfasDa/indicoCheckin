package idico.checkin.core.gui;

import javax.swing.JFrame;
import javax.swing.JButton;

public class IndicoCheckinAppMainGui extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton newUserButton;
	private JButton changePaymentButton;
	private JButton generateTicketButton;
	private JButton checkinButton;
	private JButton cancelButton;
	
	public IndicoCheckinAppMainGui(){
		// Default constructor
		this.getContentPane().setLayout(null);
		this.initWindow();
	}

	public void newUserClicked(){
		/*
		 * Listener to the new user
		 */
		
	}
	
	protected void initWindow(){
		/*
		 * Create main window
		 */
		this.setTitle("Indico checkin");
		this.setSize(600, 350);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		
		final int kButtonXSize = 130;
		final int kButtonYRow = 220;
		final int kButtonYSize = 40;
		final int kMarginY = 15;
		
		// Define button for new user
		newUserButton = new JButton("New registrant");
		newUserButton.setBounds(kMarginY, kButtonYRow, kButtonXSize, kButtonYSize);

		changePaymentButton = new JButton("Set payed");
		changePaymentButton.setBounds(2*kMarginY + kButtonXSize, kButtonYRow, kButtonXSize, kButtonYSize);
		changePaymentButton.setEnabled(false);
		
		generateTicketButton = new JButton("Generate Ticket");
		generateTicketButton.setBounds(3*kMarginY + 2*kButtonXSize, kButtonYRow, kButtonXSize, kButtonYSize);
		generateTicketButton.setEnabled(false);

		checkinButton = new JButton("Checkin");
		checkinButton.setBounds(4*kMarginY + 3*kButtonXSize, kButtonYRow, kButtonXSize, kButtonYSize);
		checkinButton.setEnabled(false);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(15, 270, 280, 40);
		
		// Add Buttons to pane
		this.getContentPane().add(newUserButton);
		this.getContentPane().add(changePaymentButton);
		this.getContentPane().add(generateTicketButton);
		this.getContentPane().add(checkinButton);
		this.getContentPane().add(cancelButton);
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
}
