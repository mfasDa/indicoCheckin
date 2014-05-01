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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Dialog for the selection of a given webcam
 * 
 * @author: Markus Fasel
 */
public class WebcamSelectionDialog extends JDialog implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;
	
	private JTable infoTable;
	private WebcamSelectionModel infoModel;
	private JButton selectButton;
	private JButton cancelButton;
	private int webcamSelected;
	
	public WebcamSelectionDialog(JFrame parent){
		super(parent, true);
		webcamSelected = -1;
		
		infoTable = new JTable();
		infoModel = new WebcamSelectionModel();
		infoTable.setModel(infoModel);
		infoTable.getSelectionModel().addListSelectionListener(this);
		infoTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		infoTable.getColumnModel().getColumn(1).setPreferredWidth(400);		

		
		selectButton = new JButton("Select");
		selectButton.setEnabled(false);
		selectButton.addActionListener(this);
		selectButton.setActionCommand("select");
		
		cancelButton = new JButton("Cancel");
		cancelButton.setEnabled(true);
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("cancel");
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(selectButton);
		buttonPanel.add(cancelButton);
		
		getContentPane().add(infoTable, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("select")){
			// get the id
			webcamSelected = infoTable.getSelectedRow();
			dispose();
		} else if(e.getActionCommand().equals("cancel")){
			dispose();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		selectButton.setEnabled(true);
	}
	
	/**
	 * Get ID of the selected webcam
	 * @return the ID
	 */
	public int getSelectedWebcamID(){
		return webcamSelected;
	}

}
