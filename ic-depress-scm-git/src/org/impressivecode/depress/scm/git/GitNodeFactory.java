package org.impressivecode.depress.scm.git;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Git" Node.
 * 
 *
 * @author Tomasz Kuzemko
 */
public class GitNodeFactory 
        extends NodeFactory<GitNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public GitNodeModel createNodeModel() {
        return new GitNodeModel();
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
    public NodeView<GitNodeModel> createNodeView(final int viewIndex,
            final GitNodeModel nodeModel) {
        return new GitNodeView(nodeModel);
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
        return new GitNodeDialog();
    }

}

