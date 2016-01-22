package org.impressivecode.depress.mg.estimationscoremetric;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ProcessMetric" Node.
 * 
 *
 * @author Szymon Jasniak
 */
public class EstimationScoreMetricNodeFactory 
        extends NodeFactory<EstimationScoreMetricNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public EstimationScoreMetricNodeModel createNodeModel() {
        return new EstimationScoreMetricNodeModel();
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
    public NodeView<EstimationScoreMetricNodeModel> createNodeView(final int viewIndex,
            final EstimationScoreMetricNodeModel nodeModel) {
        return new EstimationScoreMetricNodeView(nodeModel);
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
        return new EstimationScoreMetricNodeDialog();
    }

}

