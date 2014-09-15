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
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentPasswordField;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * 
 * @author IcDepress
 * @author Zuzanna Pacholczyk, Capgemini Poland
 */
public class SVNOnlineAdapterNodeDialog extends DefaultNodeSettingsPane {

	public static final String ADVANCED_TAB_NAME = "Advanced";
    private final SettingsModelString remoteRepo = new SettingsModelString(
            SVNOnlineAdapterNodeModel.SVN_REPOSITORY_ADDRESS, SVNOnlineAdapterNodeModel.SVN_REPOSITORY_DEFAULT);
    private final SettingsModelString remoteLogin = new SettingsModelString(
            SVNOnlineAdapterNodeModel.SVN_LOGIN, SVNOnlineAdapterNodeModel.SVN_LOGIN_DEFAULT);
    private final SettingsModelString remotePassword = new SettingsModelString(
            SVNOnlineAdapterNodeModel.SVN_PASSWORD, SVNOnlineAdapterNodeModel.SVN_PASSWORD_DEFAULT);

    protected SVNOnlineAdapterNodeDialog() {
        super();

        final DialogComponentString remoteRepoAddress = new DialogComponentString(remoteRepo, "Repository address: ", false, 20);
        final DialogComponentString remoteLoginSVN = new DialogComponentString(remoteLogin, "Login: ", false, 30);
        
        final DialogComponentPasswordField remotePasswordSVN = new DialogComponentPasswordField(remotePassword, "Password: ", 20);
        addDialogComponent(remoteRepoAddress);
        addDialogComponent(remoteLoginSVN);
        addDialogComponent(remotePasswordSVN);
        
        addDialogComponent(new DialogComponentString(new SettingsModelString(SVNOnlineAdapterNodeModel.SVN_EXTENSION, SVNOnlineAdapterNodeModel.EXTENSION_DEFAULT),"Extension pattern:", false, 30)); 
        addDialogComponent(new DialogComponentLabel(" (* = any extension, any string, ? = any character, split many extensions by comma)"));

        createNewTab(ADVANCED_TAB_NAME);
  
        createNewGroup("Java");
        addDialogComponent(new DialogComponentString(new SettingsModelString(
        		SVNOnlineAdapterNodeModel.SVN_PACKAGENAME, SVNOnlineAdapterNodeModel.SVN_PACKAGENAME_DEFAULT),
                "Package prefix: "));
        closeCurrentGroup();
    }
}
