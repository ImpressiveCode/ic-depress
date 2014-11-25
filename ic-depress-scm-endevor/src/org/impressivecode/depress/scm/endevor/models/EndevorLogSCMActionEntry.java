package org.impressivecode.depress.scm.endevor.models;

import java.util.Scanner;

import org.impressivecode.depress.scm.SCMDataType;
import org.impressivecode.depress.scm.SCMOperation;
import org.impressivecode.depress.scm.endevor.EndevorLogParserException;
import org.impressivecode.depress.scm.endevor.constants.EndevorLogKeywords;

public class EndevorLogSCMActionEntry extends EndevorLogEntryBase {
	
	public static String ROW_HEADER_MATCHING_PATTERN = "(\\s*)" 
			+ EndevorLogKeywords.VVLL_DOTTED + "(\\s+)" 
			+ EndevorLogKeywords.SCM_AUTHOR + "(\\s+)"
			+ EndevorLogKeywords.SCM_DATE_DATE + "(\\s+)"
			+ EndevorLogKeywords.SCM_DATE_TIME + "(\\s+)"
			+ EndevorLogKeywords.STMTS + "(\\s+)"
			+ EndevorLogKeywords.SCM_ACTION_INS + "(\\s+)"
			+ EndevorLogKeywords.SCM_ACTION_DEL + "(\\s+)"
			+ EndevorLogKeywords.SCM_COMMITID + "(\\s+)";
	
	private static String[] columnHeaders = new String[] {
		EndevorLogKeywords.VVLL_DOTTED, EndevorLogKeywords.SCM_AUTHOR, EndevorLogKeywords.SCM_DATE_DATE, EndevorLogKeywords.SCM_DATE_TIME,
		EndevorLogKeywords.STMTS, EndevorLogKeywords.SCM_ACTION_INS, EndevorLogKeywords.SCM_ACTION_DEL, EndevorLogKeywords.SCM_COMMITID
	};

	private int inserts;
	private int deletes;
	private SCMOperation operation;
	
	public EndevorLogSCMActionEntry(String logRow) {
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
			if (columnHeader.equals(EndevorLogKeywords.VVLL_DOTTED)) {
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
			} else if (columnHeader.equals(EndevorLogKeywords.SCM_ACTION_INS)) {
				this.inserts = Integer.parseInt(cell);
			} else if (columnHeader.equals(EndevorLogKeywords.SCM_ACTION_DEL)) {
				this.deletes = Integer.parseInt(cell);
			} else if (columnHeader.equals(EndevorLogKeywords.SCM_COMMITID)) {
				this.ccid = cell;
			}
		}
		
		this.parseDateTime();
		this.determineSCMOperation();
	}
	
	private void determineSCMOperation() {
		if (this.inserts > this.deletes) {
			operation = SCMOperation.ADDED;
		} else if (this.deletes > this.inserts) {
			operation = SCMOperation.DELETED;
		} else {
			if (this.stmts > 0) {
				operation = SCMOperation.MODIFIED;
			}
			else {
				operation = SCMOperation.OTHER;
			}
		}
	}
	
	public void fillSCMDataTypeFields(SCMDataType toFill) {
		super.fillSCMDataTypeFields(toFill);
		toFill.setOperation(this.operation);
	}

	public int getInserts() {
		return inserts;
	}

	public int getDeletes() {
		return deletes;
	}
}
