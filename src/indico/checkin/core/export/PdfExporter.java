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

import indico.checkin.core.api.IndicoAPIConnector;
import indico.checkin.core.data.IndicoEventRegistrantList;
import indico.checkin.core.data.IndicoRegistrant;
import indico.checkin.core.data.IndicoRegistrantComparator;

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
import org.apache.pdfbox.util.PDFMergerUtility;


public class PdfExporter {
	/**
	 * Exporter class loads template and fills it with registrant information
	 * 
	 * @author: Steffen Weber
	 */
	private static String exportPath = System.getProperty("java.io.tmpdir");
	private String exportFileName;
	private static String templatePath = "templates";
	private String templateFileName = "QM2014_Registration_Confirmation.pdf";
	private String mergeFileName = "merged";
	
	
	private boolean replaceSpecialCharacters = true;
	private IndicoRegistrant registrant;
	
	// for printing
	private static String os =  System.getProperty("os.name").toLowerCase();
	
	
	PrintService defaultPrintService;
	private boolean printerIsSetup = false;
	
	
	
	public void setupPrinter(){
		defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();
		printerIsSetup = true;
		
	}

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

	public void setReplace(boolean replace) {
		this.replaceSpecialCharacters = replace;
	}

	public void setFileNameFromRegistrantInforamtion() {
		if(registrant != null){
			String fileName =  replaceSpecialCharacters(registrant.getSurname().toLowerCase().replaceAll(" ", "-"))
				+ "_" + replaceSpecialCharacters(registrant.getFirstName().toLowerCase().replaceAll(" ", "-")) + "(" + registrant.getID() + ")" + (replaceSpecialCharacters? "_replaced":"") + ".pdf";
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
	public String getFullMergeFileName() {
		return exportPath + "/" + mergeFileName;
	}

	public String getExportPath() {
		return exportPath;
	}

	public String getExportFileName() {
		return exportFileName;
	}

	public String getMergeFileName() {
		return mergeFileName;
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
			// only print people that have paid
			
			if( !registrant.hasPaid()){
				System.out.println("registrant has not paid yet!");
				return false;
				
			}
			
			
			
			DecimalFormat f = new DecimalFormat("#0.00");
			DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
			Date date = new Date();
			String address = replaceSpecialCharacters ? 
					replaceSpecialCharacters(registrant.getAddress())
					: registrant.getAddress();

			
			
			String firstname = replaceSpecialCharacters ? 
					replaceSpecialCharacters(registrant.getFirstName())
					: registrant.getFirstName();

			String surname = replaceSpecialCharacters ? 
							replaceSpecialCharacters(registrant.getSurname())
							: registrant.getSurname();

							String firstlastname ="";
							String firstlastnamea ="";
							String firstlastnameb ="";
			String name = firstname + " " + surname;

			
			//create only long names
			//if((registrant.getFirstName() +" "+registrant.getSurname()).length() < 21) return false;	
			//if(address.length() < 60) return false;	
						
			
			
				// normal name			
			if( (registrant.getFirstName() +" "+registrant.getSurname()).length() < 22 ){
				firstlastname = firstname + " " + surname;
			}
			// moderately long name
			else if (registrant.getSurname().length() < 22){
				firstlastname = firstname;
				firstlastnameb = surname;
			}
			// very long name
			else{
				firstlastnamea = firstname;
					String[] surNames = registrant.getSurname().split("\\s+");
					firstlastname = surNames[0];
					for(int i=1; i< surNames.length; ++i){
						firstlastnameb+=surNames[i]+" ";
					}
			}
			String affiliation = replaceSpecialCharacters ? 
					replaceSpecialCharacters(registrant.getAffiliationForBadge())
					: registrant.getAffiliationForBadge();
			String badgeTag ="";
			String badgeTagA ="";
			// long Affiliation
			
			System.out.println(affiliation.length());
			
			
			
			if(affiliation.length() > 36){
				String[] affiliations = affiliation.split("\\s+");
				int i;
				for(i=0; i< affiliations.length/2; ++i){
					badgeTag+=affiliations[i]+" ";
				}
				for(int j=i; j< affiliations.length; ++j){
					badgeTagA+=affiliations[j]+" ";
				}
			}
			// normal affiliation
			else{
				badgeTag = affiliation;
				
			}
			String institution = replaceSpecialCharacters ? 
					replaceSpecialCharacters(registrant.getInstitution())
					: registrant.getInstitution();
					
			String city = replaceSpecialCharacters ? 
							replaceSpecialCharacters(registrant.getCity())
							: registrant.getCity();
			PDDocument pdDoc = PDDocument.load(new File(getFullTemplateFileName()));
	        PDDocumentCatalog pdCatalog = pdDoc.getDocumentCatalog();
	        PDAcroForm acroForm = pdCatalog.getAcroForm();
	        
	        // fill in the forms
	        if( acroForm != null){
	        	@SuppressWarnings("unchecked")
	        	List<PDField> fields =  acroForm.getFields();
	        
		        for (PDField field : fields) {
		            switch(field.getFullyQualifiedName()){
		            case "FirstLastName1":
		            	 field.setValue(name);
		            	 break;
		            case "FirstLastName2":
		            	 field.setValue( field.getValue().replaceAll("FirstLastName2", name));
		            	 break;
		            	 
		            	 
		            case "FirstLastName3":
		            case "FirstLastName4":
		            case "FirstLastName5":
		            case "FirstLastName6":
		            	if(field.getFullyQualifiedName().equals("FirstLastName6") && registrant.getAccompanyingPersons().equals("0")){
		            		field.setValue("");
		            	}
			            else{
			            	field.setValue(firstlastname);
			            }
		            	break;
		            case "FirstLastName3a":
		            case "FirstLastName4a":
		            case "FirstLastName5a":
		            case "FirstLastName6a":
		            	if(field.getFullyQualifiedName().equals("FirstLastName6a") && registrant.getAccompanyingPersons().equals("0")){
		            		field.setValue("");
		            	}
			            else{
			            	field.setValue(firstlastnamea);
			            }
		            	break;
		            case "FirstLastName3b":
		            case "FirstLastName4b":
		            case "FirstLastName5b":
		            case "FirstLastName6b":
		            	if(field.getFullyQualifiedName().equals("FirstLastName6b") && registrant.getAccompanyingPersons().equals("0")){
		            		field.setValue("");
		            	}
			            else{
			            	field.setValue(firstlastnameb);
			            }
		            	break;
		            	
		            	
		            	
		            case "DinnerNumber":
		            	int d = Integer.parseInt(registrant.getAccompanyingPersons()) + 1;
		            	field.setValue( field.getValue().replaceAll("X",String.valueOf(d)) );
		            	 break;
		            case "Companion":
		            	if( registrant.getAccompanyingPersons().equals("0"))  field.setValue("" );
		            	else  field.setValue("Companion" );
		            	 break;
		            case "Date":
		            	// field.setValue(dateFormat.format(date));
		            	 field.setValue("May 18, 2014");
		            	 break;
		            case "BadgeTag1":
		            	field.setValue(badgeTag);
		            	 break;
		            case "BadgeTag1a":
		            	field.setValue(badgeTagA);
		            	 break;
		            case "BadgeTag2":
		            	if(registrant.getAccompanyingPersons().equals("0")){
		            		field.setValue("");
		            	}
		            	else{
		            		field.setValue(badgeTag);
		            	}
		            	 break;
		            case "BadgeTag2a":
		            	if(registrant.getAccompanyingPersons().equals("0")){
		            		field.setValue("");
		            	}
		            	else{
		            		field.setValue(badgeTagA);
		            	}
		            	 break;
		            case "Institution":
		            	 field.setValue(institution);
		            	 break;
		            case "City":
		            	 field.setValue(city);
		            	 break;
		            case "Country":
		            	Locale country = new Locale("", registrant.getCountry());
		            	 field.setValue( country.getDisplayCountry() );
		            	 break;
		            case "PostalCode":
		            	 field.setValue(registrant.getPostalCode());
		            	 break;
		            case "PostalCodeCity":
		            	 field.setValue(registrant.getPostalCode() + " " + city);
		            	 break;
		            case "Address":
		            	 field.setValue(address);
		            	 break;
		            case "Fee":
		            	 field.setValue(field.getValue().replaceAll("XXX", String.valueOf(f.format(registrant.getConferenceFee()))) );
		            	 break;
		            case "ExcursionType":
		            	 field.setValue(registrant.getExcursion());
		            	 break;
		            case "ExcursionNumber":
		            	 field.setValue(    field.getValue().replaceAll("X",  String.valueOf( registrant.getExcursionPersons() ) )    );
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
	
	/**
	 * 
	 * exports, and prints (if param print given) all registrants in list registrante
	 * 
	 * 
	 * @param registrants: registrants to export / print
	 * @param print : whether to print after exporting or not
	 * @param merge : whether to merge into one file
	 */
	
	
	public void printAll( IndicoEventRegistrantList registrants, IndicoAPIConnector connection, boolean print, boolean merge){
		try {
			
			List<IndicoRegistrant> registrantsList = registrants.getRegistrantList();
			registrantsList.sort( new IndicoRegistrantComparator() );
			IndicoRegistrant tmp_registrant = registrant;

			PDFMergerUtility globalmerger = new PDFMergerUtility();
			PDFMergerUtility tmpmerger = new PDFMergerUtility();
			int n = 0;
			for( IndicoRegistrant iRegistrant: registrantsList ){
				if(iRegistrant != null ){
					System.out.println(n);
					setRegistrant(iRegistrant);
					connection.fetchFullRegistrantInformation(iRegistrant);
					
					boolean exported = exportPdf();
					if(exported)
						n++;
					if(print){
						printPdf();
					}
					if(merge && exported){
						tmpmerger.addSource(getFullExportFileName());
							if( n%100 == 0 ){
								tmpmerger.setDestinationFileName(getFullMergeFileName() + n + (replaceSpecialCharacters ?"_replaced":"") +".pdf") ;
								tmpmerger.mergeDocuments();
								globalmerger.addSource(getFullMergeFileName() + n + (replaceSpecialCharacters ?"_replaced":"") +".pdf");
								tmpmerger = new PDFMergerUtility();
							}
					}	
				}
			}
			if(merge){
				if( n%100 != 0 ){
					tmpmerger.setDestinationFileName(getFullMergeFileName() + n + (replaceSpecialCharacters ?"_replaced":"") +".pdf") ;
					tmpmerger.mergeDocuments();
					globalmerger.addSource(getFullMergeFileName() + n + (replaceSpecialCharacters ?"_replaced":"") +".pdf");
				}
				globalmerger.setDestinationFileName(getFullMergeFileName() + (replaceSpecialCharacters ?"_replaced":"") + ".pdf") ;
				globalmerger.mergeDocuments();
			}
			setRegistrant(tmp_registrant);
		}
		catch (Exception e) {
			System.out.println("Exception merging files.");
			System.out.println(e.getStackTrace());
		}
	}
	
	
	public boolean printPdf(){
		try {
			if(!printerIsSetup){
				setupPrinter();
			}
	      DocPrintJob printerJob = defaultPrintService.createPrintJob();
			System.out.println("printJob created");
	      SimpleDoc simpleDoc;
	      //  File pdfFile = new File(getFullExportFileName());
	    //  SimpleDoc simpleDoc= new SimpleDoc(pdfFile.toURI().toURL(), DocFlavor.URL.PDF, null);
	      
	      System.out.println(os);
	      boolean linux = (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0 );
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
	
	public String replaceSpecialCharacters(String input){
		return input.replaceAll("\u010c", "C")
				.replaceAll("\u010d", "c")
				.replaceAll("\u010e", "D")
				.replaceAll("\u0119", "e")
				.replaceAll("\u011a", "E")
				.replaceAll("\u011b", "e")
				.replaceAll("\u01cd", "A")
				.replaceAll("\u01ce", "a")
			.replaceAll("\u013d", "L")
			.replaceAll("\u013e", "l")
			.replaceAll("\u0142", "l")
			.replaceAll("\u0147", "N")
			.replaceAll("\u0148", "n")
			.replaceAll("\u0158", "R")
			.replaceAll("\u0159", "r")
			.replaceAll("\u015a", "s")
			.replaceAll("\u0160", "S")
			.replaceAll("\u0161", "s")
			.replaceAll("\u0164", "T")
			.replaceAll("\u017b", "Z")
			.replaceAll("\u017c", "z")
			.replaceAll("\u017d", "Z")
			.replaceAll("\u017e", "z")
			.replaceAll("\u01c4", "Dz")
			.replaceAll("\u01c5", "dz")
			.replaceAll("\u01cf", "I")
			.replaceAll("\u01d0", "i")
			.replaceAll("\u01d1", "O")
			.replaceAll("\u01d2", "o")
			.replaceAll("\u01d3", "U")
			.replaceAll("\u01d4", "u")
			.replaceAll("\u01d9", "U")
			.replaceAll("\u01da", "u")
			.replaceAll("\u01e6", "G")
			.replaceAll("\u01e8", "K")
			.replaceAll("\u01e9", "k")
			.replaceAll("\u01f0", "j")
			.replaceAll("\u01e7", "g")
			.replaceAll("\u021e", "H")
			.replaceAll("\u021f", "h");
		
		
	}
	
	
	
	
	
	
	
}