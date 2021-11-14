package com.cs.code.challenge;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogParserMain extends AbstractLogParser {	
	
	private static final Logger LOG = LogManager.getLogger(LogParserMain.class);
	
	public String filepath;
	public BufferedReader reader;	
	Scanner sc=null;
	ArrayList<Map<String,String>>logDetailsRecordList= new ArrayList<Map<String,String>>();	
	
	public void takeLogFilePathfromUser() throws IOException {
		sc = new Scanner(System.in);
		System.out.println("Please Enter the Log File which we need to Extract : ");
		filepath =sc.next();
		LOG.info("File Path : " + filepath + " is entered by user");
		verifyInputFile(filepath);		
	}
	
	public void verifyInputFile(String filepath) throws IOException {		
		try {
			reader = new BufferedReader(new FileReader(filepath));
			LOG.info("File is found at the location mentioned by user");
			parsedinputLogfile();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			LOG.error("File is not found at the location entered by user",e);
		}
	}	
	
		
	public void parsedinputLogfile() throws IOException {
		try {
			String inputFileLine = reader.readLine();			
				while (inputFileLine != null) {
					 inputFileLine=inputFileLine.replaceAll("\"", "").replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\\[", "").replaceAll("\\]", "");					    					    				    
					 String [] recordDetailsSplitbyComma= inputFileLine.split(",");
				    	
					 LinkedHashMap<String,String>logDetailsKeyvalueRecords=new LinkedHashMap<String,String>();
				    	
				    	for(int i=0;i<recordDetailsSplitbyComma.length;i++) {			    		
				    		String []detailsSplitByColon =recordDetailsSplitbyComma[i].split(":");
				    		logDetailsKeyvalueRecords.put(detailsSplitByColon[0].trim(), detailsSplitByColon[1].trim());
				    	}
				    		
				     logDetailsRecordList.add(logDetailsKeyvalueRecords);
					 inputFileLine = reader.readLine();	    					
				 }
							 
			caluclateEventDetails(logDetailsRecordList,4);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOG.error("I/O Error occured during parsing the File",e);
			}finally {
			    reader.close();
			}		
			
	}
		
	public void caluclateEventDetails(ArrayList<Map<String, String>> logDetails_Record_List_Report,int threshold_duration) {
		
			ArrayList<ArrayList<String>>eventDetailsListResult = new ArrayList<ArrayList<String>>();	
			
		    for(int i=0;i<logDetails_Record_List_Report.size();i++) {
		    	ArrayList<String>eventsrRecordPerLine = new ArrayList<String>();
		    	for(int j=i+1;j<logDetails_Record_List_Report.size();j++) {		    		
		    		String eventType="";
		    		String eventHost ="";
		    		Long difference=0L; 
		    		
		    		if(logDetails_Record_List_Report.get(i).get("id").equalsIgnoreCase(logDetails_Record_List_Report.get(j).get("id"))) {
		    			if(logDetails_Record_List_Report.get(i).containsKey("type")) {
		    				eventType=logDetails_Record_List_Report.get(i).get("type");
		    			}
		    			if(logDetails_Record_List_Report.get(i).containsKey("host")) {
		    				eventHost=logDetails_Record_List_Report.get(i).get("host");
		    			}
		    				    				    			
		    			if(logDetails_Record_List_Report.get(i).get("state").equalsIgnoreCase("Finished")){
		    				difference =Long.parseLong(logDetails_Record_List_Report.get(i).get("timestamp")) - Long.parseLong(logDetails_Record_List_Report.get(j).get("timestamp"));
		    				
		    			} else {
		    				difference =Long.parseLong(logDetails_Record_List_Report.get(j).get("timestamp")) - Long.parseLong(logDetails_Record_List_Report.get(i).get("timestamp"));
		    			}
		    			
		    			eventsrRecordPerLine.add(logDetails_Record_List_Report.get(i).get("id"));
		    			eventsrRecordPerLine.add(difference.toString());
		    			eventsrRecordPerLine.add(eventType);
		    			eventsrRecordPerLine.add(eventHost);
		    			
		    			if(difference>threshold_duration) {  					    						    			
		    				eventsrRecordPerLine.add("True");
			    			eventDetailsListResult.add(eventsrRecordPerLine); 			    			
			    			logDetails_Record_List_Report.remove(j);
			    			break;
		    			
		    		} else {
		    			
			    			eventsrRecordPerLine.add("False");
			    			eventDetailsListResult.add(eventsrRecordPerLine);	    			
			    			logDetails_Record_List_Report.remove(j);
			    			break;
		    		}
		    			
		    		}
		    	}
		    	
		    }
		    
		    LogParserReportGeneration logParserReportGeneration = new LogParserReportGeneration();	    	    
		    logParserReportGeneration.printReportonConsole(eventDetailsListResult);
		    logParserReportGeneration.printReportonExcel(eventDetailsListResult);
		}
	

	public static void main(String[] args) {		
		LogParserMain logParserMain = new LogParserMain();
		try {
			logParserMain.takeLogFilePathfromUser();
		} catch (IOException e) {
			LOG.error("I/O Error occured during parsing the File",e);
		}
		
	}

	

	

}
