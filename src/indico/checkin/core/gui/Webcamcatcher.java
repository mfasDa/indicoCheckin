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

import indico.checkin.core.data.IndicoParsedETicket;

import java.util.Timer;

/**
 * Thread catching the webcam and processing the information:
 * - Show image in a webcam panel
 * - Put image to the barcode reader
 * After finishing forward the parsed ticket to the main app
 * 
 * @author: Markus Fasel
 */
public class Webcamcatcher implements Runnable {
	
	private IndicoCheckinAppMainGui parent;
	
	public Webcamcatcher(IndicoCheckinAppMainGui program){
		parent = program;
	}
	
	@Override
	public void run() {
		// run scheduled task to capture a webcam image and to read the barcode
		System.out.println("Thread run called");
		long timeMax = 20000;
		Timer timer = new Timer();
		//parent.getInfoPanel().getWebcamPanel().getWebcam().open();
		WebcamProcessor processor = new WebcamProcessor(this, parent.getInfoPanel().getWebcamPanel(), System.currentTimeMillis(), timeMax, timer);
		timer.schedule(processor,10,100);
		System.out.println("end run thread");
	}
	
	public void timerReturn(IndicoParsedETicket ticket){
		parent.setTicket(ticket);
	}
}
