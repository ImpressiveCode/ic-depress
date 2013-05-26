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

import java.io.File;
import java.io.IOException;

import javax.naming.directory.InvalidAttributesException;

import org.impressivecode.depress.data.anonymisation.objects.CryptographicUtility;
import org.impressivecode.depress.data.anonymisation.objects.FileHelper;
import org.impressivecode.depress.data.anonymisation.objects.PropertiesValidator;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.container.ColumnRearranger;
import org.knime.core.data.container.SingleCellFactory;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelFilterString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * This is the model implementation of Anonymisation. Encrypts and decrypts
 * selected columns from input data set using DES cryptographic algorithm.
 * 
 * @author Andrzej Dudek
 * @author Marcin Bogusz
 * @author Konrad Kocik
 * @author Artur Bilski
 */
public class AnonymisationNodeModel extends NodeModel {

    static final String COLUMNS_CONFIG_NAME = "columns";
    static final String KEY_CONFIG_NAME = "key";
    static final int INPUT_PORT = 0;
    public static final int KEY_LENGTH = 8;
    public static int ANALYSIS_IMPORTANT_ROWS = 30;

    // saved Settings from Dialog
    public static SettingsModelFilterString filterStringSettings = new SettingsModelFilterString(COLUMNS_CONFIG_NAME);

    // Saved keyFile Settings
    public static SettingsModelString keyPathSetting = new SettingsModelString(KEY_CONFIG_NAME, FileHelper
            .getUniqueFile(FileHelper.KEY_FILENAME).getPath());

    // Saved Input DataTableSpec
    public static DataTableSpec InputTableSpec;

    /**
     * Constructor for the node model.
     */
    protected AnonymisationNodeModel() {
        super(1, 1);
    }

    public static boolean isColumnEncrypted(String columnName, int importantRows) throws InvalidAttributesException {

        if (columnName == null) {
            throw new InvalidAttributesException();
        }
        if (columnName == "") {
            throw new InvalidAttributesException();
        }

        // Column Values Set
        Object[] rows = InputTableSpec.getColumnSpec(columnName).getDomain().getValues().toArray();

        boolean isEncrypted = true;
        int i = 0;
        while(isEncrypted && i < importantRows && i < rows.length)
        {
            String rowVal = rows[i].toString();
            isEncrypted = CryptographicUtility.isEncrypted(rowVal);
            i++;
        }
        return isEncrypted && i > 0;
    }

    public static boolean isColumnEncrypted(int columnIndex, int importantRows) throws InvalidAttributesException {
        return isColumnEncrypted(InputTableSpec.getColumnSpec(columnIndex).getName(), importantRows);
    }

    /**
     * {@inheritDoc}
     */

    protected BufferedDataTable[] execute(BufferedDataTable[] inData, ExecutionContext exec) throws Exception {

        DataTableSpec inSpec = inData[0].getDataTableSpec();
        java.io.File keyFile = new File(keyPathSetting.getStringValue());
        // Able execute Node without enter into configuration
        if (!keyFile.exists()) {
            FileHelper.GenerateKeyFile(FileHelper.KEY_FILENAME);
        }
        if (filterStringSettings.getExcludeList().isEmpty() && filterStringSettings.getIncludeList().isEmpty()) {
            filterStringSettings.setIncludeList(inSpec.getColumnNames());
        }
        ColumnRearranger rearranger = createColumnRearranger(inSpec);
        BufferedDataTable outTable = exec.createColumnRearrangeTable(inData[0], rearranger, exec);
        return new BufferedDataTable[] { outTable };
    }

    private ColumnRearranger createColumnRearranger(final DataTableSpec spec) throws InvalidSettingsException {
        // check user settings against input spec here
        // fail with InvalidSettingsException if invalid
        ColumnRearranger result = new ColumnRearranger(spec);
        for (String colName : filterStringSettings.getIncludeList()) {
            // index of modified column
            final int index = spec.findColumnIndex(colName);

            // remove previous version of column
            result.remove(index);

            // new column initalization
            DataColumnSpecCreator appendSpecCreator = new DataColumnSpecCreator(
                    spec.getColumnNames()[index], StringCell.TYPE);
            DataColumnSpec appendSpec = appendSpecCreator.createSpec();
            result.insertAt(index, new SingleCellFactory(appendSpec) {
                public DataCell getCell(final DataRow row) {
                    String cellVal = "";
                    if (!row.getCell(index).isMissing() && !row.getCell(index).toString().isEmpty()) {
                        cellVal = row.getCell(index).toString();
                        //remove newLine mark from end if exists
                        cellVal = cellVal.endsWith("\r") ? cellVal.substring(0, 12) : cellVal;
                        try {
                            boolean shouldEncrypt = !CryptographicUtility.isEncrypted(cellVal);
                            cellVal = CryptographicUtility.useAlgorithm(cellVal,
                                    FileHelper.ReadFromFile(keyPathSetting.getStringValue()), shouldEncrypt);
                        } catch (IOException e) {
                            System.err.println("Error: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    DataCell resultCell = new StringCell(cellVal);
                    return resultCell;
                }
            });
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {

        // TODO: generated method stub

        // Check incoming table
        if (inSpecs[0].getNumColumns() <= 0) {
            throw new InvalidSettingsException("Input table must contain at least " + "one column");
        }

        InputTableSpec = inSpecs[0];

        return new DataTableSpec[] { null };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        // TODO: generated method stub
        filterStringSettings.saveSettingsTo(settings);
        keyPathSetting.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        // TODO: generated method stub
        filterStringSettings.loadSettingsFrom(settings);
        keyPathSetting.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        // TODO: generated method stub
        if (settings.keySet().contains(KEY_CONFIG_NAME)) {
            String path = settings.getRowKey(KEY_CONFIG_NAME).getString();
            PropertiesValidator.isKeyFileCorrect(path);
        }

        if (settings.keySet().contains(COLUMNS_CONFIG_NAME)) {
            PropertiesValidator.columnsCheck(settings.getConfig(COLUMNS_CONFIG_NAME));
        }

        keyPathSetting.validateSettings(settings);
        filterStringSettings.validateSettings(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // TODO: generated method stub
    }

}
