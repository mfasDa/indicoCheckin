package indico.checkin.core.gui;

import indico.checkin.core.api.ETicketDecodingException;
import indico.checkin.core.api.IndicoJSONBarcodeParser;
import indico.checkin.core.data.IndicoParsedETicket;

import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class IndicoBarcodeHandler {
	
	String barcode;
	
	public IndicoBarcodeHandler(){
		barcode = "";
	}
	
	public IndicoParsedETicket handleBarcode(){
		/*
		 * Read out images from webcam until barcode is successfully handled
		 */
		Webcam webcam = Webcam.getDefault();
		boolean hasRead = false;
		webcam.open();
		long timestart = System.currentTimeMillis();
		while(!hasRead){
			BufferedImage img = webcam.getImage();
			if(ProcessImage(img)){
				hasRead = true;
			} else{
				// stop test after 30 seconds
				long timenow = System.currentTimeMillis();
				if(timenow - timestart > 5000){
					hasRead = false;
					break;
				}
			}
		}
		
		IndicoParsedETicket eticket = null;
		if(hasRead){
			// Barcode parsed successfully
			IndicoJSONBarcodeParser parser = new IndicoJSONBarcodeParser();
			// System.out.printf("Ticket: %s\n", barcode);
			try{
				eticket = parser.parse(barcode);
			} catch (ETicketDecodingException e){
				e.printStackTrace();
			}
		}
		return eticket;
	}
	
	private boolean ProcessImage(BufferedImage barcodeImage){
		/*
		 * Try reading image from webcam
		 * if the image was read out successfull, store result and return true
		 */
		LuminanceSource source = new BufferedImageLuminanceSource(barcodeImage);
		BinaryBitmap bmp = new BinaryBitmap(new HybridBinarizer(source));
		boolean success = true;
		try{
			MultiFormatReader reader = new MultiFormatReader();
			Result res = reader.decode(bmp);
			barcode = res.getText(); 
		} catch (NotFoundException e){
			success = false;
		}
		return success;
	}
}
