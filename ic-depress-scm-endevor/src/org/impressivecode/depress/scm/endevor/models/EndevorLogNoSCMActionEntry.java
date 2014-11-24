package org.impressivecode.depress.scm.endevor.models;

import org.impressivecode.depress.scm.endevor.constants.EndevorLogKeywords;

public class EndevorLogNoSCMActionEntry extends EndevorLogEntryBase {

	public static String ROW_HEADER_MATCHING_PATTERN = "(\\s*)" 
			+ EndevorLogKeywords.VVLL + "(\\s+)" 
			+ EndevorLogKeywords.SYNC + "(\\s+)" 
			+ EndevorLogKeywords.SCM_AUTHOR + "(\\s+)"
			+ EndevorLogKeywords.SCM_DATE_DATE + "(\\s+)"
			+ EndevorLogKeywords.SCM_DATE_TIME + "(\\s+)"
			+ EndevorLogKeywords.STMTS + "(\\s+)"
			+ EndevorLogKeywords.SCM_COMMITID + "(\\s+)"
			+ EndevorLogKeywords.SCM_MESSAGE + "(\\s+)";
	
	private String sync;		//TODO zweryfikowac przydatnosc tego pola
	private String comment;
	
	public EndevorLogNoSCMActionEntry(String logRow) {
		super(logRow);
	}

	@Override
	public void parseRow() {
		
	}
	
	public String getSync() {
		return sync;
	}

	public String getComment() {
		return comment;
	}

}
