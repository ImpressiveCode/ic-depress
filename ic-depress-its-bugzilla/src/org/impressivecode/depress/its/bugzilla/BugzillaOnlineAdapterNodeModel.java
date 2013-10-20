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
package org.impressivecode.depress.its.bugzilla;

import static org.impressivecode.depress.its.bugzilla.BugzillaAdapterTableFactory.createTableSpec;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.impressivecode.depress.its.ITSAdapterTableFactory;
import org.impressivecode.depress.its.ITSAdapterTransformer;
import org.impressivecode.depress.its.ITSDataType;
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

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * @author Michał Negacz, Wrocław University of Technology
 * @author Piotr Wróblewski
 * 
 */
public class BugzillaOnlineAdapterNodeModel extends NodeModel {

	private static final int NUMBER_OF_INPUT_PORTS = 0;

	private static final int NUMBER_OF_OUTPUT_PORTS = 1;

	private static final NodeLogger LOGGER = NodeLogger
			.getLogger(BugzillaOnlineAdapterNodeModel.class);

	private static final String DEFAULT_VALUE = "";

	private static final String DATE_DEFAULT_VALUE = "dd-mm-rrrr";

	private static final String BUGZILLA_URL = "depress.its.bugzillaonline.url";

	private static final String BUGZILLA_USERNAME = "depress.its.bugzillaonline.username";

	private static final String BUGZILLA_PASSWORD = "depress.its.bugzillaonline.password";

	private static final String BUGZILLA_PRODUCT = "depress.its.bugzillaonline.product";

	private static final String BUGZILLA_DATE = "depress.its.bugzillaonline.date";

	private final SettingsModelString urlSettings = createURLSettings();

	private final SettingsModelString dateFromSettings = createDateSettings();

	private final SettingsModelString usernameSettings = createUsernameSettings();

	private final SettingsModelString passwordSettings = createPasswordSettings();

	private final SettingsModelString productSettings = createProductSettings();

	protected BugzillaOnlineAdapterNodeModel() {
		super(NUMBER_OF_INPUT_PORTS, NUMBER_OF_OUTPUT_PORTS);
	}

	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		LOGGER.info("Preparing to read bugzilla entries.");
		String urlAddress = urlSettings.getStringValue();
		String dateFrom = dateFromSettings.getStringValue();
		String productName = productSettings.getStringValue();
		String username = usernameSettings.getStringValue();
		String password = passwordSettings.getStringValue();

		BugzillaOnlineClientAdapter clientAdapter = new BugzillaOnlineClientAdapter(
				urlAddress);
		if (isUsernameProvided(username)) {
			LOGGER.info("Logging to bugzilla as: " + username);
			clientAdapter.login(username, password);
		}

		LOGGER.info("Reading entries from bugzilla instance: " + urlAddress
				+ " and product: " + productName);
		Date date = null;
		if (dateFrom.matches("\\d{2}-\\d{2}-\\d{4}")) {
			date = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
					.parse(dateFrom);
		}
		List<ITSDataType> entries = clientAdapter
				.listEntries(productName, date);

		LOGGER.info("Transforming to bugzilla entries.");
		BufferedDataTable out = transform(entries, exec);

		LOGGER.info("Bugzilla table created.");
		return new BufferedDataTable[] { out };
	}

	private boolean isUsernameProvided(String username) {
		return !Strings.isNullOrEmpty(username);
	}

	private BufferedDataTable transform(final List<ITSDataType> entries,
			final ExecutionContext exec) throws CanceledExecutionException {
		ITSAdapterTransformer transformer = new ITSAdapterTransformer(
				ITSAdapterTableFactory.createDataColumnSpec());
		return transformer.transform(entries, exec);
	}

	@Override
	protected void reset() {
		// NOOP
	}

	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
			throws InvalidSettingsException {
		Preconditions.checkArgument(inSpecs.length == 0);
		return createTableSpec();
	}

	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		urlSettings.saveSettingsTo(settings);
		usernameSettings.saveSettingsTo(settings);
		passwordSettings.saveSettingsTo(settings);
		productSettings.saveSettingsTo(settings);
		dateFromSettings.saveSettingsTo(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		urlSettings.loadSettingsFrom(settings);
		usernameSettings.loadSettingsFrom(settings);
		passwordSettings.loadSettingsFrom(settings);
		productSettings.loadSettingsFrom(settings);
		dateFromSettings.loadSettingsFrom(settings);
	}

	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		urlSettings.validateSettings(settings); // TODO validate url, maybe test
												// connection, bugzilla version
												// and credentials
		usernameSettings.validateSettings(settings);
		passwordSettings.validateSettings(settings);
		productSettings.validateSettings(settings);
		dateFromSettings.validateSettings(settings);
	}

	@Override
	protected void loadInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
		// NOOP
	}

	@Override
	protected void saveInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
		// NOOP
	}

	static SettingsModelString createURLSettings() {
		return new SettingsModelString(BUGZILLA_URL, DEFAULT_VALUE);
	}

	static SettingsModelString createUsernameSettings() {
		return new SettingsModelString(BUGZILLA_USERNAME, DEFAULT_VALUE);
	}

	static SettingsModelString createPasswordSettings() {
		return new SettingsModelString(BUGZILLA_PASSWORD, DEFAULT_VALUE);
	}

	static SettingsModelString createProductSettings() {
		return new SettingsModelString(BUGZILLA_PRODUCT, DEFAULT_VALUE);
	}

	static SettingsModelString createDateSettings() {
		return new SettingsModelString(BUGZILLA_DATE, DATE_DEFAULT_VALUE);
	}

}
