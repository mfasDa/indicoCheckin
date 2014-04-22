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
package indico.checkin.core.gui;

import indico.checkin.core.data.IndicoLoginData;
import indico.checkin.core.api.IndicoAuthException;
import indico.checkin.core.api.IndicoAuthentificationLayer;
import indico.checkin.core.api.IndicoKeyAuthLayer;
import indico.checkin.core.api.IndicoOAuthLayer;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

/**
 * GUI of the login dialog. Registrants enter the server url,
 * the conference ID and the api key and secret. The information
 * is passed to the mother window which establishes a connection
 * and fetches the full dataset.
 * 
 * @author Markus Fasel
 *
 */
public class IndicoAPILoginDialog extends JDialog{
	private static final long serialVersionUID = 1L;
		
	private JTextField tfserver;
	private JTextField tfevent;
	private JTextField tfapikey;
	private JTextField tfapisecret;
	private IndicoCheckinAppMainGui parentFrame;
	private boolean isKeyAuth;
	
	public IndicoAPILoginDialog(IndicoCheckinAppMainGui owner){
		super(owner, "Indico connection login", false);
		owner.setEnabled(false);
		parentFrame = owner;
		isKeyAuth = false;
		
		setAutoRequestFocus(true);
		
		JPanel pnlInfo = new JPanel(new GridBagLayout());
		GridBagConstraints cs = new GridBagConstraints();
		cs.fill = GridBagConstraints.HORIZONTAL;
		
		// Put in server
		cs.gridx = 0;		// top
		cs.gridy = 0;		// left
		cs.gridwidth = 1;	
		cs.insets = new Insets(10,10,3,3);
		pnlInfo.add(new JLabel("Server:"), cs);
		tfserver = new JTextField(20);
		tfserver.setEditable(true);
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 2;	// make bag broader
		cs.insets = new Insets(10,3,3,10);
		pnlInfo.add(tfserver, cs);

		// Put in event
		cs.gridx = 0;		// top
		cs.gridy = 1;		// left
		cs.gridwidth = 1;	
		cs.insets = new Insets(3,10,3,3);
		pnlInfo.add(new JLabel("Event ID:"), cs);
		tfevent = new JTextField(20);
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 2;	// make bag broader
		cs.insets = new Insets(3,3,3,10);
		pnlInfo.add(tfevent, cs);

		JPanel authPanel = new JPanel();
		authPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		authPanel.setLayout(new GridBagLayout());
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 3;
		authPanel.add(new JLabel("Authentification to indico:"), cs);
		ButtonGroup selectAuthentification = new ButtonGroup();
		JRadioButton oauthAuthentification = new JRadioButton("Use your indico account");
		oauthAuthentification.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				isKeyAuth = false;
				tfapikey.setEditable(false);
				tfapisecret.setEditable(false);
			}
		});
		oauthAuthentification.setSelected(true);
		selectAuthentification.add(oauthAuthentification);
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 3;
		authPanel.add(oauthAuthentification,cs);
		JRadioButton keyAuthentification = new JRadioButton("Use API key and secret");
		keyAuthentification.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				isKeyAuth = true;
				// Set fields for key and secret to editable 
				tfapikey.setEditable(true);
				tfapisecret.setEditable(true);
			}
		});
		selectAuthentification.add(keyAuthentification);
		cs.gridx = 0;
		cs.gridy = 2;
		cs.gridwidth = 3;
		authPanel.add(keyAuthentification,cs);
				
		// Put in API key
		cs.gridx = 0;		// top
		cs.gridy = 3;		// left
		cs.gridwidth = 1;	
		cs.insets = new Insets(3,10,3,3);
		authPanel.add(new JLabel("API key:"), cs);
		tfapikey = new JTextField(20);
		tfapikey.setEditable(false);
		cs.gridx = 1;
		cs.gridy = 3;
		cs.gridwidth = 2;	// make bag broader
		cs.insets = new Insets(3,3,3,10);
		authPanel.add(tfapikey, cs);
		
		// Put in API secret
		cs.gridx = 0;	// start second row
		cs.gridy = 4;
		cs.gridwidth = 1;
		cs.insets = new Insets(3,10,10,3);
		authPanel.add(new JLabel("API secret:"), cs);
		tfapisecret = new JTextField(20);
		tfapisecret.setEditable(false);
		cs.gridx = 1;
		cs.gridy = 4;
		cs.gridwidth = 2;
		cs.insets = new Insets(3,3,10,10);
		authPanel.add(tfapisecret, cs);
		
		JButton btnconfirm = new JButton("Confirm");
		btnconfirm.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				IndicoAuthentificationLayer authLayer = null;
				boolean isDispose = false;
				if(!(tfserver.getText().isEmpty() || tfevent.getText().isEmpty())){
					try{
						// check if the event ID is an integer
						Integer.parseInt(tfevent.getText());
						if(isKeyAuth){
							// Use Key authentification
							if(!(tfapikey.getText().isEmpty() || tfapisecret.getText().isEmpty())){
								IndicoKeyAuthLayer myAuthLayer = new IndicoKeyAuthLayer();
								myAuthLayer.setApikey(tfapikey.getText().replace(" ", ""));
								myAuthLayer.setApisecret(tfapisecret.getText().replace(" ", ""));
								authLayer = myAuthLayer;
								isDispose = true;
							} else {
								
								isDispose = false;
							}
						} else {
							// Use OAuth Authentification
							IndicoOAuthLayer myAuthLayer = new IndicoOAuthLayer();
							myAuthLayer.login(tfserver.getText().replace(" ", ""));
							authLayer = myAuthLayer;
							isDispose = true;
						}
						
					} catch(NumberFormatException o){
						isDispose = false;
						JOptionPane.showMessageDialog(IndicoAPILoginDialog.this, "Invalid information: Event ID has to be a number");
					} catch(IndicoAuthException o){
						isDispose = false;
						JOptionPane.showMessageDialog(IndicoAPILoginDialog.this, "Failed login to indico: " + o.getMessage());
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
					isDispose = false;
				}
				if(isDispose){
					dispose();
					// Return to parent
					parentFrame.setEnabled(true);
					parentFrame.processLoginReturn(new IndicoLoginData(tfserver.getText(), Integer.parseInt(tfevent.getText()), authLayer));
				}
			}
		});
		
		JButton btncancel = new JButton("Cancel");
		btncancel.addActionListener(new ActionListener(){
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
				parentFrame.setEnabled(true);
			}
		});
		
		// Add buttons to new panel group
		JPanel pnlBtns = new JPanel();
		pnlBtns.add(btnconfirm);
		pnlBtns.add(btncancel);
		
		// Finish Window
		this.getContentPane().add(pnlInfo, BorderLayout.NORTH);
		this.getContentPane().add(authPanel, BorderLayout.CENTER);
		this.getContentPane().add(pnlBtns, BorderLayout.PAGE_END);
		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(owner);
	}
	
}
