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
public class ColumnDecryptorTransformer {
    private static final NodeLogger LOGGER = NodeLogger.getLogger(ColumnDecryptorTransformer.class);
    private final DataTableSpec tableSpec;

    private final Set<Integer> colToBeDecrypted;

    public ColumnDecryptorTransformer(final DataTableSpec tableSpec, final String[] decrypts) {
        checkNotNull(tableSpec, "table specifikation can not be null.");
        checkNotNull(decrypts, "decrypts specifikation can not be null.");
        this.tableSpec = tableSpec;
        this.colToBeDecrypted = indices(decrypts);
    }

    public BufferedDataTable transform(final BufferedDataTable data, final ExecutionContext exec)
            throws CanceledExecutionException {
        BufferedDataContainer container = createDataContainer(exec);
        CloseableRowIterator iterator = data.iterator();
        while (iterator.hasNext()) {
            progress(exec);
            DataRow encryptedRow = decryptRow(iterator.next());
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

    private DataRow decryptRow(final DataRow row) {
        DataRow decrypted = new DefaultRow(row.getKey(), decryptCells(row));
        return decrypted;
    }

    private List<DataCell> decryptCells(final DataRow row) {
        List<DataCell> cells = Lists.newArrayList();
        for (int i = 0; i < row.getNumCells(); i++) {
            DataCell dataCell = row.getCell(i);
            if (dataCell.isMissing()) {
                cells.add(dataCell);
            } else if (colToBeDecrypted.contains(i)) {
                cells.add(decryptCell(dataCell));
            } else {
                cells.add(dataCell);
            }
        }

        return cells;
    }

    private DataCell decryptCell(final DataCell dataCell) {
        Preconditions.checkArgument(StringCell.TYPE.equals(dataCell.getType()), "Only string cells supported yet");

        try {
            String origin = ((StringCell) dataCell).getStringValue();
            String decrypted = KnimeEncryption.decrypt(origin);
            return new StringCell(decrypted);
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
