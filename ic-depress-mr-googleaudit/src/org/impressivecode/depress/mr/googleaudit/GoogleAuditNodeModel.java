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

import static org.impressivecode.depress.mr.googleaudit.GoogleAuditEntriesParser.unmarshalResults;
import static org.impressivecode.depress.mr.googleaudit.GoogleAuditTableFactory.createDataColumnSpec;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.impressivecode.depress.mr.googleaudit.GoogleAuditXmlResult.Resource;
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
 * @author Jadwiga Wozna, Wroclaw University of Technology
 */
public class GoogleAuditNodeModel extends NodeModel {

	private static final NodeLogger LOGGER = NodeLogger
			.getLogger(GoogleAuditNodeModel.class);
	private static final String DEFAULT_VALUE = "";
	private static final String CONFIG_NAME = "file chooser";
	private final SettingsModelString fileSettings = createFileChooserSettings();

	protected GoogleAuditNodeModel() {
		super(0, 1);
	}

	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {

		LOGGER.info("Preparing to read GoogleAudit logs.");
		BufferedDataContainer container = createDataContainer(exec);
		LOGGER.info("Reading file: " + fileSettings.getStringValue());
		List<Resource> result = unmarshalResults(fileSettings.getStringValue());
		BufferedDataTable out = transform(container, result, exec);
		LOGGER.info("Reading googlemetrics logs finished.");

		return new BufferedDataTable[] { out };
	}

	private void progress(final ExecutionContext exec, final int size,
			final int i) throws CanceledExecutionException {
		exec.checkCanceled();
		exec.setProgress(i / size);
	}

	private BufferedDataTable transform(final BufferedDataContainer container,
			final List<Resource> classes, final ExecutionContext exec)
			throws CanceledExecutionException {

		// TODO: add method body analog to method 'transform' from
		// GoogleMetricsNodeModel and GoogleAuditXmlResult

		return null;
	}

	private BufferedDataContainer createDataContainer(
			final ExecutionContext exec) {
		DataTableSpec outputSpec = createDataColumnSpec();
		BufferedDataContainer container = exec.createDataContainer(outputSpec);
		return container;
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
	protected void saveSettingsTo(NodeSettingsWO settings) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void validateSettings(NodeSettingsRO settings)
			throws InvalidSettingsException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
			throws InvalidSettingsException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void reset() {
		// TODO Auto-generated method stub

	}

	static SettingsModelString createFileChooserSettings() {
		return new SettingsModelString(CONFIG_NAME, DEFAULT_VALUE);
	}

}
