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
package org.impressivecode.depress.mg.astmetrics;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.impressivecode.depress.common.Cells.doubleOrMissingCell;
import static org.impressivecode.depress.common.Cells.stringOrMissingCell;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

import com.google.common.collect.Lists;

/**
 * @author Mateusz Kutyba, Wroclaw University of Technology
 */
public class AstMetricsTransformer {

    private final DataTableSpec tableSpec;
    private final ExecutionContext exec;

    public AstMetricsTransformer(final ExecutionContext exec, final DataTableSpec tableSpec) {
        checkNotNull(tableSpec, "table specification can not be null.");
        this.tableSpec = tableSpec;
        this.exec = exec;
    }

    public BufferedDataTable transformMetrics(final List<MetricEntry> astMetrics) throws CanceledExecutionException {
        BufferedDataContainer container = exec.createDataContainer(tableSpec);

        Long counter = 0l;
        for (MetricEntry entry : astMetrics) {
            exec.checkCanceled();

            DataRow row = createTableRow(String.valueOf(counter++), entry);
            container.addRowToTable(row);
            checkIfCancelledAndSetProgress(0.8d + 0.1d * counter / astMetrics.size());
        }
        container.close();
        checkIfCancelledAndSetProgress(0.9d);

        return container.getTable();
    }

    private DataRow createTableRow(String rowId, final MetricEntry entry) {
        DataCell[] cells = getMetricCells(entry);
        DataRow row = new DefaultRow(rowId, cells);
        return row;
    }

    private DataCell[] getMetricCells(final MetricEntry value) {
        DataCell[] cells = { stringOrMissingCell(value.getMethodName()),
                doubleOrMissingCell(value.getAllMethodHistories()), doubleOrMissingCell(value.getMethodHistories()),
                doubleOrMissingCell(value.getAuthors()), doubleOrMissingCell(value.getStmtAdded()),
                doubleOrMissingCell(value.getMaxStmtAdded()), doubleOrMissingCell(value.getAvgStmtAdded()),
                doubleOrMissingCell(value.getStmtUpdated()), doubleOrMissingCell(value.getMaxStmtUpdated()),
                doubleOrMissingCell(value.getAvgStmtUpdated()), doubleOrMissingCell(value.getStmtDeleted()),
                doubleOrMissingCell(value.getMaxStmtDeleted()), doubleOrMissingCell(value.getAvgStmtDeleted()),
                doubleOrMissingCell(value.getStmtParentChanged()), doubleOrMissingCell(value.getChurn()),
                doubleOrMissingCell(value.getMaxChurn()), doubleOrMissingCell(value.getAvgChurn()),
                doubleOrMissingCell(value.getDecl()), doubleOrMissingCell(value.getCond()),
                doubleOrMissingCell(value.getElseAdded()), doubleOrMissingCell(value.getElseDeleted()),
                doubleOrMissingCell(value.getLoopsAdded()), doubleOrMissingCell(value.getLoopsUpdated()),
                doubleOrMissingCell(value.getLoopsDeleted()), doubleOrMissingCell(value.getVariablesAdded()),
                doubleOrMissingCell(value.getVariablesUpdated()), doubleOrMissingCell(value.getVariablesDeleted()),
                doubleOrMissingCell(value.getAssigmentsAdded()), doubleOrMissingCell(value.getAssigmentsUpdated()),
                doubleOrMissingCell(value.getAssigmentsDeleted()), doubleOrMissingCell(value.getReturnsAdded()),
                doubleOrMissingCell(value.getReturnsUpdated()), doubleOrMissingCell(value.getReturnsDeleted()),
                doubleOrMissingCell(value.getNullsAdded()), doubleOrMissingCell(value.getNullsUpdated()),
                doubleOrMissingCell(value.getNullsDeleted()), doubleOrMissingCell(value.getCasesAdded()),
                doubleOrMissingCell(value.getCasesUpdated()), doubleOrMissingCell(value.getCasesDeleted()),
                doubleOrMissingCell(value.getBreaksAdded()), doubleOrMissingCell(value.getBreaksUpdated()),
                doubleOrMissingCell(value.getBreaksDeleted()), doubleOrMissingCell(value.getObjectsAdded()),
                doubleOrMissingCell(value.getObjectsUpdated()), doubleOrMissingCell(value.getObjectsDeleted()),
                doubleOrMissingCell(value.getCatchesAdded()), doubleOrMissingCell(value.getCatchesUpdated()),
                doubleOrMissingCell(value.getCatchesDeleted()), doubleOrMissingCell(value.getThrowsAdded()),
                doubleOrMissingCell(value.getThrowsUpdated()), doubleOrMissingCell(value.getThrowsDeleted()),
                doubleOrMissingCell(value.getLoc()) };

        return cells;
    }

    public List<MetricEntry> getMetricsEntriesFromMap(Map<String, MetricEntry> map) {
        List<MetricEntry> metrics = Lists.newLinkedList();
        for (Entry<String, MetricEntry> entry : map.entrySet()) {
            if (entry.getValue().hasResults()) {
                metrics.add(entry.getValue());
            }
        }
        return metrics;
    }

    private void checkIfCancelledAndSetProgress(Double progress) throws CanceledExecutionException {
        if (exec != null) {
            exec.checkCanceled();

            // no progress change
            if (progress != null) {
                exec.setProgress(progress);
            }
        }
    }

}
