package org.impressivecode.depress.data.anonymisation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.impressivecode.depress.data.objects.AnonymisationFileChooser;
import org.impressivecode.depress.data.objects.CryptographicUtility;
import org.impressivecode.depress.data.objects.EncryptionAnalyzer;
import org.impressivecode.depress.data.objects.FileHelper;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentButton;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnFilter;
import org.knime.core.node.defaultnodesettings.SettingsModelFilterString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "Anonymisation" Encrypts and decrypts
 * selected columns from input data set using DES cryptographic algorithm.
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

    // Private dialog components

    DialogComponentButton buttonToCreateFile;
    DialogComponentButton buttonToClear;

    DialogComponentColumnFilter columnFilter;

    private AnonymisationFileChooser fileChooser;

    // Declare Content Pane
    protected AnonymisationNodeDialog() {

        // Groups
        createNewGroup("Column selection:");

        // Buttons
        buttonToCreateFile = new DialogComponentButton("Create new and load");
        buttonToCreateFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

                String keyPath;
                try {
                    keyPath = FileHelper.CreateTmpFile("key-file");
                    FileHelper.WriteToFile(keyPath, CryptographicUtility.generateKey());
                    fileChooser.SetSelectedPath(keyPath);
                    fileChooser.UpdateComponent();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });

        buttonToClear = new DialogComponentButton("Clear");
        buttonToClear.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.SetSelectedPath("");
                fileChooser.UpdateComponent();
            }
        });

        // A little help
        buttonToCreateFile.setToolTipText("This create file with your key and load it automatically");

        // adding all components
        columnFilter = new DialogComponentColumnFilter(new SettingsModelFilterString(AnonymisationNodeModel.COLUMNS),
                AnonymisationNodeModel.INPUT_PORT, false);
        columnFilter.getModel().addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent arg0) {
                //Save actual ColumnFilterSettings into static field : AnonymisationNodeModel.filterStringSettings;
                AnonymisationNodeModel.filterStringSettings = (SettingsModelFilterString) arg0.getSource();
            }
        });
        addDialogComponent(columnFilter);
        setHorizontalPlacement(true);

        createNewGroup("Cryptographic key selection:");

        fileChooser = new AnonymisationFileChooser(new SettingsModelString(AnonymisationNodeModel.KEY, ""), "", ".txt");
        
        

        addDialogComponent(buttonToCreateFile);
        addDialogComponent(buttonToClear);
        addDialogComponent(fileChooser);
    }

}
