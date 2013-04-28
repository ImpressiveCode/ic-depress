package org.impressivecode.depress.data.anonymisation;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Anonymisation" Node. Encrypts and decrypts
 * selected input data using symmetric algorithm (Blowfish), using provided
 * encryption key.
 * 
 * @author Andrzej Dudek
 * @author Marcin Bogusz
 * @author Konrad Kocik
 * @author Artur Bilski
 */
public class AnonymisationNodeFactory extends NodeFactory<AnonymisationNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public AnonymisationNodeModel createNodeModel() {
        return new AnonymisationNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<AnonymisationNodeModel> createNodeView(final int viewIndex, final AnonymisationNodeModel nodeModel) {
        return new AnonymisationNodeView(nodeModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
        return new AnonymisationNodeDialog();
    }

}
