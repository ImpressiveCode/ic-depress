package org.impressivecode.depress.its.jiraonline.filter;

import java.util.ArrayList;
import java.util.List;

import org.impressivecode.depress.its.ITSFilter;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class ProjectNameFilter extends ITSFilter {

    private static final String JIRA_PROJECT_NAME = "depress.its.jiraonline.filter.project.name";

    private SettingsModelString projectNameModel;

    @Override
    public String getName() {
        return "Project name";
    }

    @Override
    public String getFilterValue() {
        return "project = " + projectNameModel.getStringValue();
    }

    @Override
    public List<DialogComponent> createDialogComponents() {
        List<DialogComponent> dialogComponents = new ArrayList<>();
        projectNameModel = new SettingsModelString(getFilterModelId(), "");
        dialogComponents.add(new DialogComponentString(projectNameModel, "Project name:"));
        return dialogComponents;
    }

    @Override
    public String getFilterModelId() {
        return JIRA_PROJECT_NAME;
    }

}
