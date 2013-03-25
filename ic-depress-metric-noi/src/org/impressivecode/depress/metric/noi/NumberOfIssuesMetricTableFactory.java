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
package org.impressivecode.depress.metric.noi;

import static org.impressivecode.depress.common.DataTableSpecUtils.integerCell;
import static org.impressivecode.depress.common.DataTableSpecUtils.stringListOrMissingCell;

import org.impressivecode.depress.scm.SCMAdapterTableFactory;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.collection.ListCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;

import com.google.common.collect.Sets;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public final class NumberOfIssuesMetricTableFactory {

    private static final String ISSUES = "Issues";
    private static final String NUMBER_OF_ISSUES = "NumberOfIssues";
    private static final String NUMBER_OF_UNIQUE_ISSUES = "NumberOfUniqueIssues";

    private NumberOfIssuesMetricTableFactory() {

    }

    public static DataTableSpec[] createTableSpec() {
        return new DataTableSpec[] { createDataColumnSpec() };
    }

    public static DataTableSpec createDataColumnSpec() {
        DataColumnSpec[] allColSpecs = {
                new DataColumnSpecCreator(ISSUES, ListCell.getCollectionType(StringCell.TYPE)).createSpec(),
                new DataColumnSpecCreator(NUMBER_OF_ISSUES, IntCell.TYPE).createSpec(),
                new DataColumnSpecCreator(NUMBER_OF_UNIQUE_ISSUES, IntCell.TYPE).createSpec()};
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        return outputSpec;
    }

    public static DataTableSpec createHistoryColumnSpec() {
        DataTableSpec spec = SCMAdapterTableFactory.createDataColumnSpec();
        DataColumnSpec[] allColSpecs = { 
                spec.getColumnSpec(SCMAdapterTableFactory.CLASS_COLNAME),
                spec.getColumnSpec(SCMAdapterTableFactory.MARKER) };
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        return outputSpec;
    }

    public static DataRow createTableRow(final NoIMetricType noi) {
        DataCell[] cells = { 
                stringListOrMissingCell(noi.getIssues()), 
                integerCell(noi.getIssues().size()),
                integerCell(Sets.newHashSet(noi.getIssues()).size()) };
        DataRow row = new DefaultRow(noi.getClassName(), cells);
        return row;
    }
}
