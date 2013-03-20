package org.impressivecode.depress.scm.svn;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnDomainCreator;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

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
public class SVNNodeModel extends NodeModel {

	/**
	 * Constructor for the node model.
	 */
	protected SVNNodeModel() {

		// TODO one incoming port and one outgoing port is assumed
		super(1, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {

		DataColumnSpec[] allColSpecs = new DataColumnSpec[2];

		allColSpecs[0] = createOutputColumnClassSpec();
		allColSpecs[1] = createOutputColumnAuthorSpec();

		DataTableSpec outputSpec = new DataTableSpec(allColSpecs);

		BufferedDataContainer container = exec.createDataContainer(outputSpec);

		for (int i = 0; i < 10; i++) {

			RowKey key = new RowKey("Row " + i);

			DataCell[] cells = new DataCell[3];

			cells[0] = new StringCell("Class " + i);
			cells[1] = new StringCell("Author " + i);

			DataRow row = new DefaultRow(key, cells);
			container.addRowToTable(row);

			exec.checkCanceled();
			exec.setProgress(i / (double) 10, "Adding row " + i);
		}

		container.close();

		BufferedDataTable out = container.getTable();

		return new BufferedDataTable[] { out };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
			throws InvalidSettingsException {

		return new DataTableSpec[] {
				new DataTableSpec(createOutputColumnClassSpec()),
				new DataTableSpec(createOutputColumnAuthorSpec()) };
	}

	private DataColumnSpec createOutputColumnClassSpec() {

		DataColumnSpecCreator colSpecCreator = new DataColumnSpecCreator(
				"Class", StringCell.TYPE);

		DataColumnDomainCreator domainCreator = new DataColumnDomainCreator(
				new StringCell(""), new StringCell(""));

		colSpecCreator.setDomain(domainCreator.createDomain());

		DataColumnSpec newColumnSpec = colSpecCreator.createSpec();

		return newColumnSpec;
	}

	private DataColumnSpec createOutputColumnAuthorSpec() {

		DataColumnSpecCreator colSpecCreator = new DataColumnSpecCreator(
				"Author", StringCell.TYPE);

		DataColumnDomainCreator domainCreator = new DataColumnDomainCreator(
				new StringCell(""), new StringCell(""));

		colSpecCreator.setDomain(domainCreator.createDomain());

		DataColumnSpec newColumnSpec = colSpecCreator.createSpec();

		return newColumnSpec;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		SVNSettings.saveSettingsTo(settings);
		Logger.instance().warn(SVNLocale.iSettingsSaved());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		SVNSettings.loadSettingsFrom(settings);
		Logger.instance().warn(SVNLocale.iSettingsLoaded());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		SVNSettings.validateSettings(settings);
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