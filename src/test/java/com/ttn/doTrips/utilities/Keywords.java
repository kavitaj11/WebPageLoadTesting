package com.ttn.doTrips.utilities;

import static org.testng.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonParseException;

import edu.umass.cs.benchlab.har.HarBrowser;
import edu.umass.cs.benchlab.har.HarEntries;
import edu.umass.cs.benchlab.har.HarEntry;
import edu.umass.cs.benchlab.har.HarLog;
import edu.umass.cs.benchlab.har.HarPage;
import edu.umass.cs.benchlab.har.ISO8601DateFormatter;
import edu.umass.cs.benchlab.har.tools.HarFileReader;
import edu.umass.cs.benchlab.har.tools.HarFileWriter;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class Keywords{
	
	String workingDir = System.getProperty("user.dir");
	String filename = workingDir+"\\"+GlobalVars.HARFILE_NAME;
	String filename1 = workingDir+"\\"+GlobalVars.TEXTFILE_NAME;
	
	File f = new File(filename);
	HarFileReader readhar = new HarFileReader();
	HarFileWriter writehar = new HarFileWriter();
	

	public static void setURL(String env, String ver) {
		
		
		Properties properties = new Properties();
		String fileName = "application-" + env + ".properties";
		try {
			FileReader reader = new FileReader(fileName);
			properties.load(reader);
			
			GlobalVars.AUTH_URL = properties.getProperty("AUTH_URL");
			GlobalVars.BASE_URL = properties.getProperty("BASE_URL");
			GlobalVars.LOGIN_EMAIL = properties.getProperty("LOGIN_EMAIL");
			GlobalVars.LOGIN_SIGNUP_PASSWORD_123456 = properties.getProperty("LOGIN_SIGNUP_PASSWORD_123456");
				
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public String readCellDataUsingIndexFromXLSFile(String inputFile,int workSheetNumber, int ColIndex, int rowIndex) throws IOException {
		String cellData = null;

		File inputWorkbook = new File(inputFile);
		Workbook w;

		try {
			w = Workbook.getWorkbook(inputWorkbook);

			// Get the first sheet
			Sheet sheet = w.getSheet(workSheetNumber);
			System.out.println("Specific Row data: " + sheet);

			WorkbookSettings ws = new WorkbookSettings();
			ws.setLocale(new Locale("er", "ER"));

			//Total No. of Columns
			int columns = sheet.getColumns();
			System.out.println("No. of Columns : " + columns);

			//Total No. of Rows
			int rows = sheet.getRows();
			System.out.println("No. of Rows : " + rows);

			// Loop over first 10 column and lines
			int i = 0, j = 0;
			for (i = 1; i < rows; i++) {
				if (i == rowIndex) 
				{
					break;
				}
			}
			for (j = 0; j < columns; j++) {
				if (ColIndex == j) {
					break;
				}
			}
			cellData = sheet.getCell(j, i).getContents();
			System.out.println("Cell Data :: " + cellData);

		} catch (BiffException e) 
		{
			e.printStackTrace();
		}
		
		System.out.println("Specific Row data: " + cellData);

		;

		return cellData;
	}

	public void writeCellDataInExistingXLS(String excelPath, int SheetIndex,int rowIndex, int ColIndex, String Text) throws IOException,RowsExceededException, WriteException, BiffException 
	
	{
		Workbook aWorkBook = Workbook.getWorkbook(new File(excelPath));
		WritableWorkbook aCopy = Workbook.createWorkbook(new File(excelPath),aWorkBook);
		WritableSheet aCopySheet = aCopy.getSheet(SheetIndex);// index of the needed sheet
		jxl.write.Label anotherWritableCell = new jxl.write.Label(ColIndex,rowIndex, Text);
		System.out.print(ColIndex);
		
		aCopySheet.addCell(anotherWritableCell);
		aCopy.write();
		aCopy.close();
	}

	public void writeLogToTextFile()
	{
		try{
			
			
			
			 try
			    {
			      //System.out.println("Reading " + fileName);
			
			      HarLog log = readhar.readHarFile(f);
			      // Access all elements as objects
			      HarBrowser browser = log.getBrowser();
			      HarEntries entries = log.getEntries();
			
			      // Used for loops
			      List<HarPage> pages = log.getPages().getPages();
			      List<HarEntry> hentry = entries.getEntries();
			      for (HarPage page : pages) {
			    	  System.out.println("page start time: " + ISO8601DateFormatter.format(page.getStartedDateTime()));
			    	  System.out.println("page id: " + page.getId());
			    	  System.out.println("page title: " + page.getTitle());
			      }
			      // Output "response" code of entries.
			      for (HarEntry entry : hentry) 
			      {
			    	  System.out.println("request code:  "+ entry.getRequest().getMethod()); // Output request// type
			    	  System.out.println("response code: "+ entry.getRequest().getUrl()); // Output url of request
			    	  System.out.println("response code: "+ entry.getResponse().getStatus()); // Output the
			      }

			      // Once you are done manipulating the objects, write back to a file
			      System.out.println("Writing " + GlobalVars.EXCELFILE_NAME );
			
			      File f2 = new File(filename1);
			      writehar.writeHarFile(log, f2);
			
			    }
			    catch (JsonParseException e)
			    {
			      e.printStackTrace();
			      fail("Parsing error during test");
			    }
			    catch (IOException e)
			    {
			      e.printStackTrace();
			      fail("IO exception during test");
			    }
			 	finally {
					
				}
			 }
			 finally {
					
			}
		}
			 
	public ArrayList<String> readUtmeFromTextFile()
	{
		ArrayList<String> utmeList=new ArrayList<String>();
		String workingDir = System.getProperty("user.dir");
		String filename1 = workingDir+"\\"+GlobalVars.TEXTFILE_NAME;
		
		try{
			String line = "";
			BufferedReader br = new BufferedReader(new FileReader(filename1));
			while ((line = br.readLine()) != null) 
			{
				if (line.contains("\"url\"")) 
				{
					if (line.indexOf("utme") != -1) 
					{
						utmeList.add(line.substring(line.indexOf("utme"),line.indexOf("&utmcs")));						
					}
				}
			}	
		}catch(Exception e){
			
		}
		return utmeList;
	}
		
	public ArrayList<String> readCellData(String inputFile,int ColIndex) throws IOException {
		String cellData = null;

		File inputWorkbook = new File(inputFile);
		Workbook w;
		ArrayList<String> urlList=new ArrayList<String>();
		
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			// Get the first sheet
			Sheet sheet = w.getSheet("Sheet1");
			System.out.println("Specific Row data: " + sheet);
			WorkbookSettings ws = new WorkbookSettings();
			ws.setLocale(new Locale("er", "ER"));
			int columns = sheet.getColumns();
			int rows = sheet.getRows();
			

			// Loop over first 10 column and lines
			
			for (int i = 1; i < sheet.getRows(); i++) 
			{
				cellData = sheet.getCell(ColIndex, i).getContents();
				System.out.println(cellData);
				urlList.add(cellData);
			}
			
			
			System.out.println("Cell Data :: " +urlList);

		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		

		;

		return urlList;
	}
	
	
	
}