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
import static org.impressivecode.depress.scm.SCMAdapterTableFactory.AUTHOR_COLSPEC;
import static org.impressivecode.depress.scm.SCMAdapterTableFactory.RESOURCE_NAME;
import static org.impressivecode.depress.support.commonmarker.MarkerAdapterTableFactory.AM_MARKER_COLSPEC;
import static org.impressivecode.depress.support.commonmarker.MarkerAdapterTableFactory.EXT_MARKER_COLSPEC;
import static org.impressivecode.depress.support.commonmarker.MarkerAdapterTableFactory.MARKER_COLSPEC;
import static org.impressivecode.depress.support.commonmarker.MarkerAdapterTableFactory.SEMANTIC_CONFIDENCE_COLSPEC;

import java.io.File;
import java.io.IOException;

import org.impressivecode.depress.common.InputTransformer;
import org.impressivecode.depress.its.ITSAdapterTableFactory;
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
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import com.google.common.base.Preconditions;

/**
 * 
 * @author Marek Majchrzak, Michal Jawulski, Piotr Lewicki, Maciej Luzniak,
 *         ImpressiveCode
 * 
 */
public class SemanticAnalysisNodeModel extends NodeModel {

    private static final NodeLogger LOGGER = NodeLogger.getLogger(SemanticAnalysisNodeModel.class);

    static final String CFG_RESOLUTION_WEIGHT = "resolution weight";
    static final String CFG_AUTHOR_WEIGHT = "author weight";
    static final String CFG_COMPARSION_LIMIT = "comparsion limit";
    static final String CFG_MSC_COMPARSION_OBJECT = "msc comparsion object";  
    static final String CFG_SELECTED_ALGORITHM = "selected algorithm";
    static final String CFG_SIMILARITY_WEIGHT = "similarity weight";
    
    static final Integer RESOLUTION_WEIGHT_DEFAULT = 1;
    static final Integer AUTHOR_WEIGHT_DEFAULT = 1;
    static final Integer COMPARSION_LIMIT_DEFAULT = 60;
    static final String MSC_COMPARSION_OBJECT_DEFAULT = Configuration.MSC_DT_SUMMARY;
    static final String CFG_SELECTED_ALGORITHM_DEFAULT = Configuration.LEVENSTHEIN_ALGHORITM;
    static final Integer SIMILARITY_WEIGHT_DEFAULT = 1;
    static final Integer SUM = RESOLUTION_WEIGHT_DEFAULT + AUTHOR_WEIGHT_DEFAULT + SIMILARITY_WEIGHT_DEFAULT;
    
    private final SettingsModelInteger resolutionWeight = new SettingsModelIntegerBounded(CFG_RESOLUTION_WEIGHT,
            RESOLUTION_WEIGHT_DEFAULT, 0, SUM);
    private final SettingsModelInteger authorWeight = new SettingsModelIntegerBounded(CFG_AUTHOR_WEIGHT,
            AUTHOR_WEIGHT_DEFAULT, 0, SUM);
    private final SettingsModelInteger comparsionLimit = new SettingsModelIntegerBounded(CFG_COMPARSION_LIMIT,
            COMPARSION_LIMIT_DEFAULT, 0, 100);
    private final SettingsModelString mscComparsionObject = new SettingsModelString(CFG_MSC_COMPARSION_OBJECT,
            Configuration.MSC_DT_SUMMARY);
    private final SettingsModelString selectedAlgorithm = new SettingsModelString(CFG_SELECTED_ALGORITHM,
            CFG_SELECTED_ALGORITHM_DEFAULT);
    private final SettingsModelInteger similarityWeight = new SettingsModelIntegerBounded(CFG_SIMILARITY_WEIGHT,
    		SIMILARITY_WEIGHT_DEFAULT, 0, SUM);
   

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
        return new SemanticAnalysisCellFactory(new Configuration(itsData, authorWeight.getIntValue(),
                resolutionWeight.getIntValue(), comparsionLimit.getIntValue(), mscComparsionObject.getStringValue(),
                selectedAlgorithm.getStringValue(), similarityWeight.getIntValue()), this.scmTransfomer, this.markerTransformer);
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

        this.scmTransfomer.setMinimalSpec(new DataTableSpec(RESOURCE_NAME, AUTHOR_COLSPEC)).setInputSpec(inSpecs[0])
                .validate();
        this.itsTransfomer
                .setMinimalSpec(new DataTableSpec(ISSUE_ID_COLSPEC, ITSAdapterTableFactory.RESOLUTION_COLSPEC))
                .setInputSpec(inSpecs[1]).validate();
        this.markerTransformer.setMinimalOrSpec(AM_MARKER_COLSPEC, EXT_MARKER_COLSPEC, MARKER_COLSPEC)
                .setInputSpec(inSpecs[0]).validate();
        final DataTableSpec dts = AppendedColumnTable.getTableSpec(inSpecs[0], SEMANTIC_CONFIDENCE_COLSPEC);

        return new DataTableSpec[] { dts };
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        authorWeight.saveSettingsTo(settings);
        resolutionWeight.saveSettingsTo(settings);
        comparsionLimit.saveSettingsTo(settings);
        mscComparsionObject.saveSettingsTo(settings);
        selectedAlgorithm.saveSettingsTo(settings);
        similarityWeight.saveSettingsTo(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        authorWeight.loadSettingsFrom(settings);
        resolutionWeight.loadSettingsFrom(settings);
        comparsionLimit.loadSettingsFrom(settings);
        mscComparsionObject.loadSettingsFrom(settings);
        selectedAlgorithm.loadSettingsFrom(settings);
        similarityWeight.loadSettingsFrom(settings);
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        authorWeight.validateSettings(settings);
        resolutionWeight.validateSettings(settings);
        comparsionLimit.validateSettings(settings);
        mscComparsionObject.validateSettings(settings);
        selectedAlgorithm.validateSettings(settings);
        similarityWeight.validateSettings(settings);

        int current = settings.getInt(CFG_RESOLUTION_WEIGHT) + settings.getInt(CFG_AUTHOR_WEIGHT) +  settings.getInt(CFG_SIMILARITY_WEIGHT);
        if (current != SUM) {
            throw new InvalidSettingsException("Weight sum has to be " + SUM);
        }
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
