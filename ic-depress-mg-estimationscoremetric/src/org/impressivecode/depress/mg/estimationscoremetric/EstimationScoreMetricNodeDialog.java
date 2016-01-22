package org.impressivecode.depress.mg.estimationscoremetric;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelNumber;

/**
 * <code>NodeDialog</code> for the "ProcessMetric" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Szymon Jasniak
 */
public class EstimationScoreMetricNodeDialog extends DefaultNodeSettingsPane {
	
	private final SettingsModelInteger correctEstimateMarginInfo = new SettingsModelIntegerBounded(EstimationScoreMetricNodeModel.CORRECT_ESTIMATE_MARGIN_LABEL, 15, 0, 100);
	private final SettingsModelInteger highOverUnderEstimateMarginInfo = new SettingsModelIntegerBounded(EstimationScoreMetricNodeModel.HIGH_OVER_UNDER_ESTIMATE_MARGIN_LABEL, 50, 0, 100);
	private final SettingsModelDouble weightScoreForHighValuesInfo = new SettingsModelDoubleBounded(EstimationScoreMetricNodeModel.WEIGHT_SCORE_FOR_HIGH_VALUES_LABEL, new Double(1.5), new Double(1.0), new Double(10.0));
	/**
     * New pane for configuring the ProcessMetric node
     */
    protected EstimationScoreMetricNodeDialog() {
    	super();
    	final DialogComponentNumber lowValue = new DialogComponentNumber(correctEstimateMarginInfo, EstimationScoreMetricNodeModel.CORRECT_ESTIMATE_MARGIN_LABEL, 1);
    	final DialogComponentNumber highValue = new DialogComponentNumber(highOverUnderEstimateMarginInfo, EstimationScoreMetricNodeModel.HIGH_OVER_UNDER_ESTIMATE_MARGIN_LABEL, 1);
    	final DialogComponentNumber weight = new DialogComponentNumber(weightScoreForHighValuesInfo, EstimationScoreMetricNodeModel.WEIGHT_SCORE_FOR_HIGH_VALUES_LABEL, 1);
    	addDialogComponent(lowValue);
    	addDialogComponent(highValue);
    	addDialogComponent(weight);
    }
}

