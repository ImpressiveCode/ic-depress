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
import org.knime.core.data.container.CloseableRowIterator;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.NodeLogger;
import org.knime.core.util.KnimeEncryption;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class ColumnEncryptorTransformer {
    private static final NodeLogger LOGGER = NodeLogger.getLogger(ColumnEncryptorTransformer.class);
    private final DataTableSpec tableSpec;

    private final Set<Integer> colToBeEncrypted;

    public ColumnEncryptorTransformer(final DataTableSpec tableSpec, final String[] encrypts) {
        checkNotNull(tableSpec, "table specifikation can not be null.");
        checkNotNull(encrypts, "encrypts specifikation can not be null.");
        this.tableSpec = tableSpec;
        this.colToBeEncrypted = indices(encrypts);
    }

    public BufferedDataTable transform(final BufferedDataTable data, final ExecutionContext exec)
            throws CanceledExecutionException {
        BufferedDataContainer container = createDataContainer(exec);
        CloseableRowIterator iterator = data.iterator();
        while (iterator.hasNext()) {
            progress(exec);
            DataRow encryptedRow = encryptRow(iterator.next());
            container.addRowToTable(encryptedRow);
        }

        container.close();
        BufferedDataTable out = container.getTable();
        return out;
    }

    private Set<Integer> indices(final String[] encrypts) {
        Set<Integer> colIndices = Sets.newHashSet();
        for (int i = 0; i < encrypts.length; i++) {
            int index = tableSpec.findColumnIndex(encrypts[i]);
            colIndices.add(index);
        }
        return colIndices;
    }

    private DataRow encryptRow(final DataRow row) {
        DataRow encrypted = new DefaultRow(row.getKey(), encodeCells(row));
        return encrypted;
    }

    private List<DataCell> encodeCells(final DataRow row) {
        List<DataCell> cells = Lists.newArrayList();
        for (int i = 0; i < row.getNumCells(); i++) {
            DataCell dataCell = row.getCell(i);
            if (dataCell.isMissing()) {
                cells.add(dataCell);
            } else if (colToBeEncrypted.contains(i)) {
                cells.add(encryptCell(dataCell));
            } else {
                cells.add(dataCell);
            }
        }

        return cells;
    }

    private DataCell encryptCell(final DataCell dataCell) {
        Preconditions.checkArgument(StringCell.TYPE.equals(dataCell.getType()), "Only string cells supported yet");

        try {
            String origin = ((StringCell) dataCell).getStringValue();
            String encrypted = KnimeEncryption.encrypt(origin.toCharArray());
            return new StringCell(encrypted);
        } catch (Exception e) {
            LOGGER.error("Unable to proceed due to invalid encryption settings", e);
            throw new IllegalStateException("Unable to proceed due to invalid encryption");
        }
    }

    private BufferedDataContainer createDataContainer(final ExecutionContext exec) {
        return exec.createDataContainer(tableSpec);
    }

    private void progress(final ExecutionContext exec) throws CanceledExecutionException {
        exec.checkCanceled();
    }
}
