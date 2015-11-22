/*
ImpressiveCode Depress Framework
Copyright (C) 2013  ImpressiveCode contributors

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.impressivecode.depress.scm.endevor.models;

import java.util.Date;

import org.impressivecode.depress.scm.SCMDataType;
import org.impressivecode.depress.scm.SCMOperation;
import org.impressivecode.depress.scm.endevor.EndevorLogParserException;

/**
 * 
 * @author Alicja Bodys, Wroc³aw University of Technology
 * @author Mateusz Leonowicz, Wroc³aw University of Technology
 * @author Piotr Sas, Wroc³aw University of Technology
 * @author Maciej Sznurowski, Wroc³aw University of Technology
 * 
 */
public abstract class EndevorLogEntryBase {
	
	protected String rawLogDataRow;
	
	protected String vvll;
	protected String user;
	protected String date;
	protected Date dateParsed;
	protected String time;
	protected int stmts;
	protected String ccid;
	protected String path;
	
	public EndevorLogEntryBase(String logDataRow) {
		this.rawLogDataRow = logDataRow;
		this.dateParsed = new Date(0);
	}
	
	public abstract void parseRow() throws EndevorLogParserException;
	
	protected void fillSCMDataTypeFields(SCMDataType toFill) {
		toFill.setAuthor(this.user);
		toFill.setCommitDate(this.dateParsed);
		toFill.setCommitID(this.ccid);
		toFill.setPath(this.path);
		toFill.setExtension("?");
		toFill.setResourceName("Endevor SCM");
		
		if(this.stmts > 0) {
			toFill.setOperation(SCMOperation.MODIFIED);
		}
		else if (this.stmts == 0) {
			toFill.setOperation(SCMOperation.COPIED);
		}
		else {
			toFill.setOperation(SCMOperation.OTHER);
		}
	}
	
	@SuppressWarnings("deprecation")
	protected void parseDateTime() throws EndevorLogParserException {
		try {
			int day = Integer.parseInt(this.date.substring(0, 2));
			String month = this.date.substring(2, 5);
			String year = this.date.substring(5, 7);
			
			int monthInt = parseMonth(month);
			int yearInt = (Integer.parseInt(year) > 40 ? 1900 + Integer.parseInt(year) : 2000 + Integer.parseInt(year)) - 1900;
			String[] time = this.time.split(":");
			
			int hour = Integer.parseInt(time[0]);
			int minutes = Integer.parseInt(time[1]);
			
			long utc = Date.UTC(yearInt, monthInt, day, hour, minutes, 0);
			this.dateParsed.setTime(utc);
		}
		catch (Exception e) {
			throw new EndevorLogParserException(String.format("\nCould not parse the datetime present in log file: %s, %s.\n" +
					"Raw row data:\n%s", this.date, this.time, this.rawLogDataRow));
		}
	}
	
	private int parseMonth(String month) throws EndevorLogParserException {	
		if (month.toUpperCase().equals("JAN")) {
			return 0;
		}
		else if (month.toUpperCase().equals("FEB")) {
			return 1;
		}
		else if (month.toUpperCase().equals("MAR")) {
			return 2;
		}
		else if (month.toUpperCase().equals("APR")) {
			return 3;
		}
		else if (month.toUpperCase().equals("MAY")) {
			return 4;
		}
		else if (month.toUpperCase().equals("JUN")) {
			return 5;
		}
		else if (month.toUpperCase().equals("JUL")) {
			return 6;
		}
		else if (month.toUpperCase().equals("AUG")) {
			return 7;
		}
		else if (month.toUpperCase().equals("SEP")) {
			return 8;
		}
		else if (month.toUpperCase().equals("OCT")) {
			return 9;
		}
		else if (month.toUpperCase().equals("NOV")) {
			return 10;
		}
		else if (month.toUpperCase().equals("DEC")) {
			return 11;
		}
		else {
			throw new EndevorLogParserException("Could not parse month signature: " + month + ".");
		}
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public String getRawLogDataRow() {
		return rawLogDataRow;
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

	public String getCcid() {
		return ccid;
	}
	
	
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
}
