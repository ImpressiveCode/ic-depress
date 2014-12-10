package org.impressivecode.depress.scm.endevor.models;

import java.util.Scanner;

import org.impressivecode.depress.scm.SCMDataType;
import org.impressivecode.depress.scm.endevor.EndevorLogParserException;
import org.impressivecode.depress.scm.endevor.constants.EndevorLogKeywords;

public class EndevorLogNoInsertsDeletesActionEntry extends EndevorLogEntryBase {

	public static String ROW_HEADER_MATCHING_PATTERN = "(\\s*)" 
			+ EndevorLogKeywords.VVLL + "(\\s+)" 
			+ EndevorLogKeywords.SYNC + "(\\s+)" 
			+ EndevorLogKeywords.SCM_AUTHOR + "(\\s+)"
			+ EndevorLogKeywords.SCM_DATE_DATE + "(\\s+)"
			+ EndevorLogKeywords.SCM_DATE_TIME + "(\\s+)"
			+ EndevorLogKeywords.STMTS + "(\\s+)"
			+ EndevorLogKeywords.SCM_COMMITID + "(\\s+)"
			+ EndevorLogKeywords.SCM_MESSAGE + "(\\s+)";
	
	private static String[] columnHeaders = new String[] {
		EndevorLogKeywords.VVLL, 
		//EndevorLogKeywords.SYNC,	//TODO tymczasowo, bo wiemy ze u nas to jest zawsze puste
		EndevorLogKeywords.SCM_AUTHOR, EndevorLogKeywords.SCM_DATE_DATE, EndevorLogKeywords.SCM_DATE_TIME,
		EndevorLogKeywords.STMTS, EndevorLogKeywords.SCM_COMMITID, EndevorLogKeywords.SCM_MESSAGE
	};
	
	private String sync;		//TODO zweryfikowac przydatnosc tego pola
	private String comment;
	
	public EndevorLogNoInsertsDeletesActionEntry(String logRow) {
		super(logRow);
	}

	@Override
	public void parseRow() throws EndevorLogParserException {
		Scanner scanner = new Scanner(rawLogDataRow);
		String cell;
		String columnHeader;
		int i = -1;
		while(scanner.hasNext()) {
			i++;
			cell = scanner.next();
			columnHeader = columnHeaders[i];
			if (columnHeader.equals(EndevorLogKeywords.VVLL)) {
				this.vvll = cell;
			}
			else if (columnHeader.equals(EndevorLogKeywords.SCM_AUTHOR)) {
				this.user = cell;
			}
			else if (columnHeader.equals(EndevorLogKeywords.SCM_DATE_DATE)) {
				this.date = cell;
			} else if (columnHeader.equals(EndevorLogKeywords.SCM_DATE_TIME)) {
				this.time = cell;
			} else if (columnHeader.equals(EndevorLogKeywords.STMTS)) {
				this.stmts = Integer.parseInt(cell);
			} else if (columnHeader.equals(EndevorLogKeywords.SCM_COMMITID)) {
				this.ccid = cell;
			} else if (columnHeader.equals(EndevorLogKeywords.SCM_MESSAGE)) {
				this.comment = cell;
			}
		}
		
		this.parseDateTime();
	}
	
	public void fillSCMDataTypeFields(SCMDataType toFill) {
		super.fillSCMDataTypeFields(toFill);
		toFill.setMessage(this.comment);
	}
	
	public String getSync() {
		return sync;
	}

	public String getComment() {
		return comment;
	}

}
