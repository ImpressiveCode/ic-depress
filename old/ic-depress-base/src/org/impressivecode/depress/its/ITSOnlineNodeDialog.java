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
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.impressivecode.depress.common.MultiFilterComponent;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentButton;
import org.knime.core.node.defaultnodesettings.DialogComponentPasswordField;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;

/**
 * Abstract node dialog for issue tracking systems.
 * 
 * @author Marcin Kunert, Wrocław University of Technology
 * @author Krzysztof Kwoka, Wrocław University of Technology
 * @author Bartosz Skuza, Wrocław University of Technology
 * @author Maciej Borkowski, Capgemini Poland
 */
public abstract class ITSOnlineNodeDialog extends NodeDialogPane {
    public static final String CONNECTION_TAB_NAME = "Connection";
    public static final String ADVANCED_TAB_NAME = "Advanced";
    public static final String MAPPING_TAB_NAME = "Mapping";
    public static final String URL_LABEL = "URL:";
    public static final String PASSWORD_LABEL = "Password:";
    public static final String LOGIN_LABEL = "Login:";
    public static final String CHECK_PROJECTS_BUTTON = "Check projects";
    public static final String CONNECTING = "Connecting...";
    public static final String PROJECTS_SELECTION_LABEL = "Projects:";
    public static final String ALL_PROJECTS = "All projects";
    private static final String STATUS = "Status";
    private static final String PRIORITY = "Priority";
    private static final String RESOLUTION = "Resolution";
    private static final String TYPE = "Type";
    public static final int COMPONENT_WIDTH = 32;
    public static final int LOGIN_WIDTH = 16;
    public static final int PASSWORD_WIDTH = 16;

    protected DialogComponentString url;
    protected DialogComponentButton checkProjectsButton;
    protected DialogComponentStringSelection projectSelection;
    protected DialogComponentBoolean checkAllProjects;
    protected DialogComponentString loginComponent;
    protected DialogComponentPasswordField passwordComponent;

    protected ITSMappingManager mappingManager;

    private String oldUrl = null;

    public ITSOnlineNodeDialog() {
        addTab(CONNECTION_TAB_NAME, createConnectionTab());
        addTab(ADVANCED_TAB_NAME, createAdvancedTab());
        addTab(MAPPING_TAB_NAME, createMappingTab());
        addChangeListenerToTabs();
    }

    protected Component createConnectionTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createHostnameComponent());
        panel.add(createLoginPanel());
        panel.add(createProjectSelection());
        return panel;
    }

    protected Component createHostnameComponent() {
        url = new DialogComponentString(createURLSettings(), URL_LABEL, true, COMPONENT_WIDTH);
        return url.getComponentPanel();
    }

    protected Component createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(createLoginComponent());
        panel.add(createPasswordComponent());
        return panel;
    }

    protected Component createLoginComponent() {
        loginComponent = new DialogComponentString(createLoginSettings(), LOGIN_LABEL, false, LOGIN_WIDTH);

        return loginComponent.getComponentPanel();
    }

    protected Component createPasswordComponent() {
        passwordComponent = new DialogComponentPasswordField(createPasswordSettings(), PASSWORD_LABEL, PASSWORD_WIDTH);
        return passwordComponent.getComponentPanel();
    }

    protected Component createProjectSelection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(createCheckAllProjectsComponent());
        panel.add(createProjectsPanel());
        return panel;
    }

    private Component createProjectsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createCheckProjectsButton());
        panel.add(createComponentStringSelection());
        return panel;
    }

    private Component createCheckAllProjectsComponent() {
        checkAllProjects = new DialogComponentBoolean(createCheckAllProjectsSettings(), ALL_PROJECTS);
        checkAllProjects.getModel().addChangeListener(new ChangeListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void stateChanged(ChangeEvent event) {
                boolean value = ((SettingsModelBoolean) event.getSource()).getBooleanValue();
                checkProjectsButton.setEnabled(!value);
                if (value) {
                    projectSelection.getComponentPanel().setVisible(false);
                    projectSelection.replaceListItems(Arrays.asList(""), "");
                }
                getPanel().paintImmediately(getPanel().getVisibleRect());
            }
        });
        return checkAllProjects.getComponentPanel();
    }

    @SuppressWarnings("deprecation")
    protected Component createCheckProjectsButton() {
        checkProjectsButton = new DialogComponentButton(CHECK_PROJECTS_BUTTON);
        checkProjectsButton.addActionListener(new CheckConnectionButtonListener() {
        });
        checkProjectsButton.setEnabled(false);
        return checkProjectsButton.getComponentPanel();
    }

    protected Component createComponentStringSelection() {
        ArrayList<String> projects = new ArrayList<String>();
        projects.add("");
        projectSelection = new DialogComponentStringSelection(createSelectionSettings(), PROJECTS_SELECTION_LABEL,
                projects, false);
        projectSelection.getComponentPanel().setVisible(false);
        return projectSelection.getComponentPanel();
    }

    class CheckConnectionButtonListener implements ActionListener {
        @SuppressWarnings("deprecation")
        @Override
        public void actionPerformed(ActionEvent event) {
            checkProjectsButton.setEnabled(false);
            checkProjectsButton.setText(CONNECTING);
            getPanel().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            getPanel().paintImmediately(getPanel().getVisibleRect());
            updateProjectsList();
            projectSelection.getComponentPanel().setVisible(true);
            getPanel().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            checkProjectsButton.setText(CHECK_PROJECTS_BUTTON);
            checkProjectsButton.setEnabled(true);
        }
    }

    protected Component createMappingTab() {
        JTabbedPane mappingTab = new JTabbedPane();
        createMappingManager();
        mappingTab.addTab(PRIORITY, mappingManager.getMultiFilterPriority().getPanel());
        mappingTab.addTab(TYPE, mappingManager.getMultiFilterType().getPanel());
        mappingTab.addTab(RESOLUTION, mappingManager.getMultiFilterResolution().getPanel());
        mappingTab.addTab(STATUS, mappingManager.getMultiFilterStatus().getPanel());
        return mappingTab;
    }

    @Override
    protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs) throws NotConfigurableException {
        url.loadSettingsFrom(settings, specs);
        loginComponent.loadSettingsFrom(settings, specs);
        passwordComponent.loadSettingsFrom(settings, specs);
        projectSelection.loadSettingsFrom(settings, specs);
        checkAllProjects.loadSettingsFrom(settings, specs);
        for (MultiFilterComponent component : mappingManager.getComponents()) {
            component.loadSettingsFrom(settings, specs);
        }
        loadSpecificSettingsFrom(settings, specs);
    }

    @Override
    protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
        url.saveSettingsTo(settings);
        loginComponent.saveSettingsTo(settings);
        passwordComponent.saveSettingsTo(settings);
        projectSelection.saveSettingsTo(settings);
        checkAllProjects.saveSettingsTo(settings);
        for (MultiFilterComponent component : mappingManager.getComponents()) {
            component.saveSettingsTo(settings);
        }
        saveSpecificSettingsTo(settings);
    }

    protected SettingsModelString createURLSettings() {
        return ITSOnlineNodeModel.createURLSettings();
    }

    protected SettingsModelString createLoginSettings() {
        return ITSOnlineNodeModel.createLoginSettings();
    }

    protected SettingsModelString createPasswordSettings() {
        return ITSOnlineNodeModel.createPasswordSettings();
    }

    protected SettingsModelString createSelectionSettings() {
        return ITSOnlineNodeModel.createSettingsSelection();
    }

    protected SettingsModelBoolean createCheckAllProjectsSettings() {
        return ITSOnlineNodeModel.createSettingsCheckAllProjects();
    }

    protected JTabbedPane getTabbedPane() {
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

    protected void addChangeListenerToTabs() {
        getTabbedPane().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                JTabbedPane panel = (JTabbedPane) event.getSource();
                if (panel.getTitleAt(panel.getSelectedIndex()).equals(MAPPING_TAB_NAME)) {
                    String urlString = ((SettingsModelString) (url.getModel())).getStringValue();
                    if (null == urlString || urlString.isEmpty()) {
                        panel.setSelectedIndex(panel.indexOfTab(CONNECTION_TAB_NAME));
                        JOptionPane.showMessageDialog(new JFrame(), "Invalid settings.\nPlease specify a valid URL.");
                    } else {
                        if (!urlString.equals(oldUrl)) {
                            mappingManager.reset();
                        }
                        oldUrl = urlString;
                    }
                }
            }
        });
    }

    protected abstract void updateProjectsList();

    protected abstract void createMappingManager();

    protected abstract Component createAdvancedTab();

    protected abstract void loadSpecificSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
            throws NotConfigurableException;

    protected abstract void saveSpecificSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException;

}
