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
		System.out.println("Called");
		Webcam webcam = Webcam.getDefault();
		boolean hasRead = false;
		webcam.open();
		while(!hasRead){
			BufferedImage img = webcam.getImage();
			if(ProcessImage(img)){
				hasRead = true;
			}
		}
		
		IndicoJSONBarcodeParser parser = new IndicoJSONBarcodeParser();
		IndicoParsedETicket eticket = null;
		try{
			eticket = parser.parse(barcode);
		} catch (ETicketDecodingException e){
			e.printStackTrace();
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
