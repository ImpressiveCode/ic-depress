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
package org.impressivecode.depress.data.source;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.impressivecode.depress.common.Cells;
import java.util.List;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeLogger.LEVEL;

/**
 * 
 * @author Marcin Strzeszyna
 * @author Wieslaw Rybicki
 * 
 */

public class DataSourceAdapterTransformer {

    private static final NodeLogger LOGGER = NodeLogger.getLogger(DataSourceAdapterTransformer.class);
    private int rowCounter = 0;
    private final DataTableSpec tableSpec;

    public DataSourceAdapterTransformer(final DataTableSpec tableSpec) {
        checkNotNull(tableSpec, "table specifikation can not be null.");
        this.tableSpec = tableSpec;
    }

    public BufferedDataTable transform(final List<DataSourceAdapterClassDataEntry> datasource_data, final ExecutionContext exec)
            throws CanceledExecutionException {
        BufferedDataContainer container = createDataContainer(exec);
        for (DataSourceAdapterClassDataEntry entry : datasource_data) {
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

    private DataRow createTableRow(final DataSourceAdapterClassDataEntry entry) {
        DataCell[] cells = getDataSourceCells(entry);
        DataRow row = new DefaultRow(RowKey.createRowKey(rowCounter++), cells);
        return row;
    }

    private DataCell[] getDataSourceCells(final DataSourceAdapterClassDataEntry value) {
        DataCell[] cells = {
        		Cells.stringOrMissingCell(value.getLocation()),
        		Cells.stringOrMissingCell(value.getClassName()),
        		Cells.stringOrMissingCell(value.getMethodName()),
        		BooleanCell.get(value.getIsPublic()),
        		BooleanCell.get(value.getIsProtected()),
        		BooleanCell.get(value.getIsPrivate()),
        		BooleanCell.get(value.getIsStatic()),
        		BooleanCell.get(value.getIsFinal()),
        		BooleanCell.get(value.getIsAbstract()),
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
