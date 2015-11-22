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
 along with this program.  If not, see <http://www.gnu.org/licenses/
>.
 */
package org.impressivecode.depress.data.anonymisation;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Set;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.container.CloseableRowIterator;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public abstract class ColumnCryptoTransformer {
    private final DataTableSpec tableSpec;

    private final Set<Integer> colToBeTransformed;

    public ColumnCryptoTransformer(final DataTableSpec tableSpec, final String[] transforms) {
        checkNotNull(tableSpec, "table specifikation can not be null.");
        checkNotNull(transforms, "decrypts specifikation can not be null.");
        this.tableSpec = tableSpec;
        this.colToBeTransformed = indices(transforms);
    }

    public BufferedDataTable transform(final BufferedDataTable data, final ExecutionContext exec)
            throws CanceledExecutionException {
        BufferedDataContainer container = createDataContainer(exec);
        CloseableRowIterator iterator = data.iterator();
        while (iterator.hasNext()) {
            progress(exec);
            DataRow transformedRow = transformRow(iterator.next());
            container.addRowToTable(transformedRow);
        }

        container.close();
        BufferedDataTable out = container.getTable();
        return out;
    }

    private Set<Integer> indices(final String[] transforms) {
        Set<Integer> colIndices = Sets.newHashSet();
        for (int i = 0; i < transforms.length; i++) {
            int index = tableSpec.findColumnIndex(transforms[i]);
            colIndices.add(index);
        }

        return colIndices;
    }

    private DataRow transformRow(final DataRow row) {
        if (colToBeTransformed.contains(-1)) {
            return new DefaultRow(transformKey(row.getKey()), transformCells(row));
        }
        return new DefaultRow(row.getKey().getString(), transformCells(row));
    }

    private List<DataCell> transformCells(final DataRow row) {
        List<DataCell> cells = Lists.newArrayList();
        for (int i = 0; i < row.getNumCells(); i++) {
            DataCell dataCell = row.getCell(i);
            if (dataCell.isMissing()) {
                cells.add(dataCell);
            } else if (colToBeTransformed.contains(i)) {
                cells.add(transformCell(dataCell));
            } else {
                cells.add(dataCell);
            }
        }

        return cells;
    }

    protected abstract String transformKey(final RowKey key);

    protected abstract DataCell transformCell(final DataCell dataCell);

    private BufferedDataContainer createDataContainer(final ExecutionContext exec) {
        return exec.createDataContainer(tableSpec);
    }

    private void progress(final ExecutionContext exec) throws CanceledExecutionException {
        exec.checkCanceled();
    }
}
