package com.cs.code.challenge;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LogParserReportGeneration {
	
private static final Logger LOG = LogManager.getLogger(LogParserReportGeneration.class);	
public static String currentdirectorypath=System.getProperty("user.dir");
	
	public void printReportonConsole(ArrayList<ArrayList<String>> Details_to_print_in_console) {		
		
		String eventIDvalue =null;
		String eventDurationvalue =null;
		String eventTypevalue =null;
		String eventHostvalue =null;
		String eventAlertvalue=null;
		
		System.out.printf("|%-20s|  %-20s| %-20s| %-20s|  %-20s|\n","EventID" ,"EventDuration(ns)","Type","Host","Alert" ); 
		for(int i=0;i<Details_to_print_in_console.size();i++) {			
			eventIDvalue = Details_to_print_in_console.get(i).get(0);
			eventDurationvalue=Details_to_print_in_console.get(i).get(1);
			eventTypevalue=Details_to_print_in_console.get(i).get(2);
			eventHostvalue=Details_to_print_in_console.get(i).get(3);
			eventAlertvalue=Details_to_print_in_console.get(i).get(4);
			System.out.printf("|%-20s|  %-20s| %-20s| %-20s|  %-20s|\n",eventIDvalue,eventDurationvalue,eventTypevalue,eventHostvalue,eventAlertvalue); 
		}	
		LOG.info("EventResult are printed on Console");
		
	}
	
	public void printReportonExcel(ArrayList<ArrayList<String>> Details_to_print_in_Excel) {
		
		int rownum = 1;		  
		String []exceloutputHeaders = {"EventID" ,"EventDuration(ns)" ,"Type","Host","Alert"};
		Row row;
		 
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Event Details");
		row = sheet.createRow(0);
		
		CellStyle cellStyleforExcelHeaders = workbook.createCellStyle();		
		Font fontForExcelHeader = workbook.createFont();
		fontForExcelHeader.setColor(IndexedColors.BLACK.getIndex());
		fontForExcelHeader.setBold(true);
		cellStyleforExcelHeaders.setFont(fontForExcelHeader);
		
		CellStyle cellStyleforOutputValues = workbook.createCellStyle();
		Font fontExcelOutputValues = workbook.createFont();
		fontExcelOutputValues.setColor(IndexedColors.BLUE.getIndex());
		cellStyleforOutputValues.setFont(fontExcelOutputValues);
		
		 	
		for(int i=0;i<Details_to_print_in_Excel.get(0).size();i++) {			 
			sheet.autoSizeColumn(i); 
			Cell cell = row.createCell(i);
			cellStyleforExcelHeaders.setAlignment(HorizontalAlignment.CENTER);
			cellStyleforExcelHeaders.setVerticalAlignment(VerticalAlignment.CENTER);
			cell.setCellValue(exceloutputHeaders[i]);
			cell.setCellStyle(cellStyleforExcelHeaders);
			
		  }	
		 
		 
		for(int j=0;j<Details_to_print_in_Excel.size();j++) {
			 row = sheet.createRow(rownum++);
			for(int k=0;k<Details_to_print_in_Excel.get(0).size();k++) {
				sheet.autoSizeColumn(k); 
				Cell cell = row.createCell(k);
				cellStyleforOutputValues.setAlignment(HorizontalAlignment.CENTER);
				cellStyleforOutputValues.setVerticalAlignment(VerticalAlignment.CENTER);				
				cell.setCellValue(Details_to_print_in_Excel.get(j).get(k));
				cell.setCellStyle(cellStyleforOutputValues);
			     } 	
	     }
		 
		 try {		 
	            FileOutputStream out = new FileOutputStream(new File(currentdirectorypath +"\\"+"EventResultDetails.xlsx"));	           
	            workbook.write(out);
	            out.close();
	            LOG.info("EventResult Excel sheet is created successfully on the path : " +currentdirectorypath);
	         }
	        catch (Exception e) {	        	
	            LOG.error("Exception during processing Request");
	         }
	    }

	

}
