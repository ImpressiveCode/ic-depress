package org.knime.svnplugin;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "SvnPlugin" Node.
 * 
 *
 * @author PWr team
 */
public class SvnPluginNodeFactory 
        extends NodeFactory<SvnPluginNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public SvnPluginNodeModel createNodeModel() {
        return new SvnPluginNodeModel();
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
    public NodeView<SvnPluginNodeModel> createNodeView(final int viewIndex,
            final SvnPluginNodeModel nodeModel) {
        return new SvnPluginNodeView(nodeModel);
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
        return new SvnPluginNodeDialog();
    }

}

