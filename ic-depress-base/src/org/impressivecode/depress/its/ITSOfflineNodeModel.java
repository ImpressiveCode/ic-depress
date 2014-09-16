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
package org.impressivecode.depress.its;

import java.io.File;
import java.io.IOException;

import org.impressivecode.depress.common.SettingsModelMultiFilter;
import org.impressivecode.depress.its.ITSMappingManager;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * @author Maciej Borkowski, Capgemini Poland
 */
public abstract class ITSOfflineNodeModel extends NodeModel {
    protected static final NodeLogger LOGGER = NodeLogger.getLogger(ITSOfflineNodeModel.class);

    protected static final String CHOOSER_DEFAULT_VALUE = "";

    static final String CHOOSER_CONFIG_NAME = "file chooser";

    protected final SettingsModelString fileSettings = createFileChooserSettings();
    protected static ITSMappingManager mappingManager = createMapping();

    protected ITSOfflineNodeModel() {
        super(0, 1);
    }

    @Override
    protected void reset() {
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        fileSettings.saveSettingsTo(settings);
        for (SettingsModelMultiFilter model : mappingManager.getModels()) {
            model.saveSettingsTo(settings);
        }
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        fileSettings.loadSettingsFrom(settings);
        for (SettingsModelMultiFilter model : mappingManager.getModels()) {
            model.loadSettingsFrom(settings);
        }
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        fileSettings.validateSettings(settings);
        for (SettingsModelMultiFilter model : mappingManager.getModels()) {
            model.validateSettings(settings);
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

    static ITSMappingManager createMapping() {
        return new ITSMappingManager();
    }

}
