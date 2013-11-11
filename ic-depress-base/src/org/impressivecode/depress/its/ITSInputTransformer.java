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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.impressivecode.depress.common.DataTableSpecUtils;
import org.impressivecode.depress.common.InputTransformer;
import org.impressivecode.depress.common.TableCellReader;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowIterator;
import org.knime.core.node.InvalidSettingsException;

import com.google.common.collect.Iterables;
/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class ITSInputTransformer implements InputTransformer<ITSDataType> {

    private DataTableSpec minimalTableSpec;
    private DataTableSpec inputTableSpec;

    public ITSInputTransformer() {
    }

    @Override
    public List<ITSDataType> transform(final DataTable inTable) {
        checkNotNull(this.minimalTableSpec, "Minimal DataTableSpec hat to be set");
        checkNotNull(this.inputTableSpec, "Input DataTableSpec hat to be set");
        checkNotNull(inTable, "InTable has to be set");
        List<ITSDataType> issueData = newArrayListWithExpectedSize(1000);
        RowIterator iterator = inTable.iterator();
        while (iterator.hasNext()) {
            issueData.add(transformRow(iterator.next()));
        }
        return issueData;
    }

    public ITSDataHolder transformToDataHolder(final DataTable inTable) {
        return new ITSDataHolder(transform(inTable));
    }

    @Override
    public ITSDataType transformRow(final DataRow row) {
        TableCellReader reader = new TableCellReader(this.inputTableSpec, row);
        ITSDataType its = new ITSDataType();
        //add additional if required
        its.setIssueId(reader.stringOptional(ITSAdapterTableFactory.ISSUE_ID));
        its.setResolved(reader.dateOptional(ITSAdapterTableFactory.RESOLVED_DATE));
        its.setAssignees(reader.stringSetOptional(ITSAdapterTableFactory.ASSIGNEES));
        its.setReporter(reader.stringOptional(ITSAdapterTableFactory.REPORTER));
        its.setCommentAuthors(reader.stringSetOptional(ITSAdapterTableFactory.COMMENT_AUTHORS));
        its.setResolution(readResolution(reader));
        its.setDescription(reader.stringOptional(ITSAdapterTableFactory.DESCRIPTION));
        its.setSummary(reader.stringOptional(ITSAdapterTableFactory.SUMMARY));
        its.setComments(reader.stringListOptional(ITSAdapterTableFactory.COMMENTS));

        //add check if minimaldata set requires column for large data e.g. comments or description
        return its;
    }

    private ITSResolution readResolution(final TableCellReader reader) {
        String resolution = reader.stringOptional(ITSAdapterTableFactory.RESOLUTION);
        if(resolution == null){
            return null;
        }else {
            return ITSResolution.valueOf(resolution);
        }
    }

    @Override
    public InputTransformer<ITSDataType> validate() throws InvalidSettingsException {
        checkNotNull(this.minimalTableSpec, "Minimal DataTableSpec hat to be set");
        checkNotNull(this.inputTableSpec, "Input DataTableSpec hat to be set");

        Set<String> missing = DataTableSpecUtils.findMissingColumnSubset(this.inputTableSpec, this.minimalTableSpec);
        if (!missing.isEmpty()) {
            throw new InvalidSettingsException("History data table does not contain required columns. Missing: "
                    + Iterables.toString(missing));
        }
        return this;
    }

    @Override
    public InputTransformer<ITSDataType> setMinimalSpec(final DataTableSpec spec) {
        this.minimalTableSpec = spec;
        return this;
    }

    @Override
    public InputTransformer<ITSDataType> setInputSpec(final DataTableSpec spec) {
        this.inputTableSpec = spec;
        return this;
    }

    @Override
    public InputTransformer<ITSDataType> setMinimalOrSpec(final DataColumnSpec[] orSpec) {
        throw new IllegalStateException("Unsupported operation");
    }
}
