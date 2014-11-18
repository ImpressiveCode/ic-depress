package org.impressivecode.depress.scm.endevor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.impressivecode.depress.scm.SCMDataType;
import org.impressivecode.depress.scm.SCMOperation;

public class EndevorLogParser {
	
	private File logFile;
	private LinkedList<SCMDataType> parsedData;
	
	public EndevorLogParser(File logFile) {
		this.logFile = logFile;
		this.parsedData = new LinkedList<SCMDataType>();
	}
	
	public void parseLogFile() throws FileNotFoundException {
		Scanner scanner = new Scanner(this.logFile, "UTF-8");
		while(scanner.hasNextLine()) {
			String currentLine = scanner.nextLine();
			if (isLineSensible(currentLine)) {
				EndevorParsingPhase phase = verifyLine(currentLine);
				switch(phase) {
					case SEARCHING:
						break;
						
					case SOURCE_INFO:
						while (!isLineSensible(currentLine = scanner.nextLine()));
						
						String[] columnHeadersPolluted = currentLine.split(" ");
						LinkedList<String> columnHeaders = removeEmptyEntriesFromColumnHeadersArray(columnHeadersPolluted);
						SCMDataType newRecord = new SCMDataType();
						for(String header : columnHeaders) {
							//TODO nastêpne linie zawieraj¹ informacje do sparsowania!!!
							//trzeba zmapowaæ nazwê kolumny do odpowiedniego pola klasy SCMDataType
						}
						break;
						
					case SUMMARY:
						break;
				}
			}
		}
	}

	private LinkedList<String> removeEmptyEntriesFromColumnHeadersArray(String[] columnHeaders) {
		LinkedList<String> clearedHeaders = new LinkedList<String>();
		for (String s : columnHeaders) {
			if (s.length() > 0) {
				clearedHeaders.add(s);
			}
		}
		return clearedHeaders;
	}

	private boolean isLineSensible(String currentLine) {
		//TODO sensowniejsza nazwa :P
		String step1 = currentLine.replace("-", "");
		String step2 = step1.replace("*", "");
		String trimmed = step2.trim();
		return trimmed.length() > 0;
		//return currentLine.replace("-", "").replace("*", "").trim().length() > 0;
	}

	private EndevorParsingPhase verifyLine(String currentLine) {
		if (currentLine.contains(EndevorLogKeywords.LOG_START)) {
			return EndevorParsingPhase.SOURCE_INFO;
		}
		else if (currentLine.contains(EndevorLogKeywords.LOG_SUMMARY)) {
			return EndevorParsingPhase.SUMMARY;
		}
		else {
			return EndevorParsingPhase.SEARCHING;
		}
	}

	public List<SCMDataType> getParsedCommits() throws EndevorLogParserException {
        //TODO delete test data
        SCMDataType testData = new SCMDataType();
        testData.setAuthor("Test author");
        testData.setCommitDate(new Date(System.currentTimeMillis()));
        testData.setResourceName("Test resource");
        testData.setOperation(SCMOperation.OTHER);
        testData.setPath("test Path");
        testData.setCommitID("testCommID");
        testData.setExtension("test ext");
        testData.setMessage("Test output");
        this.parsedData.add(testData);
        
        if (this.parsedData.size() == 0) {
        	throw new EndevorLogParserException("The log file has not been parsed. Call parseLogFile() first.");
        }
        else {
        	return this.parsedData;
        }
	}
	
	
}
