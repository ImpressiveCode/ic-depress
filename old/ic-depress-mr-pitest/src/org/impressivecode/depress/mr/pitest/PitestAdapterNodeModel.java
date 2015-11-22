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

package org.impressivecode.depress.mr.pitest;
import static com.google.common.base.Preconditions.checkArgument;
import static org.impressivecode.depress.mr.MRAdapterTableFactory.createDataColumnSpec;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.impressivecode.depress.mr.pitest.PitestAdapterTransformer;
import org.impressivecode.depress.mr.pitest.PitestEntriesParser;
import org.impressivecode.depress.mr.pitest.PitestEntry;
import org.knime.core.data.DataTableSpec;
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
import org.xml.sax.SAXException;


/**
 * 
 *
 * @author Zuzanna Pacholczyk, Capgemini Polska
 */
public class PitestAdapterNodeModel extends NodeModel {
    
    // the logger instance
    private static final NodeLogger LOGGER = NodeLogger
            .getLogger(PitestAdapterNodeModel.class);
        
    public static final String DEFAULT_VALUE = "";
    public static final String CONFIG_NAME = "file chooser";  
    public static final String FILE_EXTENSION = ".xml";
   
    private final SettingsModelString fileSettings = createFileChooserSettings();
    
    protected PitestAdapterNodeModel() {
        super(0, 1);
    }

    @Override
    protected void reset() {
    }

    private List<PitestEntry> parseEntries(final String pitestFilePath) throws ParserConfigurationException,
    SAXException, IOException {
        PitestEntriesParser parser = new PitestEntriesParser();
        return parser.parseEntries(pitestFilePath);
    }    
    
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        checkArgument(inSpecs.length == 0, "Invalid state");
        return new DataTableSpec[] { createDataColumnSpec() };
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {

        LOGGER.info("Preparing to read pitest entries.");
        String pitestFilePath = fileSettings.getStringValue();
        List<PitestEntry> entries = parseEntries(pitestFilePath);
        LOGGER.info("Transforming to pitest entries.");
        BufferedDataTable out = transform(entries, exec);
        LOGGER.info("Pitest table created.");
        return new BufferedDataTable[] { out };
    }

    private BufferedDataTable transform(final List<PitestEntry> entries, final ExecutionContext exec)
            throws CanceledExecutionException {
        PitestAdapterTransformer transformer = new PitestAdapterTransformer(createDataColumnSpec());
        return transformer.transform(entries, exec);
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
        // NOOP
    }
    
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // NOOP
    }
    
    static SettingsModelString createFileChooserSettings() {
        return new SettingsModelString(CONFIG_NAME, DEFAULT_VALUE);
    }

}

