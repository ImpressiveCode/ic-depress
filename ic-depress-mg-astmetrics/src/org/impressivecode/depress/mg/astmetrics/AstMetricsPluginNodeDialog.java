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
package org.impressivecode.depress.mg.astmetrics;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * @author Mateusz Kutyba, Wroclaw University of Technology
 */
public class AstMetricsPluginNodeDialog extends DefaultNodeSettingsPane {

    private final SettingsModelString gitRepositoryAddress = new SettingsModelString(
            AstMetricsPluginNodeModel.GIT_REPOSITORY_ADDRESS, AstMetricsPluginNodeModel.DEFAULT_VALUE);
    private final SettingsModelString gitPackageName = new SettingsModelString(
            AstMetricsPluginNodeModel.GIT_PACKAGENAME, AstMetricsPluginNodeModel.DEFAULT_GIT_PACKAGENAME);
    private final SettingsModelString bottomCommit = new SettingsModelString(AstMetricsPluginNodeModel.BOTTOM_COMMIT,
            AstMetricsPluginNodeModel.DEFAULT_VALUE);
    private final SettingsModelString topCommit = new SettingsModelString(AstMetricsPluginNodeModel.TOP_COMMIT,
            AstMetricsPluginNodeModel.DEFAULT_VALUE);

    protected AstMetricsPluginNodeDialog() {
        super();

        createNewGroup("Select project repository information");

        DialogComponentFileChooser comp = new DialogComponentFileChooser(gitRepositoryAddress,
                AstMetricsPluginNodeModel.GIT_REPOSITORY_ADDRESS, JFileChooser.OPEN_DIALOG, true);

        comp.setBorderTitle("Choose project directory:");
        addDialogComponent(comp);

        addDialogComponent(new DialogComponentString(gitPackageName, "Package prefix:", true, 35));

        createNewGroup("Revision range (commit hash or tag)");
        addDialogComponent(new DialogComponentString(bottomCommit, "From commit:", false, 35));
        addDialogComponent(new DialogComponentString(topCommit, "To commit:", false, 35));
    }
}
