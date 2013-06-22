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
import static com.google.common.collect.Lists.newArrayListWithExpectedSize;

import java.util.List;
import java.util.Set;

import org.impressivecode.depress.common.DataTableSpecUtils;
import org.impressivecode.depress.common.InputTransformer;
import org.impressivecode.depress.common.TableCellReader;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowIterator;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.InvalidSettingsException;

import com.google.common.collect.Iterables;
/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class ITSInputTransformer implements InputTransformer<ITSDataType> {
    private final DataTableSpec tableSpec;

    public ITSInputTransformer(final DataTableSpec tableSpec) throws InvalidSettingsException {
        this.tableSpec = validate(tableSpec);
    }

    @Override
    public List<ITSDataType> transform(final DataTable inTable) {
        checkNotNull(inTable, "InTable has to be set");
        List<ITSDataType> issueData = newArrayListWithExpectedSize(1000);
        RowIterator iterator = inTable.iterator();
        while (iterator.hasNext()) {
            issueData.add(issue(iterator.next()));
        }
        return issueData;
    }

    private ITSDataType issue(final DataRow row) {
        TableCellReader reader = new TableCellReader(tableSpec, row);
        ITSDataType its = new ITSDataType();
        its.setIssueId(reader.string(ITSAdapterTableFactory.ISSUE_ID));
        return its;
    }

    private static DataTableSpec validate(final DataTableSpec spec) throws InvalidSettingsException {
        checkNotNull(spec, "DataTableSpec hat to be set");

        Set<String> missing = DataTableSpecUtils.findMissingColumnSubset(spec, createHistoryColumnSpec());
        if (!missing.isEmpty()) {
            throw new InvalidSettingsException("Issue data table does not contain required columns. Missing: "
                    + Iterables.toString(missing));
        }

        return spec;
    }

    private static DataTableSpec createHistoryColumnSpec() {
        DataColumnSpec spec = new DataColumnSpecCreator(ITSAdapterTableFactory.ISSUE_ID, StringCell.TYPE).createSpec();
        return new DataTableSpec(spec);
    }
}
