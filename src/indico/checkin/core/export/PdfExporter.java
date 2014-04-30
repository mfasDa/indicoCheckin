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
import java.io.*;
import java.net.MalformedURLException;
import java.util.List;

import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
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
	public boolean exportPdf() {
		try {
			PDDocument pdDoc = PDDocument.load(new File(getFullTemplateFileName()));
	        PDDocumentCatalog pdCatalog = pdDoc.getDocumentCatalog();
	        PDAcroForm acroForm = pdCatalog.getAcroForm();
	        
	        if( acroForm != null){
	        	@SuppressWarnings("unchecked")
	        	List<PDField> fields =  acroForm.getFields();
	        
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
	        }
	        pdDoc.save(getFullExportFileName());
	        pdDoc.close();
	        System.out.println("pdf created: " + getFullExportFileName());
			
		} catch (Exception e) {
			System.out.println("Error while creating the pdf.");
			e.printStackTrace();
			return false;
		}
		return true;
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

	public boolean openPdf() {

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
	
	
	public boolean printPdf(){
		try {
		 PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();
	      DocPrintJob printerJob = defaultPrintService.createPrintJob();
	      File pdfFile = new File(getFullExportFileName());
	      SimpleDoc simpleDoc= new SimpleDoc(pdfFile.toURI().toURL(), DocFlavor.URL.AUTOSENSE, null);
	      printerJob.print(simpleDoc, null);
		} catch (PrintException e) {
			System.out.println("Error while printing!");
			e.printStackTrace();
			return false;
		}catch (MalformedURLException e) {
			System.out.println("Error while printing!");
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}
}