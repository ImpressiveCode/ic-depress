package org.impressivecode.depress.data.anonymisation;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "Anonymisation" Node.
 * Encrypts and decrypts selected input data using symmetric algorithm (DES), using provided encryption key.
 *
 * @author Andrzej Dudek
 */
public class AnonymisationNodeView extends NodeView<AnonymisationNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link AnonymisationNodeModel})
     */
    protected AnonymisationNodeView(final AnonymisationNodeModel nodeModel) {
        super(nodeModel);
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {
        // TODO: generated method stub
    }

}

