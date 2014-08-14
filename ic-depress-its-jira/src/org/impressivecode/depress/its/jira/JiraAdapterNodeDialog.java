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

import static org.impressivecode.depress.its.jira.JiraAdapterNodeModel.createFileChooserSettings;

import java.awt.Component;
import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.impressivecode.depress.its.ITSPriority;
import org.impressivecode.depress.its.ITSType;
import org.impressivecode.depress.its.ITSResolution;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;

/**
 * @author Marek Majchrzak, ImpressiveCode
 * @author Maciej Borkowski, Capgemini Poland
 */
public class JiraAdapterNodeDialog extends NodeDialogPane {

    private static final String FILE_EXTENSION = ".xml";
    private static final String HISTORY_ID = "depress.its.jira.historyid";

    private DialogComponentFileChooser chooser;
    private MultiFilterComponent multiFilterComponentPriority;
    private MultiFilterComponent multiFilterComponentType;
    private MultiFilterComponent multiFilterComponentResolution;
    private File oldFile = null;

    protected JiraAdapterNodeDialog() {
        createSettingsTab();
        createAdvancedTab();
    }

    private void createAdvancedTab() {
        JTabbedPane tabbedPane = new JTabbedPane();
        createPriorityTab(tabbedPane);
        createTypeTab(tabbedPane);
        createResolutionTab(tabbedPane);
        addTab("Advanced", tabbedPane);
        addChangeListenerToTabs();
    }

    private void createSettingsTab() {
        chooser = createFileChooserComponent(HISTORY_ID, FILE_EXTENSION);
        addTab("Settings", chooser.getComponentPanel());
    }

    private void createPriorityTab(final JTabbedPane tabbedPane) {
        multiFilterComponentPriority = new MultiFilterComponent(JiraAdapterNodeModel.createSettingsModelBoolean(
                JiraAdapterNodeModel.PRIORITY_BOOLEAN_CONFIG_NAME, false), JiraAdapterNodeModel.PRIORITY_CONFIG_NAME,
                ITSPriority.labels(), new refreshPriorityCaller());

        tabbedPane.addTab("Priority", multiFilterComponentPriority.getPanel());
    }

    private void createTypeTab(final JTabbedPane tabbedPane) {
        multiFilterComponentType = new MultiFilterComponent(JiraAdapterNodeModel.createSettingsModelBoolean(
                JiraAdapterNodeModel.TYPE_BOOLEAN_CONFIG_NAME, false), JiraAdapterNodeModel.TYPE_CONFIG_NAME,
                ITSType.labels(), new refreshTypeCaller());

        tabbedPane.addTab("Type", multiFilterComponentType.getPanel());

    }

    private void createResolutionTab(final JTabbedPane tabbedPane) {
        multiFilterComponentResolution = new MultiFilterComponent(JiraAdapterNodeModel.createSettingsModelBoolean(
                JiraAdapterNodeModel.RESOLUTION_BOOLEAN_CONFIG_NAME, false),
                JiraAdapterNodeModel.RESOLUTION_CONFIG_NAME, ITSResolution.labels(), new refreshResolutionCaller());
        tabbedPane.addTab("Resolution", multiFilterComponentResolution.getPanel());
    }

    private DialogComponentFileChooser createFileChooserComponent(final String historyId, final String fileExtension) {
        return new DialogComponentFileChooser(createFileChooserSettings(), historyId, fileExtension);
    }

    @Override
    public final void loadSettingsFrom(final NodeSettingsRO settings, final PortObjectSpec[] specs)
            throws NotConfigurableException {
        chooser.loadSettingsFrom(settings, specs);
        multiFilterComponentPriority.loadSettingsFrom(settings, specs, null);
        multiFilterComponentType.loadSettingsFrom(settings, specs, null);
        multiFilterComponentResolution.loadSettingsFrom(settings, specs, null);
    }

    @Override
    public final void saveSettingsTo(final NodeSettingsWO settings) throws InvalidSettingsException {
        chooser.saveSettingsTo(settings);
        multiFilterComponentPriority.saveSettingsTo(settings);
        multiFilterComponentType.saveSettingsTo(settings);
        multiFilterComponentResolution.saveSettingsTo(settings);
    }

    private class refreshPriorityCaller implements Callable<List<String>> {
        @Override
        public List<String> call() throws Exception {
            JiraFileParser parser = new JiraFileParser();
            File file = new File(((SettingsModelString) (chooser.getModel())).getStringValue());
            String expression = "/rss/channel/item/priority[not(preceding::priority/. = .)]";
            return parser.parseXPath(file, expression);
        }
    }

    private class refreshTypeCaller implements Callable<List<String>> {
        @Override
        public List<String> call() throws Exception {
            JiraFileParser parser = new JiraFileParser();
            File file = new File(((SettingsModelString) (chooser.getModel())).getStringValue());
            String expression = "/rss/channel/item/type[not(preceding::type/. = .)]";
            return parser.parseXPath(file, expression);
        }
    }

    private class refreshResolutionCaller implements Callable<List<String>> {
        @Override
        public List<String> call() throws Exception {
            JiraFileParser parser = new JiraFileParser();
            File file = new File(((SettingsModelString) (chooser.getModel())).getStringValue());
            String expression = "/rss/channel/item/resolution[not(preceding::resolution/. = .)]";
            return parser.parseXPath(file, expression);
        }
    }

    private void addChangeListenerToTabs() {
        Component[] components = getPanel().getComponents();
        Component component = null;
        for (int i = 0; i < components.length; i++) {
            component = components[i];
            if (component instanceof JTabbedPane) {
                ((JTabbedPane) component).addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent event) {
                        JTabbedPane panel = (JTabbedPane) event.getSource();
                        if (panel.getTitleAt(panel.getSelectedIndex()).equals("Advanced")) {
                            File file = new File(((SettingsModelString) (chooser.getModel())).getStringValue());
                            if (null == file || !file.isFile()) {
                                panel.setSelectedIndex(panel.indexOfTab("Settings"));
                                JOptionPane.showMessageDialog(new JFrame(),
                                        "Invalid settings.\nPlease specify a valid filename.");
                            } else {
                                if(!file.equals(oldFile)) {
                                    multiFilterComponentPriority.setEnabled(false);
                                    multiFilterComponentType.setEnabled(false);
                                    multiFilterComponentResolution.setEnabled(false);
                                }
                                oldFile = file;
                            }
                        }
                    }
                });
                break;
            }
        }
    }

}
