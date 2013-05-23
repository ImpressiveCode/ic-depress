package org.impressivecode.depress.data.anonymisation;

import java.io.File;
import java.io.IOException;

import javax.naming.directory.InvalidAttributesException;

import org.impressivecode.depress.data.objects.CryptographicUtility;
import org.impressivecode.depress.data.objects.FileHelper;
import org.impressivecode.depress.data.objects.PropertiesValidator;
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

        // TODO: Specify the amount of input and output ports needed.
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

        for (int i = 0; i < importantRows && i < rows.length; i++) {
            String rowVal = rows[i].toString();
            boolean isEncrypted = CryptographicUtility.isEncrypted(rowVal);
            if (!isEncrypted) {
                return false;
            }
        }
        return true;
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

    private ColumnRearranger createColumnRearranger(DataTableSpec spec) throws InvalidSettingsException {
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
                    spec.getColumnNames()[index].toUpperCase(), StringCell.TYPE);
            DataColumnSpec appendSpec = appendSpecCreator.createSpec();
            result.insertAt(index, new SingleCellFactory(appendSpec) {
                public DataCell getCell(final DataRow row) {
                    // TODO
                    // Anonymize cell of selected column here
                    String cellVal = "";
                    if (!row.getCell(index).isMissing() && !row.getCell(index).toString().isEmpty()) {
                        cellVal = row.getCell(index).toString();
                        try {
                            cellVal = CryptographicUtility.useAlgorithm(cellVal,
                                    FileHelper.ReadFromFile(keyPathSetting.getStringValue()), true);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
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
