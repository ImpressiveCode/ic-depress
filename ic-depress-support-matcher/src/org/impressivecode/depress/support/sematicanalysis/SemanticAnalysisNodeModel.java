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
package org.impressivecode.depress.support.sematicanalysis;

import static org.impressivecode.depress.its.ITSAdapterTableFactory.ISSUE_ID_COLSPEC;
import static org.impressivecode.depress.its.ITSAdapterTableFactory.RESOLVED_DATE_COLSPEC;
import static org.impressivecode.depress.scm.SCMAdapterTableFactory.AUTHOR_COLSPEC;
import static org.impressivecode.depress.support.commonmarker.MarkerAdapterTableFactory.AM_MARKER_COLSPEC;
import static org.impressivecode.depress.support.commonmarker.MarkerAdapterTableFactory.EXT_MARKER_COLSPEC;
import static org.impressivecode.depress.support.commonmarker.MarkerAdapterTableFactory.MARKER_COLSPEC;
import static org.impressivecode.depress.support.commonmarker.MarkerAdapterTableFactory.SEMANTIC_CONFIDENCE_COLSPEC;

import java.io.File;
import java.io.IOException;

import org.impressivecode.depress.common.InputTransformer;
import org.impressivecode.depress.its.ITSDataHolder;
import org.impressivecode.depress.its.ITSInputTransformer;
import org.impressivecode.depress.scm.SCMDataType;
import org.impressivecode.depress.scm.SCMInputTransformer;
import org.impressivecode.depress.support.commonmarker.MarkerInputTransformer;
import org.knime.base.data.append.column.AppendedColumnTable;
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
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;

import com.google.common.base.Preconditions;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class SemanticAnalysisNodeModel extends NodeModel {

    private static final NodeLogger LOGGER = NodeLogger.getLogger(SemanticAnalysisNodeModel.class);

    private final SettingsModelInteger interval = new SettingsModelInteger(CFG_INTERVAL, INTERVAL_DEFAULT);
    static final String CFG_INTERVAL = "depress.support.matcher.sematicanalysis.interval";
    static final Integer INTERVAL_DEFAULT = 15;

    private final SettingsModelInteger intervalWeight = new SettingsModelInteger(CFG_INTERVAL, INTERVAL_DEFAULT);
    static final String CFG_INTERVAL_WEIGHT = "depress.support.matcher.sematicanalysis.intervalweight";
    static final Integer INTERVAL_WEIGHT_DEFAULT = 15;

    private ITSInputTransformer itsTransfomer;
    private InputTransformer<SCMDataType> scmTransfomer;

    private MarkerInputTransformer markerTransformer;

    protected SemanticAnalysisNodeModel() {
        super(2, 1);
        this.markerTransformer = new MarkerInputTransformer();
        this.scmTransfomer = new SCMInputTransformer();
        this.itsTransfomer = new ITSInputTransformer();
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {
        try {
            AppendedColumnTable table = new AppendedColumnTable(inData[0], cellFactory(inData),
                    SEMANTIC_CONFIDENCE_COLSPEC);
            return new BufferedDataTable[] { preapreTable(table, exec) };
        } catch (Exception ex) {
            LOGGER.error("Unable to perform semantic analysis.", ex);
            throw ex;
        }
    }

    private SemanticAnalysisCellFactory cellFactory(final BufferedDataTable[] inData) {
        ITSDataHolder itsData = itsTransfomer.transformToDataHolder(inData[1]);
        return new SemanticAnalysisCellFactory(new Configuration(itsData), this.scmTransfomer, this.markerTransformer);
    }

    private BufferedDataTable preapreTable(final AppendedColumnTable table, final ExecutionContext exec)
            throws CanceledExecutionException {
        return exec.createBufferedDataTable(table, exec);
    }

    @Override
    protected void reset() {
        // NOOP
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        Preconditions.checkArgument(inSpecs.length == 2);

        this.scmTransfomer.setMinimalSpec(new DataTableSpec(AUTHOR_COLSPEC)).setInputSpec(inSpecs[0]).validate();
        this.itsTransfomer.setMinimalSpec(new DataTableSpec(ISSUE_ID_COLSPEC, RESOLVED_DATE_COLSPEC)).setInputSpec(inSpecs[1]).validate();
        this.markerTransformer.setMinimalOrSpec(AM_MARKER_COLSPEC, EXT_MARKER_COLSPEC, MARKER_COLSPEC).setInputSpec(inSpecs[0]).validate();
        final DataTableSpec dts = AppendedColumnTable.getTableSpec(inSpecs[0], SEMANTIC_CONFIDENCE_COLSPEC);

        return new DataTableSpec[] { dts };
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        interval.saveSettingsTo(settings);
        intervalWeight.saveSettingsTo(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        interval.loadSettingsFrom(settings);
        intervalWeight.loadSettingsFrom(settings);
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        interval.validateSettings(settings);
        intervalWeight.validateSettings(settings);
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
