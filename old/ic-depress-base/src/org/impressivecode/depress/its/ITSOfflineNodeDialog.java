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

import java.awt.Component;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.impressivecode.depress.common.MultiFilterComponent;
import org.impressivecode.depress.its.ITSMappingManager;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;

/**
 * @author Maciej Borkowski, Capgemini Poland
 */
public abstract class ITSOfflineNodeDialog extends NodeDialogPane {
    public static final String SETTINGS_TAB_NAME = "Settings";
    public static final String MAPPING_TAB_NAME = "Mapping";
    private static final String STATUS = "Status";
    private static final String PRIORITY = "Priority";
    private static final String RESOLUTION = "Resolution";
    private static final String TYPE = "Type";

    private static final String FILE_EXTENSION = ".xml";
    private static final String HISTORY_ID = "history";

    protected DialogComponentFileChooser chooser;
    protected ITSMappingManager mappingManager;

    private File oldFile = null;

    protected ITSOfflineNodeDialog() {
        createSettingsTab();
        createMappingTab();
        addChangeListenerToTabs();
    }

    private void createSettingsTab() {
        chooser = createFileChooserComponent(HISTORY_ID, FILE_EXTENSION);
        addTab(SETTINGS_TAB_NAME, chooser.getComponentPanel());
    }

    private void createMappingTab() {
        JTabbedPane mappingTab = new JTabbedPane();
        mappingManager = ITSOfflineNodeModel.createMapping();
        createMappingManager();
        mappingTab.addTab(PRIORITY, mappingManager.getMultiFilterPriority().getPanel());
        mappingTab.addTab(TYPE, mappingManager.getMultiFilterType().getPanel());
        mappingTab.addTab(RESOLUTION, mappingManager.getMultiFilterResolution().getPanel());
        mappingTab.addTab(STATUS, mappingManager.getMultiFilterStatus().getPanel());
        addTab(MAPPING_TAB_NAME, mappingTab);
    }

    @Override
    public final void loadSettingsFrom(final NodeSettingsRO settings, final PortObjectSpec[] specs)
            throws NotConfigurableException {
        setTab(SETTINGS_TAB_NAME);
        chooser.loadSettingsFrom(settings, specs);
        oldFile = new File(((SettingsModelString) (chooser.getModel())).getStringValue());
        for (MultiFilterComponent component : mappingManager.getComponents()) {
            component.loadSettingsFrom(settings, specs);
        }
    }

    @Override
    public final void saveSettingsTo(final NodeSettingsWO settings) throws InvalidSettingsException {
        chooser.saveSettingsTo(settings);
        for (MultiFilterComponent component : mappingManager.getComponents()) {
            component.saveSettingsTo(settings);
        }
    }

    private DialogComponentFileChooser createFileChooserComponent(final String historyId, final String fileExtension) {
        return new DialogComponentFileChooser(ITSOfflineNodeModel.createFileChooserSettings(), historyId, fileExtension);
    }

    private void setTab(final String tabName) {
        JTabbedPane pane = getTabbedPane();
        pane.setSelectedIndex(pane.indexOfTab(SETTINGS_TAB_NAME));
    }

    private JTabbedPane getTabbedPane() {
        Component[] components = getPanel().getComponents();
        Component component = null;
        for (int i = 0; i < components.length; i++) {
            component = components[i];
            if (component instanceof JTabbedPane) {
                return (JTabbedPane) component;
            }
        }
        return null;
    }

    private void addChangeListenerToTabs() {
        getTabbedPane().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                JTabbedPane panel = (JTabbedPane) event.getSource();
                if (panel.getTitleAt(panel.getSelectedIndex()).equals(MAPPING_TAB_NAME)) {
                    File file = new File(((SettingsModelString) (chooser.getModel())).getStringValue());
                    if (null == file || !file.isFile()) {
                        panel.setSelectedIndex(panel.indexOfTab(SETTINGS_TAB_NAME));
                        JOptionPane.showMessageDialog(new JFrame(), "Invalid settings.\nPlease specify a valid file.");
                    } else {
                        if (!file.equals(oldFile)) {
                            mappingManager.reset();
                        }
                        oldFile = file;
                    }
                }
            }
        });
    }

    protected abstract void createMappingManager();
}
