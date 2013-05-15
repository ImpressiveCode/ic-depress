package org.impressivecode.depress.data.anonymisation;

import java.io.File;
import java.io.IOException;

import org.impressivecode.depress.data.objects.CryptographicUtility;
import org.impressivecode.depress.data.objects.EncryptionAnalyzer;
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

    static final String COLUMNS = "columns";
    static final String KEY = "key";
    static final int INPUT_PORT = 0;
    public static final int KEY_LENGTH = 8;

    // Settings from Dialog
    public static SettingsModelFilterString filterStringSettings = new SettingsModelFilterString("columnfilterConfig");
    
    public static String KeyPathSetting = "";
    

    /**
     * Constructor for the node model.
     */
    protected AnonymisationNodeModel() {

        // TODO: Specify the amount of input and output ports needed.
        super(1, 1);
    }

    /**
     * {@inheritDoc}
     */
        
    protected BufferedDataTable[] execute(BufferedDataTable[] inData, ExecutionContext exec) throws Exception {
        DataTableSpec inSpec = inData[0].getDataTableSpec();
        ColumnRearranger rearranger = createColumnRearranger(inSpec);
        BufferedDataTable outTable = exec.createColumnRearrangeTable(inData[0], rearranger, exec);
        return new BufferedDataTable[] { outTable };
    }

    private ColumnRearranger createColumnRearranger(DataTableSpec spec) throws InvalidSettingsException {
        // check user settings against input spec here
        // fail with InvalidSettingsException if invalid
        ColumnRearranger result = new ColumnRearranger(spec);
        for (String colName : filterStringSettings.getExcludeList()) {
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
                    if(!row.getCell(index).isMissing())
                    {
                        cellVal =  row.getCell(index).toString();
                        try {
                            cellVal = CryptographicUtility.useAlgorithm(cellVal, FileHelper.ReadFromFile(KeyPathSetting), true);
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
        return new DataTableSpec[] { null };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        // TODO: generated method stub
        if (settings.keySet().contains(KEY)) {
            String path = settings.getRowKey(KEY).getString();
            PropertiesValidator.isKeyFileCorrect(path);
        }

        if (settings.keySet().contains(COLUMNS)) {
            PropertiesValidator.columnsCheck(settings.getConfig(COLUMNS));
        }

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
