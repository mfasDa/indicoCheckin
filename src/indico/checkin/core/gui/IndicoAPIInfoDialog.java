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

import indico.checkin.core.api.IndicoAuthentificationLayer;
import indico.checkin.core.api.IndicoKeyAuthLayer;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class IndicoAPIInfoDialog extends JDialog{

	/**
	 * Shows a dialog with several user information (server url, event ID, api key
	 * and secret)
	 * License: GPLv3 (a copy of the license is provided with the package)
	 * 
	 * @author: Markus Fasel
	 */
	private static final long serialVersionUID = 1L;
	
	public IndicoAPIInfoDialog(Frame owner, String server, int event, IndicoAuthentificationLayer authentifier){
		super(owner);
				
		JPanel pnlinfo = new JPanel(new GridBagLayout());
		GridBagConstraints cs = new GridBagConstraints();
		cs.fill = GridBagConstraints.HORIZONTAL;
		
		String apikey = "";
		String apisecret = "";
		if(authentifier instanceof IndicoKeyAuthLayer){
			IndicoKeyAuthLayer layer = (IndicoKeyAuthLayer) authentifier;
			apikey = layer.getApikey();
			apisecret = layer.getApisecret();
		}
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		cs.insets = new Insets(10,10,3,3);
		pnlinfo.add(getAdjustedLabel("URL:"), cs);
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 2;
		cs.insets = new Insets(10,3,3,10);
		pnlinfo.add(getAdjustedLabel(server), cs);
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		cs.insets = new Insets(3,10,3,3);
		pnlinfo.add(getAdjustedLabel("Event:"), cs);
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 2;
		cs.insets = new Insets(3,3,3,10);
		pnlinfo.add(getAdjustedLabel(String.format("%d", event)), cs);
		cs.gridx = 0;
		cs.gridy = 2;
		cs.gridwidth = 1;
		cs.insets = new Insets(3,10,3,3);
		pnlinfo.add(getAdjustedLabel("API key:"), cs);
		cs.gridx = 1;
		cs.gridy = 2;
		cs.gridwidth = 2;
		cs.insets = new Insets(3,3,3,10);
		pnlinfo.add(getAdjustedLabel(apikey), cs);
		cs.gridx = 0;
		cs.gridy = 3;
		cs.gridwidth = 1;
		cs.insets = new Insets(3,10,3,3);
		pnlinfo.add(getAdjustedLabel("API secret:"), cs);
		cs.gridx = 1;
		cs.gridy = 3;
		cs.gridwidth = 2;
		cs.insets = new Insets(3,3,3,10);
		pnlinfo.add(getAdjustedLabel(apisecret), cs);

		
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
			
		});
		
		JPanel pnlbtn = new JPanel();
		pnlbtn.add(btnOK);
		
		this.getContentPane().add(pnlinfo, BorderLayout.CENTER);
		this.getContentPane().add(pnlbtn, BorderLayout.PAGE_END);
		this.pack();
		this.setLocationRelativeTo(owner);
	}

	private JLabel getAdjustedLabel(String message){
		JLabel lbl = new JLabel(message);
		lbl.setHorizontalAlignment(SwingConstants.LEFT);
		return lbl;
	}
}
