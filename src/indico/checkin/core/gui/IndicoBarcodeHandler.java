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

/**
 * Class handling reading of the barcode. The class tries to catch a webcam
 * picture. Class is deprecated. Barcode handling replaced by a webcam watching thread,
 * which is much more performant
 * 
 * Class dependent on additional libraries:
 *   webcam-capture (license attached to the package)
 *   zxing published under Apache License v2 (attached to the package)
 * 
 * @author: Markus Fasel
 * @deprecated
 */
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
