package org.impressivecode.depress.data.anonymisation;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.impressivecode.depress.data.objects.PropertiesValidator;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * This is the model implementation of Anonymisation. Encrypts and decrypts selected 
 * columns from input data set using Blowfish cryptographic algorithm.
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
    public static final int KEY_LENGTH = 128;

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
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {

        // TODO: Return a BufferedDataTable for each output port

        for(BufferedDataTable table : inData){
            Iterator<DataColumnSpec> columnsItarator = table.getDataTableSpec().iterator(); 
            while(columnsItarator.hasNext())
            {
              //TODO
                //iterating every column. Should iterate only those from NodeDialog.ColumnFilter.
                //Other just add to output. (but how?)
                DataColumnSpec columnSpec = columnsItarator.next();
                for(DataCell cell : columnSpec.getDomain().getValues())
                {
                    if(!cell.isMissing())
                    {
                        //TODO
                        //iterating every cell. Here should be anonymisation for that column
                        String value = cell.toString();
                        value.toString();
                    }
                }
            }
        }
        return new BufferedDataTable[] {};
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
