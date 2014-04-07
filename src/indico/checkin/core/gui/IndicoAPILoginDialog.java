package indico.checkin.core.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class IndicoAPILoginDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextField tfserver;
	private JTextField tfevent;
	private JTextField tfapikey;
	private JTextField tfapisecret;
	private JButton btnconfirm;
	private JButton btncancel;
	private boolean infoset;
	
	public IndicoAPILoginDialog(Frame owner) {
		super(owner, "Indico connection login", true);
		
		this.infoset = false;
		
		JPanel pnlInfo = new JPanel(new GridBagLayout());
		GridBagConstraints cs = new GridBagConstraints();
		cs.fill = GridBagConstraints.HORIZONTAL;
		
		// Put in server
		cs.gridx = 0;		// top
		cs.gridy = 0;		// left
		cs.gridwidth = 1;	
		cs.insets = new Insets(10,10,3,3);
		pnlInfo.add(new JLabel("Server:"), cs);
		this.tfserver = new JTextField(20);
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 2;	// make bag broader
		cs.insets = new Insets(10,3,3,10);
		pnlInfo.add(this.tfserver, cs);

		// Put in event
		cs.gridx = 0;		// top
		cs.gridy = 1;		// left
		cs.gridwidth = 1;	
		cs.insets = new Insets(3,10,3,3);
		pnlInfo.add(new JLabel("Event ID:"), cs);
		this.tfevent = new JTextField(20);
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 2;	// make bag broader
		cs.insets = new Insets(3,3,3,10);
		pnlInfo.add(this.tfevent, cs);

		// Put in API key
		cs.gridx = 0;		// top
		cs.gridy = 2;		// left
		cs.gridwidth = 1;	
		cs.insets = new Insets(3,10,3,3);
		pnlInfo.add(new JLabel("API key:"), cs);
		this.tfapikey = new JTextField(20);
		cs.gridx = 1;
		cs.gridy = 2;
		cs.gridwidth = 2;	// make bag broader
		cs.insets = new Insets(3,3,3,10);
		pnlInfo.add(this.tfapikey, cs);
		
		// Put in API secret
		cs.gridx = 0;	// start second row
		cs.gridy = 3;
		cs.gridwidth = 1;
		cs.insets = new Insets(3,10,10,3);
		pnlInfo.add(new JLabel("API secret:"), cs);
		this.tfapisecret = new JTextField(20);
		cs.gridx = 1;
		cs.gridy = 3;
		cs.gridwidth = 2;
		cs.insets = new Insets(3,3,10,10);
		pnlInfo.add(this.tfapisecret, cs);
		
		this.btnconfirm = new JButton("Confirm");
		this.btnconfirm.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!( tfapikey.getText().isEmpty() || tfapisecret.getText().isEmpty() 
						|| tfserver.getText().isEmpty() || tfevent.getText().isEmpty() )){
					// check if the event ID is an integer
					try{
						Integer.parseInt(tfevent.getText());
						infoset = true;
					} catch(NumberFormatException o){
						JOptionPane.showMessageDialog(IndicoAPILoginDialog.this, "Invalid information: Event ID has to be a number");
					}
				} else {
					// Not all information set
					// Inform the user about failures
					JOptionPane.showMessageDialog(IndicoAPILoginDialog.this, "Invalid information");
					if(!tfserver.getText().isEmpty())
						tfserver.setText("");
					if(!tfevent.getText().isEmpty())
						tfevent.setText("");
					if(!tfapikey.getText().isEmpty())
						tfapikey.setText("");
					if(!tfapisecret.getText().isEmpty()){
						tfapisecret.setText("");
					}
					infoset = false;
				}
				dispose();
			}
			
		});
		
		this.btncancel = new JButton("Cancel");
		this.btncancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * For cancel button remove all texts
				 */
				if(!tfserver.getText().isEmpty())
					tfserver.setText("");
				if(!tfevent.getText().isEmpty())
					tfevent.setText("");
				if(!tfapikey.getText().isEmpty())
					tfapikey.setText("");
				if(!tfapisecret.getText().isEmpty()){
					tfapisecret.setText("");
				}
				dispose();
			}
			
		});
		
		// Add buttons to new panel group
		JPanel pnlBtns = new JPanel();
		pnlBtns.add(this.btnconfirm);
		pnlBtns.add(this.btncancel);
		
		// Finish Window
		this.getContentPane().add(pnlInfo, BorderLayout.CENTER);
		this.getContentPane().add(pnlBtns, BorderLayout.PAGE_END);
		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(owner);
	}
	
	public String getServerAddress(){
		/*
		 * Return http(s) address of the server
		 */
		return tfserver.getText().replace(" ", "");
	}
	
	public int getEventID(){
		/*
		 * Return event ID
		 */
		if(infoset){
			return Integer.parseInt(tfevent.getText());
		} else {
			return -1;
		}
	}

	public String getAPIkey(){
		/*
		 * Return API key
		 * remove whitespaces
		 */
		return this.tfapikey.getText().replace(" ", "");
	}
	
	public String getAPIsecret(){
		/*
		 * Return API secret
		 * remove whitespaces
		 */
		return this.tfapisecret.getText().replace(" ", "");
	}
	
	public boolean isInfoSet(){
		return this.infoset;
	}
}
