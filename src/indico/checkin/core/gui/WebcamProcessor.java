package indico.checkin.core.gui;

import indico.checkin.core.api.ETicketDecodingException;
import indico.checkin.core.api.IndicoJSONBarcodeParser;
import indico.checkin.core.data.IndicoParsedETicket;

import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class WebcamProcessor extends TimerTask {
	private Webcamcatcher catcher;
	private WebcamImagePanel webcampanel;
	private long starttime;
	private long limit;
	private Timer parent;
	private IndicoParsedETicket ticket;
	
	public WebcamProcessor(Webcamcatcher catcher, WebcamImagePanel webcampanel, long starttime, long limit, Timer parent){
		this.catcher = catcher;
		this.webcampanel = webcampanel;
		this.starttime = starttime;
		this.parent = parent;
		this.ticket = null;
		this.limit = limit;
	}

	@Override
	public void run() {
		/*
		 * run method called in the timer:
		 * display image in the webcam panel
		 * try to read the barcode
		 * return to exit function after max time, or success of the 
		 * barcode scan
		 */
		System.out.println("Timer Task run called");
		webcampanel.Update();
		// if the ticket has not yet read in successfully, try to get it
		if(ticket == null){
			String ticketString = ProcessImage(webcampanel.getImage());
			if(ticketString.length() > 0){
				ticket = decodeTicket(ticketString);
			}
		} else {
			// Ticket was already read in successfully, terminate timer
			webcampanel.getWebcam().close();
			parent.cancel();
			catcher.timerReturn(ticket);
		}
		// in any case, termiate timer if after limit is reached
		if(System.currentTimeMillis() - starttime > limit){
			webcampanel.getWebcam().close();
			parent.cancel();
			catcher.timerReturn(ticket);
		}
	}
	
	public IndicoParsedETicket getTicket(){
		return ticket;
	}
		
	private String ProcessImage(BufferedImage barcodeImage){
		/*
		 * Try reading image from webcam
		 * if the image was read out successfull, store result and return true
		 */
		String barcode = "";
		LuminanceSource source = new BufferedImageLuminanceSource(barcodeImage);
		BinaryBitmap bmp = new BinaryBitmap(new HybridBinarizer(source));
		try{
			MultiFormatReader reader = new MultiFormatReader();
			Result res = reader.decode(bmp);
			barcode = res.getText(); 
		} catch (NotFoundException e){
			barcode = "";
		}
		return barcode;
	}
	
	private IndicoParsedETicket decodeTicket(String ticketString){
		/*
		 * Convert the ticket string to a parsed eticket
		 */
		IndicoParsedETicket eticket = null;
		IndicoJSONBarcodeParser parser = new IndicoJSONBarcodeParser();
		try{
			eticket = parser.parse(ticketString);
		} catch (ETicketDecodingException e){
			e.printStackTrace();
		}
		return eticket;
	}

}