package org.impressivecode.depress.metric.ckjm;

import static org.impressivecode.depress.metric.ckjm.CKJMTableFactory.createDataColumnSpec;
import static org.impressivecode.depress.metric.ckjm.CKJMTableFactory.createTableRow;
import static org.impressivecode.depress.metric.ckjm.CKJMTableFactory.createTableSpec;
import static org.impressivecode.depress.metric.ckjm.CKJMEntriesParser.unmarshalResults;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.impressivecode.depress.metric.ckjm.CKJMXmlResult.Classes.Class;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;


/**
 * This is the model implementation of CKJM.
 * 
 *
 * @author Kamil Krzyzanowski, Wroclaw University of Technology
 */
public class CKJMNodeModel extends NodeModel {
   
	private static final String DEFAULT_VALUE = "";

    private static final String CONFIG_NAME = "depress.metric.ckjm.confname";

    private static final NodeLogger LOGGER = NodeLogger.getLogger(CKJMNodeModel.class);

    private final SettingsModelString fileSettings = createFileChooserSettings();

    protected CKJMNodeModel() {
        super(0, 1);
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {

        LOGGER.info("Preparing to read ckjm logs.");
        BufferedDataContainer container = createDataContainer(exec);
        LOGGER.info("Reading file: " + fileSettings.getStringValue());
        List<Class> result = unmarshalResults(fileSettings.getStringValue());
        BufferedDataTable out = transform(container, result, exec);
        LOGGER.info("Reading ckjm logs finished.");

        return new BufferedDataTable[] { out };
    }

    @Override
    protected void reset() {
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        // TODO mmajchrz add validate from apache common or guava
        return createTableSpec();
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        fileSettings.saveSettingsTo(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        fileSettings.loadSettingsFrom(settings);
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        fileSettings.validateSettings(settings);
    }

    @Override
    protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // NOOP
    }

    @Override
    protected void saveInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // NOOP
    }

    private BufferedDataTable transform(final BufferedDataContainer container, final List<Class> classes,
            final ExecutionContext exec) throws CanceledExecutionException {
        int size = classes.size();
        for (int i = 0; i < size; i++) {
            progress(exec, size, i);

            Class clazz = classes.get(i);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Transforming ckjm log, class: " + clazz.getName() + ", score: " + clazz.getScore());
            }

            BigDecimal score = clazz.getScore();
            String className = clazz.getName();
            addRowToTable(container, className, score);
        }
        container.close();
        BufferedDataTable out = container.getTable();
        return out;
    }

    private void progress(final ExecutionContext exec, final int size, final int i) throws CanceledExecutionException {
        exec.checkCanceled();
        exec.setProgress(i / size);
    }

    private BufferedDataContainer createDataContainer(final ExecutionContext exec) {
        DataTableSpec outputSpec = createDataColumnSpec();
        BufferedDataContainer container = exec.createDataContainer(outputSpec);
        return container;
    }

    private void addRowToTable(final BufferedDataContainer container, final String className, final BigDecimal score) {
        container.addRowToTable(createTableRow(className, score));
    }

    static SettingsModelString createFileChooserSettings() {
        return new SettingsModelString(CONFIG_NAME, DEFAULT_VALUE);
    }
}

