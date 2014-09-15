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

import java.util.ArrayList;
import java.util.Iterator;
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

    private final SettingsModelMultiFilter model;
    private final String[] radioLabels;
    private final Callable<List<String>> refreshCaller;
    private final Map<String, List<String>> includeLists = new LinkedHashMap<String, List<String>>();
    private ArrayList<String> excludeList;
    private boolean active = false;

    /**
     * Creates a component that lets user put some input strings into multiple
     * groups. You can add this component to your dialog using getPanel().
     * 
     * @param SettingsModelMultiFilter
     *            model used by this component
     * @param refreshCall
     *            function called after every enabled click, intended for
     *            loading input Strings
     */
    public MultiFilterComponent(final SettingsModelMultiFilter model, final Callable<List<String>> refreshCall) {
        this.radioLabels = model.getRadio();
        this.model = model;
        this.refreshCaller = refreshCall;
        JPanel north = new JPanel(new FlowLayout(FlowLayout.CENTER));

        model.getEnabledModel().addChangeListener(new EnabledListener());
        filterEnabled = new DialogComponentBoolean(model.getEnabledModel(), "Customized properties");
        north.add(filterEnabled.getComponentPanel());

        model.getRadioModel().addChangeListener(new RadioButtonChangeListener());
        radioButton = new DialogComponentButtonGroup(model.getRadioModel(), null, false, radioLabels, radioLabels);
        north.add(radioButton.getComponentPanel());

        filterPanel = new ColumnFilterPanel(false);
        filterPanel.setExcludeTitle("Available");
        filterPanel.setIncludeTitle(model.getRadioModel().getStringValue());
        filterPanel.addChangeListener(new FilteringChangeListener());

        panel.add(north, BorderLayout.NORTH);
        panel.add(filterPanel, BorderLayout.CENTER);
    }
    
    public SettingsModelMultiFilter getModel() {
        return model;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void reset() {
        model.getEnabledModel().setBooleanValue(false);
    }
    
    public void setEnabled(final boolean enabled) {
        filterPanel.setEnabled(enabled);
    }
    
    public final void saveSettingsTo(final NodeSettingsWO settings) throws InvalidSettingsException {
        filterEnabled.saveSettingsTo(settings);
        radioButton.saveSettingsTo(settings);
        for (String label : radioLabels) {
            List<String> value = includeLists.get(label);
            model.setIncludedValue(label, value.toArray(new String[value.size()]));
        }
        Set<String> value = filterPanel.getExcludedColumnSet();
        model.setExcludedValue(value.toArray(new String[value.size()]));
        model.saveSettingsTo(settings);
    }

    public final void loadSettingsFrom(final NodeSettingsRO settings, final PortObjectSpec[] specs)
            throws NotConfigurableException {
        filterEnabled.loadSettingsFrom(settings, specs);
        radioButton.loadSettingsFrom(settings, specs);
        model.loadSettingsForDialog(settings, specs);
        for (String label : radioLabels) {
            includeLists.put(label, Lists.newArrayList(model.getIncludedValue(label)));
        }
        excludeList = Lists.newArrayList(model.getExcludedValue());
        initPanel(((SettingsModelBoolean) filterEnabled.getModel()).getBooleanValue());
    }

    private void initPanel(final boolean enabled) {
        setEnabled(enabled);
        active = true;
        if (!enabled) {
            setDefault(null);
        }
        loadFilter();
    }

    private void setDefault(final List<String> valuePool) {
        for (List<String> include : includeLists.values()) {
            include.clear();
        }
        excludeList.clear();
        if (null == valuePool) {
            for (String label : radioLabels) {
                includeLists.get(label).add(label);
            }
        } else {
            for (String label : radioLabels) {
                for (Iterator<String> iter = valuePool.listIterator(); iter.hasNext();) {
                    String value = iter.next();
                    if (label.equalsIgnoreCase(value)) {
                        includeLists.get(label).add(value);
                        iter.remove();
                    }
                }
            }
            excludeList.addAll(valuePool);
        }
    }

    private void loadFilter() {
        List<String> included = includeLists.get(((SettingsModelString) radioButton.getModel()).getStringValue());
        if (null != included) {
            List<String> onScreen = new LinkedList<String>(excludeList);
            onScreen.addAll(included);
            filterPanel.update(createTableSpec(onScreen), included, excludeList);
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
        List<String> properties = refreshCaller.call();
        setDefault(properties);
        loadFilter();
    }

    private class FilteringChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent event) {
            List<String> includeList = includeLists
                    .get(((SettingsModelString) radioButton.getModel()).getStringValue());
            includeList.clear();
            includeList.addAll(filterPanel.getIncludedColumnSet());
            excludeList.clear();
            excludeList.addAll(filterPanel.getExcludedColumnSet());
        }
    }

    private class RadioButtonChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent event) {
            SettingsModelString radioSettings = (SettingsModelString) (event.getSource());
            filterPanel.setIncludeTitle(radioSettings.getStringValue());
            loadFilter();
        }
    }

    private class EnabledListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent event) {
            SettingsModelBoolean enabledModel = (SettingsModelBoolean) (event.getSource());
            if (enabledModel.getBooleanValue() && active) {
                refresh();
            } else if (!enabledModel.getBooleanValue()) {
                setDefault(null);
                loadFilter();
                setEnabled(false);
            }
        }
    }

}
