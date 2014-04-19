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

import indico.checkin.core.data.IndicoRegistrantSocialEvent;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * Dialog showing the events booked by the registrant
 * 
 * @author: Markus Fasel
 */
public class SocialEventInfoDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	public SocialEventInfoDialog(JFrame parent, List<IndicoRegistrantSocialEvent> events){
		super(parent, true);
		
		JTable infotable = new JTable(new SocialEventInfoModel(events));
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		getContentPane().add(new JScrollPane(infotable), BorderLayout.CENTER);
		getContentPane().add(closeButton, BorderLayout.SOUTH);
		pack();
	}
}
