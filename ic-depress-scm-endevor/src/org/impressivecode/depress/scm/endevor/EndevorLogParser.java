package org.impressivecode.depress.scm.endevor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.impressivecode.depress.scm.SCMDataType;
import org.impressivecode.depress.scm.SCMOperation;
import org.impressivecode.depress.scm.endevor.constants.EndevorLogEntryType;
import org.impressivecode.depress.scm.endevor.constants.EndevorLogKeywords;
import org.impressivecode.depress.scm.endevor.constants.EndevorParsingPhase;
import org.impressivecode.depress.scm.endevor.models.EndevorElementPathModel;
import org.impressivecode.depress.scm.endevor.models.EndevorLogEntryBase;
import org.impressivecode.depress.scm.endevor.models.EndevorLogNoSCMActionEntry;
import org.impressivecode.depress.scm.endevor.models.EndevorLogSCMActionEntry;

public class EndevorLogParser {
	
	private File logFile;
	private LinkedList<SCMDataType> parsedData;
	private LinkedList <EndevorLogEntryBase> rawEndevorData;
	
	public EndevorLogParser() {}
	
	public EndevorLogParser(File logFile) {
		this.logFile = logFile;
		this.parsedData = new LinkedList<SCMDataType>();
		this.rawEndevorData = new LinkedList<EndevorLogEntryBase>();
	}
	
	public void parseLogFile() throws FileNotFoundException, EndevorLogParserException {
		Scanner scanner = new Scanner(this.logFile, "UTF-8");
		while(scanner.hasNextLine()) {
			String currentLine = scanner.nextLine();
			if (isLineUsable(currentLine)) {
				EndevorParsingPhase phase = verifyLine(currentLine);
				switch(phase) {
					case SEARCHING:
						break;
						
					case SOURCE_INFO:
						currentLine = skipUselessLogLines(scanner);
						
						EndevorLogEntryType currentLogType = determineLogEntriesTypeUsingColumnHeadersRow(currentLine);
						EndevorLogEntryBase currentLogEntry;
						
						currentLine = skipUselessLogLines(scanner);
						
						while (isLineASourceLevelInformationData(currentLine)) {
							switch (currentLogType) {
							case NO_SCM_ACTION:
								currentLogEntry = new EndevorLogNoSCMActionEntry(currentLine);
								currentLogEntry.parseRow();
								break;
								
							case WITH_SCM_ACTION:
								currentLogEntry = new EndevorLogSCMActionEntry(currentLine);
								currentLogEntry.parseRow();
								break;
								
								default:
									throw new EndevorLogParserException("Exception while parsing Source Level Information data. Current Line: " + currentLine);
							}
							
							this.rawEndevorData.add(currentLogEntry);
							currentLine = scanner.nextLine();
						}
						
						break;
						
					case SUMMARY:
						currentLine = skipUselessLogLines(scanner);
						EndevorElementPathModel pathModel = new EndevorElementPathModel();
						
						while (	(currentLine.contains(EndevorLogKeywords.PATH_ENVIRONMENT) 
								&& currentLine.contains(EndevorLogKeywords.PATH_SYSTEM) 
								&& currentLine.contains(EndevorLogKeywords.PATH_SUBSYSTEM))
								||
								(currentLine.contains(EndevorLogKeywords.PATH_ELEMENT) 
								&& currentLine.contains(EndevorLogKeywords.PATH_TYPE) 
								&& currentLine.contains(EndevorLogKeywords.PATH_STAGEID))) {
							
							Scanner lineScanner = new Scanner(currentLine);
							while(lineScanner.hasNext()) {
								String currentPathElementName = lineScanner.next();
								String currentPathElementValue = null;
								if (currentPathElementName.contains(EndevorLogKeywords.PATH_ENVIRONMENT)) {
									currentPathElementValue = lineScanner.next();
									pathModel.setEnvironment(currentPathElementValue);
								} else if (currentPathElementName.contains(EndevorLogKeywords.PATH_SUBSYSTEM)) {
									currentPathElementValue = lineScanner.next();
									pathModel.setSubsystem(currentPathElementValue);
								} else if (currentPathElementName.contains(EndevorLogKeywords.PATH_SYSTEM)) {
									currentPathElementValue = lineScanner.next();
									pathModel.setSystem(currentPathElementValue);
								} else if (currentPathElementName.contains(EndevorLogKeywords.PATH_ELEMENT)) {
									currentPathElementValue = lineScanner.next();
									pathModel.setElement(currentPathElementValue);
								} else if (currentPathElementName.contains(EndevorLogKeywords.PATH_TYPE)) {
									currentPathElementValue = lineScanner.next();
									pathModel.setType(currentPathElementValue);
								} else if (currentPathElementName.contains(EndevorLogKeywords.PATH_STAGEID)) {
									currentPathElementValue = lineScanner.next();
									pathModel.setStageId(currentPathElementValue);
								}
							}
							
							currentLine = scanner.nextLine();
						}
						
						applyPathModelToPathlessEntries(pathModel);
						break;
				}
			}
		}
		
		convertRawEntriesToSCMFormat();
	}
	
	private boolean isLineUsable(String currentLine) {
		String step1 = currentLine.replace("-", "");
		String step2 = step1.replace("*", "");
		String trimmed = step2.trim();
		return trimmed.length() > 0;
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
	
	private String skipUselessLogLines(Scanner scanner) {
		String currentLine;
		while (!isLineUsable(currentLine = scanner.nextLine()));
		return currentLine;
	}
	
	private EndevorLogEntryType determineLogEntriesTypeUsingColumnHeadersRow(String currentLine) {
		if (currentLine.matches(EndevorLogNoSCMActionEntry.ROW_HEADER_MATCHING_PATTERN)) {
			return EndevorLogEntryType.NO_SCM_ACTION;
		}
		else if (currentLine.matches(EndevorLogSCMActionEntry.ROW_HEADER_MATCHING_PATTERN)) {
			return EndevorLogEntryType.WITH_SCM_ACTION;
		}
		else {	
			return null;
		}
	}
	
	private boolean isLineASourceLevelInformationData(String currentLine) {
		//TODO sprawdzic byc moze lepiej, czy ta linia to dane do tabeli, czyli dane do VVLL USER COSTAM CCID etc
		return currentLine.startsWith("  ");
	}
	
	private void applyPathModelToPathlessEntries(EndevorElementPathModel pathModel) {
		for(EndevorLogEntryBase rawScm : this.rawEndevorData) {
			if (rawScm.getPath() == null) {
				rawScm.setPath(pathModel.toString());
			}
		}
	}

	private void convertRawEntriesToSCMFormat() {
		for (EndevorLogEntryBase rawBase : this.rawEndevorData) {
			SCMDataType scm = new SCMDataType();
			if (rawBase instanceof EndevorLogNoSCMActionEntry) {
				EndevorLogNoSCMActionEntry rawNonBase = (EndevorLogNoSCMActionEntry)rawBase;
				rawNonBase.fillSCMDataTypeFields(scm);
			}
			if (rawBase instanceof EndevorLogSCMActionEntry) {
				EndevorLogSCMActionEntry rawNonBase = (EndevorLogSCMActionEntry)rawBase;
				rawNonBase.fillSCMDataTypeFields(scm);
			}
			
			//TODO byc moze do usuniecia, gdy bedzie joinowanie
			if(scm.getOperation() == null) {
				scm.setOperation(SCMOperation.OTHER);
			}
			
			this.parsedData.add(scm);
		}
	}

	public List<SCMDataType> getParsedCommits() throws EndevorLogParserException {
        if (this.parsedData.size() == 0) {
        	throw new EndevorLogParserException("The log file has not been parsed. Call parseLogFile() first.");
        }
        else {
        	return this.parsedData;
        }
	}	
}