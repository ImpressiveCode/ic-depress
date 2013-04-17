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
import org.knime.core.node.defaultnodesettings.DialogComponentOptionalString;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelOptionalString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "Git" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Tomasz Kuzemko
 * @author Sławomir Kapłoński
 */
public class GitNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring Git node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected GitNodeDialog() {
    	super();
        
        addDialogComponent(new DialogComponentFileChooser(
        		  				new SettingsModelString(GitNodeModel.GIT_FILENAME, GitNodeModel.GIT_FILENAME_DEFAULT),
        		  				GitNodeModel.GIT_FILENAME_DEFAULT, 
        		  				JFileChooser.OPEN_DIALOG, 
        		  				false));
          
        addDialogComponent(new DialogComponentString(
        		  				new SettingsModelString(GitNodeModel.GIT_REGEXP, GitNodeModel.GIT_REGEXP_DEFAULT),
        		  				"Issue marker: "));
          
        addDialogComponent(new DialogComponentOptionalString(
	  							new SettingsModelOptionalString(GitNodeModel.GIT_PACKAGENAME, 
	  															GitNodeModel.GIT_PACKAGENAME_DEFAULT, 
	  															GitNodeModel.GIT_PACKAGENAME_ACTIVE_STATE),
	  							"Package: "));
    }
}

