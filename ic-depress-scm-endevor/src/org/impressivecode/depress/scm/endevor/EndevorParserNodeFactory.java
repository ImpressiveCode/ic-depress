package org.impressivecode.depress.scm.endevor;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "EndevorParser" Node.
 * 
 *
 * @author zmwo-proj
 */
public class EndevorParserNodeFactory 
        extends NodeFactory<EndevorParserNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public EndevorParserNodeModel createNodeModel() {
        return new EndevorParserNodeModel();
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
    public NodeView<EndevorParserNodeModel> createNodeView(final int viewIndex,
            final EndevorParserNodeModel nodeModel) {
        return new EndevorParserNodeView(nodeModel);
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
        return new EndevorParserNodeDialog();
    }

}

