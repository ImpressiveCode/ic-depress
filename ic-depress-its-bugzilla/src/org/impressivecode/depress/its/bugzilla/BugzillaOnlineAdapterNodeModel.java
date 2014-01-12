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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.Integer.parseInt;
import static org.impressivecode.depress.its.ITSAdapterTableFactory.createDataColumnSpec;
import static org.impressivecode.depress.its.bugzilla.BugzillaAdapterTableFactory.createTableSpec;
import static org.knime.core.util.KnimeEncryption.decrypt;
import static org.knime.core.util.KnimeEncryption.encrypt;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.util.Date;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

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
import org.knime.core.node.defaultnodesettings.SettingsModelDate;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelOptionalString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * @author Michał Negacz, Wrocław University of Technology
 * @author Piotr Wróblewski, Wrocław University of Technology
 * @author Bartosz Skuza, Wrocław University of Technology
 * 
 */
public class BugzillaOnlineAdapterNodeModel extends NodeModel {

	public static final int NUMBER_OF_INPUT_PORTS = 0;

	public static final int NUMBER_OF_OUTPUT_PORTS = 1;

	public static final String DEFAULT_STRING_VALUE = "";

	public static final int DEFAULT_BUGS_PER_TASK_VALUE = 1000;
	
	public static final String DEFAULT_COMBOBOX_ANY_VALUE = "Any";

	public static final String BUGZILLA_URL = "depress.its.bugzillaonline.url";

	public static final String BUGZILLA_USERNAME = "depress.its.bugzillaonline.username";

	public static final String BUGZILLA_PASSWORD = "depress.its.bugzillaonline.password";

	public static final String BUGZILLA_PRODUCT = "depress.its.bugzillaonline.product";

	public static final String BUGZILLA_DATE = "depress.its.bugzillaonline.date";

	public static final String BUGZILLA_HISTORY = "depress.its.bugzillaonline.history";

	public static final String BUGZILLA_COMMENT = "depress.its.bugzillaonline.comment";

	public static final String BUGZILLA_LIMIT = "depress.its.bugzillaonline.limit";

	public static final String BUGZILLA_OFFSET = "depress.its.bugzillaonline.offset";
	
	public static final String BUGZILLA_ASSIGNED_TO = "depress.its.bugzillaonline.assignedTo";

	public static final String BUGZILLA_REPORTER = "depress.its.bugzillaonline.creator";
	
	public static final String BUGZILLA_VERSION = "depress.its.bugzillaonline.version";
	
	public static final String BUGZILLA_PRIORITY = "depress.its.bugzillaonline.priority";

	public static final String BUGZILLA_THREADS_COUNT = "depress.its.bugzillaonline.threadsCount";

	public static final String BUGZILLA_BUGS_PER_TASK = "depress.its.bugzillaonline.bugsPerTask";

	private static final NodeLogger LOGGER = NodeLogger.getLogger(BugzillaOnlineAdapterNodeModel.class);

	private final SettingsModelString urlSettings = createURLSettings();

	private final SettingsModelDate dateFromSettings = createDateSettings();

	private final SettingsModelString usernameSettings = createUsernameSettings();

	private final SettingsModelString passwordSettings = createPasswordSettings();

	private final SettingsModelString productSettings = createProductSettings();

	private final SettingsModelOptionalString limitSettings = createLimitSettings();
	
	private final SettingsModelOptionalString offsetSettings = createOffsetSettings();

	private final SettingsModelOptionalString assignedToSettings = createAssignedToSettings();

	private final SettingsModelOptionalString reporterSettings = createReporterSettings();
	
	private final SettingsModelOptionalString versionSettings = createVersionSettings();
	
	private final SettingsModelString prioritySettings = createPrioritySettings();

	private final SettingsModelInteger threadsCountSettings = createThreadsCountSettings();

	private final SettingsModelInteger bugsPerTaskSettings = createBugsPerTaskSettings();

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private static final String URL_PATTERN = "^https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

	protected BugzillaOnlineAdapterNodeModel() {
		super(NUMBER_OF_INPUT_PORTS, NUMBER_OF_OUTPUT_PORTS);
	}

	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext context) throws Exception {
		LOGGER.info("Preparing to read bugzilla entries.");
		BugzillaOnlineClientAdapter clientAdapter = new BugzillaOnlineClientAdapter(getURL(), context);

		if (isUsernameProvided(getUsername())) {
			LOGGER.info("Logging to bugzilla as: " + getUsername());
			clientAdapter.login(getUsername(), getPassword());
		}

		LOGGER.info("Reading entries from bugzilla instance: " + getURL() + " and product: " + getProductName());
		List<ITSDataType> entries = clientAdapter.listEntries(getBugzillaOptions());

		LOGGER.info("Transforming to bugzilla entries.");
		BufferedDataTable out = transform(entries, context);

		LOGGER.info("Bugzilla table created.");
		return new BufferedDataTable[] { out };
	}

	private String getURL() {
		return urlSettings.getStringValue();
	}

	private boolean isUsernameProvided(String username) {
		return !isNullOrEmpty(username);
	}
	
	private String getPassword() throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, IOException {
		return decrypt(passwordSettings.getStringValue());
	}

	private String getUsername() {
		return usernameSettings.getStringValue();
	}
	
	private BugzillaOnlineOptions getBugzillaOptions() {
		BugzillaOnlineOptions options = new BugzillaOnlineOptions();
		options.setProductName(getProductName());
		options.setDateFrom(getDateFrom());
		options.setAssignedTo(getAssignedTo());
		options.setReporter(getReporter());
		options.setVersion(getVersion());
		options.setPriority(getPriority());
		options.setLimit(getLimit());
		options.setOffset(getOffset());
		options.setThreadsCount(getThreadsCount());
		options.setBugsPerTask(getBugsPerTask());
		return options;
	}

	private String getProductName() {
		return productSettings.getStringValue();
	}

	private Date getDateFrom() {
		return dateFromSettings.getDate();
	}
	
	private String getAssignedTo() {
		return !isNullOrEmpty(assignedToSettings.getStringValue()) ? assignedToSettings.getStringValue() : null;
	}

	private String getReporter() {
		return !isNullOrEmpty(reporterSettings.getStringValue()) ? reporterSettings.getStringValue() : null;
	}
	
	private String getVersion(){
		return !isNullOrEmpty(versionSettings.getStringValue()) ? versionSettings.getStringValue() : null;
	}
	
	private String getPriority() {
		return isComboboxChoosen(prioritySettings.getStringValue()) ? prioritySettings.getStringValue() : null;
	}

	private boolean isComboboxChoosen(String value) {
		return !DEFAULT_COMBOBOX_ANY_VALUE.equals(value);
	}
	
	private Integer getLimit() {
		return getOptionalIntegerValue(limitSettings);
	}
	
	private Integer getOffset() {
		return getOptionalIntegerValue(offsetSettings);
	}

	private Integer getOptionalIntegerValue(SettingsModelOptionalString settings) {
		Integer result = null;
		try {
			result = parseInt(settings.getStringValue());
		} catch (NumberFormatException e) {
		}
		return result;
	}
	
	private Integer getThreadsCount() {
		return threadsCountSettings.getIntValue();
	}

	private Integer getBugsPerTask() {
		return bugsPerTaskSettings.getIntValue();
	}

	private BufferedDataTable transform(final List<ITSDataType> entries, final ExecutionContext exec) throws CanceledExecutionException {
		ITSAdapterTransformer transformer = new ITSAdapterTransformer(createDataColumnSpec());
		return transformer.transform(entries, exec);
	}

	@Override
	protected void reset() {
		// NOOP
	}

	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		checkArgument(inSpecs.length == 0);
		return createTableSpec();
	}

	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		urlSettings.saveSettingsTo(settings);
		usernameSettings.saveSettingsTo(settings);

		try {
			String password = encrypt(passwordSettings.getStringValue().toCharArray());
			passwordSettings.setStringValue(password);
			passwordSettings.saveSettingsTo(settings);
		} catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
			LOGGER.error("Could not encrypt password, reason: " + e.getMessage(), e);
		}
		
		productSettings.saveSettingsTo(settings);
		dateFromSettings.saveSettingsTo(settings);
		limitSettings.saveSettingsTo(settings);
		offsetSettings.saveSettingsTo(settings);
		assignedToSettings.saveSettingsTo(settings);
		reporterSettings.saveSettingsTo(settings);
		versionSettings.saveSettingsTo(settings);
		prioritySettings.saveSettingsTo(settings);
		threadsCountSettings.saveSettingsTo(settings);
		bugsPerTaskSettings.saveSettingsTo(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		urlSettings.loadSettingsFrom(settings);
		usernameSettings.loadSettingsFrom(settings);
		passwordSettings.loadSettingsFrom(settings);
		productSettings.loadSettingsFrom(settings);
		dateFromSettings.loadSettingsFrom(settings);
		limitSettings.loadSettingsFrom(settings);
		offsetSettings.loadSettingsFrom(settings);
		assignedToSettings.loadSettingsFrom(settings);
		reporterSettings.loadSettingsFrom(settings);
		versionSettings.loadSettingsFrom(settings);
		prioritySettings.loadSettingsFrom(settings);
		threadsCountSettings.loadSettingsFrom(settings);
		bugsPerTaskSettings.loadSettingsFrom(settings);
	}

	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		urlSettings.validateSettings(settings); 
		usernameSettings.validateSettings(settings);
		passwordSettings.validateSettings(settings);
		productSettings.validateSettings(settings);
		dateFromSettings.validateSettings(settings);
		limitSettings.validateSettings(settings);
		offsetSettings.validateSettings(settings);
		assignedToSettings.validateSettings(settings);
		reporterSettings.validateSettings(settings);
		prioritySettings.validateSettings(settings);
		versionSettings.validateSettings(settings);
		threadsCountSettings.validateSettings(settings);
		bugsPerTaskSettings.validateSettings(settings);

		SettingsModelString url = urlSettings.createCloneWithValidatedValue(settings);
		if (!isNullOrEmpty(url.getStringValue()) && !url.getStringValue().matches(URL_PATTERN)) {
			throw new InvalidSettingsException("Invalid URL address. Valid example: 'https://website.com'");
		}

		SettingsModelString username = usernameSettings.createCloneWithValidatedValue(settings);
		if (!isNullOrEmpty(username.getStringValue()) && !username.getStringValue().matches(EMAIL_PATTERN)) {
			throw new InvalidSettingsException("Invalid username");
		}
	}

	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException {
		// NOOP
	}

	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException {
		// NOOP
	}

	static SettingsModelString createURLSettings() {
		return new SettingsModelString(BUGZILLA_URL, DEFAULT_STRING_VALUE);
	}

	static SettingsModelString createUsernameSettings() {
		return new SettingsModelString(BUGZILLA_USERNAME, DEFAULT_STRING_VALUE);
	}

	static SettingsModelString createPasswordSettings() {
		return new SettingsModelString(BUGZILLA_PASSWORD, DEFAULT_STRING_VALUE);
	}

	static SettingsModelString createProductSettings() {
		return new SettingsModelString(BUGZILLA_PRODUCT, DEFAULT_STRING_VALUE);
	}

	static SettingsModelDate createDateSettings() {
		return new SettingsModelDate(BUGZILLA_DATE);
	}

	static SettingsModelOptionalString createLimitSettings() {
		return new SettingsModelOptionalString(BUGZILLA_LIMIT, DEFAULT_STRING_VALUE, false);
	}
	
	static SettingsModelOptionalString createOffsetSettings() {
		return new SettingsModelOptionalString(BUGZILLA_OFFSET, DEFAULT_STRING_VALUE, false);
	}

	static SettingsModelOptionalString createAssignedToSettings() {
		return new SettingsModelOptionalString(BUGZILLA_ASSIGNED_TO, DEFAULT_STRING_VALUE, false);
	}

	static SettingsModelOptionalString createReporterSettings() {
		return new SettingsModelOptionalString(BUGZILLA_REPORTER, DEFAULT_STRING_VALUE, false);
	}
	
	static SettingsModelOptionalString createVersionSettings() {
		return new SettingsModelOptionalString(BUGZILLA_VERSION, DEFAULT_STRING_VALUE, false);
	}
	
	static SettingsModelString createPrioritySettings() {
		return new SettingsModelString(BUGZILLA_PRIORITY, DEFAULT_COMBOBOX_ANY_VALUE);
	}

	static SettingsModelInteger createThreadsCountSettings() {
		return new SettingsModelInteger(BUGZILLA_THREADS_COUNT, getOptimalThreadsCount());
	}

	static SettingsModelInteger createBugsPerTaskSettings() {
		return new SettingsModelInteger(BUGZILLA_BUGS_PER_TASK, DEFAULT_BUGS_PER_TASK_VALUE);
	}

	static private int getOptimalThreadsCount() {
		return Runtime.getRuntime().availableProcessors() + 1;
	}

}
