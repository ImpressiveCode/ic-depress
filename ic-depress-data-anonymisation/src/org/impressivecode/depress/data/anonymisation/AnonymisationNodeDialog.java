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
 along with this program.  If not, see <http://www.gnu.org/licenses/
>.
 */

package org.impressivecode.depress.data.anonymisation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.naming.directory.InvalidAttributesException;

import org.impressivecode.depress.data.anonymisation.objects.AnonymisationFileChooser;
import org.impressivecode.depress.data.anonymisation.objects.FileHelper;
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
 * 
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

    AnonymisationFileChooser fileChooser;

    // Declare Content Pane
    protected AnonymisationNodeDialog() {
        super();

        try {
            // Groups
            createNewGroup("Column selection:");

            // Buttons
            buttonToCreateFile = new DialogComponentButton("Create new and load");
            buttonToCreateFile.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    String keyPath;
                    try {
                        keyPath = FileHelper.GenerateKeyFile(FileHelper.KEY_FILENAME);
                        fileChooser.SetSelectedPath(keyPath);
                        fileChooser.UpdateComponent();
                    } catch (IOException ex) {
                        System.err.println("Error: " + ex.getMessage());
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

            SettingsModelFilterString columnFilterSettings = new SettingsModelFilterString(
                    AnonymisationNodeModel.COLUMNS_CONFIG_NAME);

            if (isInputDataEncrypted()) {
                // if is data encrypted, then node should decrypt incoming
                // encrypted Data and leave plain text data;
                columnFilterSettings.setIncludeList(getColumnsFromInput(true));
                columnFilterSettings.setExcludeList(getColumnsFromInput(false));
            } else {
                // if incoming data is not containing encrypted columns, then
                // all column should be encrypted
                columnFilterSettings.setIncludeList(getColumnsFromInput(false));
            }

            columnFilter = new DialogComponentColumnFilter(columnFilterSettings, AnonymisationNodeModel.INPUT_PORT,
                    false);
            addDialogComponent(columnFilter);
            setHorizontalPlacement(true);

            createNewGroup("Cryptographic key selection:");

            fileChooser = new AnonymisationFileChooser(new SettingsModelString(AnonymisationNodeModel.KEY_CONFIG_NAME,
                    FileHelper.getUniqueFile(FileHelper.KEY_FILENAME).getPath()), "", ".txt");

            // adding all components
            addDialogComponent(buttonToCreateFile);
            addDialogComponent(buttonToClear);
            addDialogComponent(fileChooser);
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }

    /**
     * Method is analyzing incoming data from Input.
     * 
     * @return Return TRUE if at least one of input data is probably encrypted
     * @throws InvalidAttributesException
     */
    private boolean isInputDataEncrypted() throws InvalidAttributesException {
        for (String columnName : AnonymisationNodeModel.InputTableSpec.getColumnNames()) {
            boolean isColumnEncrypted = AnonymisationNodeModel.isColumnEncrypted(columnName,
                    AnonymisationNodeModel.ANALYSIS_IMPORTANT_ROWS);
            if (isColumnEncrypted) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * 
     * @param isEncryptedParam
     *            Defines type of returned columns
     * @return Return List of columns which specified state from input
     * @throws InvalidAttributesException
     */
    private Collection<String> getColumnsFromInput(boolean isEncryptedParam) throws InvalidAttributesException {
        ArrayList<String> columnList = new ArrayList<String>();

        for (String columnName : AnonymisationNodeModel.InputTableSpec.getColumnNames()) {
            boolean isCurrentColumnEnctypted = AnonymisationNodeModel.isColumnEncrypted(columnName,
                    AnonymisationNodeModel.ANALYSIS_IMPORTANT_ROWS);
            if (isCurrentColumnEnctypted && isEncryptedParam) {
                columnList.add(columnName);
            } else if (!isCurrentColumnEnctypted && !isEncryptedParam) {
                columnList.add(columnName);
            }

        }
        return columnList;
    }

}
