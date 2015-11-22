package org.impressivecode.depress.mr.astcompare.utils;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.impressivecode.depress.common.Cells.doubleOrMissingCell;
import static org.impressivecode.depress.mr.astcompare.utils.Utils.addWeeksToDate;
import static org.impressivecode.depress.mr.astcompare.utils.Utils.getDateAsString;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.impressivecode.depress.mr.astcompare.db.DbHandler;
import org.impressivecode.depress.mr.astcompare.db.Metric;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

import com.google.common.collect.Lists;

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
public class AstMetricsTransformer {

    private final DataTableSpec tableSpec;

    public AstMetricsTransformer(final DataTableSpec tableSpec) {
        checkNotNull(tableSpec, "table specification can not be null.");
        this.tableSpec = tableSpec;
    }

    public BufferedDataTable transform(final List<Metric> astMetrics, final ExecutionContext exec)
            throws CanceledExecutionException {
        BufferedDataContainer container = createDataContainer(exec);
        exec.setProgress(0.85d);

        for (Metric entry : astMetrics) {
            exec.checkCanceled();

            DataRow row = createTableRow(entry);
            container.addRowToTable(row);
        }
        container.close();
        BufferedDataTable out = container.getTable();
        exec.setProgress(1.0d);
        return out;
    }

    private BufferedDataContainer createDataContainer(final ExecutionContext exec) {
        return exec.createDataContainer(tableSpec);
    }

    private DataRow createTableRow(final Metric entry) {
        DataCell[] cells = getMetricCells(entry);
        DataRow row = new DefaultRow(entry.getMethodName(), cells);
        return row;
    }

    private DataCell[] getMetricCells(final Metric value) {
        DataCell[] cells = { doubleOrMissingCell(value.getAllMethodHistories()),
                doubleOrMissingCell(value.getMethodHistories()), doubleOrMissingCell(value.getAuthors()),
                doubleOrMissingCell(value.getStmtAdded()), doubleOrMissingCell(value.getMaxStmtAdded()),
                doubleOrMissingCell(value.getAvgStmtAdded()), doubleOrMissingCell(value.getStmtUpdated()),
                doubleOrMissingCell(value.getMaxStmtUpdated()), doubleOrMissingCell(value.getAvgStmtUpdated()),
                doubleOrMissingCell(value.getStmtDeleted()), doubleOrMissingCell(value.getMaxStmtDeleted()),
                doubleOrMissingCell(value.getAvgStmtDeleted()), doubleOrMissingCell(value.getStmtParentChanged()),
                doubleOrMissingCell(value.getChurn()), doubleOrMissingCell(value.getMaxChurn()),
                doubleOrMissingCell(value.getAvgChurn()), doubleOrMissingCell(value.getDecl()),
                doubleOrMissingCell(value.getCond()), doubleOrMissingCell(value.getElseAdded()),
                doubleOrMissingCell(value.getElseDeleted()) };

        return cells;
    }

    public List<Metric> getMetricEntries(DbHandler db, String selectedProjectName, long revisionDateMin,
            long revisionDateMax, int weeks) throws CanceledExecutionException, SQLException {
        List<Metric> metrics = Lists.newLinkedList();
        long timeFrameStart = revisionDateMin;
        long timeFrameEnd = addWeeksToDate(timeFrameStart, weeks);

        while (true) {
            if (timeFrameEnd <= revisionDateMax) {
                db.getDataFromDB(timeFrameStart, timeFrameEnd, selectedProjectName, revisionDateMin, revisionDateMax);

                metrics.add(getEntryWithDateRange(getDateAsString(timeFrameStart) + " - "
                        + getDateAsString(timeFrameEnd)));
                metrics.addAll(getEntryFromMap(db.getMetrics()));

                timeFrameStart = timeFrameEnd + 1;
            } else {
                if (timeFrameStart < revisionDateMax) {
                    timeFrameEnd = revisionDateMax;
                    continue;
                } else {
                    break;
                }
            }
            timeFrameEnd = addWeeksToDate(timeFrameStart, weeks);
        }

        return metrics;
    }

    private Metric getEntryWithDateRange(String dateRange) {
        return new Metric(dateRange);
    }

    private List<Metric> getEntryFromMap(Map<String, Metric> map) {
        List<Metric> metrics = Lists.newLinkedList();
        for (Entry<String, Metric> entry : map.entrySet()) {
            if (entry.getValue().hasResults()) {
                metrics.add(entry.getValue());
            }
        }
        return metrics;
    }

}
