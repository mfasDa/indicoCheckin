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
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.net.MalformedURLException;
import java.util.List;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPageable;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;


public class PdfExporter {
	/**
	 * Exporter class loads template and fills it with registrant information
	 * 
	 * @author: Steffen Weber
	 */
	private static String exportPath = System.getProperty("java.io.tmpdir");
	private String exportFileName;
	private static String templatePath = "templates";
	private String templateFileName = "template-test.pdf";

	private IndicoRegistrant registrant;

	public void setExportPath(String path) {
		PdfExporter.exportPath = path;
	}

	public void setExportFileName(String fileName) {
		this.exportFileName = fileName;
	}

	public void setTemplatePath(String path) {
		PdfExporter.templatePath = path;
	}

	public void setTemplateFileName(String fileName) {
		this.templateFileName = fileName;
	}

	public void setFileNameFromRegistrantInforamtion() {
		String fileName = registrant.getID() + "-" + registrant.getSurname()
				+ "_" + registrant.getFirstName() + ".pdf";
		setExportFileName(fileName);
	}

	public void setRegistrant(IndicoRegistrant registrant) {
		this.registrant = registrant;
		setFileNameFromRegistrantInforamtion();
	}

	public String getFullExportFileName() {
		return exportPath + "/" + exportFileName;
	}

	public String getFullTemplateFileName() {
		return templatePath + "/" + templateFileName;
	}
	/*
	 * 
	 * 
	 * 
	 */
	public void exportPdf() {
		try {
			PDDocument pdDoc = PDDocument.load(new File(getFullTemplateFileName()));
	        PDDocumentCatalog pdCatalog = pdDoc.getDocumentCatalog();
	        PDAcroForm acroForm = pdCatalog.getAcroForm();
	        
	        @SuppressWarnings("unchecked")
			List<PDField> fields = acroForm.getFields();
	        
	        for (PDField field : fields) {
	            switch(field.getFullyQualifiedName()){
	            case "FirstName":
	            	 field.setValue(registrant.getFirstName());
	            	 break;
	            case "Surname":
	            	 field.setValue(registrant.getSurname());
	            	 break;
	            case "Institution":
	            	 field.setValue(registrant.getInstitution());
	            	 break;
	            case "ID":
	            	 field.setValue(String.valueOf(registrant.getID()));
	            	 break;
	            }   
	        }
	        
	        pdDoc.save(getFullExportFileName());
	        pdDoc.close();
	        System.out.println("pdf created: " + getFullExportFileName());
			
		} catch (Exception e) {
			System.out.println("Error while creating the pdf.");
			e.printStackTrace();
		}
	}
	
	
	/*
	public void exportPdfOld() {
		try {
			PdfReader reader = new PdfReader(getFullTemplateFileName());
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
					getFullExportFileName()));

			stamper.setFormFlattening(true);
			if(stamper.getAcroFields().getField("FirstName") != null)
				stamper.getAcroFields().setField("FirstName", registrant.getFirstName());
			if(stamper.getAcroFields().getField("Surname") != null)
				stamper.getAcroFields().setField("Surname", registrant.getSurname());
			if(stamper.getAcroFields().getField("Institution") != null)
				stamper.getAcroFields().setField("Institution", registrant.getInstitution());
			if(stamper.getAcroFields().getField("ID") != null)
				stamper.getAcroFields().setField("ID", String.valueOf(registrant.getID()));
			if(stamper.getAcroFields().getField("TEST") != null)
				stamper.getAcroFields().setField("TEST", String.valueOf(registrant.getID()));
		
			//PdfAction action = new PdfAction(PdfAction.PRINTDIALOG);
			//PdfWriter writer = stamper.getWriter();
			//writer.setOpenAction(action);
			//writer.close();
			stamper.close();
			
			reader.close();
			System.out.println("pdf created: " + getFullExportFileName());

		} catch (Exception e) {
			System.out.println("Error while creating the pdf.");
			e.printStackTrace();
		}
	}

	public Boolean printPdf2() {
		if (!Desktop.isDesktopSupported()) {
			System.out.println("Desktop not supported!");
			return false;

		} else if (Desktop.getDesktop().isSupported(Desktop.Action.PRINT)) {
			try {
				Desktop.getDesktop().print(new File(getFullExportFileName()));
			} catch (IOException e) {
				System.out.println("I/O Exception while printing file!");
				return false;
			}
		} else {
			System.out.println("No default pdf printing command!");
			return false;
		}
		return true;
	}
*/

	public Boolean openPdf() {

		if (!Desktop.isDesktopSupported()) {
			System.out.println("Desktop not supported!");
		} else if (Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
			try {
				Desktop.getDesktop().open(new File(getFullExportFileName()));
			} catch (IOException e) {
				System.out.println("I/O Exception while opening file!");
				return false;
			}
		} else {
			System.out.println("No default pdf opening command!");
			return false;
		}
		return true;

	}
	
	
	public Boolean printPdf(){
	

        try {
        	
        	PDDocument document = PDDocument.load(getFullExportFileName() );
        	
        	
        	
        PrinterJob job = PrinterJob.getPrinterJob();
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        
        job.setPageable(new PDPageable(document, job));
        boolean ok = job.printDialog(aset);
        
        if (ok) {
        	job.print(aset);
        }
        else{
        	System.out.println("printing canceld by user!");
        	return false;
        	
        }} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			// TODO Auto-generated catch block
			e1.printStackTrace();
            return false;
		}catch (PrinterException e) {
            System.out.println("PrinterException!!!");
			e.printStackTrace();
            return false;
           }
		return true;
		
	}
}