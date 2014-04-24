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


package indico.checkin.core.export;

//import java.io.FileOutputStream;
//import java.util.Date;

import indico.checkin.core.data.IndicoRegistrant;

import java.awt.Desktop;
import java.io.File;
//import java.awt.print.PrinterJob;
import java.io.FileOutputStream;
import java.io.IOException;
//import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;



public class PdfExporter {
/**
 * Exporter class 
 * loads template and fills it with registrant information
 * 
 * @author: Steffen Weber
 */
	 private static String exportPath = System.getProperty("java.io.tmpdir");
	 private String exportFileName;
	 private static String templatePath = "templates";
	 private String templateFileName = "template-test.pdf";

	 private IndicoRegistrant registrant;
	 

	 public void setExportPath(String path){
		 PdfExporter.exportPath = path;
	 }
	 public void setExportFileName(String fileName){
		 this.exportFileName = fileName;
	 }
	 
	 public void setTemplatePath(String path){
		 PdfExporter.templatePath = path;
	 }
	 public void setTemplateFileName(String fileName){
		 this.templateFileName = fileName;
	 }
	 public void setFileNameFromRegistrantInforamtion(){
		 String fileName = registrant.getID() + "-" + registrant.getSurname() + "_" + registrant.getFirstName() + ".pdf";
		 setExportFileName(fileName);
	 }
	 public void setRegistrant(IndicoRegistrant registrant){
		 this.registrant = registrant;
		 setFileNameFromRegistrantInforamtion();
	 }
	 public String getFullExportFileName(){
		 return exportPath + "/" + exportFileName;
	 }
	 public String getFullTemplateFileName(){
		 return templatePath + "/" + templateFileName;
	 }
	 
	 

	 public void exportPdf(){
		 try {			 
			 PdfReader reader = new PdfReader( getFullTemplateFileName() );
			 PdfStamper stamper = new PdfStamper(reader, new FileOutputStream( getFullExportFileName() ));
			 
			 stamper.getAcroFields().setField("FirstName", registrant.getFirstName());
			 stamper.getAcroFields().setField("Surname", registrant.getSurname());
			 stamper.getAcroFields().setField("Institution", registrant.getInstitution());
			 stamper.getAcroFields().setField("ID", String.valueOf(registrant.getID()) );
			// stamper.setFormFlattening(true);
			 stamper.close();
			 reader.close();
			 System.out.println("pdf created: " + getFullExportFileName() );
			 
		 } catch (Exception e) {
			 System.out.println("Error while creating the pdf.");
		 }
	 }
	 
	 public Boolean printPdf(){
		 
		 if (!Desktop.isDesktopSupported()) {
			System.out.println("Desktop not supported!");
			 return false;
			 
		 }
		 else{
			    try {
			        Desktop.getDesktop().print(new File(getFullExportFileName()));
			    } catch (UnsupportedOperationException e) {
					System.out.println("No default pdf printing command, opening pdf instead!");
			    	return false;
			    }
		    	catch(IOException e){
					System.out.println("I/O Exception while printing pdf!!");
			    	return false;
		    	}

		    	return true;
			}
			 
	 }	
	 
	 public void openPdf() {
		 
		 if (!Desktop.isDesktopSupported()) {
				System.out.println("Desktop not supported!");
			 }
			 else{
			    try {
			        Desktop.getDesktop().open(new File(getFullExportFileName()));
			    }
		    	catch(UnsupportedOperationException e){
					System.out.println("No default pdf viewer!");
		    	}
		    	catch(IOException e){
					System.out.println("I/O Exception while opening pdf!!");
		    	}
			}
			 
	 }

	 
	 
}
