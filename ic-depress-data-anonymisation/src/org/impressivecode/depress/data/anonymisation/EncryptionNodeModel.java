package org.impressivecode.depress.data.anonymisation;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.collection.ListCell;
import org.knime.core.data.collection.SetCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.NodeLogger;
import org.knime.core.util.KnimeEncryption;

import com.google.common.base.Preconditions;
/**
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class EncryptionNodeModel extends CryptoNodeModel {
    private static final NodeLogger LOGGER = NodeLogger.getLogger(EncryptionNodeModel.class);

    public static final String CFG_ENCRYPTION_KEY_FILTER = "depress.data.anonencryption";

    protected EncryptionNodeModel() {
        super(CFG_ENCRYPTION_KEY_FILTER);
    }

    @Override
    protected ColumnCryptoTransformer transformer(final DataTableSpec spec, final String[] transforms) {
        return new ColumnCryptoTransformer(spec, transforms) {

            @Override
            protected DataCell transformCell(final DataCell dataCell) {
                Preconditions.checkArgument(
                    dataCell.getType().equals(StringCell.TYPE)
                    || SetCell.class.isInstance(dataCell)
                    || ListCell.class.isInstance(dataCell),
                    "Cell type " + dataCell.getType().toString() + " is not supported");

                try {
                    String origin = ((StringCell) dataCell).getStringValue();
                    String transformed = KnimeEncryption.encrypt(origin.toCharArray());
                    return new StringCell(transformed);
                } catch (Exception e) {
                    LOGGER.error("Unable to proceed due to invalid cryptography settings", e);
                    throw new IllegalStateException("Unable to proceed due to invalid cryptography settings");
                }
            }
        };
    }
}
