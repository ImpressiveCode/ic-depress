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
package org.impressivecode.depress.scm.gitonline;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * @author Tomasz Kuzemko
 * @author Sławomir Kapłoński
 */
public class GitonlineAdapterNodeDialog extends DefaultNodeSettingsPane {

    protected GitonlineAdapterNodeDialog() {
        super();

        addDialogComponent(new DialogComponentString(new SettingsModelString(GitonlineAdapterNodeModel.GIT_REPOSITORY_ADDRESS,
                GitonlineAdapterNodeModel.GIT_REPOSITORY_DEFAULT), "Repository address: "));

        addDialogComponent(new DialogComponentString(new SettingsModelString(GitonlineAdapterNodeModel.GIT_REGEXP,
                GitonlineAdapterNodeModel.GIT_REGEXP_DEFAULT), "Issue marker: "));

        addDialogComponent(new DialogComponentString(new SettingsModelString(GitonlineAdapterNodeModel.GIT_PACKAGENAME, 
                GitonlineAdapterNodeModel.GIT_PACKAGENAME_DEFAULT), "Package: "));
    }
}
