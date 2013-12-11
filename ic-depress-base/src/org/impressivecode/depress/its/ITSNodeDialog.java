package org.impressivecode.depress.its;

import java.awt.event.ActionListener;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentButton;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnFilter;
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;
import org.knime.core.node.defaultnodesettings.DialogComponentPasswordField;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public abstract class ITSNodeDialog extends DefaultNodeSettingsPane {

    private static final String ITS_TAB_CONNECTION = "Connection";
    private static final String ITS_TAB_LOGIN = "Login";
    private static final String ITS_TAB_FILTERS = "Filters";

    private static final String ITS_LABEL_URL = "URL: ";
    private static final String ITS_LABEL_PROJECT = "Project: ";
    private static final String ITS_LABEL_PASSWORD = "Password: ";
    private static final String ITS_LABEL_LOGIN = "Login: ";

    private static final String ITS_BUTTON_PROJECTS_CHECK = "Check projects";

    private static final String ITS_LABEL_PROJECTS_CHECK_INIT = "After the check you should be able to choose available projects from the project list.";
    private static final String ITS_LABEL_PROJECTS_CHECK_OK = "Projects list updated.";
    private static final String ITS_LABEL_PROJECTS_CHECK_FAIL = "Connection failed.";

    private static final int ITS_COMPONENT_WIDTH = 32;

    /**
     * Connection components
     */
    private DialogComponentString url;
    private DialogComponentStringSelection project;
    private DialogComponentButton projectsCheckButton;
    private DialogComponentLabel projectsCheckLabel;
    /**
     * Login components
     */
    private DialogComponentString login;
    private DialogComponentPasswordField password;
    /**
     * Filter components
     */
    private DialogComponentColumnFilter availableFilters;
    private Object filterOptions;

    public ITSNodeDialog() {
        createConnectionTab();
        createLoginTab();
        createFiltersTab();
    }

    protected void createConnectionTab() {
        createNewGroup(ITS_TAB_CONNECTION);
        createHostnameComponent();
        createProjectChooser();
        createCheckProjectsButton();
        createCheckProjectsLabel();
    }

    private void createHostnameComponent() {
        SettingsModelString hostnameComponentModel = createSettingsURL();
        url = new DialogComponentString(hostnameComponentModel, ITS_LABEL_URL, true, ITS_COMPONENT_WIDTH);
        addDialogComponent(url);
    }

    protected abstract SettingsModelString createSettingsURL();

    private void createProjectChooser() {
        SettingsModelString projectChooserModel = createSettingsProject();
        project = new DialogComponentStringSelection(projectChooserModel, ITS_LABEL_PROJECT, null, false);
        addDialogComponent(project);
    }

    protected abstract SettingsModelString createSettingsProject();

    private void createCheckProjectsButton() {
        projectsCheckButton = new DialogComponentButton(ITS_BUTTON_PROJECTS_CHECK);
        projectsCheckButton.addActionListener(getButtonConnectionCheckListener());
        addDialogComponent(projectsCheckButton);
    }

    protected abstract ActionListener getButtonConnectionCheckListener();

    private void createCheckProjectsLabel() {
        projectsCheckLabel = new DialogComponentLabel(ITS_LABEL_PROJECTS_CHECK_INIT);
        addDialogComponent(projectsCheckLabel);
    }

    protected void updateCheckProjectsLabel(boolean connectionOK) {
        if (connectionOK)
            projectsCheckLabel.setText(ITS_LABEL_PROJECTS_CHECK_OK);
        else
            projectsCheckLabel.setText(ITS_LABEL_PROJECTS_CHECK_FAIL);
    }

    protected void createLoginTab() {
        createNewTab(ITS_TAB_LOGIN);
        createNewGroup(ITS_TAB_LOGIN);
        createLoginComponent();
        createPasswordComponent();
    }

    private void createLoginComponent() {
        login = new DialogComponentString(createSettingsLogin(), ITS_LABEL_LOGIN, false, ITS_COMPONENT_WIDTH);
        addDialogComponent(login);
    }

    private void createPasswordComponent() {
        password = new DialogComponentPasswordField(createSettingsPassword(), ITS_LABEL_PASSWORD, ITS_COMPONENT_WIDTH);
        addDialogComponent(password);
    }

    protected abstract SettingsModelString createSettingsLogin();

    protected abstract SettingsModelString createSettingsPassword();

    protected void createFiltersTab() {
        createNewTab(ITS_TAB_FILTERS);
        createNewGroup(ITS_TAB_FILTERS);
        createAvailableFiltersComponent();
        createFilterOptionsComponent();
    }

    private void createFilterOptionsComponent() {
        // TODO Auto-generated method stub
        // availableFilters = new
    }

    private void createAvailableFiltersComponent() {
        // TODO Auto-generated method stub

    }

    public DialogComponentString getUrl() {
        return url;
    }

    public void setUrl(DialogComponentString url) {
        this.url = url;
    }

    public DialogComponentStringSelection getProject() {
        return project;
    }

    public void setProject(DialogComponentStringSelection project) {
        this.project = project;
    }

    public DialogComponentButton getProjectsCheckButton() {
        return projectsCheckButton;
    }

    public void setProjectsCheckButton(DialogComponentButton projectsCheckButton) {
        this.projectsCheckButton = projectsCheckButton;
    }

    public DialogComponentLabel getProjectsCheckLabel() {
        return projectsCheckLabel;
    }

    public void setProjectsCheckLabel(DialogComponentLabel projectsCheckLabel) {
        this.projectsCheckLabel = projectsCheckLabel;
    }

    public DialogComponentString getLogin() {
        return login;
    }

    public void setLogin(DialogComponentString login) {
        this.login = login;
    }

    public DialogComponentPasswordField getPassword() {
        return password;
    }

    public void setPassword(DialogComponentPasswordField password) {
        this.password = password;
    }

    public DialogComponentColumnFilter getAvailableFilters() {
        return availableFilters;
    }

    public void setAvailableFilters(DialogComponentColumnFilter availableFilters) {
        this.availableFilters = availableFilters;
    }

    public Object getFilterOptions() {
        return filterOptions;
    }

    public void setFilterOptions(Object filterOptions) {
        this.filterOptions = filterOptions;
    }

}
