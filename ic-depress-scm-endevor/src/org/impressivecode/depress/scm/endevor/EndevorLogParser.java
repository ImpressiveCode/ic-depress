package org.impressivecode.depress.scm.endevor;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.impressivecode.depress.scm.SCMDataType;
import org.impressivecode.depress.scm.SCMOperation;

public class EndevorLogParser {
	
	private File logFile;
	private LinkedList<SCMDataType> parsedData;
	
	public EndevorLogParser(File logFile) {
		this.logFile = logFile;
		this.parsedData = new LinkedList<SCMDataType>();
	}
	
	public void parseLogFile() {
		//TODO parsing start
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
