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
package org.impressivecode.depress.metric.datasource;

import static com.google.common.base.Preconditions.checkArgument;
import static org.impressivecode.depress.metric.datasource.DataSourceAdapterTableFactory.createDataColumnSpec;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

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
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.xml.sax.SAXException;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class DataSourceAdapterNodeModel extends NodeModel {

    private static final NodeLogger LOGGER = NodeLogger.getLogger(DataSourceAdapterNodeModel.class);

    private static final String DEFAULT_VALUE = "";

    private static final String CONFIG_NAME = "depress.metric.datasource.confname";

    private final SettingsModelString fileSettings = createFileChooserSettings();
    
    protected DataSourceAdapterNodeModel() {
        super(0, 1);  
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {

        LOGGER.info("Preparing to read datasource entries.");
        String datasourceFilePath = fileSettings.getStringValue();
        ArrayList<ClassDataEntry> entries = parseEntries(datasourceFilePath);
        LOGGER.info("Transforming to datasource entries.");
        BufferedDataTable out = transform(entries, exec);
        LOGGER.info("datasource table created.");
        return new BufferedDataTable[] { out };
    }

    private BufferedDataTable transform(final ArrayList<ClassDataEntry> entries, final ExecutionContext exec)
            throws CanceledExecutionException {
        DataSourceAdapterTransformer transformer = new DataSourceAdapterTransformer(createDataColumnSpec());
        return transformer.transform(entries, exec);
    }

    private ArrayList<ClassDataEntry> parseEntries(final String datasourceFilePath) throws ParserConfigurationException,
    SAXException, IOException {
    	ClassDataEntriesParser parser = new ClassDataEntriesParser();
        return parser.parseEntries(datasourceFilePath);
    }

    @Override
    protected void reset() {
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        checkArgument(inSpecs.length == 0, "Invalid state");
        return new DataTableSpec[] { createDataColumnSpec() };
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        fileSettings.saveSettingsTo(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        fileSettings.loadSettingsFrom(settings);
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        fileSettings.validateSettings(settings);
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

    static SettingsModelString createFileChooserSettings() {
        return new SettingsModelString(CONFIG_NAME, DEFAULT_VALUE);
    }
    
    static SettingsModelString BBBB() {
        return new SettingsModelString(CONFIG_NAME, DEFAULT_VALUE);
    }
}
