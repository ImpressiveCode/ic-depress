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

package org.impressivecode.depress.mr.ckjm;

import static org.impressivecode.depress.mr.ckjm.ChidamberKemererJavaMetricsTableFactory.createTableSpec;
import static org.impressivecode.depress.mr.ckjm.ChidamberKemererJavaMetricsTableFactory.createDataColumnSpec;
import static org.impressivecode.depress.mr.ckjm.ChidamberKemererJavaMetricsTableFactory.createTableRow;
import static org.impressivecode.depress.mr.ckjm.ChidamberKemererJavaMetricsEntriesParser.unmarshalResults;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.impressivecode.depress.mr.ckjm.ChidamberKemererJavaMetricsXmlResult.Class;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;


/**
 * @author Zuzanna Pacholczyk, Capgemini Poland
 */
public class ChidamberKemererJavaMetricsNodeModel extends NodeModel {
        
    private static final String DEFAULT_VALUE = "";

    private static final String CONFIG_NAME = "file chooser";

    private static final NodeLogger LOGGER = NodeLogger.getLogger(ChidamberKemererJavaMetricsNodeModel.class);

    private final SettingsModelString fileSettings = createFileChooserSettings();
    
    protected ChidamberKemererJavaMetricsNodeModel() {
    
        super(0, 1);
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

        LOGGER.info("Preparing to read judy logs.");
        BufferedDataContainer container = createDataContainer(exec);
        LOGGER.info("Reading file: " + fileSettings.getStringValue());
        List<Class> result = unmarshalResults(fileSettings.getStringValue());
        BufferedDataTable out = transform(container, result, exec);
        LOGGER.info("Reading judy logs finished.");

        return new BufferedDataTable[] { out };
    }

    @Override
    protected void reset() {
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
        
    	return createTableSpec();
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	fileSettings.saveSettingsTo(settings); 
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        fileSettings.loadSettingsFrom(settings);
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	fileSettings.validateSettings(settings);
    }
    
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    	//NOOP
    }  

    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    	//NOOP
    }
    
    private BufferedDataTable transform(final BufferedDataContainer container, final List<Class> classes,
            final ExecutionContext exec) throws CanceledExecutionException {
        int size = classes.size();
        for (int i = 0; i < size; i++) {
            Class clazz = classes.get(i);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Transforming ckjm log, class: " + clazz.getName());
            }

            addRowToTable(container, clazz);
        }
        container.close();
        BufferedDataTable out = container.getTable();
        return out;
    }

    private BufferedDataContainer createDataContainer(final ExecutionContext exec) {
        DataTableSpec outputSpec = createDataColumnSpec();
        BufferedDataContainer container = exec.createDataContainer(outputSpec);
        return container;
    }

    private void addRowToTable(final BufferedDataContainer container, final Class clazz) {
        container.addRowToTable(createTableRow(clazz));
    }
    
    static SettingsModelString createFileChooserSettings() {
        return new SettingsModelString(CONFIG_NAME, DEFAULT_VALUE);
    }

}

