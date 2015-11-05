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
package org.impressivecode.depress.mg.ipa;

import static com.google.common.collect.Sets.newHashSet;
import static org.impressivecode.depress.common.Cells.integerCell;
import static org.impressivecode.depress.common.Cells.stringListOrMissingCell;
import java.util.List;

import org.impressivecode.depress.its.ITSDataType;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.collection.ListCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public final class IssuesMetricTableFactory {

    private static final String ISSUES = "Issues";
    private static final String NUMBER_OF_ISSUES = "NumberOfIssues";
    private static final String NUMBER_OF_UNIQUE_ISSUES = "NumberOfUniqueIssues";
    private static final String FOUND_ANY = "HasIssues";

    private IssuesMetricTableFactory() {

    }

    public static DataTableSpec[] createTableSpec() {
        return new DataTableSpec[] { createDataColumnSpec() };
    }

    public static DataTableSpec createDataColumnSpec() {
        DataColumnSpec[] allColSpecs = {
                new DataColumnSpecCreator(ISSUES, ListCell.getCollectionType(StringCell.TYPE)).createSpec(),
                new DataColumnSpecCreator(NUMBER_OF_ISSUES, IntCell.TYPE).createSpec(),
                new DataColumnSpecCreator(NUMBER_OF_UNIQUE_ISSUES, IntCell.TYPE).createSpec(),
                new DataColumnSpecCreator(FOUND_ANY, IntCell.TYPE).createSpec()};
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        return outputSpec;
    }

    public static DataRow  createTableRow(final IssuesMetricType noi) {
       
        List<String> ids = transform(noi.getIssues());

        DataRow row;
        DataCell[] cells = { 
                stringListOrMissingCell(ids), 
                integerCell(ids.size()),
                integerCell(newHashSet(ids).size()),
                integerCell(!ids.isEmpty()) 
                };
        row = new DefaultRow(noi.getResourceName(), cells);
        return row;
    }

    private static List<String> transform(final List<ITSDataType> issues) {
        return Lists.transform(issues, new Function<ITSDataType, String>() {
            @Override
            public String apply(final ITSDataType its) {
                return its.getIssueId();
            }
        });
    }
}
