package org.impressivecode.depress.scm.git;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelOptionalString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;


/**
 * This is the model implementation of Git.
 * 
 *
 * @author Tomasz Kuzemko
 */
public class GitNodeModel extends NodeModel {
    
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(GitNodeModel.class);
        
	static final String GIT_FILENAME = "depress.scm.git.filename";
	static final String GIT_FILENAME_DEFAULT = "";
    static final String GIT_REGEXP = "depress.scm.git.regexp";
    static final String GIT_REGEXP_DEFAULT = "";
    static final String GIT_PACKAGENAME = "depress.scm.git.package";
    static final String GIT_PACKAGENAME_DEFAULT = "org";
    
    static final Boolean GIT_PACKAGENAME_ACTIVE_STATE = false;
    
    // example value: the models count variable filled from the dialog 
    // and used in the models execution method. The default components of the
    // dialog work with "SettingsModels".
    private final SettingsModelString gitFileName =  new SettingsModelString(GitNodeModel.GIT_FILENAME,
                    														 GitNodeModel.GIT_FILENAME_DEFAULT);
    private final SettingsModelString gitRegExp = new SettingsModelString(GitNodeModel.GIT_REGEXP,
    																	 GitNodeModel.GIT_REGEXP_DEFAULT);
    private final SettingsModelOptionalString gitPackageName = new SettingsModelOptionalString(GitNodeModel.GIT_PACKAGENAME,
    																		  GitNodeModel.GIT_PACKAGENAME_DEFAULT,
    																		  GitNodeModel.GIT_PACKAGENAME_ACTIVE_STATE);

    /**
     * Constructor for the node model.
     */
    protected GitNodeModel() {
    
        // TODO none incoming ports and one outgoing port is assumed
        super(0, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

        logger.info("Reading logs from file "+gitFileName);
        DataTableSpec outputSpec = GitTableFactory.createTable();
        BufferedDataContainer container = exec.createDataContainer(outputSpec);
        
        logger.info("Creating Output table with data from git file...");
        //trzeba przygotować tabelę wyjściową
        
        // once we are done, we close the container and return its table
        container.close();
        BufferedDataTable out = container.getTable();
        return new BufferedDataTable[]{out};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // TODO Code executed on reset.
        // Models build during execute are cleared here.
        // Also data handled in load/saveInternals will be erased here.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
        
        // TODO: check if user settings are available, fit to the incoming
        // table structure, and the incoming types are feasible for the node
        // to execute. If the node can execute in its current state return
        // the spec of its output data table(s) (if you can, otherwise an array
        // with null elements), or throw an exception with a useful user message

        return new DataTableSpec[]{null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {

    	gitFileName.saveSettingsTo(settings);
    	gitRegExp.saveSettingsTo(settings);
    	gitPackageName.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        gitFileName.loadSettingsFrom(settings);
        gitRegExp.loadSettingsFrom(settings);
        gitPackageName.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        gitFileName.validateSettings(settings);
        gitRegExp.validateSettings(settings);
        gitPackageName.validateSettings(settings);

    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        
        // TODO load internal data. 
        // Everything handed to output ports is loaded automatically (data
        // returned by the execute method, models loaded in loadModelContent,
        // and user settings set through loadSettingsFrom - is all taken care 
        // of). Load here only the other internals that need to be restored
        // (e.g. data used by the views).

    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
       
        // TODO save internal models. 
        // Everything written to output ports is saved automatically (data
        // returned by the execute method, models saved in the saveModelContent,
        // and user settings saved through saveSettingsTo - is all taken care 
        // of). Save here only the other internals that need to be preserved
        // (e.g. data used by the views).

    }

}

