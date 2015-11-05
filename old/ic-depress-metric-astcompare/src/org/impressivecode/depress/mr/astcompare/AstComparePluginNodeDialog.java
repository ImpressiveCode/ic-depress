package org.impressivecode.depress.mr.astcompare;

import static org.impressivecode.depress.mr.astcompare.utils.Utils.DATE_FROM;
import static org.impressivecode.depress.mr.astcompare.utils.Utils.DATE_TO;
import static org.impressivecode.depress.mr.astcompare.utils.Utils.PROJECTS_NAMES;
import static org.impressivecode.depress.mr.astcompare.utils.Utils.WEEKS;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.impressivecode.depress.mr.astcompare.utils.Utils;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

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
public class AstComparePluginNodeDialog extends DefaultNodeSettingsPane {

    protected AstComparePluginNodeDialog() {
        super();

        List<String> projects = new ArrayList<String>();
        IWorkspace root = ResourcesPlugin.getWorkspace();

        for (IProject project : root.getRoot().getProjects()) {
            projects.add(project.getName());
        }

        createNewGroup("Select project");
        addDialogComponent(new DialogComponentStringSelection(new SettingsModelString(PROJECTS_NAMES, ""), "Project:",
                projects));
        createNewGroup("Set revision's date range (DD-MM-YYYY)");
        setHorizontalPlacement(true);
        addDialogComponent(new DialogComponentString(new SettingsModelString(DATE_FROM, Utils.getCurrentDayPlus(
                Calendar.MONTH, -2)), "From:"));
        setHorizontalPlacement(true);
        addDialogComponent(new DialogComponentString(new SettingsModelString(DATE_TO, Utils.getCurrentDate()), "To:"));
        createNewGroup("Choose sample's timeframe");
        addDialogComponent(new DialogComponentStringSelection(new SettingsModelString(WEEKS, "All"), "Weeks:",
                new String[] { "2", "4", "8", "All" }));
    }
}
