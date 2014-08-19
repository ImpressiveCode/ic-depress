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

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentButtonGroup;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.util.ColumnFilterPanel;

import com.google.common.collect.Lists;

/**
 * @author Maciej Borkowski, Capgemini Poland
 */
public class MultiFilterComponent {
    private final JPanel panel = new JPanel(new BorderLayout());
    private final DialogComponentButtonGroup radioButton;
    private final ColumnFilterPanel filterPanel;
    private final DialogComponentBoolean filterEnabled;

    private final String[] radioLabels;
    private final String configName;
    private final Callable<List<String>> refreshCaller;
    private final Map<String, List<String>> includeLists = new LinkedHashMap<String, List<String>>();
    private boolean active = false;

    /**
     * Creates a component that lets user put some input strings into multiple
     * groups. You can add this component to your dialog using getPanel().
     * 
     * @param filterEnabledModel
     *            gives state of panel - enabled or disabled.
     * @param configName
     *            the beggining of your chosen configuration String used by
     *            SettingsModel, together with radioLabels it stores
     *            data(configName+radioLabel) settings for
     *            DialogComponentButtonGroup
     * @param radioLabels
     *            labels for DialogComponentButtonGroup, defines groups
     * @param refreshCall
     *            function called after every Refresh button click, intended for
     *            loading input Strings
     */
    public MultiFilterComponent(SettingsModelBoolean filterEnabledModel, final String configName,
            final String[] radioLabels, Callable<List<String>> refreshCall) {
        this.configName = configName;
        this.radioLabels = radioLabels;
        this.refreshCaller = refreshCall;

        JPanel north = new JPanel(new FlowLayout(FlowLayout.CENTER));

        filterEnabledModel.addChangeListener(new EnabledListener());
        filterEnabled = new DialogComponentBoolean(filterEnabledModel, "Customized properties");
        north.add(filterEnabled.getComponentPanel());

        SettingsModelString radioButtonModel = new SettingsModelString(configName, radioLabels[0]);
        radioButtonModel.addChangeListener(new RadioButtonChangeListener());
        radioButton = new DialogComponentButtonGroup(radioButtonModel, null, false, radioLabels, radioLabels);
        north.add(radioButton.getComponentPanel());

        filterPanel = new ColumnFilterPanel(false);
        filterPanel.setExcludeTitle("Available");
        filterPanel.setIncludeTitle(radioButtonModel.getStringValue());
        filterPanel.addChangeListener(new FilteringChangeListener());

        panel.add(north, BorderLayout.NORTH);
        panel.add(filterPanel, BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setEnabled(final boolean enabled) {
        radioButton.getModel().setEnabled(enabled);
        filterPanel.setEnabled(enabled);
    }

    public final void saveSettingsTo(final NodeSettingsWO settings) throws InvalidSettingsException {
        filterEnabled.saveSettingsTo(settings);
        radioButton.saveSettingsTo(settings);
        List<String> list;
        for (String label : radioLabels) {
            list = includeLists.get(label);
            settings.addStringArray(configName + "." + label, list.toArray(new String[list.size()]));
        }
        Set<String> set = filterPanel.getExcludedColumnSet();
        settings.addStringArray(configName + "excluded", set.toArray(new String[set.size()]));
    }

    public final void loadSettingsFrom(final NodeSettingsRO settings, final PortObjectSpec[] specs)
            throws NotConfigurableException {
        filterEnabled.loadSettingsFrom(settings, specs);
        radioButton.loadSettingsFrom(settings, specs);
        List<String> list = null;
        List<String> excludeList;
        try {
            for (String label : radioLabels) {
                list = new LinkedList<String>(Arrays.asList(settings.getStringArray(configName + "." + label)));
                includeLists.put(label, list);
            }
            excludeList = new LinkedList<String>(Arrays.asList(settings.getStringArray(configName + "excluded")));
            initPanel(((SettingsModelBoolean) filterEnabled.getModel()).getBooleanValue(), excludeList);
        } catch (InvalidSettingsException e) {
            Logger.getLogger("Error").severe("InvalidSettings : " + e.getMessage());
        }
    }

    private void initPanel(final boolean enabled, final List<String> excluded) {
        setEnabled(enabled);
        loadFilter(excluded);
        active = true;
    }

    private void loadFilter(final List<String> excluded) {
        List<String> included = includeLists.get(((SettingsModelString) radioButton.getModel()).getStringValue());
        if (null != included) {
            List<String> onScreen = new LinkedList<String>(excluded);
            onScreen.addAll(included);
            filterPanel.update(createTableSpec(onScreen), included, excluded);
        }
    }

    private DataTableSpec createTableSpec(List<String> list) {
        DataColumnSpec[] columns = new DataColumnSpec[list.size()];
        int index = 0;
        for (String s : list) {
            columns[index++] = new DataColumnSpecCreator(s, StringCell.TYPE).createSpec();
        }
        return new DataTableSpec(columns);
    }

    private void setRefreshing(final boolean refreshing) {
        active = false;
        if (refreshing) {
            panel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        } else {
            panel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        filterEnabled.getModel().setEnabled(!refreshing);
        setEnabled(!refreshing);
        panel.paintImmediately(panel.getVisibleRect());
        active = true;
    }

    private void refresh() {
        setRefreshing(true);
        try {
            loadProperties();
        } catch (Exception e) {
            Logger.getLogger("Error").severe("File parsing error : " + e.getMessage());
        }
        setRefreshing(false);
    }

    private void loadProperties() throws Exception {
        List<String> properties = null;
        properties = refreshCaller.call();
        for (List<String> include : includeLists.values()) {
            include.clear();
        }
        List<String> excluded = new LinkedList<String>(properties);
        for (String label : radioLabels) {
            if (properties.contains(label)) {
                includeLists.get(label).add(label);
                excluded.remove(label);
            }
        }
        loadFilter(excluded);
    }

    private class FilteringChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent event) {
            List<String> memorizedIncludeList = includeLists.get(((SettingsModelString) radioButton.getModel())
                    .getStringValue());
            memorizedIncludeList.clear();
            memorizedIncludeList.addAll(filterPanel.getIncludedColumnSet());
        }
    }

    private class RadioButtonChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent event) {
            SettingsModelString radioSettings = (SettingsModelString) (event.getSource());
            filterPanel.setIncludeTitle(radioSettings.getStringValue());
            loadFilter(Lists.newArrayList(filterPanel.getExcludedColumnSet()));
        }
    }

    private class EnabledListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent event) {
            SettingsModelBoolean enabledModel = (SettingsModelBoolean) (event.getSource());
            if (enabledModel.getBooleanValue() && active) {
                refresh();
            } else if (!enabledModel.getBooleanValue()) {
                setEnabled(false);
            }
        }
    }

}
