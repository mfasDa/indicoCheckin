package indico.checkin.core.gui;

import indico.checkin.core.data.IndicoRegistrant;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

public class InfoPanel extends JPanel implements ListSelectionListener{
	
	/**
	 * Class for user info panel in the main window. The panel contains two
	 * parts:
	 * - a webcam window where the current webcam picture is shown, so that the 
	 *   user can put the ticket in a way that the barcode can be easily read in
	 * - a table showing the registrant details. This table is updated when the
	 *   a new registrant is processed.
	 * License: GPLv3 (a copy of the license is provided with the package)
	 * 
	 * Class dependent on additional libraries:
	 *   webcam-capture (license attached to the package)
	 *  
	 * @author Markus Fasel
	 */
	private static final long serialVersionUID = 1L;
	WebcamPanel webcampanel;
	JTable userdata;
	JFrame parentFrame;
	RegistrantInfoDisplayModel tablemodel;
	
	public InfoPanel(JFrame parentFrame){
		this.setLayout(new BorderLayout());
		this.parentFrame = parentFrame;
		webcampanel = new WebcamPanel(Webcam.getDefault());
		webcampanel.setSize(600, 400);
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
	
	public JPanel getWebcamPanel(){
		return webcampanel;
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
