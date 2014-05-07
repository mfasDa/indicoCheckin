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

import indico.checkin.core.data.IndicoRegistrant;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.github.sarxos.webcam.Webcam;

/**
 * Class for user info panel in the main window. The panel contains two
 * parts:
 * - a webcam window where the current webcam picture is shown, so that the 
 *   user can put the ticket in a way that the barcode can be easily read in
 * - a table showing the registrant details. This table is updated when the
 *   a new registrant is processed.
 * 
 * Class dependent on additional libraries:
 *   webcam-capture (license attached to the package)
 *  
 * @author Markus Fasel
 */
public class InfoPanel extends JPanel implements ListSelectionListener{
	
	private static final long serialVersionUID = 1L;
	WebcamImagePanel webcampanel;
	JTable userdata;
	JFrame parentFrame;
	RegistrantInfoDisplayModel tablemodel;
	
	public InfoPanel(JFrame parentFrame){
		this.setLayout(new BorderLayout());
		this.parentFrame = parentFrame;

		webcampanel = new WebcamImagePanel(Webcam.getDefault());
		//webcampanel.setPreferredSize(new Dimension(600, 400));
		this.add(webcampanel, BorderLayout.WEST);
		
		tablemodel = new RegistrantInfoDisplayModel();
		userdata = new JTable(tablemodel);
		userdata.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		userdata.getColumnModel().getColumn(0).setPreferredWidth(175);
		userdata.getColumnModel().getColumn(1).setPreferredWidth(200);	
		userdata.setDefaultRenderer(Object.class, new RegistrantInfoRenderer());
		userdata.getSelectionModel().addListSelectionListener(this);
		this.add(userdata, BorderLayout.EAST);
	}
	
	public void openWebcam(){
		webcampanel.openWebcam();
	}
	
	public void closeWebcam(){
		webcampanel.closeWebcam();
	}
	
	public WebcamImagePanel getWebcamPanel(){
		return webcampanel;
	}
	
	public void setWebcam(int webcamID){
		List<Webcam> listofwebcams = Webcam.getWebcams();
		if(webcamID >= 0 && webcamID < listofwebcams.size())
		webcampanel.SetWebcam(listofwebcams.get(webcamID));
	}
	
	public JTable getUserData(){
		return userdata;
	}
	
	public void UpdateRegistrantData(IndicoRegistrant registrant){
		tablemodel.SetRegistrant(registrant);
	}

	public void UpdateRegistrantDisplay() {
		tablemodel.setInfoUpdated();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		/*
		 * Open info dialog
		 */
		switch(userdata.getSelectedRow()){
		case 7:
			DinnerInfoDialog dinnerdialog = new DinnerInfoDialog(parentFrame,tablemodel.getData());
			dinnerdialog.setVisible(true);
			break;
		case 8:
			SocialEventInfoDialog eventDialog = new SocialEventInfoDialog(parentFrame,tablemodel.getData().getFullInformation().getSocialEvents());
			eventDialog.setVisible(true);
			break;
		}
	}
	
}
