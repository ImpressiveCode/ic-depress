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
package org.impressivecode.depress.mr.googleaudit;

import static com.google.common.base.Preconditions.checkArgument;
import static org.impressivecode.depress.mr.googleaudit.GoogleAuditTableFactory.createDataColumnSpec;
import static org.impressivecode.depress.mr.googleaudit.GoogleAuditTableFactory.createTableSpec;

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
 * @author Jadwiga Wozna, Wroclaw University of Technology
 * @author Katarzyna Debowa, Wroclaw University of Technology
 * @author Pawel Krzos, Wroclaw University of Technology
 */
public class GoogleAuditNodeModel extends NodeModel {

	private static final NodeLogger LOGGER = NodeLogger.getLogger(GoogleAuditNodeModel.class);
	private static final String DEFAULT_VALUE = "";
	private static final String CONFIG_NAME = "file chooser";
	private final SettingsModelString fileSettings = createFileChooserSettings();

	protected GoogleAuditNodeModel() {
		super(0, 1);
	}

	@Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {
        LOGGER.info("Preparing to read GoogleAudit entries.");
        String googleAuditFilePath = fileSettings.getStringValue();
        List<GoogleAuditEntry> entries = parseEntries(googleAuditFilePath);
        LOGGER.info("Transforming to GoogleAudit method-level entries.");
        BufferedDataTable output = transform(entries, exec);
        LOGGER.info("GoogleAudit method-level table created.");

        return new BufferedDataTable[] { output };
	}

    private BufferedDataTable transform(final List<GoogleAuditEntry> entries, final ExecutionContext exec)
            throws CanceledExecutionException {
        GoogleAuditTransformer transformer = new GoogleAuditTransformer(createDataColumnSpec());
        return transformer.transformMethodLevel(entries, exec);
    }

    private List<GoogleAuditEntry> parseEntries(final String googleAuditFilePath)
            throws ParserConfigurationException, SAXException, IOException {
        GoogleAuditEntriesParser parser = new GoogleAuditEntriesParser();
        return parser.parseEntries(googleAuditFilePath);
    }

    @Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// TODO Auto-generated method stub

	}

	@Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        fileSettings.saveSettingsTo(settings);
    }

	@Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        fileSettings.validateSettings(settings);
    }

	@Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        fileSettings.loadSettingsFrom(settings);
    }

	@Override
	protected void reset() {
		// TODO Auto-generated method stub

	}

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        checkArgument(inSpecs.length == 0, "Invalid state");
        return createTableSpec();
    }

	static SettingsModelString createFileChooserSettings() {
		return new SettingsModelString(CONFIG_NAME, DEFAULT_VALUE);
	}

}
