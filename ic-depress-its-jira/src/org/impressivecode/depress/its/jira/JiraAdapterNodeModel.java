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
package org.impressivecode.depress.its.jira;

import static org.impressivecode.depress.its.jira.JiraAdapterTableFactory.createTableSpec;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.impressivecode.depress.its.ITSAdapterTableFactory;
import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSAdapterTransformer;
import org.impressivecode.depress.its.ITSPriority;
import org.impressivecode.depress.its.ITSType;
import org.impressivecode.depress.its.ITSResolution;
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
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.xml.sax.SAXException;

import com.google.common.base.Preconditions;

/**
 * @author Marek Majchrzak, ImpressiveCode
 * @author Maciej Borkowski, Capgemini Poland
 */
public class JiraAdapterNodeModel extends NodeModel {

    private static final NodeLogger LOGGER = NodeLogger.getLogger(JiraAdapterNodeModel.class);

    private static final String CHOOSER_DEFAULT_VALUE = "";

    private static final String CONFIG_NAME = "depress.its.jira.";
    static final String CHOOSER_CONFIG_NAME = CONFIG_NAME + "chooser";
    static final String PRIORITY_CONFIG_NAME = CONFIG_NAME + "priority";
    static final String TYPE_CONFIG_NAME = CONFIG_NAME + "type";
    static final String RESOLUTION_CONFIG_NAME = CONFIG_NAME + "resolution";
    static final String PRIORITY_BOOLEAN_CONFIG_NAME = CONFIG_NAME + "prioritybool";
    static final String TYPE_BOOLEAN_CONFIG_NAME = CONFIG_NAME + "typebool"; 
    static final String RESOLUTION_BOOLEAN_CONFIG_NAME = CONFIG_NAME + "resolutionbool";
    private final SettingsModelString fileSettings = createFileChooserSettings();
    private final SettingsModelBoolean priorityEnabled = createSettingsModelBoolean(PRIORITY_BOOLEAN_CONFIG_NAME, false);
    private final SettingsModelBoolean typeEnabled = createSettingsModelBoolean(TYPE_BOOLEAN_CONFIG_NAME, false);
    private final SettingsModelBoolean resolutionEnabled = createSettingsModelBoolean(RESOLUTION_BOOLEAN_CONFIG_NAME, false);
    private final HashMap<String, SettingsModelStringArray> groupSettings = new HashMap<String, SettingsModelStringArray>();

    protected JiraAdapterNodeModel() {
        super(0, 1);
        initializeSettings();
    }

    private void initializeSettings() {
        for (String label : ITSPriority.labels()) {
            groupSettings.put(label, new SettingsModelStringArray(PRIORITY_CONFIG_NAME + "." + label, null));
        }
        for (String label : ITSType.labels()) {
            groupSettings.put(label, new SettingsModelStringArray(TYPE_CONFIG_NAME + "." + label, null));
        }
        for (String label : ITSResolution.labels()) {
            groupSettings.put(label, new SettingsModelStringArray(RESOLUTION_CONFIG_NAME + "." + label, null));
        }
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {
        LOGGER.info("Preparing to read jira entries.");
        String filePath = fileSettings.getStringValue();
        List<ITSDataType> entries = parseEntries(filePath);
        LOGGER.info("Transforming to jira entries.");
        BufferedDataTable out = transform(entries, exec);
        LOGGER.info("Jira table created.");
        return new BufferedDataTable[] { out };
    }

    private HashMap<String, String[]> getSettings() {
        HashMap<String, String[]> currentSettings = new HashMap<String, String[]>();
        for (Entry<String, SettingsModelStringArray> entry : groupSettings.entrySet()) {
            currentSettings.put(entry.getKey(), entry.getValue().getStringArrayValue());
        }
        return currentSettings;
    }

    private BufferedDataTable transform(final List<ITSDataType> entries, final ExecutionContext exec)
            throws CanceledExecutionException {
        ITSAdapterTransformer transformer = new ITSAdapterTransformer(ITSAdapterTableFactory.createDataColumnSpec());
        return transformer.transform(entries, exec);
    }

    private List<ITSDataType> parseEntries(final String filePath) throws ParserConfigurationException, SAXException,
            IOException, ParseException {
        return new JiraEntriesParser(getSettings()).parseEntries(filePath);
    }

    @Override
    protected void reset() {
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        Preconditions.checkArgument(inSpecs.length == 0);
        return createTableSpec();
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        fileSettings.saveSettingsTo(settings);
        priorityEnabled.saveSettingsTo(settings);
        typeEnabled.saveSettingsTo(settings);
        resolutionEnabled.saveSettingsTo(settings);
        for (SettingsModelStringArray prioritySetting : groupSettings.values()) {
            prioritySetting.saveSettingsTo(settings);
        }
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        fileSettings.loadSettingsFrom(settings);
        priorityEnabled.loadSettingsFrom(settings);
        typeEnabled.loadSettingsFrom(settings);
        resolutionEnabled.loadSettingsFrom(settings);
        for (SettingsModelStringArray prioritySetting : groupSettings.values()) {
            prioritySetting.loadSettingsFrom(settings);
        }
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        fileSettings.validateSettings(settings);
        priorityEnabled.validateSettings(settings);
        typeEnabled.validateSettings(settings);
        resolutionEnabled.validateSettings(settings);
        for (SettingsModelStringArray prioritySetting : groupSettings.values()) {
            prioritySetting.validateSettings(settings);
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

    static SettingsModelString createFileChooserSettings() {
        return new SettingsModelString(CHOOSER_CONFIG_NAME, CHOOSER_DEFAULT_VALUE);
    }
    
    static SettingsModelBoolean createSettingsModelBoolean(final String config, final boolean defaultValue) {
        return new SettingsModelBoolean(config, defaultValue);
    }

}
