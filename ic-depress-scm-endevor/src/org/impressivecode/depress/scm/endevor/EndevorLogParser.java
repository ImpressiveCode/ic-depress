package org.impressivecode.depress.scm.endevor;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.impressivecode.depress.scm.SCMDataType;
import org.impressivecode.depress.scm.SCMOperation;
import org.impressivecode.depress.scm.endevor.constants.EndevorLogKeywords;
import org.impressivecode.depress.scm.endevor.constants.EndevorParsingPhase;
import org.impressivecode.depress.scm.endevor.models.EndevorElementPathModel;

public class EndevorLogParser {
	
	private File logFile;
	private LinkedList<SCMDataType> parsedData;
	
	public EndevorLogParser() {}
	
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
						currentLine = skipUselessLogLines(scanner);
						
						String[] columnHeadersPolluted = currentLine.split(" ");
						String[] columnHeaders = removeEmptyOrUselessEntriesFromColumnHeadersOrDataArray(columnHeadersPolluted);
						
						currentLine = skipUselessLogLines(scanner);
						
						while(isLineASourceLevelInformationData(currentLine)) {
							SCMDataType newRecord = new SCMDataType();
							String[] columnDataPolluted = currentLine.split(" ");
							String[] columnData = removeEmptyOrUselessEntriesFromColumnHeadersOrDataArray(columnDataPolluted);
							for (int i = 0; i < columnData.length; i++) {
								assignSCMRecordFieldValue(columnHeaders[i], columnData[i], newRecord);
							}
							
							this.parsedData.add(newRecord);
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
						
						applyPathModelToPathlessSCMEntries(pathModel);
						break;
				}
			}
		}
		
		//TODO delete
		METHODTOREMOVE();
	}

	private void applyPathModelToPathlessSCMEntries(EndevorElementPathModel pathModel) {
		for(SCMDataType scm : this.parsedData) {
			if (scm.getPath() == null) {
				scm.setPath(pathModel.toString());
			}
		}
	}

	/**
	 * METODA WYPE£NIA PUSTE, ALE OBLIGATORYJNE POLA W SCMDataType, aby mozna bylo na biezaco testowac wtyczke
	 */
	private void METHODTOREMOVE() {
		// TODO METODA DO USUNIÊCIA PO KOÑCOWYM PARSOWANIU
		for (SCMDataType scm : this.parsedData) {
			scm.setResourceName("Endevor SCM");
			scm.setExtension(".txt");
			if (scm.getOperation() == null) {
				scm.setOperation(SCMOperation.RENAMED);
			}
		}
	}

	private String skipUselessLogLines(Scanner scanner) {
		String currentLine;
		while (!isLineSensible(currentLine = scanner.nextLine()));
		return currentLine;
	}

	private void assignSCMRecordFieldValue(String columnHeader, String columnData, SCMDataType scmRecord) {
		try {
			if (columnHeader.equals(EndevorLogKeywords.SCM_AUTHOR)) {
				scmRecord.setAuthor(columnData);
			}
			else if (columnHeader.equals(EndevorLogKeywords.SCM_DATE_DATE)) {
				if (scmRecord.getCommitDate() == null) {
					Date date = new Date(0);
					date = parseDate(columnData, date);
					scmRecord.setCommitDate(date);
				}
			} else if (columnHeader.equals(EndevorLogKeywords.SCM_DATE_TIME)) {
				Date date = scmRecord.getCommitDate();
				date = parseTime(columnData, date);
				scmRecord.setCommitDate(date);
			} else if (columnHeader.equals(EndevorLogKeywords.SCM_COMMITID)) {
				scmRecord.setCommitID(columnData);
			} else if (columnHeader.equals(EndevorLogKeywords.SCM_MESSAGE)) {
				scmRecord.setMessage(columnData);
			} else if (columnHeader.equals(EndevorLogKeywords.SCM_ACTION_DEL)) {
				if (Integer.parseInt(columnData) > 0) {
					//TODO tutaj ewentualna niescislosc -> w razie problemow, dopytac Volvo
					//TODO INSERTS/DELETES trzeba bedzie analizowaæ par¹
					scmRecord.setOperation(SCMOperation.DELETED);
				}
				else {
					scmRecord.setOperation(SCMOperation.OTHER);
				}
			} else if (columnHeader.equals(EndevorLogKeywords.SCM_ACTION_INS)) {
				if (Integer.parseInt(columnData) > 0) {
					scmRecord.setOperation(SCMOperation.ADDED);
				}
				else {
					scmRecord.setOperation(SCMOperation.OTHER);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private Date parseTime(String columnData, Date date) throws EndevorLogParserException {
		try {
			String[] time = columnData.split(":");
			int hour = Integer.parseInt(time[0]);
			int minutes = Integer.parseInt(time[1]);
			date.setHours(hour);
			date.setMinutes(minutes);
			return date;
		}
		catch (Exception e) {
			throw new EndevorLogParserException("Could not parse time: " + columnData + ".");
		}
	}

	private Date parseDate(String columnData, Date date) throws EndevorLogParserException {
		String day = columnData.substring(0, 2);
		String month = columnData.substring(2, 5);
		String year = columnData.substring(5, 7);
		
		try {
			String monthParsed = parseMonth(month);
			int yearInt = Integer.parseInt(year) > 40 ? 1900 + Integer.parseInt(year) : 2000 + Integer.parseInt(year);
			
			String yyyyMMddString = yearInt + "-" + monthParsed + "-" + day;
			SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd");
			return dateFormat.parse(yyyyMMddString);
		}
		catch (Exception e) {
			throw new EndevorLogParserException("Could not parse date value: " + columnData + ".");
		}
	}

	private String parseMonth(String month) throws EndevorLogParserException {
		if (month.equals("JAN")) {
			return "01";
		}
		else if (month.equals("FEB")) {
			return "02";
		}
		else if (month.equals("MAR")) {
			return "03";
		}
		else if (month.equals("APR")) {
			return "04";
		}
		else if (month.equals("MAY")) {
			return "05";
		}
		else if (month.equals("JUN")) {
			return "06";
		}
		else if (month.equals("JUL")) {
			return "07";
		}
		else if (month.equals("AUG")) {
			return "08";
		}
		else if (month.equals("SEP")) {
			return "09";
		}
		else if (month.equals("OCT")) {
			return "10";
		}
		else if (month.equals("NOV")) {
			return "11";
		}
		else if (month.equals("DEC")) {
			return "12";
		}
		else {
			throw new EndevorLogParserException("Could not parse month signature: " + month + ".");
		}
	}

	private boolean isLineASourceLevelInformationData(String currentLine) {
		//TODO sprawdzic byc moze lepiej, czy ta linia to dane do tabeli, czyli dane do VVLL USER COSTAM CCID etc
		return currentLine.startsWith("  ");
	}

	private String[] removeEmptyOrUselessEntriesFromColumnHeadersOrDataArray(String[] columnHeadersOrData) {
		LinkedList<String> clearedHeaders = new LinkedList<String>();
		for (String s : columnHeadersOrData) {
			if (s.length() > 0 && !s.equals(EndevorLogKeywords.SYNC)) {
				clearedHeaders.add(s);
			}
		}
		String[] clearedHeadersArray = new String[clearedHeaders.size()];
		
		return clearedHeaders.toArray(clearedHeadersArray);
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
