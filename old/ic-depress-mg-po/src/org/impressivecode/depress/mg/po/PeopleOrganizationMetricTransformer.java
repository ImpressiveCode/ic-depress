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
package org.impressivecode.depress.mg.po;

import static org.impressivecode.depress.common.Cells.doubleOrMissingCell;
import static org.impressivecode.depress.common.Cells.integerOrMissingCell;

import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeLogger.LEVEL;

import com.google.common.base.Preconditions;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class PeopleOrganizationMetricTransformer {

    private static final NodeLogger LOGGER = NodeLogger.getLogger(PeopleOrganizationMetricTransformer.class);
    private final DataTableSpec tableSpec;

    public PeopleOrganizationMetricTransformer(final DataTableSpec tableSpec) {
        Preconditions.checkNotNull(tableSpec, "table specifikation can not be null.");
        this.tableSpec = tableSpec;
    }

    public BufferedDataTable transform(final List<PeopleOrganizationMetric> podata, final ExecutionContext exec)
            throws CanceledExecutionException {
        BufferedDataContainer container = createDataContainer(exec);
        for (PeopleOrganizationMetric metric : podata) {
            progress(exec);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Transforming metric, class: " + metric.getClassName());
            }

            if (LOGGER.isEnabledFor(LEVEL.ALL)) {
                LOGGER.debug("Transforming metric:" + metric.toString());
            }
            DataRow row = createTableRow(metric);
            container.addRowToTable(row);
        }
        container.close();
        BufferedDataTable out = container.getTable();
        return out;
    }

    private DataRow createTableRow(final PeopleOrganizationMetric metric) {
        DataCell[] cells = getPOCells(metric);
        DataRow row = new DefaultRow(metric.getClassName(), cells);
        return row;
    }

    private static DataCell[] getPOCells(final PeopleOrganizationMetric value) {
        DataCell[] cells = { 
                new IntCell(value.getNOE()), 
                new IntCell(value.getNOEE()), 
                new IntCell(value.getEF()),
                integerOrMissingCell(value.getDMO()), 
                doubleOrMissingCell((value.getPO())), 
                new DoubleCell(value.getOCO()),
                doubleOrMissingCell(value.getOOW()), 
                new IntCell(value.getOIF()), 
                new IntCell(value.getNOE1()),
                new IntCell(value.getNOE2()), 
                new IntCell(value.getNOE3()), 
                new IntCell(value.getNOE4()), 
                new IntCell(value.getNOE5()) 
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
