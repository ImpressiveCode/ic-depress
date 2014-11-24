package org.impressivecode.depress.scm.endevor.models;

import java.util.Date;

public abstract class EndevorLogEntryBase {
	
	private String logDataRow;
	
	private String vvll;
	private String user;
	private String date;
	private Date dateParsed;
	private String time;
	private int stmts;
	private int ccid;
	
	public EndevorLogEntryBase(String logDataRow) {
		this.logDataRow = logDataRow;
	}
	
	public abstract void parseRow();
	
	public String getLogDataRow() {
		return logDataRow;
	}

	public String getVvll() {
		return vvll;
	}

	public String getUser() {
		return user;
	}
	
	public String getDate() {
		return date;
	}

	public Date getDateParsed() {
		return dateParsed;
	}

	public String getTime() {
		return time;
	}

	public int getStmts() {
		return stmts;
	}

	public int getCcid() {
		return ccid;
	}
}
