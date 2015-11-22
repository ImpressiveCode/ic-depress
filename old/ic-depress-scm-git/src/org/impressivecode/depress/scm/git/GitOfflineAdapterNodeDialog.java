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
package org.impressivecode.depress.scm.git;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * @author Tomasz Kuzemko
 * @author Sławomir Kapłoński
 * @author Maciej Borkowski Capgemini Poland
 */
public class GitOfflineAdapterNodeDialog extends DefaultNodeSettingsPane {

    public static final String ADVANCED_TAB_NAME = "Advanced";
	
    protected GitOfflineAdapterNodeDialog() {
        super();
        addDialogComponent(new DialogComponentFileChooser(new SettingsModelString(GitOfflineAdapterNodeModel.GIT_FILENAME,
                GitOfflineAdapterNodeModel.GIT_FILENAME_DEFAULT), GitOfflineAdapterNodeModel.GIT_FILENAME_DEFAULT, JFileChooser.OPEN_DIALOG, false));
        addDialogComponent(new DialogComponentString(GitOfflineAdapterNodeModel.extensions, "Extension pattern: (* = any extension, any string, ? = any character, split many extensions by comma)", false, 30)); 
        createNewTab(ADVANCED_TAB_NAME);
        addDialogComponent(new DialogComponentString(GitOfflineAdapterNodeModel.gitPackageName, "Package: ", false, 30));  
    }
}
