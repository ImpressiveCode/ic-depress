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
package org.impressivecode.depress.its.clearquest;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
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
import org.knime.core.util.MutableInteger;
import org.xml.sax.SAXException;

import com.google.common.collect.Lists;

/**
 * 
 * @author Łukasz Leśniczek, Wrocław, Poland
 * 
 */
public class ClearQuestEntriesParser {
    private static final String ClearQuest_DATE_FORMAT = "dd MMMM yyyy HH:mm:ss z";
    private static final Integer NOSUFFIX = new Integer(0);

    private final Hashtable<String, Number> m_rowIDhash = new Hashtable<String, Number>();

    private HashMap<String, Integer> columnIds;

    public List<ITSDataType> parseEntries(BufferedDataTable table) throws ParserConfigurationException, SAXException,
            IOException, ParseException {
        List<ITSDataType> entries = Lists.newLinkedList();
        CloseableRowIterator itr = table.iterator();

        columnIds = new HashMap<String, Integer>();
        for (int i = 0; i < table.getSpec().getNumColumns(); i++) {
            columnIds.put(table.getSpec().getColumnSpec(i).getName(), i);
        }

        while (itr.hasNext()) {
            entries.add(parse(itr.next()));
        }
        return entries;
    }

    private ITSDataType parse(final DataRow row) throws ParseException {
        ITSDataType data = new ITSDataType();
        data.setIssueId(getUniqueId(row, "id"));
        data.setComments(getListValues(row, "Title"));
        data.setCreated(getDateValue(row, "SubmissionDate"));
        data.setDescription(getStringValue(getDataCell(row, "Description")));
        data.setFixVersion(getListValues(row, "CorrectedInBuild"));
        data.setLink("");
        data.setPriority(getPriority(row));
        data.setResolved(getDateValue(row, ""));
        data.setStatus(getStatus(row));
        data.setSummary("");
        data.setType(getType(row));
        data.setUpdated(getDateValue(row, ""));
        data.setVersion(getListValues(row, "FoundInBuild"));
        data.setResolution(getResolution(row));
        data.setReporter(getStringValue(getDataCell(row, "Submitter")));
        data.setAssignees(getSetValues(row, ""));
        data.setCommentAuthors(getSetValues(row, ""));
        return data;
    }

    private DataCell getDataCell(final DataRow row, String name) {
        Integer columnId = columnIds.get(name);
        return columnId != null ? row.getCell(columnId) : null;
    }

    private String getStringValue(final DataCell cell) {
        return cell != null ? cell.toString() : "";
    }

    private String getUniqueId(final DataRow row, String name) {
        DataCell cell = getDataCell(row, name);
        String value = getStringValue(cell);
        String uniqueId = uniquifyRowId(value);

        return uniqueId;
    }

    private String uniquifyRowId(final String newRowHeader) {

        Number oldSuffix = m_rowIDhash.put(newRowHeader, NOSUFFIX);

        if (oldSuffix == null) {
            // haven't seen the rowID so far.
            return newRowHeader;
        }

        String result = newRowHeader;
        while (oldSuffix != null) {

            // we have seen this rowID before!
            int idx = oldSuffix.intValue();

            assert idx >= NOSUFFIX.intValue();

            idx++;

            if (oldSuffix == NOSUFFIX) {
                // until now the NOSUFFIX placeholder was in the hash
                assert idx - 1 == NOSUFFIX.intValue();
                m_rowIDhash.put(result, new MutableInteger(idx));
            } else {
                assert oldSuffix instanceof MutableInteger;
                ((MutableInteger) oldSuffix).inc();
                assert idx == oldSuffix.intValue();
                // put back the old (incr.) suffix (overridden with NOSUFFIX).
                m_rowIDhash.put(result, oldSuffix);
            }

            result = result + "_" + idx;
            oldSuffix = m_rowIDhash.put(result, NOSUFFIX);

        }

        return result;
    }

    private List<String> getListValues(final DataRow row, String name) {
        DataCell value = getDataCell(row, name);
        return value == null ? new LinkedList<String>() : Arrays.asList(name.split(","));
    }

    private Set<String> getSetValues(final DataRow row, String name) {
        DataCell value = getDataCell(row, name);
        return value == null ? new HashSet<String>() : new HashSet<String>(Arrays.asList(name.split(",")));
    }

    private Date getDateValue(final DataRow row, String name) throws ParseException {
        DataCell value = getDataCell(row, name);
        return value == null ? new Date() : parseDate(value.toString());
    }

    private Date parseDate(final String nodeValue) throws ParseException {
        // 29 october 2013 07:35:03 GMT+01:00
        SimpleDateFormat sdf = new SimpleDateFormat(ClearQuest_DATE_FORMAT, Locale.US);
        sdf.setLenient(true);
        Date date = sdf.parse(nodeValue);
        return date;
    }

    private ITSResolution getResolution(final DataRow row) {
        DataCell resolution = getDataCell(row, "action_name");
        if (resolution == null) {
            return ITSResolution.UNKNOWN;
        }
        switch (resolution.toString()) {
        case "Start_Correction":
        case "Submit":
        case "Modify":
        case "Assign":
        case "Resubmit":
        case "Prep_For_Acceptance_Test":
        case "Prepare_For_System_Test":
        case "Set_Priority":
            return ITSResolution.UNRESOLVED;
        case "System_Test_Failed":
            return ITSResolution.WONT_FIX;
        case "To_Duplicate":
        case "Duplicate":
        case "Confirm_Duplicate":
            return ITSResolution.DUPLICATE;
        case "Reject":
        case "Change_By_Admin":
            return ITSResolution.INVALID;
        case "Solution_Implemented":
        case "System_Test_Passed":
        case "Finish_Correction":
            return ITSResolution.FIXED;
        default:
            return ITSResolution.UNKNOWN;
        }
    }

    private ITSType getType(final DataRow row) {
        DataCell type = getDataCell(row, "record_type");
        if (type == null) {
            return ITSType.UNKNOWN;
        }
        switch (type.toString()) {
        case "BugReport":
            return ITSType.BUG;
        default:
            return ITSType.UNKNOWN;
        }
    }

    private ITSStatus getStatus(final DataRow row) {
        DataCell status = getDataCell(row, "action_name");
        if (status == null) {
            return ITSStatus.UNKNOWN;
        }
        switch (status.toString()) {
        case "Submit":
            return ITSStatus.OPEN;
        case "Resubmit":
            return ITSStatus.REOPEN;
        case "Start_Correction":
        case "Set_Priority":
        case "Assign":
        case "Modify":
        case "Change_By_Admin":
        case "System_Test_Failed":
            return ITSStatus.IN_PROGRESS;
        case "Prep_For_Acceptance_Test":
        case "Prepare_For_System_Test":
        case "Finish_Correction":
            return ITSStatus.RESOLVED;
        case "Solution_Implemented":
        case "System_Test_Passed":
        case "Reject":
        case "To_Duplicate":
        case "Duplicate":
        case "Confirm_Duplicate":
            return ITSStatus.CLOSED;
        default:
            return ITSStatus.UNKNOWN;
        }
    }

    private ITSPriority getPriority(final DataRow row) {
        DataCell priority = getDataCell(row, "Severity");
        if (priority == null) {
            return ITSPriority.UNKNOWN;
        }
        switch (priority.toString()) {
        case "4 - Minor":
            return ITSPriority.TRIVIAL;
        case "3 - Average":
            return ITSPriority.MINOR;
        case "2 - Major":
            return ITSPriority.MAJOR;
        case "1 - Critical":
            return ITSPriority.CRITICAL;
        default:
            return ITSPriority.UNKNOWN;
        }
    }
}
