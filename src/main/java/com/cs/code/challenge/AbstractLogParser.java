package com.cs.code.challenge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public abstract class AbstractLogParser {
	
	public abstract void parsedinputLogfile() throws IOException;
	public abstract void caluclateEventDetails(ArrayList<Map<String, String>> logDetails_Record_List_Report,int threshold_duration);
	

}
