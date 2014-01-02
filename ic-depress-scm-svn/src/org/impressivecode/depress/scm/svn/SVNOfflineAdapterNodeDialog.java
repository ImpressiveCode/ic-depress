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

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * @author Krystian Dabrowski, Capgemini Poland
 * 
 */
public class SVNOfflineAdapterNodeDialog extends DefaultNodeSettingsPane {

    protected SVNOfflineAdapterNodeDialog() {
        super();

        addDialogComponent(new DialogComponentFileChooser(new SettingsModelString(
                SVNOfflineAdapterNodeModel.CFG_FILENAME, SVNOfflineAdapterNodeModel.FILENAME_DEFAULT),
                SVNOfflineAdapterNodeModel.FILENAME_DEFAULT, JFileChooser.OPEN_DIALOG, false));

        addDialogComponent(new DialogComponentLabel("Filter only file extensions:"));
        
        for(SVNFileExtensions supportedExtension:SVNFileExtensions.values()) {
        	SettingsModelBoolean settingsModelBoolean = new SettingsModelBoolean(supportedExtension.getConfigName(), supportedExtension.isDefaultSelected());
        	addDialogComponent(new DialogComponentBoolean(settingsModelBoolean, supportedExtension.getExtension()));	
        }
        
        
        addDialogComponent(new DialogComponentString(new SettingsModelString(
                SVNOfflineAdapterNodeModel.CFG_PACKAGENAME, SVNOfflineAdapterNodeModel.PACKAGENAME_DEFAULT),
                "Package: "));
        
    }
}
