package org.impressivecode.depress.metric.astcompare;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "AstComparePlugin" Node. Plugin is checking
 * project repository and generating metrics by ast compare.
 * 
 * @author Piotr Mitka
 */
public class AstComparePluginNodeFactory extends NodeFactory<AstComparePluginNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public AstComparePluginNodeModel createNodeModel() {
        return new AstComparePluginNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<AstComparePluginNodeModel> createNodeView(final int viewIndex,
            final AstComparePluginNodeModel nodeModel) {
        return new AstComparePluginNodeView(nodeModel);
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
        return new AstComparePluginNodeDialog();
    }

}
