package org.knime.svnplugin;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.eclipse.equinox.log.Logger;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "SvnPlugin" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author PWr team
 */
public class SvnPluginNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring SvnPlugin node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected SvnPluginNodeDialog() {
        super();
        
        createNewGroup("SVN plugin");
        addDialogComponent(new DialogComponentString(new SettingsModelString("urlTextBox", "URL"), "Write here your URL")); 
    }
    
    private void createNewPanels() {
        
    }
}

