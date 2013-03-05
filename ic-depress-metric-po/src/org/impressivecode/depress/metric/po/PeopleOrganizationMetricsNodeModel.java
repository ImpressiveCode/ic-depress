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
package org.impressivecode.depress.metric.po;

import static org.impressivecode.depress.metric.po.MetricProcessor.processHistory;
import static org.impressivecode.depress.metric.po.MetricProcessor.updateOrganization;
import static org.impressivecode.depress.metric.po.PeopleOrganizationMetricTableFactory.createDataColumnSpec;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.impressivecode.depress.common.DataTableSpecUtils;
import org.impressivecode.depress.scm.SCMAdapterTableFactory;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.container.CloseableRowIterator;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.google.common.collect.Maps;

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
        BufferedDataContainer container = createDataContainer(exec);
        Map<String, POData> poData = computeMetric(inData[0], inData[1], exec);
        LOGGER.info("Transforming to build PO data.");
        BufferedDataTable out = transform(container, poData, exec);
        LOGGER.info("Building PO Metric finished.");

        return new BufferedDataTable[] { out };
    }

    @Override
    protected void reset() {
        this.developersDataSpec = null;
        this.historyDataSpec = null;
    }

    private Map<String, POData> computeMetric(final BufferedDataTable devTable, final BufferedDataTable changeHistory,
            final ExecutionContext exec) throws CanceledExecutionException {
        Map<String, POData> poDataMetric = Maps.newHashMap();
        processChangeHistory(changeHistory, devTable, exec, poDataMetric);
        processOrganizationData(exec, poDataMetric);
        return poDataMetric;
    }

    private void processOrganizationData(final ExecutionContext exec, final Map<String, POData> poDataMetric) {
        for (Map.Entry<String, POData> entry : poDataMetric.entrySet()) {
            updateOrganization(entry.getValue());
        }
    }

    private Map<String, TeamMemberData> createDevData(final BufferedDataTable devTable) {
        Map<String, TeamMemberData> devMap = Maps.newHashMap();
        CloseableRowIterator iterator = devTable.iterator();
        TeamMemberTransformer transformer = new TeamMemberTransformer(developersDataSpec);
        while (iterator.hasNext()) {
            TeamMemberData value = transformer.transform(iterator.next());
            devMap.put(value.getName(), value);
        }
        return devMap;
    }

    private void processChangeHistory(final BufferedDataTable changeHistory, final BufferedDataTable devTable,
            final ExecutionContext exec, final Map<String, POData> poDataMetric) throws CanceledExecutionException {
        CloseableRowIterator iterator = changeHistory.iterator();
        Map<String, TeamMemberData> devMap = createDevData(devTable);
        ChangeHistoryTransformer transformer = new ChangeHistoryTransformer(historyDataSpec, devMap);
        while (iterator.hasNext()) {
            progress(exec);
            POData curr = transformer.transform(iterator.next());
            POData existing = poDataMetric.get(curr.getClassName());
            POData processed = processHistory(existing, curr);
            poDataMetric.put(processed.getClassName(), processed);
        }
    }

    private BufferedDataTable transform(final BufferedDataContainer container, final Map<String, POData> poData,
            final ExecutionContext exec) throws CanceledExecutionException {

        BufferedDataTable out = container.getTable();
        return out;
    }

    private BufferedDataContainer createDataContainer(final ExecutionContext exec) {
        DataTableSpec outputSpec = createDataColumnSpec();
        BufferedDataContainer container = exec.createDataContainer(outputSpec);
        return container;
    }

    private void progress(final ExecutionContext exec) throws CanceledExecutionException {
        exec.checkCanceled();
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
        if (SCMAdapterTableFactory.createDataColumnSpec().equals(dataTableSpec)) {
            throw new InvalidSettingsException("History data table does not contain required column");
        } else {
            this.historyDataSpec = dataTableSpec;
        }
    }

    private void configureDevDataSpec(final DataTableSpec dataTableSpec) throws InvalidSettingsException {
        if (!DataTableSpecUtils.containsColumnSubset(dataTableSpec,
                PeopleOrganizationMetricTableFactory.createDevDataColumnSpec())) {
            throw new InvalidSettingsException("Developers data table does not contain required column");
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

    //
    // private void analyzeChangeHistory(final BufferedDataTable history, final
    // ExecutionContext exec, final Map<String, POData> targetData)
    // throws CanceledExecutionException {
    // // CloseableRowIterator iterator = history.iterator();
    // // while (iterator.hasNext()) {
    // // DataRow row = iterator.next();
    // // int classColIndex =
    // historyDataSpec.findColumnIndex(SCMDataTableSpecFactory.CLASS_COLNAME);
    // // int authorColIndex =
    // historyDataSpec.findColumnIndex(SCMDataTableSpecFactory.AUTHOR_COLNAME);
    // // StringCell classCell = (StringCell) row.getCell(classColIndex);
    // // StringCell authorCell = (StringCell) row.getCell(authorColIndex);
    // // POData poData = targetData.get(classCell.getStringValue());
    // // if (poData != null) {
    // // poData.getInvolvedDevelopers().add(authorCell.getStringValue());
    // // poData.setEF(1 + poData.getEF());
    // // }
    // // }
    // }
    //
    // private void analyzeInvolvementHistory(final BufferedDataTable
    // developers, final Map<String, POData> targetData) {
    // for (Map.Entry<String, POData> targetEntries : targetData.entrySet()) {
    // POData poData = targetEntries.getValue();
    // Set<String> involvedDevelopers = new
    // HashSet<String>(poData.getInvolvedDevelopers());
    // poData.setNOE(involvedDevelopers.size());
    // poData.setNOEE(getNumberOfExEngineers(involvedDevelopers, developers));
    // poData.setNOE1(getNumberOfEngineersForGivenLevel(involvedDevelopers,
    // developers, "1"));
    // poData.setNOE2(getNumberOfEngineersForGivenLevel(involvedDevelopers,
    // developers, "2"));
    // poData.setNOE3(getNumberOfEngineersForGivenLevel(involvedDevelopers,
    // developers, "3"));
    // poData.setNOE4(getNumberOfEngineersForGivenLevel(involvedDevelopers,
    // developers, "4"));
    // poData.setNOE5(getNumberOfEngineersForGivenLevel(involvedDevelopers,
    // developers, "5"));
    // }
    // }
    //
    // private Integer getNumberOfEngineersForGivenLevel(final Set<String>
    // involvedDevelopers, final BufferedDataTable developers,
    // final String level) {
    // int numberOfEingineers = 0;
    // // for (String engineer : involvedDevelopers) {
    // // StringCell devLevel = (StringCell)
    // getDataCellForGivenEngineerAndAttribute(developers, engineer,
    // //
    // developersDataSpec.findColumnIndex(DevDataTableSpecFactory.EXPIERIENCE_COLNAME));
    // // if (StringUtils.equals(devLevel.getStringValue(), level)) {
    // // numberOfEingineers++;
    // // }
    // // }
    // //
    // return numberOfEingineers;
    // }
    //
    // private Integer getNumberOfExEngineers(final Set<String>
    // involvedDevelopers, final BufferedDataTable developers) {
    // // int numberOfExEingineers = 0;
    // // for (String engineer : involvedDevelopers) {
    // // if (isExEngineer(developers, engineer)) {
    // // numberOfExEingineers++;
    // // }
    // // }
    //
    // return numberOfExEingineers;
    // }
    //
    // private DataCell getDataCellForGivenEngineerAndAttribute(final
    // BufferedDataTable developers, final String engineer, final int colIndex)
    // {
    // // CloseableRowIterator iterator = developers.iterator();
    // // while (iterator.hasNext()) {
    // // DataRow rowData = iterator.next();
    // // StringCell engineerCell = (StringCell)
    // rowData.getCell(developersDataSpec
    // // .findColumnIndex(DevDataTableSpecFactory.DEVELOPER_COLNAME));
    // // if (engineerCell.getStringValue().equals(engineer)) {
    // // DataCell cell = rowData.getCell(colIndex);
    // // iterator.close();
    // // return cell;
    // // }
    // // }
    //
    // throw new IllegalStateException("Unable to find engineer: " + engineer);
    // }
    //
    // // private boolean isExEngineer(final BufferedDataTable developers, final
    // String engineer) {
    // // StringCell cell = (StringCell)
    // getDataCellForGivenEngineerAndAttribute(developers, engineer,
    // //
    // developersDataSpec.findColumnIndex(DevDataTableSpecFactory.EX_ENGINEER_COLNAME));
    // // return BooleanUtils.toBoolean(cell.getStringValue());
    // // }
    //
    // private void prepareBaseDate(final BufferedDataTable target, final
    // Map<String, POData> targetData) {
    // CloseableRowIterator iterator = target.iterator();
    // while (iterator.hasNext()) {
    // DataRow row = iterator.next();
    // targetData.put(row.getKey().getString(), new POData());
    // }
    // }
    //
    // private BufferedDataTable[] convert2TableModel(final ExecutionContext
    // exec, final Map<String, POData> targetData) {
    // // BufferedDataContainer container = createDataContainer(exec);
    // // for (Map.Entry<String, POData> entry : targetData.entrySet()) {
    // // DataCell[] cells = createCells(entry.getValue());
    // //
    // // DataRow row = new DefaultRow(entry.getKey(), cells);
    // // container.addRowToTable(row);
    // // }
    // //
    // // container.close();
    // // BufferedDataTable out = container.getTable();
    // // return new BufferedDataTable[] { out };
    // }
    //
    //
    //

}
