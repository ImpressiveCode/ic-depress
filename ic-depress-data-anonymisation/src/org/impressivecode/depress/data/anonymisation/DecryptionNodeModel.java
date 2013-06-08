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

import org.knime.core.data.DataCell;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.NodeLogger;
import org.knime.core.util.KnimeEncryption;

import com.google.common.base.Preconditions;

/**
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class DecryptionNodeModel extends CryptoNodeModel {
    private static final NodeLogger LOGGER = NodeLogger.getLogger(DecryptionNodeModel.class);

    public static final String CFG_DECRYPTION_KEY_FILTER = "depress.data.anondecryption";

    protected DecryptionNodeModel() {
        super(CFG_DECRYPTION_KEY_FILTER);
    }

    @Override
    protected ColumnCryptoTransformer transformer(final DataTableSpec spec, final String[] transforms) {
        return new ColumnCryptoTransformer(spec, transforms) {

            @Override
            protected DataCell transformCell(final DataCell dataCell) {
                Preconditions.checkArgument(StringCell.TYPE.equals(dataCell.getType()),
                        "Only string cells supported yet");

                try {
                    String origin = ((StringCell) dataCell).getStringValue();
                    String transformed = KnimeEncryption.decrypt(origin);
                    return new StringCell(transformed);
                } catch (Exception e) {
                    LOGGER.error("Unable to proceed due to invalid encryption settings", e);
                    throw new IllegalStateException("Unable to proceed due to invalid encryption");
                }
            }
        };
    }
}
