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
package org.impressivecode.depress.metric.jacoco;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.impressivecode.depress.common.DataTableSpecUtils.doubleOrMissingCell;

import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeLogger.LEVEL;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class JaCoCoAdapterTransformer {

    private static final NodeLogger LOGGER = NodeLogger.getLogger(JaCoCoAdapterTransformer.class);

    private final DataTableSpec tableSpec;

    public JaCoCoAdapterTransformer(final DataTableSpec tableSpec) {
        checkNotNull(tableSpec, "table specifikation can not be null.");
        this.tableSpec = tableSpec;
    }

    public BufferedDataTable transform(final List<JaCoCoEntry> jacocodata, final ExecutionContext exec)
            throws CanceledExecutionException {
        BufferedDataContainer container = createDataContainer(exec);
        for (JaCoCoEntry entry : jacocodata) {
            progress(exec);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Transforming metric, class: " + entry.getClassName());
            }

            if (LOGGER.isEnabledFor(LEVEL.ALL)) {
                LOGGER.debug("Transforming metric:" + entry.toString());
            }
            DataRow row = createTableRow(entry);
            container.addRowToTable(row);
        }
        container.close();
        BufferedDataTable out = container.getTable();
        return out;
    }

    private DataRow createTableRow(final JaCoCoEntry entry) {
        DataCell[] cells = getJaCoCoCells(entry);
        DataRow row = new DefaultRow(entry.getClassName(), cells);
        return row;
    }

    private static DataCell[] getJaCoCoCells(final JaCoCoEntry value) {
        DataCell[] cells = { 
                doubleOrMissingCell(value.getLineCoverageCounter()),
                doubleOrMissingCell(value.getInstructionCoverageCounter()),
                doubleOrMissingCell(value.getBranchCoverageCounter()),
                doubleOrMissingCell(value.getComplexityCoverageCounter()),
                doubleOrMissingCell(value.getMethodCoverageCounter()),
                doubleOrMissingCell(value.getClassCoverageCounter())};
        return cells;
    }

    private BufferedDataContainer createDataContainer(final ExecutionContext exec) {
        return exec.createDataContainer(tableSpec);
    }

    private void progress(final ExecutionContext exec) throws CanceledExecutionException {
        exec.checkCanceled();
    }
}
