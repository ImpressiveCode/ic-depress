package org.impressivecode.depress.metric.astcompare;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "AstComparePlugin" Node. Plugin is checking
 * project repository and generating metrics by ast compare.
 * 
 * @author Piotr Mitka
 */
public class AstComparePluginNodeView extends NodeView<AstComparePluginNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel
     *            The model (class: {@link AstComparePluginNodeModel})
     */
    protected AstComparePluginNodeView(final AstComparePluginNodeModel nodeModel) {
        super(nodeModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {
        AstComparePluginNodeModel nodeModel = (AstComparePluginNodeModel) getNodeModel();
        assert nodeModel != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {
    }

}
