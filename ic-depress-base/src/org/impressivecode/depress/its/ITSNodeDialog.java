package org.impressivecode.depress.its;

import java.awt.event.ActionListener;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentButton;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnFilter;
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.DialogComponentPasswordField;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public abstract class ITSNodeDialog extends DefaultNodeSettingsPane {

    public static final String CONNECTION_TAB_NAME = "Connection";
    
    public static final String LOGIN_TAB_NAME = "Login";
    
    public static final String FILTERS_TAB_NAME = "Filters";
    
    public static final String ADVANCED_TAB_NAME = "Advanced";

    public static final String URL_LABEL = "URL:";
    
    public static final String PROJECT_LABEL = "Project:";
    
    public static final String PASSWORD_LABEL = "Password:";
    
    public static final String LOGIN_LABEL = "Login:";

    public static final String CHECK_PROJECTS_BUTTON = "Check projects";

    public static final String CHECK_PROJECTS_INIT_LABEL = "After the check you should be able to choose available projects from the project list.";
    
    public static final String CHECK_PROJECTS_SUCCESS_LABEL = "Projects list updated.";
    
    public static final String CHECK_PROJECTS_FAILURE_LABEL = "Connection failed.";
    
    public static final String THREADS_COUNT_LABEL = "Threads:";

    public static final int COMPONENT_WIDTH = 32;

    /**
     * Connection components
     */
    protected DialogComponentString url;
    
    protected DialogComponentStringSelection project;
    
    protected DialogComponentButton checkProjectsButton;
    
    protected DialogComponentLabel checkProjectsLabel;
    
    /**
     * Login components
     */
    protected DialogComponentString login;
    
    protected DialogComponentPasswordField password;
    
    /**
     * Filter components
     */
    protected DialogComponentColumnFilter availableFilters;
    
    protected Object filterOptions;
    
    /**
     * Advanced components
     */
    protected DialogComponentNumberEdit threadsCount;

    public ITSNodeDialog() {
        createConnectionTab();
        createLoginTab();
        createFiltersTab();
        createAdvancedTab();
    }

    protected void createConnectionTab() {
    	setDefaultTabTitle(CONNECTION_TAB_NAME);
        createHostnameComponent();
        createProjectChooser();
        createCheckProjectsButton();
        createCheckProjectsLabel();
    }

    protected void createHostnameComponent() {
        SettingsModelString hostnameComponentModel = createURLSettings();
        url = new DialogComponentString(hostnameComponentModel, URL_LABEL, true, COMPONENT_WIDTH);
        addDialogComponent(url);
    }

    protected abstract SettingsModelString createURLSettings();

    protected void createProjectChooser() {
        SettingsModelString projectChooserModel = createProjectSettings();
        project = new DialogComponentStringSelection(projectChooserModel, PROJECT_LABEL, null, false);
        addDialogComponent(project);
    }

    protected abstract SettingsModelString createProjectSettings();

    protected void createCheckProjectsButton() {
        checkProjectsButton = new DialogComponentButton(CHECK_PROJECTS_BUTTON);
        checkProjectsButton.addActionListener(getButtonConnectionCheckListener());
        addDialogComponent(checkProjectsButton);
    }

    protected abstract ActionListener getButtonConnectionCheckListener();

    protected void createCheckProjectsLabel() {
        checkProjectsLabel = new DialogComponentLabel(CHECK_PROJECTS_INIT_LABEL);
        addDialogComponent(checkProjectsLabel);
    }

    protected void updateCheckProjectsLabel(boolean connectionOK) {
        if (connectionOK) {
            checkProjectsLabel.setText(CHECK_PROJECTS_SUCCESS_LABEL);
        } else {
            checkProjectsLabel.setText(CHECK_PROJECTS_FAILURE_LABEL);
        }
    }

    protected void createLoginTab() {
        createNewTab(LOGIN_TAB_NAME);
        createLoginComponent();
        createPasswordComponent();
    }

    protected void createLoginComponent() {
        login = new DialogComponentString(createLoginSettings(), LOGIN_LABEL, false, COMPONENT_WIDTH);
        addDialogComponent(login);
    }

    protected void createPasswordComponent() {
        password = new DialogComponentPasswordField(createPasswordSettings(), PASSWORD_LABEL, COMPONENT_WIDTH);
        addDialogComponent(password);
    }

    protected abstract SettingsModelString createLoginSettings();

    protected abstract SettingsModelString createPasswordSettings();

    protected void createFiltersTab() {
        createNewTab(FILTERS_TAB_NAME);
        createAvailableFiltersComponent();
        createFilterOptionsComponent();
    }

    protected void createFilterOptionsComponent() {
    	
    }

    protected void createAvailableFiltersComponent() {
    	
    }
    
    protected void createAdvancedTab() {
        createNewTab(ADVANCED_TAB_NAME);
        createThreadsCountComponent();
    }

    protected void createThreadsCountComponent() {
		threadsCount = new DialogComponentNumberEdit(createThreadsCountSettings(), THREADS_COUNT_LABEL, COMPONENT_WIDTH);
		addDialogComponent(threadsCount);
	}
	
	protected abstract SettingsModelInteger createThreadsCountSettings();

}
