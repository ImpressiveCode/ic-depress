package org.impressivecode.depress.scm.endevor.models;

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

	private int inserts;
	private int deletes;
	
	public EndevorLogSCMActionEntry(String logRow) {
		super(logRow);
	}

	@Override
	public void parseRow() {
		// TODO Auto-generated method stub
		
	}

	public int getInserts() {
		return inserts;
	}

	public int getDeletes() {
		return deletes;
	}
}
