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
package org.impressivecode.depress.scm.git;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.impressivecode.depress.common.Cells.dateTimeOrMissingCell;
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
public class GitOnlineAdapterExtendedTransformer {

    private final DataTableSpec tableSpec;
    private final ExecutionContext exec;

    public GitOnlineAdapterExtendedTransformer(final ExecutionContext exec, final DataTableSpec tableSpec) {
        checkNotNull(tableSpec, "table specification can not be null.");
        this.tableSpec = tableSpec;
        this.exec = exec;
    }

    public BufferedDataTable transformHistory(final List<GitOnlineAdapterExtendedEntry> histories) throws CanceledExecutionException {
        BufferedDataContainer container = exec.createDataContainer(tableSpec);

        Long counter = 0l;
        for (GitOnlineAdapterExtendedEntry history : histories) {
            exec.checkCanceled();

            DataRow row = createTableRow(String.valueOf(counter++), history);
            container.addRowToTable(row);
            checkIfCancelledAndSetProgress(0.9d + 0.1d * counter / histories.size());
        }
        container.close();
        checkIfCancelledAndSetProgress(1.0d);

        return container.getTable();
    }

    private DataRow createTableRow(String rowId, final GitOnlineAdapterExtendedEntry history) {
        DataCell[] cells = getHistoryCells(history);
        DataRow row = new DefaultRow(rowId, cells);
        return row;
    }

    private DataCell[] getHistoryCells(final GitOnlineAdapterExtendedEntry value) {
        DataCell[] cells = { stringOrMissingCell(value.getMethodName()), stringOrMissingCell(value.getAuthor()),
                stringOrMissingCell(value.getMessage()), dateTimeOrMissingCell(value.getDate()),
                stringOrMissingCell(value.getCommitId()) };

        return cells;
    }

    public List<GitOnlineAdapterExtendedEntry> getHistoryEntriesFromMap(Map<String, GitOnlineAdapterExtendedEntry> map) {
        List<GitOnlineAdapterExtendedEntry> histories = Lists.newLinkedList();
        for (Entry<String, GitOnlineAdapterExtendedEntry> entry : map.entrySet()) {
            if (entry.getValue().hasResults()) {
                histories.add(entry.getValue());
            }
        }
        return histories;
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
