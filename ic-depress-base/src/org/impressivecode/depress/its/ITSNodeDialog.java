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
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentButton;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnFilter;
import org.knime.core.node.defaultnodesettings.DialogComponentPasswordField;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.knime.core.node.port.PortObjectSpec;

/**
 * Abstract node dialog for issue tracking systems.
 * 
 * @author Marcin Kunert, Wrocław University of Technology
 * @author Krzysztof Kwoka, Wrocław University of Technology
 * @author Bartosz Skuza, Wrocław University of Technology
 * @author Maciej Borkowski, Capgemini Poland
 * 
 */
public abstract class ITSNodeDialog extends NodeDialogPane {

    public static final String CONNECTION_TAB_NAME = "Connection";
    public static final String LOGIN_TAB_NAME = "Login";
    public static final String FILTERS_TAB_NAME = "Filters";
    public static final String ADVANCED_TAB_NAME = "Advanced";
    public static final String URL_LABEL = "URL:";
    public static final String PROJECT_LABEL = "Project:";
    public static final String PASSWORD_LABEL = "Password:";
    public static final String LOGIN_LABEL = "Login:";
    public static final String CHECK_PROJECTS_BUTTON = "Check projects";
    public static final String CONNECTING = "Connecting...";
    public static final String CHECK_PROJECTS_INIT_LABEL = "After the check you should be able to choose available projects from the project list.";
    public static final String CHECK_PROJECTS_SUCCESS_LABEL = "Projects list updated.";
    public static final String CHECK_PROJECTS_FAILURE_LABEL = "Connection failed.";
    public static final String PROJECTS_SELECTION_LABEL = "Projects:";
    public static final String ALL_PROJECTS = "All projects";
    public static final int COMPONENT_WIDTH = 32;
    public static final int LOGIN_WIDTH = 16;
    public static final int PASSWORD_WIDTH = 16;

    /**
     * Connection components
     */
    protected DialogComponentString url;
    protected DialogComponentButton checkProjectsButton;
    protected DialogComponentStringSelection projectSelection;
    protected DialogComponentBoolean checkAllProjects;
    /**
     * Login components
     */
    protected DialogComponentString loginComponent;
    protected DialogComponentPasswordField passwordComponent;
    /**
     * Filter components
     */
    protected DialogComponentColumnFilter availableFilters;
    protected Object filterOptions;
    /**
     * Advanced components
     */
    protected ITSFiltersDialogComponent filterSelection;
    private JPanel filterPanel;

    public ITSNodeDialog() {
        addTab(CONNECTION_TAB_NAME, createConnectionTab());
        addTab(FILTERS_TAB_NAME, createFiltersTab());
        addTab(ADVANCED_TAB_NAME, createAdvancedTab());
    }

    protected Component createConnectionTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createHostnameComponent());
        panel.add(createLoginPanel());
        panel.add(createProjectSelection());
        return panel;
    }

    protected Component createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(createLoginComponent());
        panel.add(createPasswordComponent());
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
                projectSelection.setEnabled(!value);
                getPanel().paintImmediately(getPanel().getVisibleRect());
            }
        });
        return checkAllProjects.getComponentPanel();
    }

    protected Component createHostnameComponent() {
        url = new DialogComponentString(createURLSettings(), URL_LABEL, true, COMPONENT_WIDTH);
        return url.getComponentPanel();
    }

    @SuppressWarnings("deprecation")
    protected Component createCheckProjectsButton() {
        checkProjectsButton = new DialogComponentButton(CHECK_PROJECTS_BUTTON);
        checkProjectsButton.addActionListener(new CheckConnectionButtonListener() {});
        checkProjectsButton.setEnabled(false);
        return checkProjectsButton.getComponentPanel();
    }

    @SuppressWarnings("deprecation")
    protected Component createComponentStringSelection() {
        ArrayList<String> projects = new ArrayList<String>();
        projects.add("");
        projectSelection = new DialogComponentStringSelection(createSelectionSettings(), PROJECTS_SELECTION_LABEL,
                projects, false);
        projectSelection.setEnabled(false);
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
            getPanel().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            checkProjectsButton.setText(CHECK_PROJECTS_BUTTON);
            checkProjectsButton.setEnabled(true);
        }
    }

    protected Component createProjectSelection() {
        JPanel panel = new JPanel();
        panel.add(createCheckAllProjectsComponent());
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(createCheckProjectsButton());
        panel.add(createComponentStringSelection());
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

    protected Component createFiltersTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(createAvailableFiltersComponent());
        panel.add(createFilterOptions());

        return panel;
    }

    protected Component createAvailableFiltersComponent() {
        filterSelection = new ITSFiltersDialogComponent(getFilters(), createFilterSettings());
        filterSelection.addListItemSelectionListener(new ITSFiltersDialogComponent.ListItemSelectedListener() {

            @Override
            public void listItemSelected(ITSFilter filter, int tableId) {

                filterPanel.removeAll();

                if (filter != null) {
                    if (tableId == 2) {
                        for (DialogComponent component : filter.getDialogComponents()) {
                            filterPanel.add(component.getComponentPanel());
                        }
                        ITSNodeDialog.this.getPanel().repaint();
                    }
                }

                if (filter != null) {
                    if (tableId == 2) {
                        for (DialogComponent component : filter.getDialogComponents()) {
                            filterPanel.add(component.getComponentPanel());
                        }
                        ITSNodeDialog.this.getPanel().repaint();
                    }
                }
            }
        });

        return filterSelection.getComponentPanel();
    }

    protected Component createFilterOptions() {
        filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        addLargestFilter(filterPanel);
        return filterPanel;
    }

    protected void addLargestFilter(JPanel panel) {
        // NOOP, should be implemented by dialogs to set their size
    }

    protected Component createAdvancedTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        return panel;
    }

    @Override
    protected void addFlowVariablesTab() {
        // NOOP the flow variables tab is not needed
    }

    @Override
    protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs) throws NotConfigurableException {
        url.loadSettingsFrom(settings, specs);
        loginComponent.loadSettingsFrom(settings, specs);
        passwordComponent.loadSettingsFrom(settings, specs);
        projectSelection.loadSettingsFrom(settings, specs);
        checkAllProjects.loadSettingsFrom(settings, specs);

        if (filterSelection != null) {
            filterSelection.loadSettingsFrom(settings, specs);
        }

        for (ITSFilter filter : getFilters()) {
            for (DialogComponent component : filter.getDialogComponents()) {
                component.loadSettingsFrom(settings, specs);
            }
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

        if (filterSelection != null) {
            filterSelection.saveSettingsTo(settings);
        }

        for (ITSFilter filter : getFilters()) {
            for (DialogComponent component : filter.getDialogComponents()) {
                try {
                    component.saveSettingsTo(settings);
                } catch (Exception e) {
                    System.out.println(filter.getName() + " -> " + e.getMessage());
                }
            }
        }

        saveSpecificSettingsTo(settings);
    }
    

    protected abstract void updateProjectsList();

    protected abstract SettingsModelBoolean createCheckAllProjectsSettings();

    protected abstract SettingsModelString createURLSettings();

    protected abstract SettingsModelString createProjectSettings();

    protected abstract SettingsModelString createSelectionSettings();

    protected abstract SettingsModelString createLoginSettings();

    protected abstract SettingsModelString createPasswordSettings();

    protected abstract SettingsModelStringArray createFilterSettings();

    protected abstract Collection<ITSFilter> getFilters();

    protected abstract void loadSpecificSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
            throws NotConfigurableException;

    protected abstract void saveSpecificSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException;

}
