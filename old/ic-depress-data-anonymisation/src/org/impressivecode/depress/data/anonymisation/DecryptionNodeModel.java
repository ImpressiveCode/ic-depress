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

import static org.impressivecode.depress.data.anonymisation.ConfigurationFactory.checkStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.collection.CollectionCellFactory;
import org.knime.core.data.collection.ListCell;
import org.knime.core.data.collection.SetCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.NodeLogger;
import org.knime.core.util.KnimeEncryption;

import com.google.common.base.Preconditions;

/**
 * @author Marek Majchrzak, ImpressiveCode
 * @author Andrzej Dudek, Wrocław University of Technology
 * 
 */
public class DecryptionNodeModel extends CryptoNodeModel {
    private static final NodeLogger LOGGER = NodeLogger.getLogger(DecryptionNodeModel.class);

    protected DecryptionNodeModel() {
        super();
    }

    @Override
    protected ColumnCryptoTransformer transformer(final DataTableSpec spec, final String[] transforms) {
        return new ColumnCryptoTransformer(spec, transforms) {

            @Override
            protected DataCell transformCell(final DataCell dataCell) {
                Preconditions.checkArgument(checkStructure(dataCell.getType()), "Cell type "
                        + dataCell.getType().toString() + " is not supported");

                DataCell transformedCell = null;

                if (dataCell.getType().equals(StringCell.TYPE)) {
                    transformedCell = makeTransformation((StringCell) dataCell);
                } else if (dataCell instanceof SetCell) {
                    transformedCell = makeTransformation((SetCell) dataCell);
                } else if (dataCell instanceof ListCell) {
                    transformedCell = makeTransformation((ListCell) dataCell);
                } else {
                    LOGGER.error("This exception should never be thrown unless something is wrong");
                    throw new IllegalStateException("This exception should never be thrown unless something is wrong");
                }
                return transformedCell;
            }

            protected DataCell makeTransformation(final StringCell stringCell) {
                try {
                    String origin = stringCell.getStringValue();
                    return new StringCell(KnimeEncryption.decrypt(origin));
                } catch (Exception e) {
                    LOGGER.error("Unable to proceed due to invalid encryption settings", e);
                    throw new IllegalStateException("Unable to proceed due to invalid encryption");
                }
            }

            private DataCell makeTransformation(final SetCell setCell) {
                Collection<DataCell> transformedSet = new HashSet<DataCell>();
                Iterator<DataCell> setCellIterator = setCell.iterator();

                while (setCellIterator.hasNext()) {
                    DataCell original = setCellIterator.next();
                    DataCell transformed = original;

                    if (!original.isMissing()) {
                        StringCell origin = ((StringCell) original);
                        transformed = makeTransformation(origin);
                    }
                    transformedSet.add(transformed);
                }
                return CollectionCellFactory.createSetCell(transformedSet);
            }

            protected DataCell makeTransformation(final ListCell listCell) {
                Collection<DataCell> transformedList = new ArrayList<DataCell>();
                Iterator<DataCell> listCellIterator = listCell.iterator();

                while (listCellIterator.hasNext()) {
                    DataCell original = listCellIterator.next();
                    DataCell transformed = original;

                    if (!original.isMissing()) {
                        StringCell origin = ((StringCell) original);
                        transformed = makeTransformation(origin);
                    }
                    transformedList.add(transformed);
                }
                return CollectionCellFactory.createListCell(transformedList);
            }

            @Override
            protected String transformKey(final RowKey key) {
                try {
                    return KnimeEncryption.decrypt(key.getString());
                } catch (Exception e) {
                    LOGGER.error("Unable to proceed due to invalid cryptography settings", e);
                    throw new IllegalStateException("Unable to proceed due to invalid cryptography settings");
                }
            }
        };
    }
}
