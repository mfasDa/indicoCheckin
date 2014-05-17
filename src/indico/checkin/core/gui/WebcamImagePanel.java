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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.github.sarxos.webcam.Webcam;

/**
 * Panel for webcam image
 * Only displays the image which is forwarded by the webcam processor
 * 
 * @author Markus Fasel
 *
 */
public class WebcamImagePanel extends JPanel {

	/**
	 * @author Markus Fasel
	 */
	private static final long serialVersionUID = 1L;
	Webcam webcam;
	BufferedImage webcamImage;
	
	public WebcamImagePanel(){
		webcam = null;
		this.setPreferredSize(new Dimension(640,480));
	}
	
	public WebcamImagePanel(Webcam wc){
		webcam = wc;
		webcam.setViewSize(new Dimension(640,480));
		this.setPreferredSize(webcam.getViewSize());
	}
	public void SetWebcam(Webcam webcam){
		boolean wasWebcamOpen = false;
		webcam.setViewSize(new Dimension(640,480));
		if(this.webcam.isOpen()){
			this.webcam.close();
			wasWebcamOpen = true;
		}
		this.webcam = webcam;
		// Open new webcam if needed
		if(wasWebcamOpen) 
			this.webcam.open();

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
	
	public void openWebcam(){
		if(webcam != null)
			webcam.open();
	}
	
	public void closeWebcam(){
		if(webcam != null && webcam.isOpen())
			webcam.close();
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(webcamImage, 0, 0, null);
	}
}
