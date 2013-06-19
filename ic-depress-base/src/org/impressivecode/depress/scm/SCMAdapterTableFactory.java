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
package org.impressivecode.depress.scm;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.impressivecode.depress.common.Cells.dateTimeCell;
import static org.impressivecode.depress.common.Cells.stringCell;
import static org.impressivecode.depress.common.Cells.stringOrMissingCell;
import static org.impressivecode.depress.common.Cells.stringSetCell;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.collection.SetCell;
import org.knime.core.data.date.DateAndTimeCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class SCMAdapterTableFactory {
    public static final String MARKER = "Marker";
    public static final String EXT_MARKER = "Marker(+)";
    public static final String AM_MARKER = "Marker(a)";

    public static final String AUTHOR_COLNAME = "Author";
    public static final String RESOURCE_NAME = "Class";
    public static final DataColumnSpec RESOURCE_COLSPEC = new DataColumnSpecCreator(RESOURCE_NAME, StringCell.TYPE).createSpec();
    public final static String ACTION_COLNAME = "Action";
    public final static String MESSAGE_COLNAME = "Message";
    public final static String PATH_COLNAME = "Path";
    public final static String DATE_COLNAME = "Date";
    public final static String UID_COLNAME = "CommitID";

    public final static String EXT_CONFIDENCE_COLNAME = "Confidence(+)";
    public final static String AM_CONFIDENCE_COLNAME = "Confidence(a)";

    public static final DataColumnSpec MARKER_COLSPEC = new DataColumnSpecCreator(MARKER,
            SetCell.getCollectionType(StringCell.TYPE)).createSpec();

    public static final DataColumnSpec EXT_MARKER_COLSPEC = new DataColumnSpecCreator(EXT_MARKER,
            SetCell.getCollectionType(StringCell.TYPE)).createSpec();

    public static final DataColumnSpec EXT_CONFIDENCE_COLSPEC = new DataColumnSpecCreator(EXT_CONFIDENCE_COLNAME,
            IntCell.TYPE).createSpec();

    public static final DataColumnSpec AM_MARKER_COLSPEC = new DataColumnSpecCreator(AM_MARKER,
            StringCell.TYPE).createSpec();

    public static final DataColumnSpec AM_CONFIDENCE_COLSPEC = new DataColumnSpecCreator(AM_CONFIDENCE_COLNAME,
            IntCell.TYPE).createSpec();

    public static final DataColumnSpec MESSAGE_COLSPEC = new DataColumnSpecCreator(MESSAGE_COLNAME, StringCell.TYPE)
    .createSpec();

    public static DataTableSpec[] createTableSpec() {
        return new DataTableSpec[] { SCMAdapterTableFactory.createDataColumnSpec() };
    }

    public static DataTableSpec createDataColumnSpec() {
        DataColumnSpec[] allColSpecs = { 
                RESOURCE_COLSPEC,
                MARKER_COLSPEC,
                new DataColumnSpecCreator(AUTHOR_COLNAME, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(ACTION_COLNAME, StringCell.TYPE).createSpec(), 
                MESSAGE_COLSPEC,
                new DataColumnSpecCreator(PATH_COLNAME, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(DATE_COLNAME, DateAndTimeCell.TYPE).createSpec(),
                new DataColumnSpecCreator(UID_COLNAME, StringCell.TYPE).createSpec() };
        return new DataTableSpec(allColSpecs);
    }

    public static DataRow createTableRow(final String rowId, final SCMDataType scmData) {
        assertData(scmData);
        DataCell[] cells = { stringCell(scmData.getResourceName()), stringSetCell(scmData.getMarkers()),
                stringCell(scmData.getAuthor()), stringCell(scmData.getOperation()),
                stringOrMissingCell(scmData.getMessage()), stringCell(scmData.getPath()),
                dateTimeCell(scmData.getCommitDate()), stringCell(scmData.getCommitID()), };
        DataRow row = new DefaultRow(rowId, cells);
        return row;
    }

    private static void assertData(final SCMDataType scmData) {
        checkNotNull(scmData, "Issue Tracking System data has to be set.");
        checkNotNull(scmData.getResourceName(), "Resource has to be set.");
        checkNotNull(scmData.getAuthor(), "Author has to be set.");
        checkNotNull(scmData.getOperation(), "Operation has to be set.");
        checkNotNull(scmData.getPath(), "Path has to be set.");
        checkNotNull(scmData.getCommitDate(), "CommitDate has to be set.");
        checkNotNull(scmData.getCommitID(), "CommitId has to be set.");
    }
}
