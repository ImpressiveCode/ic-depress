package org.impressivecode.depress.data.anonymisation;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentButton;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnFilter;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelFilterString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "Anonymisation" Node. Encrypts and decrypts
 * selected input data using symmetric algorithm (Blowfish), using provided
 * encryption key.
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
     * 
     * I suggests a little refactoring and adding a little help for user @author
     * Fizgon
     */

    // Declare Content Pane

    protected AnonymisationNodeDialog() {
        // Groups
        createNewGroup("Column selection:");
        

        // Buttons
        DialogComponentButton ButtonToCreateFile = new DialogComponentButton("Create new and load");
        DialogComponentButton ButtonToClear = new DialogComponentButton("Clear");

        // A little help
        ButtonToCreateFile.setToolTipText("This create file with your key and load it automatically");

        // adding all components
        addDialogComponent(new DialogComponentColumnFilter(
                new SettingsModelFilterString(AnonymisationNodeModel.COLUMNS), AnonymisationNodeModel.INPUT_PORT, false));
        setHorizontalPlacement(true);
        
        createNewGroup("Cryptographic key selection:");
        addDialogComponent(new DialogComponentFileChooser(new SettingsModelString(AnonymisationNodeModel.KEY, ""), "",
                ".txt"));
        addDialogComponent(ButtonToCreateFile);
        addDialogComponent(ButtonToClear);

    }

}
