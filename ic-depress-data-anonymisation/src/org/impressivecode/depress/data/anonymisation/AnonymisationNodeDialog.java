package org.impressivecode.depress.data.anonymisation;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentButton;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnFilter;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelFilterString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "Anonymisation" Node.
 * Encrypts and decrypts selected input data using symmetric algorithm (Blowfish), using provided encryption key.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andrzej Dudek 
 * @author Marcin Bogusz
 * @author Konrad Kocik
 * @author Artur Bilski
 */
public class AnonymisationNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the Anonymisation node.
     */
	
    protected AnonymisationNodeDialog() {
    	createNewGroup("Column selection:");
    	addDialogComponent(new DialogComponentColumnFilter(new SettingsModelFilterString(AnonymisationNodeModel.COLUMNS), 
    			AnonymisationNodeModel.INUPT_PORT, false));
    	
    	createNewGroup("Cryptographic key selection:");
    	setHorizontalPlacement(true);    	
    	addDialogComponent(new DialogComponentFileChooser(new SettingsModelString(AnonymisationNodeModel.KEY, ""), "", ".txt"));
    	addDialogComponent(new DialogComponentButton("Create new and load"));
    	addDialogComponent(new DialogComponentButton("Clear")); 
    	  	
    }
}

