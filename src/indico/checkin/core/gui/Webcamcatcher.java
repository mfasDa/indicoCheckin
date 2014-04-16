package indico.checkin.core.gui;

import indico.checkin.core.data.IndicoParsedETicket;

import java.util.Timer;

public class Webcamcatcher implements Runnable {
	
	/**
	 * @author: Markus Fasel
	 */
	
	private IndicoCheckinAppMainGui parent;
	
	public Webcamcatcher(IndicoCheckinAppMainGui program){
		parent = program;
	}
	
	@Override
	public void run() {
		// run scheduled task to capture a webcam image and to read the barcode
		System.out.println("Thread run called");
		long timeMax = 10000;
		Timer timer = new Timer();
		parent.getInfoPanel().getWebcamPanel().getWebcam().open();
		WebcamProcessor processor = new WebcamProcessor(this, parent.getInfoPanel().getWebcamPanel(), System.currentTimeMillis(), timeMax, timer);
		timer.schedule(processor,10,100);
		System.out.println("end run thread");
	}
	
	public void timerReturn(IndicoParsedETicket ticket){
		parent.setTicket(ticket);
	}
}
