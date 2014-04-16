package indico.checkin.core.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.github.sarxos.webcam.Webcam;

public class WebcamImagePanel extends JPanel {

	/**
	 * @author Markus Fasel
	 */
	private static final long serialVersionUID = 1L;
	Webcam webcam;
	BufferedImage webcamImage;
	
	public WebcamImagePanel(){
		webcam = null;
	}
	
	public WebcamImagePanel(Webcam wc){
		webcam = wc;
		this.setPreferredSize(webcam.getViewSize());
	}
	
	public void SetWebcam(Webcam webcam){
		this.webcam = webcam;
	}
	
	public Webcam getWebcam(){
		return webcam;
	}

	public BufferedImage getImage() {
		return webcamImage;
	}
	public void setImage(BufferedImage image) {
		this.webcamImage = image;
	}
	
	public void Update(){
		if(webcam.isOpen()){
			setImage(webcam.getImage());
			repaint();
		}	
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(webcamImage, 0, 0, null);
	}
}
