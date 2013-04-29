package org.impressivecode.depress.data.anonymisation;

import java.io.File;
import java.io.IOException;

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
 * This is the model implementation of Anonymisation. Encrypts and decrypts
 * selected input data using symmetric algorithm (Blowfish), using provided
 * encryption key.
 * 
 * @author Andrzej Dudek
 * @author Marcin Bogusz
 * @author Konrad Kocik
 * @author Artur Bilski
 */
public class AnonymisationNodeModel extends NodeModel {

    static final String COLUMNS = "columns";
    static final String KEY = "key";
    static final int INUPT_PORT = 0;

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
        String path = settings.getRowKey("key").getString();
        isKeyFileCorrect(path); 
    }
   
    protected static final boolean isKeyFileCorrect(String path) throws InvalidSettingsException {
        File keyFile = new File(path);
        if(!keyFile.exists())
        {
            throw new InvalidSettingsException("Key File doesnt exists!");
        }
        if(!keyFile.isFile())
        {
            throw new InvalidSettingsException("Key File is not a file!");
        }
        return true;
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
