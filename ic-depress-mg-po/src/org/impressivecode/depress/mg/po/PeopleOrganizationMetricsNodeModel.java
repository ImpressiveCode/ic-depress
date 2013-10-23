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
package org.impressivecode.depress.mg.po;

import static org.impressivecode.depress.common.DataTableSpecUtils.findMissingColumnSubset;
import static org.impressivecode.depress.mg.po.PeopleOrganizationMetricTableFactory.createDataColumnSpec;
import static org.impressivecode.depress.mg.po.PeopleOrganizationMetricTableFactory.createDevDataColumnSpec;
import static org.impressivecode.depress.mg.po.PeopleOrganizationMetricTableFactory.createHistoryColumnSpec;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.google.common.collect.Iterables;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class PeopleOrganizationMetricsNodeModel extends NodeModel {

    private static final NodeLogger LOGGER = NodeLogger.getLogger(PeopleOrganizationMetricsNodeModel.class);
    private DataTableSpec historyDataSpec;
    private DataTableSpec developersDataSpec;

    protected PeopleOrganizationMetricsNodeModel() {
        super(2, 1);
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {

        LOGGER.info("Preparing to build PO metric.");
        List<PeopleOrganizationMetric> poData = computeMetric(inData[0], inData[1], exec);
        LOGGER.info("Transforming to build PO data.");
        BufferedDataTable out = transform(poData, exec);
        LOGGER.info("Building PO Metric finished.");
        return new BufferedDataTable[] { out };
    }

    @Override
    protected void reset() {
        this.developersDataSpec = null;
        this.historyDataSpec = null;
    }

    private List<PeopleOrganizationMetric> computeMetric(final BufferedDataTable devTable,
            final BufferedDataTable changeHistory, final ExecutionContext exec) throws CanceledExecutionException {
        Map<String, ChangeData> changeData = buildChangeData(changeHistory, exec);
        Map<String, TeamMemberData> engineersData = buildEngineerData(devTable, exec);
        return buildMetric(changeData, engineersData, exec);
    }

    private List<PeopleOrganizationMetric> buildMetric(final Map<String, ChangeData> changeData,
            final Map<String, TeamMemberData> engineersData, final ExecutionContext exec) {
        return new PeopleOrganizationMetricProcessor(changeData, engineersData).buildMetric();
    }

    private Map<String, ChangeData> buildChangeData(final BufferedDataTable changeHistory, final ExecutionContext exec)
            throws CanceledExecutionException {
        return new ChangeDataTransformer(historyDataSpec).transformChangeData(changeHistory, exec);
    }

    private Map<String, TeamMemberData> buildEngineerData(final BufferedDataTable devTable, final ExecutionContext exec)
            throws CanceledExecutionException {
        return new TeamMemberDataTransformer(developersDataSpec).transformEngineerData(devTable, exec);
    }

    private BufferedDataTable transform(final List<PeopleOrganizationMetric> poData, final ExecutionContext exec)
            throws CanceledExecutionException {
        PeopleOrganizationMetricTransformer transformer = new PeopleOrganizationMetricTransformer(
                createDataColumnSpec());
        return transformer.transform(poData, exec);
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        if (inSpecs.length != 2) {
            throw new InvalidSettingsException("Wrong number of input suorces");
        }
        configureDevDataSpec(inSpecs[0]);
        configureChangeHistoryDataSpec(inSpecs[1]);

        return new DataTableSpec[] { createDataColumnSpec() };
    }

    private void configureChangeHistoryDataSpec(final DataTableSpec dataTableSpec) throws InvalidSettingsException {
        Set<String> missing = findMissingColumnSubset(dataTableSpec, createHistoryColumnSpec());
        if (!missing.isEmpty()) {
            throw new InvalidSettingsException("History data table does not contain required columns. Missing: "
                    + Iterables.toString(missing));
        } else {
            this.historyDataSpec = dataTableSpec;
        }
    }

    private void configureDevDataSpec(final DataTableSpec dataTableSpec) throws InvalidSettingsException {
        Set<String> missing = findMissingColumnSubset(dataTableSpec, createDevDataColumnSpec());
        if (!missing.isEmpty()) {
            throw new InvalidSettingsException("Developers data table does not contain required column.  Missing: "
                    + Iterables.toString(missing));
        } else {
            this.developersDataSpec = dataTableSpec;
        }
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
