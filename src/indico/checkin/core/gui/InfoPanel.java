package indico.checkin.core.gui;

import indico.checkin.core.data.IndicoRegistrant;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTable;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

public class InfoPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	WebcamPanel webcampanel;
	JTable userdata;
	RegistrantInfoDisplayModel tablemodel;
	
	public InfoPanel(){
		this.setLayout(new BorderLayout());
		webcampanel = new WebcamPanel(Webcam.getDefault());
		webcampanel.setSize(600, 400);
		this.add(webcampanel, BorderLayout.WEST);
		
		tablemodel = new RegistrantInfoDisplayModel();
		userdata = new JTable(tablemodel);
		userdata.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		userdata.getColumnModel().getColumn(0).setPreferredWidth(125);
		userdata.getColumnModel().getColumn(1).setPreferredWidth(200);		
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
}
