/*
 ImpressiveCode Depress Framework
 Copyright (C) 2013  ImpressiveCode contributors

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.impressivecode.depress.metric.im;

import static com.google.common.base.Preconditions.checkState;
import static org.impressivecode.depress.metric.im.IssuesMetricTableFactory.createDataColumnSpec;
import static org.impressivecode.depress.scm.SCMAdapterTableFactory.MARKER_COLSPEC;
import static org.impressivecode.depress.scm.SCMAdapterTableFactory.RESOURCE_COLSPEC;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.impressivecode.depress.common.InputTransformer;
import org.impressivecode.depress.common.OutputTransformer;
import org.impressivecode.depress.its.ITSAdapterTableFactory;
import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSInputTransformer;
import org.impressivecode.depress.scm.SCMDataType;
import org.impressivecode.depress.scm.SCMInputTransformer;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class IssuesMetricNodeModel extends NodeModel {

    private static final NodeLogger LOGGER = NodeLogger.getLogger(IssuesMetricNodeModel.class);

    private InputTransformer<ITSDataType> issueTransfomer;
    private InputTransformer<SCMDataType> historyTransfomer;

    protected IssuesMetricNodeModel() {
        super(2, 1);
        this.historyTransfomer = new SCMInputTransformer(new DataTableSpec(RESOURCE_COLSPEC, MARKER_COLSPEC));
        this.issueTransfomer = new ITSInputTransformer(new DataTableSpec(ITSAdapterTableFactory.ISSUE_ID_COLSPEC));
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {

        LOGGER.info("Preparing to build NoI metric.");
        List<IssuesMetricType> metricData = computeMetric(inData, exec);
        LOGGER.info("Transforming to build NoI data.");
        BufferedDataTable out = transform(metricData, exec);
        LOGGER.info("Building PO Metric finished.");
        return new BufferedDataTable[] { out };
    }

    @Override
    protected void reset() {
    }

    private List<IssuesMetricType> computeMetric(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws CanceledExecutionException {
        checkState(this.issueTransfomer != null, "IssueTransformer has to be configured first");
        checkState(this.historyTransfomer != null, "HistoryTransformer has to be configured first");

        List<SCMDataType> history = historyTransfomer.transform(inData[0]);
        List<ITSDataType> issues = issueTransfomer.transform(inData[1]);

        IssuesMetricMetricProcessor metricProcessor = new IssuesMetricMetricProcessor(issues, history);
        return metricProcessor.computeMetric();
    }

    private BufferedDataTable transform(final List<IssuesMetricType> data, final ExecutionContext exec)
            throws CanceledExecutionException {
        OutputTransformer<IssuesMetricType> transformer = new IssuesMetricOutputTransformer(createDataColumnSpec());
        return transformer.transform(data, exec);
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        if (inSpecs.length != 2) {
            throw new InvalidSettingsException("Wrong number of input suorces");
        }
        this.historyTransfomer.validate(inSpecs[0]);
        this.issueTransfomer.validate(inSpecs[1]);

        return new DataTableSpec[] { createDataColumnSpec() };
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        // NOOP
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        // NOOP
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        // NOOP
    }

    @Override
    protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
    CanceledExecutionException {
        // NOOP
    }

    @Override
    protected void saveInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
    CanceledExecutionException {
        // NOOP
    }
}
