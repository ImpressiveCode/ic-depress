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
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.impressivecode.depress.metric.judy;

import static org.impressivecode.depress.metric.judy.JudyAdapterTableFactory.createDataColumnSpec;
import static org.impressivecode.depress.metric.judy.JudyAdapterTableFactory.createTableRow;
import static org.impressivecode.depress.metric.judy.JudyAdapterTableFactory.createTableSpec;
import static org.impressivecode.depress.metric.judy.JudyEntriesParser.unmarshalResults;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.impressivecode.depress.metric.judy.JudyXmlResult.Classes.Class;
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
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class JudyAdapterNodeModel extends NodeModel {

    private static final String DEFAULT_VALUE = "";

    private static final String CONFIG_NAME = "depress.metric.judy.confname";

    private static final NodeLogger LOGGER = NodeLogger.getLogger(JudyAdapterNodeModel.class);

    private final SettingsModelString fileSettings = createFileChooserSettings();

    protected JudyAdapterNodeModel() {
        super(0, 1);
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {

        LOGGER.info("Preparing to read judy logs.");
        BufferedDataContainer container = createDataContainer(exec);
        LOGGER.info("Reading file: " + fileSettings.getStringValue());
        List<Class> result = unmarshalResults(fileSettings.getStringValue());
        BufferedDataTable out = transform(container, result, exec);
        LOGGER.info("Reading judy logs finished.");

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
                LOGGER.debug("Transforming judy log, class: " + clazz.getName() + ", score: " + clazz.getScore());
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
