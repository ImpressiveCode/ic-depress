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

import org.impressivecode.depress.common.MultiFilterComponent;
import org.impressivecode.depress.its.FileParser;

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
    private MultiFilterComponent multiFilterPriority;
    private MultiFilterComponent multiFilterType;
    private MultiFilterComponent multiFilterResolution;
    private File oldFile = null;

    protected JiraAdapterNodeDialog() {
        createSettingsTab();
        createAdvancedTab();
    }

    private void createSettingsTab() {
        chooser = createFileChooserComponent(HISTORY_ID, FILE_EXTENSION);
        addTab("Settings", chooser.getComponentPanel());
    }
    
    private void createAdvancedTab() {
        JTabbedPane tabbedPane = new JTabbedPane();
        createPriorityTab(tabbedPane);
        createTypeTab(tabbedPane);
        createResolutionTab(tabbedPane);
        addTab("Advanced", tabbedPane);
        addChangeListenerToTabs();
    }

    private void createPriorityTab(final JTabbedPane tabbedPane) {
        multiFilterPriority = new MultiFilterComponent(JiraAdapterNodeModel.createMultiFilterPriorityModel(),
                new refreshPriorityCaller());
        tabbedPane.addTab("Priority", multiFilterPriority.getPanel());
    }

    private void createTypeTab(final JTabbedPane tabbedPane) {
        multiFilterType = new MultiFilterComponent(JiraAdapterNodeModel.createMultiFilterTypeModel(),
                new refreshTypeCaller());
        tabbedPane.addTab("Type", multiFilterType.getPanel());

    }

    private void createResolutionTab(final JTabbedPane tabbedPane) {
        multiFilterResolution = new MultiFilterComponent(JiraAdapterNodeModel.createMultiFilterResolutionModel(),
                new refreshResolutionCaller());
        tabbedPane.addTab("Resolution", multiFilterResolution.getPanel());
    }

    private DialogComponentFileChooser createFileChooserComponent(final String historyId, final String fileExtension) {
        return new DialogComponentFileChooser(createFileChooserSettings(), historyId, fileExtension);
    }

    @Override
    public final void loadSettingsFrom(final NodeSettingsRO settings, final PortObjectSpec[] specs)
            throws NotConfigurableException {
        chooser.loadSettingsFrom(settings, specs);
        oldFile = new File(((SettingsModelString) (chooser.getModel())).getStringValue());
        multiFilterPriority.loadSettingsFrom(settings, specs);
        multiFilterType.loadSettingsFrom(settings, specs);
        multiFilterResolution.loadSettingsFrom(settings, specs);
    }

    @Override
    public final void saveSettingsTo(final NodeSettingsWO settings) throws InvalidSettingsException {
        chooser.saveSettingsTo(settings);
        multiFilterPriority.saveSettingsTo(settings);
        multiFilterType.saveSettingsTo(settings);
        multiFilterResolution.saveSettingsTo(settings);
    }

    private class refreshPriorityCaller implements Callable<List<String>> {
        @Override
        public List<String> call() throws Exception {
            FileParser parser = new FileParser();
            File file = new File(((SettingsModelString) (chooser.getModel())).getStringValue());
            String expression = "/rss/channel/item/priority[not(preceding::priority/. = .)]";
            return parser.parseXPath(file, expression);
        }
    }

    private class refreshTypeCaller implements Callable<List<String>> {
        @Override
        public List<String> call() throws Exception {
            FileParser parser = new FileParser();
            File file = new File(((SettingsModelString) (chooser.getModel())).getStringValue());
            String expression = "/rss/channel/item/type[not(preceding::type/. = .)]";
            return parser.parseXPath(file, expression);
        }
    }

    private class refreshResolutionCaller implements Callable<List<String>> {
        @Override
        public List<String> call() throws Exception {
            FileParser parser = new FileParser();
            File file = new File(((SettingsModelString) (chooser.getModel())).getStringValue());
            String expression = "/rss/channel/item/resolution[not(preceding::resolution/. = .)]";
            return parser.parseXPath(file, expression);
        }
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
                if (panel.getTitleAt(panel.getSelectedIndex()).equals("Advanced")) {
                    File file = new File(((SettingsModelString) (chooser.getModel())).getStringValue());
                    if (null == file || !file.isFile()) {
                        panel.setSelectedIndex(panel.indexOfTab("Settings"));
                        JOptionPane.showMessageDialog(new JFrame(), "Invalid settings.\nPlease specify a valid file.");
                    } else {
                        if (!file.equals(oldFile)) {
                            multiFilterPriority.setEnabled(false);
                            multiFilterType.setEnabled(false);
                            multiFilterResolution.setEnabled(false);
                        }
                        oldFile = file;
                    }
                }
            }
        });
    }

}
