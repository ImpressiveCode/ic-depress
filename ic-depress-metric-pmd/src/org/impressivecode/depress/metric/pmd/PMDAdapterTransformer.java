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
package org.impressivecode.depress.metric.pmd;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.impressivecode.depress.common.Cells;
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
 * @author Tomasz Banach
 * @author Łukasz Waga
 * @author Monika Pruszkowska
 * 
 */
public class PMDAdapterTransformer {

    private static final NodeLogger LOGGER = NodeLogger.getLogger(PMDAdapterTransformer.class);

    private final DataTableSpec tableSpec;

    private int i=0;

    public PMDAdapterTransformer(final DataTableSpec tableSpec) {
        checkNotNull(tableSpec, "table specifikation can not be null.");
        this.tableSpec = tableSpec;
    }

    public BufferedDataTable transform(final List<PMDEntry> pmddata, final ExecutionContext exec)
            throws CanceledExecutionException {
        BufferedDataContainer container = createDataContainer(exec);
        for (PMDEntry entry : pmddata) {
            progress(exec);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Transforming metric, class: " + entry.getClassName());
            }

            if (LOGGER.isEnabledFor(LEVEL.ALL)) {
                LOGGER.debug("Transforming metric:" + entry.toString());
            }
            DataRow row = createTableRow(entry);
            container.addRowToTable(row);
            i++;
        }
        container.close();
        BufferedDataTable out = container.getTable();
        i=0;
        return out;
    }

    private DataRow createTableRow(final PMDEntry entry) {
        DataCell[] cells = getPMDCells(entry);
        String keyi = Integer.toString(i);
        DataRow row = new DefaultRow(keyi, cells);
        return row;
    }

    private DataCell[] getPMDCells(final PMDEntry value) {
        DataCell[] cells = { 
                Cells.stringOrMissingCell(value.getClassName()),
                Cells.stringOrMissingCell(value.getPriority()),
                Cells.stringOrMissingCell(value.getBeginLine()),
                Cells.stringOrMissingCell(value.getEndLine()),
                Cells.stringOrMissingCell(value.getBeginColumn()),
                Cells.stringOrMissingCell(value.getEndColumn()),
                Cells.stringOrMissingCell(value.getRule()),
                Cells.stringOrMissingCell(value.getRuleSet()),
                Cells.stringOrMissingCell(value.getInfoUrl()),
                Cells.stringOrMissingCell(value.getMessageText())
        };
        return cells;
    }

    private BufferedDataContainer createDataContainer(final ExecutionContext exec) {
        return exec.createDataContainer(tableSpec);
    }

    private void progress(final ExecutionContext exec) throws CanceledExecutionException {
        exec.checkCanceled();
    }
}
