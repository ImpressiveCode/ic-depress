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
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.impressivecode.depress.its.ITSAdapterTableFactory;
import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSAdapterTransformer;
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
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.xml.sax.SAXException;

import com.google.common.base.Preconditions;

/**
 * @author Marek Majchrzak, ImpressiveCode
 * @author Maciej Borkowski, Capgemini Poland
 */
public class JiraAdapterNodeModel extends NodeModel {

	private static final NodeLogger LOGGER = NodeLogger.getLogger(JiraAdapterNodeModel.class);
	
    private static final String DEFAULT_VALUE = "";
    private static final String CONFIG_NAME = "depress.its.jira.confname";
    private static final String RADIO_DEFAULT_VALUE = "Trivial";
    private static final String RADIO_CONFIG_NAME = "depress.its.jira.radio";
    
    static final String UNKNOWN_CONFIG_NAME = "depress.its.jira.unknown";
    static final String BLOCKER_CONFIG_NAME = "depress.its.jira.blocker";
    static final String CRITICAL_CONFIG_NAME = "depress.its.jira.critical";
    static final String MAJOR_CONFIG_NAME = "depress.its.jira.major";
    static final String MINOR_CONFIG_NAME = "depress.its.jira.minor";
    static final String TRIVIAL_CONFIG_NAME = "depress.its.jira.trivial";

    private final SettingsModelString fileSettings = createFileChooserSettings();
    private final SettingsModelString radioSettings = createRadioSettings();
    //refactor
    private final SettingsModelStringArray unknownPrioritySettings = new SettingsModelStringArray(UNKNOWN_CONFIG_NAME, null);
    private final SettingsModelStringArray blockerPrioritySettings = new SettingsModelStringArray(BLOCKER_CONFIG_NAME, null);
    private final SettingsModelStringArray criticalPrioritySettings = new SettingsModelStringArray(CRITICAL_CONFIG_NAME, null);
    private final SettingsModelStringArray majorPrioritySettings = new SettingsModelStringArray(MAJOR_CONFIG_NAME, null);
    private final SettingsModelStringArray minorPrioritySettings = new SettingsModelStringArray(MINOR_CONFIG_NAME, null);
    private final SettingsModelStringArray trivialPrioritySettings = new SettingsModelStringArray(TRIVIAL_CONFIG_NAME, null);
    
    protected JiraAdapterNodeModel() {
        super(0, 1);
    }

	@Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec) throws Exception {
        LOGGER.info("Preparing to read jira entries.");
        String filePath = fileSettings.getStringValue();
        List<ITSDataType> entries = parseEntries(filePath);
        LOGGER.info("Transforming to jira entries.");
        BufferedDataTable out = transform(entries, exec);
        LOGGER.info("Jira table created.");
        return new BufferedDataTable[] { out };
    }

    private BufferedDataTable transform(final List<ITSDataType> entries, final ExecutionContext exec) throws CanceledExecutionException {
        ITSAdapterTransformer transformer = new ITSAdapterTransformer(ITSAdapterTableFactory.createDataColumnSpec());
        return transformer.transform(entries, exec);
    }

    private List<ITSDataType> parseEntries(final String filePath) throws ParserConfigurationException, SAXException,
    IOException, ParseException {
        return new JiraEntriesParser().parseEntries(filePath);
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
        radioSettings.saveSettingsTo(settings);
        unknownPrioritySettings.saveSettingsTo(settings);
        blockerPrioritySettings.saveSettingsTo(settings);
        criticalPrioritySettings.saveSettingsTo(settings);
        majorPrioritySettings.saveSettingsTo(settings);
        minorPrioritySettings.saveSettingsTo(settings);
        trivialPrioritySettings.saveSettingsTo(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        fileSettings.loadSettingsFrom(settings);
        radioSettings.loadSettingsFrom(settings);
        unknownPrioritySettings.loadSettingsFrom(settings);
        blockerPrioritySettings.loadSettingsFrom(settings);
        criticalPrioritySettings.loadSettingsFrom(settings);
        majorPrioritySettings.loadSettingsFrom(settings);
        minorPrioritySettings.loadSettingsFrom(settings);
        trivialPrioritySettings.loadSettingsFrom(settings);
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        fileSettings.validateSettings(settings);
        radioSettings.validateSettings(settings);
        unknownPrioritySettings.validateSettings(settings);
        blockerPrioritySettings.validateSettings(settings);
        criticalPrioritySettings.validateSettings(settings);
        majorPrioritySettings.validateSettings(settings);
        minorPrioritySettings.validateSettings(settings);
        trivialPrioritySettings.validateSettings(settings);
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

	static SettingsModelString createRadioSettings() {
		return new SettingsModelString(RADIO_CONFIG_NAME, RADIO_DEFAULT_VALUE);
	}
    
}
