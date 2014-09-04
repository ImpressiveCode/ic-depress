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
package org.impressivecode.depress.mr.eclipsemetrics;

import static com.google.common.base.Preconditions.checkArgument;
import static org.impressivecode.depress.mr.eclipsemetrics.EclipseMetricsTableFactory.createDataColumnSpec;
import static org.impressivecode.depress.mr.eclipsemetrics.EclipseMetricsTableFactory.createDataColumnSpecMethodLevel;
import static org.impressivecode.depress.mr.eclipsemetrics.EclipseMetricsTableFactory.createTableSpec;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.knime.core.data.DataTableSpec;
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
import org.xml.sax.SAXException;

/**
 * 
 * @author Mateusz Kutyba, Wroclaw University of Technology
 * 
 */
public class EclipseMetricsNodeModel extends NodeModel {

    private static final NodeLogger LOGGER = NodeLogger.getLogger(EclipseMetricsNodeModel.class);
    private static final String DEFAULT_VALUE = "";
    private static final String CONFIG_NAME = "file chooser";
    private final SettingsModelString fileSettings = createFileChooserSettings();

    protected EclipseMetricsNodeModel() {
        super(0, 2);
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {

        LOGGER.info("Preparing to read EclipseMetrics entries.");
        String eclipsemetricsFilePath = fileSettings.getStringValue();

        List<EclipseMetricsEntryClassLevel> entriesClassLevel = parseEntriesClassLevel(eclipsemetricsFilePath);
        LOGGER.info("Transforming to EclipseMetrics class-level entries.");
        BufferedDataTable output1 = transformClassLevel(entriesClassLevel, exec);
        LOGGER.info("EclipseMetrics class-level table created.");

        List<EclipseMetricsEntryMethodLevel> entriesMethodLevel = parseEntriesMethodLevel(eclipsemetricsFilePath);
        LOGGER.info("Transforming to EclipseMetrics method-level entries.");
        BufferedDataTable output2 = transformMethodLevel(entriesMethodLevel, exec);
        LOGGER.info("EclipseMetrics method-level table created.");

        return new BufferedDataTable[] { output1, output2 };
    }

    private BufferedDataTable transformClassLevel(final List<EclipseMetricsEntryClassLevel> entries, final ExecutionContext exec)
            throws CanceledExecutionException {
        EclipseMetricsTransformer transformer = new EclipseMetricsTransformer(createDataColumnSpec());
        return transformer.transformClassLevel(entries, exec);
    }

    private BufferedDataTable transformMethodLevel(final List<EclipseMetricsEntryMethodLevel> entries,
            final ExecutionContext exec) throws CanceledExecutionException {
        EclipseMetricsTransformer transformer = new EclipseMetricsTransformer(createDataColumnSpecMethodLevel());
        return transformer.transformMethodLevel(entries, exec);
    }

    private List<EclipseMetricsEntryClassLevel> parseEntriesClassLevel(final String eclipsemetricsFilePath)
            throws ParserConfigurationException, SAXException, IOException {
        EclipseMetricsEntriesParser parser = new EclipseMetricsEntriesParser();
        return parser.parseEntriesClassLevel(eclipsemetricsFilePath);
    }

    private List<EclipseMetricsEntryMethodLevel> parseEntriesMethodLevel(final String eclipsemetricsFilePath)
            throws ParserConfigurationException, SAXException, IOException {
        EclipseMetricsEntriesParser parser = new EclipseMetricsEntriesParser();
        return parser.parseEntriesMethodLevel(eclipsemetricsFilePath);
    }

    @Override
    protected void reset() {
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        checkArgument(inSpecs.length == 0, "Invalid state");
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

    static SettingsModelString createFileChooserSettings() {
        return new SettingsModelString(CONFIG_NAME, DEFAULT_VALUE);
    }
}
