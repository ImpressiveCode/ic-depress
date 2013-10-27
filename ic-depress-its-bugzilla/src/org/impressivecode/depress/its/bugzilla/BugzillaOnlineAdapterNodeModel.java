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
import java.util.Date;
import java.util.List;

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
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDate;
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

	public static final int NUMBER_OF_INPUT_PORTS = 0;

	public static final int NUMBER_OF_OUTPUT_PORTS = 1;

	public static final String DEFAULT_VALUE = "";

	public static final String BUGZILLA_URL = "depress.its.bugzillaonline.url";

	public static final String BUGZILLA_USERNAME = "depress.its.bugzillaonline.username";

	public static final String BUGZILLA_PASSWORD = "depress.its.bugzillaonline.password";

	public static final String BUGZILLA_PRODUCT = "depress.its.bugzillaonline.product";

	public static final String BUGZILLA_DATE = "depress.its.bugzillaonline.date";
	
	public static final String BUGZILLA_HISTORY = "depress.its.bugzillaonline.history";
	
	public static final String BUGZILLA_COMMENT = "depress.its.bugzillaonline.comment";
	
	public static final String BUGZILLA_ATTACHMENT = "depress.its.bugzillaonline.attachment";

	private static final NodeLogger LOGGER = NodeLogger.getLogger(BugzillaOnlineAdapterNodeModel.class);

	private final SettingsModelString urlSettings = createURLSettings();

	private final SettingsModelDate dateFromSettings = createDateSettings();
	
	private final SettingsModelBoolean historyFromSettings= createHistorySettings();
	
	private final SettingsModelBoolean commentFromSettings= createCommentSettings();
	
	private final SettingsModelBoolean attachmentFromSettings= createAttachmentSetting();

	private final SettingsModelString usernameSettings = createUsernameSettings();

	private final SettingsModelString passwordSettings = createPasswordSettings();

	private final SettingsModelString productSettings = createProductSettings();
	
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private static final String URL_PATTERN = 
			"^(https?|ftp|file)://" +
			"[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

	protected BugzillaOnlineAdapterNodeModel() {
		super(NUMBER_OF_INPUT_PORTS, NUMBER_OF_OUTPUT_PORTS);
	}

	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec) throws Exception {
		LOGGER.info("Preparing to read bugzilla entries.");
		BugzillaOnlineClientAdapter clientAdapter = new BugzillaOnlineClientAdapter(getURL());

		if (isUsernameProvided(getUsername())) {
			LOGGER.info("Logging to bugzilla as: " + getUsername());
			clientAdapter.login(getUsername(), getPassword());
		}

		LOGGER.info("Reading entries from bugzilla instance: " + getURL() + " and product: " + getProductName());
		List<ITSDataType> entries = clientAdapter.listEntries(getBugFilter());

		LOGGER.info("Transforming to bugzilla entries.");
		BufferedDataTable out = transform(entries, exec);

		LOGGER.info("Bugzilla table created.");
		return new BufferedDataTable[] { out };
	}

	private boolean isUsernameProvided(String username) {
		return !Strings.isNullOrEmpty(username);
	}

	private String getPassword() {
		return passwordSettings.getStringValue();
	}

	private String getUsername() {
		return usernameSettings.getStringValue();
	}

	private String getProductName() {
		return productSettings.getStringValue();
	}

	private Date getDateFrom() {
		return dateFromSettings.getDate();
	}
	
	private boolean isHistoryEnable(){
		return historyFromSettings.getBooleanValue();
	}
	
	private boolean isCommentEnable(){
		return commentFromSettings.getBooleanValue();
	}
	
	private boolean isAttachmentEnable(){
		return attachmentFromSettings.getBooleanValue();
	}

	private String getURL() {
		return urlSettings.getStringValue();
	}

	private BugzillaOnlineFilter getBugFilter() {
		BugzillaOnlineFilter bugFilter = new BugzillaOnlineFilter();
		bugFilter.setProductName(getProductName());
		bugFilter.setDateFrom(getDateFrom());
		bugFilter.setHistoryOfChanges(isHistoryEnable());
		bugFilter.setComments(isCommentEnable());
		bugFilter.setAttachments(isAttachmentEnable());
		return bugFilter;
	}

	private BufferedDataTable transform(final List<ITSDataType> entries, final ExecutionContext exec) throws CanceledExecutionException {
		ITSAdapterTransformer transformer = new ITSAdapterTransformer(ITSAdapterTableFactory.createDataColumnSpec());
		return transformer.transform(entries, exec);
	}

	@Override
	protected void reset() {
		// NOOP
	}

	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
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
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		urlSettings.loadSettingsFrom(settings);
		usernameSettings.loadSettingsFrom(settings);
		passwordSettings.loadSettingsFrom(settings);
		productSettings.loadSettingsFrom(settings);
		dateFromSettings.loadSettingsFrom(settings);
	}

	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		urlSettings.validateSettings(settings); // TODO validate url, maybe test connection, bugzilla version and credentials
		usernameSettings.validateSettings(settings);
		passwordSettings.validateSettings(settings);
		productSettings.validateSettings(settings);
		dateFromSettings.validateSettings(settings);
		
		SettingsModelString url_test = urlSettings.createCloneWithValidatedValue(settings);
		if(url_test.getStringValue().length()>0 && !url_test.getStringValue().matches(URL_PATTERN)){
			throw new InvalidSettingsException("Invalid URL address");
		}
		
		SettingsModelString email_test = usernameSettings.createCloneWithValidatedValue(settings);
		if(email_test.getStringValue().length()>0 && !email_test.getStringValue().matches(EMAIL_PATTERN)){
			throw new InvalidSettingsException("Invalid email address");
		}
		
		SettingsModelString password_test = passwordSettings.createCloneWithValidatedValue(settings);
		if(!password_test.getStringValue().isEmpty() && password_test.getStringValue().length()<6){
			throw new InvalidSettingsException("The password must be at least 6 characters long.");
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

	static SettingsModelDate createDateSettings() {
		return new SettingsModelDate(BUGZILLA_DATE);
	}
	
	static SettingsModelBoolean createHistorySettings() {
		return new SettingsModelBoolean(BUGZILLA_HISTORY, false);
	}
	
	static SettingsModelBoolean createCommentSettings() {
		return new SettingsModelBoolean(BUGZILLA_COMMENT, false);
	}
	
	static SettingsModelBoolean createAttachmentSetting() {
		return new SettingsModelBoolean(BUGZILLA_ATTACHMENT, false);
	}

}
