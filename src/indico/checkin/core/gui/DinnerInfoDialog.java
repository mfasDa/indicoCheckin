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

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import indico.checkin.core.data.IndicoRegistrant;
import indico.checkin.core.data.IndicoRegistrantInfoField;
import indico.checkin.core.data.IndicoRegistrantInfoGroup;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Info dialog for conference dinner options
 * 
 * @author: Markus Fasel
 */
public class DinnerInfoDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	public DinnerInfoDialog(Frame parent, IndicoRegistrant reg){
		super(parent, true);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
	
		// Retrieve data (adapted to the settings of the Quark Matter 2014 conference for the moment)
		IndicoRegistrantInfoGroup dinnergroup = reg.getFullInformation().findGroupByTitle("Lunch and conference dinner options");
		IndicoRegistrantInfoField selffield = dinnergroup.findFieldByCaption("Conference Dinner (included in the conference fee)");
		IndicoRegistrantInfoField accompanyField = dinnergroup.findFieldByCaption("Accompanying person conference dinner");

		// Create output labels
		JLabel captionPart = new JLabel("Participation in conference dinner");
		captionPart.setHorizontalAlignment(SwingConstants.LEFT);
		JLabel labPart = new JLabel(Boolean.parseBoolean(selffield.getValue()) ? "yes" : "no");
		labPart.setHorizontalAlignment(SwingConstants.LEFT);
		JLabel captionAccomp = new JLabel("Number of accompanying persons:");
		captionAccomp.setHorizontalAlignment(SwingConstants.LEFT);
		JLabel labAccomp = new JLabel(String.format("%d", accompanyField.getValue().length() > 0 ? Integer.parseInt(accompanyField.getValue()) : 0));
		labAccomp.setHorizontalAlignment(SwingConstants.LEFT);
		
		// Add close button
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints cs = new GridBagConstraints();
		cs.fill = GridBagConstraints.HORIZONTAL;
		
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		cs.insets = new Insets(10, 10, 3, 3);
		getContentPane().add(captionPart,cs);
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 1;
		cs.insets = new Insets(10, 3, 3, 10);
		getContentPane().add(labPart,cs);
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		cs.insets = new Insets(3, 10, 10, 3);
		getContentPane().add(captionAccomp,cs);
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 1;
		cs.insets = new Insets(3, 3, 10, 10);
		getContentPane().add(labAccomp,cs);
		cs.gridx = 0;
		cs.gridy = 2;
		cs.gridwidth = 2;
		cs.insets = new Insets(10, 10, 10, 10);
		getContentPane().add(closeButton,cs);

		pack();
	}

}
