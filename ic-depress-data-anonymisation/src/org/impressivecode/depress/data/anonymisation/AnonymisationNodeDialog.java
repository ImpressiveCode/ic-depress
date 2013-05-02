package org.impressivecode.depress.data.anonymisation;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.security.SecureRandom;

import javax.swing.plaf.FileChooserUI;

import org.impressivecode.depress.data.objects.AnonymisationFileChooser;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentButton;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnFilter;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelFilterString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "Anonymisation" Encrypts and decrypts 
 * selected columns from input data set using Blowfish cryptographic algorithm.
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

    //Private dialog components
    
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
                
                String keyPath = GenerateRandomFile("TempKey");
                
                fileChooser.SetSelectedPath(keyPath);
                fileChooser.UpdateComponent();

            }
        });
            
        
        buttonToClear = new DialogComponentButton("Clear");
        buttonToClear.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
            }
        });


        // A little help
        buttonToCreateFile.setToolTipText("This create file with your key and load it automatically");

        // adding all components
        columnFilter = new DialogComponentColumnFilter(
                new SettingsModelFilterString(AnonymisationNodeModel.COLUMNS), AnonymisationNodeModel.INPUT_PORT, false);
        addDialogComponent(columnFilter);
        setHorizontalPlacement(true);

        createNewGroup("Cryptographic key selection:");
        
        fileChooser =  new AnonymisationFileChooser(new SettingsModelString(AnonymisationNodeModel.KEY, ""), "",
                ".txt");
        
        
        addDialogComponent(buttonToCreateFile);
        addDialogComponent(buttonToClear);
        addDialogComponent(fileChooser);
    }
    protected String GenerateRandomFile(String fileName) {
        String path = System.getProperty("java.io.tmpdir") + "\\" ;
        String ext = ".txt";
        File keyFile = new File(path + fileName + ext);
        
        try
        {

        int i = 0;
        while(keyFile.exists())
        {
            keyFile = new File(path+fileName + ((i>0)?"("+i+")":"") + ext);
            i++;
        }
                
        FileWriter fstream = new FileWriter(keyFile.getPath());
        BufferedWriter out = new BufferedWriter(fstream);
        out.write(RandomString(AnonymisationNodeModel.KEY_LENGTH));
        //Close the output stream
        out.close();
        }catch (Exception e){//Catch exception if any
        System.err.println("Error: " + e.getMessage());
        }
        return keyFile.getPath();
    }
    private String RandomString(int length) {
        
        SecureRandom random = new SecureRandom();
        return new java.math.BigInteger(length*5, random).toString(32);
    }
}
