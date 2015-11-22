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

import org.impressivecode.depress.scm.SCMDataType;
import org.impressivecode.depress.scm.endevor.EndevorLogParserException;
import org.impressivecode.depress.scm.endevor.constants.EndevorLogKeywords;
import org.impressivecode.depress.scm.endevor.helpers.StringHelpers;

/**
 * 
 * @author Alicja Bodys, Wroc³aw University of Technology
 * @author Mateusz Leonowicz, Wroc³aw University of Technology
 * @author Piotr Sas, Wroc³aw University of Technology
 * @author Maciej Sznurowski, Wroc³aw University of Technology
 * 
 */
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
	
	private static String[] tableHeaders = new String[] {
		EndevorLogKeywords.VVLL, 
		EndevorLogKeywords.SYNC,
		EndevorLogKeywords.SCM_AUTHOR, EndevorLogKeywords.SCM_DATE_DATE, EndevorLogKeywords.SCM_DATE_TIME,
		EndevorLogKeywords.STMTS, EndevorLogKeywords.SCM_COMMITID, EndevorLogKeywords.SCM_MESSAGE
	};
	
	private String sync;
	private String comment;
	
	private String rowStringMask;
	
	public EndevorLogNoInsertsDeletesActionEntry(String logRow, String rowStringMask) {
		super(logRow);
		
		this.rowStringMask = rowStringMask;
	}

	@Override
	public void parseRow() throws EndevorLogParserException {
		String[] arrayedLogRow = StringHelpers.applyMask(this.rawLogDataRow, this.rowStringMask);
		if (arrayedLogRow.length == tableHeaders.length) {
			String cell = null;
			String columnHeader = null;
			
			for(int i = 0; i < tableHeaders.length; i++) {
				cell = arrayedLogRow[i].trim();
				columnHeader = tableHeaders[i];
				
				if (columnHeader.equals(EndevorLogKeywords.VVLL)) {
					this.vvll = cell;
				}
				else if (columnHeader.equals(EndevorLogKeywords.SYNC)) {
					this.sync = cell;
				}
				else if (columnHeader.equals(EndevorLogKeywords.SCM_AUTHOR)) {
					this.user = cell;
				}
				else if (columnHeader.equals(EndevorLogKeywords.SCM_DATE_DATE)) {
					this.date = cell;
				}
				else if (columnHeader.equals(EndevorLogKeywords.SCM_DATE_TIME)) {
					this.time = cell;
				}
				else if (columnHeader.equals(EndevorLogKeywords.STMTS)) {
					try {
						this.stmts = Integer.parseInt(cell);
					}
					catch (NumberFormatException e) {
						this.stmts = 0;
					}
				}
				else if (columnHeader.equals(EndevorLogKeywords.SCM_COMMITID)) {
					this.ccid = cell;
				}
				else if (columnHeader.equals(EndevorLogKeywords.SCM_MESSAGE)) {
					this.comment = cell;
				}
			}
			
			this.parseDateTime();
		}
		else {
			throw new EndevorLogParserException(String.format("Parsed log row has different number of cells than column headers. Parsing row: \n%s",
					this.rawLogDataRow));
		}
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
