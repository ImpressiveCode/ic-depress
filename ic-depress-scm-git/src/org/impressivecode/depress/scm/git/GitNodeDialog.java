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
        		  				"Issue maker: "));
          
        addDialogComponent(new DialogComponentOptionalString(
	  							new SettingsModelOptionalString(GitNodeModel.GIT_PACKAGENAME, 
	  															GitNodeModel.GIT_PACKAGENAME_DEFAULT, 
	  															GitNodeModel.GIT_PACKAGENAME_ACTIVE_STATE),
	  							"Package: "));
    }
}

