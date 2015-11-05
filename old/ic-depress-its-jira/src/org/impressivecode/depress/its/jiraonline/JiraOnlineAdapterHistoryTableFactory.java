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
package org.impressivecode.depress.its.jiraonline;


import static com.google.common.base.Preconditions.checkNotNull;
import static org.impressivecode.depress.common.Cells.dateTimeCell;
import static org.impressivecode.depress.common.Cells.stringCell;
import static org.impressivecode.depress.common.Cells.stringOrMissingCell;

import org.impressivecode.depress.its.jiraonline.model.JiraOnlineIssueChangeRowItem;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.date.DateAndTimeCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;

/**
 * 
 * @author Marcin Kunert, Wroclaw University of Technology
 *
 */
public class JiraOnlineAdapterHistoryTableFactory {

    public static final String ISSUE_KEY = "Key";
    public static final String TIMESTAMP = "Timestamp";
    public static final String AUTHOR = "Author";
    public static final String FIELD = "Field";
    public static final String CHANGED_FROM = "Changed from";
    public static final String CHANGED_TO = "Changed to";

    public static final DataColumnSpec ISSUE_KEY_COLSPEC = new DataColumnSpecCreator(ISSUE_KEY, StringCell.TYPE).createSpec();
    public static final DataColumnSpec TIMESTAMP_COLSPEC = new DataColumnSpecCreator(TIMESTAMP, DateAndTimeCell.TYPE).createSpec();
    public static final DataColumnSpec AUTHOR_COLSPEC = new DataColumnSpecCreator(AUTHOR, StringCell.TYPE).createSpec();
    public static final DataColumnSpec CHANGED_FIELD_COLSPEC = new DataColumnSpecCreator(FIELD, StringCell.TYPE).createSpec();
    public static final DataColumnSpec CHANGED_FROM_COLSPEC = new DataColumnSpecCreator(CHANGED_FROM, StringCell.TYPE).createSpec();
    public static final DataColumnSpec CHANGED_TO_COLSPEC = new DataColumnSpecCreator(CHANGED_TO, StringCell.TYPE).createSpec();

    private static int rowIdCounter = 1;

    private JiraOnlineAdapterHistoryTableFactory() { }

    public static DataTableSpec createDataColumnSpec() {

        DataColumnSpec[] allColSpecs = { 
                ISSUE_KEY_COLSPEC,
                TIMESTAMP_COLSPEC,
                AUTHOR_COLSPEC,
                CHANGED_FIELD_COLSPEC,
                CHANGED_FROM_COLSPEC,
                CHANGED_TO_COLSPEC
        };

        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        return outputSpec;
    }

    public static DataRow createTableRow(final JiraOnlineIssueChangeRowItem changeRow) {
        assertData(changeRow);
        DataCell[] cells = { 
                stringCell(changeRow.getKey()),
                dateTimeCell(changeRow.getTimestamp()), 
                stringCell(changeRow.getAuthor()),
                stringCell(changeRow.getField()),
                stringOrMissingCell(changeRow.getChangedFrom()),
                stringOrMissingCell(changeRow.getChangedTo()) 
        };
        DataRow row = new DefaultRow(Integer.toString(rowIdCounter), cells);
        rowIdCounter++;
        return row;
    }

    private static void assertData(final JiraOnlineIssueChangeRowItem changeRow) {
        checkNotNull(changeRow, "Issue Tracking System data has to be set.");
        checkNotNull(changeRow.getKey(), "Issue key has to be set.");
    }

}
