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

import indico.checkin.core.data.IndicoEventRegistrantList;
import indico.checkin.core.data.IndicoRegistrant;

import java.awt.Desktop;
import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
	private String templateFileName = "template-normal.pdf";
	private String templateLongFileName = "template-long.pdf";
	private static String os =  System.getProperty("os.name").toLowerCase();
	PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();

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
		if(registrant != null){
			String fileName = registrant.getID() + "-" + registrant.getSurname()
				+ "_" + registrant.getFirstName() + ".pdf";
			setExportFileName(fileName);
		}
	}

	public void setRegistrant(IndicoRegistrant registrant) {
		this.registrant = registrant;
		setFileNameFromRegistrantInforamtion();
	}

	public String getFullExportFileName() {
		return exportPath + "/" + exportFileName;
	}

	public String getExportPath() {
		return exportPath;
	}

	public String getExportFileName() {
		return exportFileName;
	}

	public String getFullTemplateFileName() {
		return templatePath + "/" + templateFileName;
	}
	public String getFullTemplateLongFileName() {
		return templatePath + "/" + templateLongFileName;
	}
	/*
	 * 
	 * 
	 * 
	 */
	public boolean exportPdf() {
		try {
			DecimalFormat f = new DecimalFormat("#0.00");
			DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
			Date date = new Date();
			String name = registrant.getFirstName() +" "+registrant.getSurname();
			

	        // test, which template to use
			boolean longName = (registrant.getFirstName() +" "+registrant.getSurname()).length() > 18;
			boolean longLongName = registrant.getFirstName().length() > 18;
			String templateToUse = longName ? getFullTemplateLongFileName() : getFullTemplateFileName();
			
			PDDocument pdDoc = PDDocument.load(new File(templateToUse));
	        PDDocumentCatalog pdCatalog = pdDoc.getDocumentCatalog();
	        PDAcroForm acroForm = pdCatalog.getAcroForm();
	        
	        
	        if( acroForm != null){
	        	@SuppressWarnings("unchecked")
	        	List<PDField> fields =  acroForm.getFields();
	        
		        for (PDField field : fields) {
		            switch(field.getFullyQualifiedName()){
		            case "FirstLastName1":
		            case "FirstLastName2":
		            case "FirstLastName3":
		            case "FirstLastName4":
		            case "FirstLastName5":
		            case "FirstLastName6":
		            	 field.setValue(name);
		            	//field.setValue( registrant.getFullInformation().getFullName() );
		            	 break;
		            	 /*
		            case "FirstLastName6":
		            	// check if companion
		            	if( registrant.getAccompanyingPersons().equals("0") ) field.setValue("" );
		            	else field.setValue(name);
		            	 break;
		            case "FirstLastName4":
		            	// check if companion
		            	if( registrant.getExcursion().equals("No Excursion") ) field.setValue("" );
		            	else field.setValue(name);
		            	 break;
		            	 */
		            case "#D":
		            	int d = Integer.parseInt(registrant.getAccompanyingPersons()) + 1;
		            	field.setValue( String.valueOf(d) );
		            	 break;
		            case "Companion":
		            	if( registrant.getAccompanyingPersons().equals("0"))  field.setValue("No Companion" );
		            	else  field.setValue("Companion" );
		            	 break;
		            case "Date":
		            	 field.setValue(dateFormat.format(date));
		            	 break;
		            case "BadgeTag":
		            	 field.setValue(registrant.getAffiliationForBadge());
		            	 break;
		            case "Institution":
		            	 field.setValue(registrant.getInstitution());
		            	 break;
		            case "City":
		            	 field.setValue(registrant.getCity());
		            	 break;
		            case "Country":
		            	Locale country = new Locale("", registrant.getCountry());
		            	 field.setValue( country.getDisplayCountry() );
		            	 break;
		            case "PostalCode":
		            	 field.setValue(registrant.getPostalCode());
		            	 break;
		            case "Address":
		            	 field.setValue(registrant.getAddress());
		            	 break;
		            case "Fee":
		            	 field.setValue(String.valueOf(f.format(registrant.getFullInformation().getAmountPaid())) );
		            	 break;
		            case "Excursion":
		            	 field.setValue(registrant.getExcursion());
		            	 break;
		            case "#E":
		            	 field.setValue(registrant.getExcursionPersons() );
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
	
	
	public void printAll( IndicoEventRegistrantList registrants, boolean print){
		List<IndicoRegistrant> registrantsList = registrants.getRegistrantList();
		IndicoRegistrant tmp_registrant = registrant;
		for( IndicoRegistrant iRegistrant: registrantsList ){
			if(iRegistrant != null){
				setRegistrant(iRegistrant);
				exportPdf();
				if(print)
					printPdf();
			}
		}
		setRegistrant(tmp_registrant);
	}
	
	
	public boolean printPdf(){
		System.out.println("entering print fnc");
		try {
	      DocPrintJob printerJob = defaultPrintService.createPrintJob();
			System.out.println("printJob created");
	      SimpleDoc simpleDoc;
	      //  File pdfFile = new File(getFullExportFileName());
	    //  SimpleDoc simpleDoc= new SimpleDoc(pdfFile.toURI().toURL(), DocFlavor.URL.PDF, null);
	      
	      System.out.println(os);
	      boolean linux =  (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0 );
	      if(linux){

	    	  String psFilname = getFullExportFileName().replace(".pdf", ".ps");
	    	  Runtime r = Runtime.getRuntime();

	    	  System.out.println("creating ps");
	    	  String[] commandArray = {"pdftops" , getFullExportFileName() , psFilname };
	    	 // System.out.println("now executing " + command);
	    	 Process p = r.exec(commandArray );
	    	  // wait for ps to be created
	    	 p.waitFor() ;
		    	  System.out.println("ps created");
	    	  InputStream stream =new FileInputStream(psFilname) ;
		      simpleDoc= new SimpleDoc(stream, DocFlavor.INPUT_STREAM.POSTSCRIPT, null);
	    	  
	      }
	      else{
	    	  InputStream stream =new FileInputStream(getFullExportFileName()) ;
		      simpleDoc= new SimpleDoc(stream, DocFlavor.INPUT_STREAM.PDF, null);
	      }
	      
	      
	      System.out.println("Now printing!");
	      printerJob.print(simpleDoc, null);
		} catch (PrintException e) {
			System.out.println("Error while printing!");
			e.printStackTrace();
			return false;
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
		
	}
}