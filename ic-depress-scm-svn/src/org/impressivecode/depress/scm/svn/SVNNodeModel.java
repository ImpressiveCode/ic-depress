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

package org.impressivecode.depress.scm.svn;

import java.io.File;
import java.io.IOException;

import org.impressivecode.depress.scm.svn.SVNLogLoader.IReadProgressListener;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

public class SVNNodeModel extends NodeModel {

	class ModelRowReader implements IReadProgressListener {

		private int rowId = 0;
		private BufferedDataContainer container;
		private ExecutionContext exec;

		public ModelRowReader(final BufferedDataContainer inContainer,
				ExecutionContext inExec) {
			container = inContainer;
			exec = inExec;
		}

		public void close() {
			container.close();
		}

		@Override
		public void onReadProgress(double inProgres, SVNLogRow inRow) {
			if (inRow != null) {
				container
						.addRowToTable(SVNLogRowSpec.createRow(++rowId, inRow));
			}

			exec.setProgress(inProgres);// ,
										// SVNLocale.iCurrentProgress(inProgres));
		}

		public BufferedDataTable[] toDataTable() {
			return new BufferedDataTable[] { container.getTable() };
		}

		@Override
		public void checkLoading() throws CanceledExecutionException {
			exec.checkCanceled();
		}
	}

	private BufferedDataContainer dataContainer;
	private ModelRowReader reader;
	private SVNLogLoader loader;

	/**
	 * Constructor for the node model.
	 */
	protected SVNNodeModel() {
		super(0, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
			throws InvalidSettingsException {
		return new DataTableSpec[] { new DataTableSpec(
				SVNLogRowSpec.createColumnSpec()) };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {

		Logger.instance().warn(SVNLocale.iStartLoading());

		dataContainer = exec.createDataContainer(new DataTableSpec(
				SVNLogRowSpec.createColumnSpec()));

		reader = new ModelRowReader(dataContainer, exec);

		loader = new SVNLogRepoLoader();

		loader.load(SVNSettings.SVN_PATH.getStringValue(),
				SVNSettings.SVN_ISSUE_MARKER.getStringValue(),
				SVNSettings.SVN_PACKAGE.getStringValue(), reader);

		Logger.instance().warn(SVNLocale.iEndLoading());

		reader.close();

		return reader.toDataTable();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
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
	protected void reset() {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {

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
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		SVNSettings.validateSettings(settings);
	}

}
