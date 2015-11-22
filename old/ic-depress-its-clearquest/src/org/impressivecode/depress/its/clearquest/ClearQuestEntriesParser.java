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

import org.impressivecode.depress.its.ITSAdapterTableFactory;
import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSPriority;
import org.impressivecode.depress.its.ITSResolution;
import org.impressivecode.depress.its.ITSStatus;
import org.impressivecode.depress.its.ITSType;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
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

    public List<ITSDataType> parseEntries(BufferedDataTable table, ClearQuestUserSettings settings)
            throws ParserConfigurationException, SAXException, IOException, ParseException {
        List<ITSDataType> entries = Lists.newLinkedList();
        CloseableRowIterator itr = table.iterator();

        HashMap<String, String> itsColumnsMap = new HashMap<String, String>();
        DataTableSpec dts = ITSAdapterTableFactory.createDataColumnSpec();
        String[] itsColumnNames = dts.getColumnNames();
        for (int i = 0; i < itsColumnNames.length; i++) {
            itsColumnsMap.put(itsColumnNames[i], settings.getMapping()[i]);
        }

        columnIds = new HashMap<String, Integer>();
        for (int i = 0; i < table.getSpec().getNumColumns(); i++) {
            columnIds.put(table.getSpec().getColumnSpec(i).getName(), i);
        }

        while (itr.hasNext()) {
            entries.add(parse(itr.next(), itsColumnsMap));
        }
        return entries;
    }

    private ITSDataType parse(final DataRow row, HashMap<String, String> itsColumnsMap) {
        ITSDataType data = new ITSDataType();
        data.setIssueId(getUniqueId(row, itsColumnsMap.get(ITSAdapterTableFactory.ISSUE_ID)));
        data.setComments(getListValues(row, itsColumnsMap.get(ITSAdapterTableFactory.COMMENTS)));
        data.setCreated(getCreated(row, itsColumnsMap.get(ITSAdapterTableFactory.CREATION_DATE)));
        data.setDescription(getStringValue(getDataCell(row, itsColumnsMap.get(ITSAdapterTableFactory.DESCRIPTION))));
        data.setFixVersion(getListValues(row, itsColumnsMap.get(ITSAdapterTableFactory.FIX_VERSION)));
        data.setLink(getStringValue(getDataCell(row, itsColumnsMap.get(ITSAdapterTableFactory.LINK))));
        data.setPriority(getPriority(row, itsColumnsMap.get(ITSAdapterTableFactory.PRIORITY)));
        data.setResolved(getDateValue(row, itsColumnsMap.get(ITSAdapterTableFactory.RESOLVED_DATE)));
        data.setStatus(getStatus(row, itsColumnsMap.get(ITSAdapterTableFactory.STATUS)));
        data.setSummary(getStringValue(getDataCell(row, itsColumnsMap.get(ITSAdapterTableFactory.SUMMARY))));
        data.setType(getType(row, itsColumnsMap.get(ITSAdapterTableFactory.TYPE)));
        data.setUpdated(getDateValue(row, itsColumnsMap.get(ITSAdapterTableFactory.UPDATED_DATE)));
        data.setVersion(getListValues(row, itsColumnsMap.get(ITSAdapterTableFactory.VERSION)));
        data.setResolution(getResolution(row, itsColumnsMap.get(ITSAdapterTableFactory.RESOLUTION)));
        data.setReporter(getStringValue(getDataCell(row, itsColumnsMap.get(ITSAdapterTableFactory.REPORTER))));
        data.setAssignees(getSetValues(row, itsColumnsMap.get(ITSAdapterTableFactory.ASSIGNEES)));
        data.setCommentAuthors(getSetValues(row, itsColumnsMap.get(ITSAdapterTableFactory.COMMENT_AUTHORS)));
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
        return value == null ? new LinkedList<String>() : Arrays.asList(value.toString().split(","));
    }

    private Set<String> getSetValues(final DataRow row, String name) {
        DataCell value = getDataCell(row, name);
        return value == null ? new HashSet<String>() : new HashSet<String>(Arrays.asList(value.toString().split(",")));
    }

    private Date getDateValue(final DataRow row, String name) {
        DataCell value = getDataCell(row, name);
        return value == null ? null : parseDate(value.toString());
    }

    private Date getCreated(final DataRow row, String name) {
        DataCell value = getDataCell(row, name);
        return value == null ? new Date() : parseCreationDate(value.toString());
    }

    private Date parseDate(final String nodeValue) {
        // 29 october 2013 07:35:03 GMT+01:00
        SimpleDateFormat sdf = new SimpleDateFormat(ClearQuest_DATE_FORMAT, Locale.getDefault());
        sdf.setLenient(true);
        Date date = null;
        try {
            date = sdf.parse(nodeValue);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
        }
        return date;
    }

    private Date parseCreationDate(final String nodeValue) {
        // 29 october 2013 07:35:03 GMT+01:00
        SimpleDateFormat sdf = new SimpleDateFormat(ClearQuest_DATE_FORMAT, Locale.getDefault());
        sdf.setLenient(true);
        Date date = new Date();
        try {
            date = sdf.parse(nodeValue);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
        }
        return date;
    }

    private ITSResolution getResolution(final DataRow row, String name) {
        DataCell resolution = getDataCell(row, name);
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

    private ITSType getType(final DataRow row, String name) {
        DataCell type = getDataCell(row, name);
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

    private ITSStatus getStatus(final DataRow row, String name) {
        DataCell status = getDataCell(row, name);
        if (status == null) {
            return ITSStatus.UNKNOWN;
        }
        switch (status.toString()) {
        case "Submit":
            return ITSStatus.OPEN;
        case "Resubmit":
            return ITSStatus.REOPENED;
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

    private ITSPriority getPriority(final DataRow row, String name) {
        DataCell priority = getDataCell(row, name);
        if (priority == null) {
            return ITSPriority.UNKNOWN;
        }
        switch (priority.toString()) {
        case "5 - Enhancement":
            return ITSPriority.TRIVIAL;
        case "3 - Average":
        case "4 - Minor":
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
