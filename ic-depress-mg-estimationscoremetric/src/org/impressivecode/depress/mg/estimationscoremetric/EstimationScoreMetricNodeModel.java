package org.impressivecode.depress.mg.estimationscoremetric;

import static com.google.common.base.Preconditions.checkState;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.impressivecode.depress.common.InputTransformer;
import org.impressivecode.depress.common.OutputTransformer;
import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSInputTransformer;
import org.impressivecode.depress.mg.ipa.IssuesMetricType;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.collection.ListCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelNumber;

/**
 * This is the model implementation of ProcessMetric.
 * 
 * 
 * @author Szymon Jasniak
 */
public class EstimationScoreMetricNodeModel extends NodeModel {

	/**
	 * the logger instance
	 */
	private static final NodeLogger LOGGER = NodeLogger
			.getLogger(EstimationScoreMetricNodeModel.class);
	private InputTransformer<ITSDataType> issueTransfomer;
	private InputTransformer<IssuesMetricType> ipaTransfomer;

	public static String CORRECT_ESTIMATE_MARGIN_LABEL = "Correct estimate margin[%]:";
	public static String HIGH_OVER_UNDER_ESTIMATE_MARGIN_LABEL = "High over/under estimate margin[%]:";
	public static String WEIGHT_SCORE_FOR_HIGH_VALUES_LABEL = "Weight score for high values:";

	/**
	 * 
	 * Default values in node
	 */

	/**
	 * Create new dialog element in node
	 */

	private final SettingsModelInteger correctEstimateMarginInfo = new SettingsModelIntegerBounded(
			EstimationScoreMetricNodeModel.CORRECT_ESTIMATE_MARGIN_LABEL, 15, 0, 100);
	private final SettingsModelInteger highOverUnderEstimateMarginInfo = new SettingsModelIntegerBounded(
			EstimationScoreMetricNodeModel.HIGH_OVER_UNDER_ESTIMATE_MARGIN_LABEL, 50, 0, 100);
	private final SettingsModelDouble weightScoreForHighValuesInfo = new SettingsModelDoubleBounded(EstimationScoreMetricNodeModel.WEIGHT_SCORE_FOR_HIGH_VALUES_LABEL, new Double(1.5), new Double(1.0), new Double(10.0));

	/**
	 * Constructor for the node model.
	 */
	protected EstimationScoreMetricNodeModel() {
		super(2, 1);
		this.issueTransfomer = new ITSInputTransformer();
		this.ipaTransfomer = new IPAInputTransformer();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		Integer correctEstimateMargin = 0;
		Integer highOverUnderEstimateMargin = 0;
		correctEstimateMargin = correctEstimateMarginInfo.getIntValue();
		highOverUnderEstimateMargin = highOverUnderEstimateMarginInfo
				.getIntValue();

		checkState(correctEstimateMargin < highOverUnderEstimateMargin,
				"Correct estimate margin cannot be higher than High over/under estimate margin");
		checkState(this.issueTransfomer != null,
				"IssueTransformer has to be configured first");
		checkState(this.ipaTransfomer != null,
				"IPATransformer has to be configured first");

		List<ITSDataType> issues = issueTransfomer.transform(inData[0]);
		List<IssuesMetricType> history = ipaTransfomer.transform(inData[1]);

		EstimationScoreMetricProcessor metricProcessor = new EstimationScoreMetricProcessor(
				issues, history, correctEstimateMargin,
				highOverUnderEstimateMargin,weightScoreForHighValuesInfo.getDoubleValue());
		List<EstimationScoreMetricType> metricResult = metricProcessor.transform();

		BufferedDataTable out = transform(metricResult, exec);
		return new BufferedDataTable[] { out };
	}

	private BufferedDataTable transform(final List<EstimationScoreMetricType> data,
			final ExecutionContext exec) throws CanceledExecutionException {
		OutputTransformer<EstimationScoreMetricType> transformer = new EstimationScoreMetricOutputTransformer(
				EstimationScoreMetricTableFactory.createDataColumnSpec());
		return transformer.transform(data, exec);
	}

	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
			throws InvalidSettingsException {
		if (inSpecs.length != 2) {
			throw new InvalidSettingsException("Wrong number of input suorces");
		}
		final DataColumnSpec RESOURCE_COLSPEC = new DataColumnSpecCreator(
				"Issues", ListCell.getCollectionType(StringCell.TYPE))
				.createSpec();
		final DataColumnSpec ISSUE_ID = new DataColumnSpecCreator("ID",
				StringCell.TYPE).createSpec();

		this.ipaTransfomer.setMinimalSpec(new DataTableSpec(RESOURCE_COLSPEC))
				.setInputSpec(inSpecs[1]).validate();
		this.issueTransfomer.setMinimalSpec(new DataTableSpec(ISSUE_ID))
				.setInputSpec(inSpecs[0]).validate();
		return new DataTableSpec[] { EstimationScoreMetricTableFactory
				.createDataColumnSpec() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		// TODO: generated method stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		// TODO: generated method stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		correctEstimateMarginInfo.loadSettingsFrom(settings);
		highOverUnderEstimateMarginInfo.loadSettingsFrom(settings);
		weightScoreForHighValuesInfo.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		correctEstimateMarginInfo.validateSettings(settings);
		highOverUnderEstimateMarginInfo.validateSettings(settings);
		weightScoreForHighValuesInfo.validateSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
		// TODO: generated method stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
		// TODO: generated method stub
	}

}
