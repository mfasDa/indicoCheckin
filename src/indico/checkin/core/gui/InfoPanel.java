package indico.checkin.core.gui;

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
	
	public InfoPanel(){
		this.setLayout(new BorderLayout());
		webcampanel = new WebcamPanel(Webcam.getDefault());
		webcampanel.setSize(600, 400);
		this.add(webcampanel, BorderLayout.WEST);
		
		userdata = new JTable();
		this.add(userdata, BorderLayout.EAST);
	}
	
	public JPanel getWebcamPanel(){
		return webcampanel;
	}
	
	public JTable getUserData(){
		return userdata;
	}
}
