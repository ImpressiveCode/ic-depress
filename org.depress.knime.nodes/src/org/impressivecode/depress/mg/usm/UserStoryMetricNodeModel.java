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
package org.impressivecode.depress.mg.usm;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;

import org.impressivecode.depress.common.Cells;
import org.impressivecode.depress.its.common.ITSAdapterTableFactory;
import org.impressivecode.depress.its.common.ITSDataType;
import org.impressivecode.depress.its.common.ITSInputTransformer;
import org.knime.base.data.append.column.AppendedCellFactory;
import org.knime.base.data.append.column.AppendedColumnTable;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.google.common.base.Preconditions;

/**
 * 
 * @author Artur Kondziela, Capgemini Polska
 * 
 */
public class UserStoryMetricNodeModel extends NodeModel {

    protected UserStoryMetricNodeModel() {
        super(1, 1);
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {

        AppendedColumnTable table = new AppendedColumnTable(inData[0], cellFactory(inData[0]), UserStoryMetricAdapterTableFactory.COL_SPEC);

        return new BufferedDataTable[] { preapreTable(table, exec) };
    }

    private CellFactory cellFactory(final BufferedDataTable inData) {
        return new CellFactory();
    }

    private BufferedDataTable preapreTable(final AppendedColumnTable table, final ExecutionContext exec)
            throws CanceledExecutionException {
        return exec.createBufferedDataTable(table, exec);
    }

    @Override
    protected void reset() {
        // NOOP
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        Preconditions.checkArgument(inSpecs.length == 1);
        validate(inSpecs[0]);

        final DataTableSpec dts = AppendedColumnTable.getTableSpec(inSpecs[0], UserStoryMetricAdapterTableFactory.COL_SPEC);

        return new DataTableSpec[] { dts };
    }

    private void validate(final DataTableSpec spec) throws InvalidSettingsException {
        checkNotNull(spec, "DataTableSpec hat to be set");
        new ITSInputTransformer().setMinimalSpec(new DataTableSpec(ITSAdapterTableFactory.COMMENTS)).setInputSpec(spec).validate();
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        // Do nothing
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        // Do nothing
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        // Do nothing
    }

    @Override
    protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
    CanceledExecutionException {
        // NOOP
    }

    @Override
    protected void saveInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
    CanceledExecutionException {
        // NOOP
    }

    public class CellFactory implements AppendedCellFactory {

        @Override
        public DataCell[] getAppendedCell(final DataRow row) {

            ITSDataType data = new ITSInputTransformer()
            		.setInputSpec(ITSAdapterTableFactory.createDataColumnSpec())
            		.transformRow(row);
            
            Integer commentsCount = (Integer) new NumberOfCommentsMetricProcessor().computeMetric(data);
            Integer attachmentCount = (Integer) new NumberOfAttachmentsMetricProcessor().computeMetric(data);
            
            return new DataCell[] { 
            		Cells.integerCell(commentsCount), 
            		Cells.integerCell(attachmentCount) 
            	};            
        }
    }
}
