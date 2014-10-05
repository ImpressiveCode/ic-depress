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

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.knime.core.util.KnimeEncryption.decrypt;
import static org.knime.core.util.KnimeEncryption.encrypt;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.impressivecode.depress.common.SettingsModelMultiFilter;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * @author Maciej Borkowski, Capgemini Poland
 */
public abstract class ITSOnlineNodeModel extends NodeModel {
    protected static final NodeLogger LOGGER = NodeLogger.getLogger(ITSOnlineNodeModel.class);

    private static final String URL_PATTERN = "^https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    protected static final String DEFAULT_STRING_VALUE = "";

    private static final String CFG_ITS_URL = "url";
    private static final String CFG_ITS_Login = "login";
    private static final String CFG_ITS_PASSWORD = "password";
    private static final String CFG_ITS_SELECTION = "project";
    private static final String CFG_ITS_ALL_PROJECTS = "allprojects check";

    private final SettingsModelString urlSettings = createURLSettings();
    private final SettingsModelString loginSettings = createLoginSettings();
    private final SettingsModelString passwordSettings = createPasswordSettings();
    private final SettingsModelString selectionSettings = createSettingsSelection();
    private final SettingsModelBoolean allProjectsSettings = createSettingsCheckAllProjects();
    protected static ITSMappingManager mappingManager = createMapping();

    protected ITSOnlineNodeModel(final int inputPorts, final int outputPorts) {
        super(inputPorts, outputPorts);
    }

    protected String getURL() {
        return urlSettings.getStringValue();
    }

    protected boolean isUsernameProvided(String username) {
        return !isNullOrEmpty(username);
    }

    protected String getPassword() throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException,
            UnsupportedEncodingException, IOException {
        return decrypt(passwordSettings.getStringValue());
    }

    protected String getLogin() {
        return loginSettings.getStringValue();
    }

    protected String getProductName() {
        if (allProjectsSettings.getBooleanValue() || selectionSettings.getStringValue() == "") {
            return null;
        }
        return selectionSettings.getStringValue();
    }

    public static SettingsModelString createURLSettings() {
        return new SettingsModelString(CFG_ITS_URL, DEFAULT_STRING_VALUE);
    }

    public static SettingsModelString createLoginSettings() {
        return new SettingsModelString(CFG_ITS_Login, DEFAULT_STRING_VALUE);
    }

    public static SettingsModelString createPasswordSettings() {
        return new SettingsModelString(CFG_ITS_PASSWORD, DEFAULT_STRING_VALUE);
    }

    public static SettingsModelString createSettingsSelection() {
        return new SettingsModelString(CFG_ITS_SELECTION, DEFAULT_STRING_VALUE);
    }

    public static SettingsModelBoolean createSettingsCheckAllProjects() {
        return new SettingsModelBoolean(CFG_ITS_ALL_PROJECTS, true);
    }

    public static ITSMappingManager createMapping() {
        return new ITSMappingManager();
    }

    @Override
    protected void saveSettingsTo(NodeSettingsWO settings) {
        urlSettings.saveSettingsTo(settings);
        loginSettings.saveSettingsTo(settings);
        try {
            String password = encrypt(passwordSettings.getStringValue().toCharArray());
            passwordSettings.setStringValue(password);
            passwordSettings.saveSettingsTo(settings);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
            LOGGER.error("Could not encrypt password, reason: " + e.getMessage(), e);
        }
        selectionSettings.saveSettingsTo(settings);
        allProjectsSettings.saveSettingsTo(settings);
        for (SettingsModelMultiFilter model : mappingManager.getModels()) {
            model.saveSettingsTo(settings);
        }
        saveSpecificSettingsTo(settings);
    }

    @Override
    protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
        urlSettings.validateSettings(settings);
        loginSettings.validateSettings(settings);
        passwordSettings.validateSettings(settings);
        selectionSettings.validateSettings(settings);
        allProjectsSettings.validateSettings(settings);
        for (SettingsModelMultiFilter model : mappingManager.getModels()) {
            model.validateSettings(settings);
        }
        SettingsModelString url = urlSettings.createCloneWithValidatedValue(settings);
        if (!isNullOrEmpty(url.getStringValue()) && !url.getStringValue().matches(URL_PATTERN)) {
            throw new InvalidSettingsException("Invalid URL address. Valid example: 'https://website.com'");
        }
        validateSpecificSettings(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
        urlSettings.loadSettingsFrom(settings);
        loginSettings.loadSettingsFrom(settings);
        passwordSettings.loadSettingsFrom(settings);
        selectionSettings.loadSettingsFrom(settings);
        allProjectsSettings.loadSettingsFrom(settings);
        for (SettingsModelMultiFilter model : mappingManager.getModels()) {
            model.loadSettingsFrom(settings);
        }
        loadSpecificSettingsFrom(settings);
    }

    @Override
    protected void reset() {
    }

    @Override
    protected void loadInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    }

    @Override
    protected void saveInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    }

    protected abstract void saveSpecificSettingsTo(NodeSettingsWO settings);

    protected abstract void validateSpecificSettings(NodeSettingsRO settings) throws InvalidSettingsException;

    protected abstract void loadSpecificSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException;

}
