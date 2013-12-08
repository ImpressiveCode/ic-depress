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
package org.impressivecode.depress.its.hpqc;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSPriority;
import org.impressivecode.depress.its.ITSResolution;
import org.impressivecode.depress.its.ITSStatus;
import org.impressivecode.depress.its.ITSType;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.container.CloseableRowIterator;
import org.knime.core.node.BufferedDataTable;
import org.xml.sax.SAXException;
import com.google.common.collect.Lists;

/**
 * 
 * @author Mariusz Mulka, Wrocław, Poland
 * 
 */
public class HPQCEntriesParser {
    private HashMap<String, Integer> columnIds;
    private static final String HPQC_DATE_FORMAT = "mm/dd/yy HH:mm";

    public List<ITSDataType> parseEntries(BufferedDataTable table) throws ParserConfigurationException, SAXException,
    IOException, ParseException {
        List<ITSDataType> entries = Lists.newLinkedList();
    	 CloseableRowIterator itr = table.iterator();
    	 
    	 columnIds = new HashMap<String, Integer>();
         for (int i = 0; i < table.getSpec().getNumColumns(); i++){
        	 columnIds.put(table.getSpec().getColumnSpec(i).getName(), i);
         }
         while(itr.hasNext()){
        	 entries.add(parse(itr.next()));
         }
        return entries;
    }

    private ITSDataType parse(final DataRow row) throws ParseException {
        ITSDataType data = new ITSDataType();
        data.setIssueId(getStringValue(getDataCell(row, "CQ ID")));
        String[] comments = new String[3];
        comments[0] = getStringValue(getDataCell(row, "Comments"));
        comments[1] = getStringValue(getDataCell(row, "Design Comments"));
        comments[2] = getStringValue(getDataCell(row, "Requirement Comments"));
        data.setComments(Arrays.asList(comments));
        data.setCreated(getDateValue(row, "Created"));
        data.setDescription(getStringValue(getDataCell(row, "Problem Description" )));
        data.setFixVersion(getListValues(row, "Closing Version"));
        data.setLink("");
        data.setPriority(getPriority(row));
        data.setResolved(getDateValue(row, "Closing Date"));
        data.setStatus(getStatus(row));
        data.setSummary("Description Log");
        data.setType(getType(row));
        data.setUpdated(getDateValue(row, "Modified"));
        data.setVersion(getListValues(row, "Planed Closing Version"));
        data.setResolution(getResolution(row));
        data.setReporter(getStringValue(getDataCell(row, "Submitter")));
        data.setAssignees(getSetValues(row, "Assigned To"));
        data.setCommentAuthors(new HashSet<String>(Arrays.asList(comments)));
        return data;
    }
    private DataCell getDataCell(final DataRow row, String name)
    {
    	Integer columnId =columnIds.get(name);
    	return  columnId != null ? row.getCell(columnId) : null;
    }
    private String getStringValue(final DataCell cell)
    {
    	return cell != null ? cell.toString() : "";
    }
    private List<String> getListValues(final DataRow row, String name)
    {
    	DataCell value = getDataCell(row, name);
    	return value == null  ? new LinkedList<String>() : Arrays.asList( name.split(","));
    }
    private Set<String> getSetValues(final DataRow row, String name)
    {
    	DataCell value = getDataCell(row, name);
    	return value == null  ? new HashSet<String>() : new HashSet<String>(Arrays.asList(name.split(",")));
    }
	private Date getDateValue(final DataRow row, String name)
			throws ParseException {
		DataCell value = getDataCell(row, name);
		return value == null ? new Date() : parseDate(value.toString());
	}

	private Date parseDate(final String nodeValue) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(HPQC_DATE_FORMAT,
				Locale.forLanguageTag("PL"));
		sdf.setLenient(true);
		Date date = sdf.parse(nodeValue);
		return date;
	}

    private ITSResolution getResolution(final DataRow row) {
    	DataCell resolution = getDataCell(row, "resolution");
        if (resolution == null) {
            return ITSResolution.UNKNOWN;
        }
        switch (resolution.toString()) {
        case "Unresolved":
            return ITSResolution.UNRESOLVED;
        case "Fixed":
            return ITSResolution.FIXED;
        case "Wont't Fix":
            return ITSResolution.WONT_FIX;
        case "Duplicate":
            return ITSResolution.DUPLICATE;
        case "Invalid":
            return ITSResolution.INVALID;
        case "Incomplete":
            return ITSResolution.INVALID;
        case "Cannot Reproduce":
            return ITSResolution.WONT_FIX;
        case "Later":
            return ITSResolution.WONT_FIX;
        case "Not A Problem":
            return ITSResolution.WONT_FIX;
        case "Implemented":
            return ITSResolution.FIXED;
        default:
            return ITSResolution.UNKNOWN;
        }
    }

    private ITSType getType(final DataRow row) {
    	DataCell type = getDataCell(row, "RecordType");
        if (type == null) {
            return ITSType.UNKNOWN;
        }
        switch (type.toString()) {
        case "BugReport":
            return ITSType.BUG;
        case "TestReport":
            return ITSType.TEST;
        case "ImprovementReport":
        case "New FeatureReport":
        case "TaskReport":
        case "WishReport":
            return ITSType.ENHANCEMENT;
        default:
            return ITSType.UNKNOWN;
        }
    }

    private ITSStatus getStatus(final DataRow row) {
    	DataCell status = getDataCell(row, "Status");
        if (status == null) {
            return ITSStatus.UNKNOWN;
        }
        switch (status.toString()) {
        case "New":
            return ITSStatus.OPEN;
        case "Reopen":
            return ITSStatus.REOPEN;
        case "In Progress":
            return ITSStatus.IN_PROGRESS;
        case "Resolved":
            return ITSStatus.RESOLVED;
        case "Closed":
            return ITSStatus.CLOSED;
        default:
            return ITSStatus.UNKNOWN;
        }
    }

    private ITSPriority getPriority(final DataRow row) {
    	DataCell priority = getDataCell(row, "Priority");
        if (priority == null) {
            return ITSPriority.UNKNOWN;
        }
        switch (priority.toString()) {
        case "Trivial":
            return ITSPriority.TRIVIAL;
        case "3 - Normal Queue":
            return ITSPriority.MINOR;
        case "2 - Give High Attention":
            return ITSPriority.MAJOR;
        case "1 - Resolve Immediately":
            return ITSPriority.CRITICAL;
        case "Blocker":
            return ITSPriority.BLOCKER;
        default:
            return ITSPriority.UNKNOWN;
        }
    }
}
