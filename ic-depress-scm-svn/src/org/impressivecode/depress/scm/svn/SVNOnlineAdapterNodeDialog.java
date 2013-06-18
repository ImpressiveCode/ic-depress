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
package org.impressivecode.depress.scm.svn;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * 
 * @author IcDepress
 */
public class SVNOnlineAdapterNodeDialog extends DefaultNodeSettingsPane {

    private final SettingsModelString remoteRepo = new SettingsModelString(
            SVNOnlineAdapterNodeModel.SVN_REPOSITORY_ADDRESS, SVNOnlineAdapterNodeModel.SVN_REPOSITORY_DEFAULT);

    protected SVNOnlineAdapterNodeDialog() {
        super();

        final DialogComponentString remoteRepoAddress = new DialogComponentString(remoteRepo, "Repository address: ");

        addDialogComponent(remoteRepoAddress);

        addDialogComponent(new DialogComponentString(new SettingsModelString(SVNOnlineAdapterNodeModel.SVN_REGEXP,
                SVNOnlineAdapterNodeModel.SVN_REGEXP_DEFAULT), "Issue marker: "));

        addDialogComponent(new DialogComponentString(new SettingsModelString(SVNOnlineAdapterNodeModel.SVN_PACKAGENAME,
                SVNOnlineAdapterNodeModel.SVN_PACKAGENAME_DEFAULT), "Package: "));
    }
}
