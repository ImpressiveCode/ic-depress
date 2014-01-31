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
package org.impressivecode.depress.its.jiraonline.filter;

import java.util.ArrayList;
import java.util.List;

import org.impressivecode.depress.its.ITSFilter;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * Filter for selecting project in Jira system
 * 
 * @author Marcin Kunert, Wroclaw University of Technology
 * 
 */
public class JiraOnlineFilterProjectName extends ITSFilter {

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
