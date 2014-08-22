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
package org.impressivecode.depress.its;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.impressivecode.depress.common.Cells.dateTimeCell;
import static org.impressivecode.depress.common.Cells.dateTimeOrMissingCell;
import static org.impressivecode.depress.common.Cells.stringCell;
import static org.impressivecode.depress.common.Cells.stringListOrMissingCell;
import static org.impressivecode.depress.common.Cells.stringOrMissingCell;
import static org.impressivecode.depress.common.Cells.stringSetCell;
import static org.impressivecode.depress.common.Cells.integerOrMissingCell;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.collection.ListCell;
import org.knime.core.data.collection.SetCell;
import org.knime.core.data.date.DateAndTimeCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;

/**
 * @author Marek Majchrzak, ImpressiveCode
 * @author Michał Negacz, Wrocław University of Technology
 * @author Maciej Borkowski, Capgemini Poland
 */
public class ITSAdapterTableFactory {

    public static final String ISSUE_ID = "ID";
    public static final String CREATION_DATE = "Created";
    public static final String RESOLVED_DATE = "Resolved";
    public static final String UPDATED_DATE = "Updated";
    public static final String RESOLUTION = "Resolution";
    public static final String STATUS = "Status";
    public static final String TYPE = "Type";
    public static final String VERSION = "Version";
    public static final String FIX_VERSION = "FixVersion";
    public static final String PRIORITY = "Priority";
    public static final String SUMMARY = "Summary";
    public static final String LINK = "Link";
    public static final String DESCRIPTION = "Description";
    public static final String COMMENTS = "Comments";
    public static final String REPORTER = "Reporter";
    public static final String ASSIGNEES = "Assignees";
    public static final String COMMENT_AUTHORS = "CommentAuthors";
    public static final String TIME_ESTIMATE = "Time Estimate [minutes]";
    public static final String TIME_SPENT = "Time Spent [minutes]";

    public static final DataColumnSpec ISSUE_ID_COLSPEC = new DataColumnSpecCreator(ISSUE_ID, StringCell.TYPE).createSpec();
    public static final DataColumnSpec RESOLVED_DATE_COLSPEC = new DataColumnSpecCreator(RESOLVED_DATE, DateAndTimeCell.TYPE).createSpec();
    public static final DataColumnSpec REPORTER_COLSPEC = new DataColumnSpecCreator(REPORTER, StringCell.TYPE).createSpec();
    public static final DataColumnSpec ASSIGNEES_COLSPEC = new DataColumnSpecCreator(ASSIGNEES, SetCell.getCollectionType(StringCell.TYPE)).createSpec();
    public static final DataColumnSpec COMMENT_AUTHORS_COLSPEC = new DataColumnSpecCreator(COMMENT_AUTHORS, SetCell.getCollectionType(StringCell.TYPE)).createSpec();
    public static final DataColumnSpec RESOLUTION_COLSPEC = new DataColumnSpecCreator(RESOLUTION, StringCell.TYPE).createSpec();

    protected ITSAdapterTableFactory() {
    }

    public static DataTableSpec createDataColumnSpec() {
        DataColumnSpec[] allColSpecs = { 
            ISSUE_ID_COLSPEC,
            new DataColumnSpecCreator(CREATION_DATE, DateAndTimeCell.TYPE).createSpec(),
            RESOLVED_DATE_COLSPEC, //should be a list or new column wit all updates
            new DataColumnSpecCreator(UPDATED_DATE, DateAndTimeCell.TYPE).createSpec(), //should be a list or new column wit all updates
            new DataColumnSpecCreator(TIME_ESTIMATE, IntCell.TYPE).createSpec(),
            new DataColumnSpecCreator(TIME_SPENT, IntCell.TYPE).createSpec(),
            new DataColumnSpecCreator(STATUS, StringCell.TYPE).createSpec(),
            new DataColumnSpecCreator(TYPE, StringCell.TYPE).createSpec(),
            RESOLUTION_COLSPEC,
            new DataColumnSpecCreator(VERSION, ListCell.getCollectionType(StringCell.TYPE)).createSpec(),
            new DataColumnSpecCreator(FIX_VERSION, ListCell.getCollectionType(StringCell.TYPE)).createSpec(),
            new DataColumnSpecCreator(PRIORITY, StringCell.TYPE).createSpec(),
            new DataColumnSpecCreator(SUMMARY, StringCell.TYPE).createSpec(),
            REPORTER_COLSPEC, 
            ASSIGNEES_COLSPEC, 
            COMMENT_AUTHORS_COLSPEC,
            new DataColumnSpecCreator(LINK, StringCell.TYPE).createSpec(),
            new DataColumnSpecCreator(DESCRIPTION, StringCell.TYPE).createSpec(),
            new DataColumnSpecCreator(COMMENTS, ListCell.getCollectionType(StringCell.TYPE)).createSpec()};
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        return outputSpec;
    }

    public static DataRow createTableRow(final ITSDataType itsData) {
        assertData(itsData);
        DataCell[] cells = { 
            stringCell(itsData.getIssueId()),
            dateTimeCell(itsData.getCreated()), 
            dateTimeOrMissingCell(itsData.getResolved()),
            dateTimeOrMissingCell(itsData.getUpdated()), 
            integerOrMissingCell(itsData.getTimeEstimate()),
            integerOrMissingCell(itsData.getTimeSpent()),
            stringOrMissingCell(itsData.getStatus()),
            stringOrMissingCell(itsData.getType()), 
            stringOrMissingCell(itsData.getResolution()),
            stringListOrMissingCell(itsData.getVersion()), 
            stringListOrMissingCell(itsData.getFixVersion()),
            stringOrMissingCell(itsData.getPriority()), 
            stringOrMissingCell(itsData.getSummary()),
            stringOrMissingCell(itsData.getReporter()),
            stringSetCell(itsData.getAssignees()),
            stringSetCell(itsData.getCommentAuthors()),
            stringOrMissingCell(itsData.getLink()), 
            stringOrMissingCell(itsData.getDescription()),
            stringListOrMissingCell(itsData.getComments())
        };
        DataRow row = new DefaultRow(itsData.getIssueId(), cells);
        return row;
    }

    protected static void assertData(final ITSDataType itsData) {
        checkNotNull(itsData, "Issue Tracking System data has to be set.");
        checkNotNull(itsData.getIssueId(), "BugId has to be set.");
    }
}
