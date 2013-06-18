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
                DataCell transformedCell = null;
                Preconditions.checkArgument(
                    dataCell.getType().equals(StringCell.TYPE)
                    || dataCell instanceof SetCell
                    || dataCell instanceof ListCell,
                    "Cell type " + dataCell.getType().toString() + " is not supported");
                
                if(dataCell.getType().equals(StringCell.TYPE)){
                    transformedCell = makeTransformation((StringCell)dataCell);
                }
                else if(dataCell instanceof SetCell){
                    transformedCell = makeTransformation((SetCell)dataCell);
                }
                else if(dataCell instanceof ListCell){
                    transformedCell = makeTransformation((ListCell)dataCell);
                }
                else {
                    LOGGER.error("This exception should not be thrown unless something is wrong");
                    throw new IllegalStateException("This exception should not be thrown unless something is wrong");
                }
                return transformedCell;
            }
            
            protected DataCell makeTransformation(final StringCell stringCell)
            {
                String transformed = null;
                String origin = stringCell.getStringValue();
                try {
                    transformed = KnimeEncryption.encrypt(origin.toCharArray());
                } catch (Exception e) {
                    LOGGER.error("Unable to proceed due to invalid cryptography settings", e);
                    throw new IllegalStateException("Unable to proceed due to invalid cryptography settings");
                }
                return new StringCell(transformed);
            }
            
            protected DataCell makeTransformation(final SetCell setCell)
            {
                return null;
            }
            
            protected DataCell makeTransformation(final ListCell listCell)
            {
                return null;
            }
        };
    }
}
