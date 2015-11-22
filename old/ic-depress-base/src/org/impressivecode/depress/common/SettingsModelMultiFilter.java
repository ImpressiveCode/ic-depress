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
package org.impressivecode.depress.common;

import java.util.HashMap;
import java.util.Map;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;

import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;

/* 
 * @author Maciej Borkowski, Capgemini Poland
 */
public class SettingsModelMultiFilter extends SettingsModel {
    private final String configName;
    private final SettingsModelBoolean enabledModel;
    private final String[] radioLabels;
    private final SettingsModelString radioModel;
    private final HashMap<String, String[]> included;
    private String[] excluded;

    /**
     * Creates a new object holding all parameters needed by
     * MultiFilterComponent.
     * 
     * @param configName
     *            the identifier of model, take care of its uniqueness
     * @param enabled
     *            the default state of component
     * @param radioLabels
     *            labels defining groups
     */
    public SettingsModelMultiFilter(final String configName, final boolean enabled, final String[] radioLabels) {
        super();
        this.configName = configName;
        this.enabledModel = new SettingsModelBoolean(configName + ".enabled", enabled);
        this.radioLabels = radioLabels;
        this.radioModel = new SettingsModelString(configName + ".radio", radioLabels[0]);
        this.included = new HashMap<String, String[]>();
        for (String label : radioLabels) {
            this.included.put(label, new String[] {});
        }
        this.excluded = new String[] {};
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    protected SettingsModelMultiFilter createClone() {
        return new SettingsModelMultiFilter(configName, enabledModel.getBooleanValue(), radioLabels);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getModelTypeID() {
        return "SMID_multifilter";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getConfigName() {
        return configName;
    }

    public SettingsModelBoolean getEnabledModel() {
        return enabledModel;
    }

    public SettingsModelString getRadioModel() {
        return radioModel;
    }

    public HashMap<String, String[]> getIncluded() {
        return included;
    }

    public String[] getExcluded() {
        return excluded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadSettingsForDialog(NodeSettingsRO settings, PortObjectSpec[] specs)
            throws NotConfigurableException {
        try {
            for (Map.Entry<String, String[]> entry : included.entrySet()) {
                setIncludedValue(entry.getKey(),
                        settings.getStringArray(configName + "." + entry.getKey(), entry.getValue()));
            }
            setExcludedValue(settings.getStringArray(configName + ".excluded", excluded));
        } catch (IllegalArgumentException iae) {
            // if the argument is not accepted: keep the old value.
        }
    }

    public void setExcludedValue(String[] newValue) {
        if (newValue == null) {
            excluded = null;
        } else {
            excluded = new String[newValue.length];
            System.arraycopy(newValue, 0, excluded, 0, newValue.length);
        }
    }

    public void setIncludedValue(final String key, final String[] newValue) {
        String[] value = new String[newValue.length];
        System.arraycopy(newValue, 0, value, 0, newValue.length);
        included.put(key, newValue);
    }

    public String[] getRadio() {
        return radioLabels;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsForDialog(NodeSettingsWO settings) throws InvalidSettingsException {
        saveSettingsForModel(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettingsForModel(NodeSettingsRO settings) throws InvalidSettingsException {
        radioModel.validateSettings(settings);
        enabledModel.validateSettings(settings);
        for (Map.Entry<String, String[]> entry : included.entrySet()) {
            settings.getStringArray(configName + "." + entry.getKey());
        }
        settings.getStringArray(configName + ".excluded", excluded);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadSettingsForModel(NodeSettingsRO settings) throws InvalidSettingsException {
        radioModel.loadSettingsFrom(settings);
        enabledModel.loadSettingsFrom(settings);
        try {
            // no default value, throw an exception instead
            for (Map.Entry<String, String[]> entry : included.entrySet()) {
                setIncludedValue(entry.getKey(), settings.getStringArray(configName + "." + entry.getKey()));
            }
            setExcludedValue(settings.getStringArray(configName + ".excluded", excluded));
        } catch (IllegalArgumentException iae) {
            throw new InvalidSettingsException(iae.getMessage());
        }
    }

    @Override
    protected void saveSettingsForModel(NodeSettingsWO settings) {
        radioModel.saveSettingsTo(settings);
        enabledModel.saveSettingsTo(settings);
        for (Map.Entry<String, String[]> entry : included.entrySet()) {
            settings.addStringArray(configName + "." + entry.getKey(), getIncludedValue(entry.getKey()));
        }
        settings.addStringArray(configName + ".excluded", getExcludedValue());
    }

    public String[] getExcludedValue() {
        if (excluded == null) {
            return null;
        }
        String[] result = new String[excluded.length];
        System.arraycopy(excluded, 0, result, 0, excluded.length);
        return result;
    }

    public String[] getIncludedValue(final String key) {
        String[] value = included.get(key);
        if (value == null) {
            return null;
        }
        String[] result = new String[value.length];
        System.arraycopy(value, 0, result, 0, value.length);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " ('" + configName + "')";
    }

}
