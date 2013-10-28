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
package org.impressivecode.depress.its.jira;

import static org.impressivecode.depress.its.jira.JiraOnlineAdapterTableFactory.createTableSpec;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

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
import org.knime.core.node.defaultnodesettings.SettingsModelDate;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.xml.sax.SAXException;

import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.Client;

/**
 * 
 * @author Marcin Kunert, Krzysztof Kwoka, Dawid Rutowicz
 * 
 */
public class JiraOnlineAdapterNodeModel extends NodeModel {

	private static final String DEFAULT_VALUE = "";

	private static final String JIRA_URL = "depress.its.jiraonline.url";
	private static final String JIRA_LOGIN = "depress.its.jiraonline.login";
	private static final String JIRA_PASS = "depress.its.jiraonline.password";
	private static final String JIRA_START_DATE = "depress.its.jiraonline.startDate";
	private static final String JIRA_END_DATE = "depress.its.jiraonline.endDate";
	private static final String JIRA_JQL = "depress.its.jiraonline.jql";
	
	private final SettingsModelString jiraSettingsURL = createSettingsURL();
	private final SettingsModelString jiraSettingsLogin = createSettingsLogin();
	private final SettingsModelString jiraSettingsPass = createSettingsPass();
	private final SettingsModelDate jiraSettingsDateStart = createSettingsDateStart();
	private final SettingsModelDate jiraSettingsDateEnd = createSettingsDateEnd();
	private final SettingsModelString jiraSettingsJQL = createSettingsJQL();

	private static final NodeLogger LOGGER = NodeLogger
			.getLogger(JiraOnlineAdapterNodeModel.class);

	protected JiraOnlineAdapterNodeModel() {
		super(0, 1);
	}

	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		LOGGER.info("Preparing to download jira entries.");
		String hostname = jiraSettingsURL.getStringValue();
		String jql = jiraSettingsJQL.getStringValue();
		String login = jiraSettingsLogin.getStringValue();
		String pass = jiraSettingsPass.getStringValue();
		//TODO check if datefields are enabled, as default getDate() gives unix era date
		Date dateStart = jiraSettingsDateStart.getDate();
		Date dateEnd = jiraSettingsDateEnd.getDate();
		// TODO use proper functions based on arguments given
		String response = getResource(hostname, jql, login, pass, dateStart, dateEnd);
		// TODO response parser
		// List<ITSDataType> entries = parseEntries(hostname);
		LOGGER.info("Transforming jira entries.");
		// BufferedDataTable out = transform(entries, exec);
		// return new BufferedDataTable[] { out };
		return new BufferedDataTable[] {};
	}

	private String getResource(String hostname, String jql, String login, String pass,
			Date dateStart, Date dateEnd) {
//		String dateStartString = new SimpleDateFormat("yyyy-MM-dd").format(dateStart);
//		String dateEndString = new SimpleDateFormat("yyyy-MM-dd").format(dateEnd);
		//TODO repair uri - cos tu robie zle i sie sypie generacja uri
		String uri = JiraOnlineAdapterUriFactory.createJiraUriByJql(hostname, jql);
		//TODO login with login/pass if given
		Client client = JiraOnlineAdapterClientFactory.createClient();
		//TODO proper try-catch
		String response = null;
		try {
			response = JiraOnlineAdapterResourceDownloader.getResource(client, uri);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	private BufferedDataTable transform(final List<ITSDataType> entries,
			final ExecutionContext exec) throws CanceledExecutionException {
		ITSAdapterTransformer transformer = new ITSAdapterTransformer(
				ITSAdapterTableFactory.createDataColumnSpec());
		return transformer.transform(entries, exec);
	}

	private List<ITSDataType> parseEntries(final String filePath)
			throws ParserConfigurationException, SAXException, IOException,
			ParseException {
		return new JiraEntriesParser().parseEntries(filePath);
	}

	@Override
	protected void reset() {
		//NOOP
	}

	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
			throws InvalidSettingsException {
		Preconditions.checkArgument(inSpecs.length == 0);
		return createTableSpec();
	}

	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		jiraSettingsURL.saveSettingsTo(settings);
		jiraSettingsLogin.saveSettingsTo(settings);
		jiraSettingsPass.saveSettingsTo(settings);
		jiraSettingsDateStart.saveSettingsTo(settings);
		jiraSettingsDateEnd.saveSettingsTo(settings);
		jiraSettingsJQL.saveSettingsTo(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		jiraSettingsURL.loadSettingsFrom(settings);
		jiraSettingsLogin.loadSettingsFrom(settings);
		jiraSettingsPass.loadSettingsFrom(settings);
		jiraSettingsDateStart.loadSettingsFrom(settings);
		jiraSettingsDateEnd.loadSettingsFrom(settings);
		jiraSettingsJQL.loadSettingsFrom(settings);
	}

	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		jiraSettingsURL.validateSettings(settings);
		jiraSettingsLogin.validateSettings(settings);
		jiraSettingsPass.validateSettings(settings);
		jiraSettingsDateStart.validateSettings(settings);
		jiraSettingsDateEnd.validateSettings(settings);
		jiraSettingsJQL.validateSettings(settings);
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

	static SettingsModelString createSettingsURL() {
		return new SettingsModelString(JIRA_URL, DEFAULT_VALUE);
	}
	
	static SettingsModelString createSettingsLogin() {
		return new SettingsModelString(JIRA_LOGIN, DEFAULT_VALUE);
	}
	
	static SettingsModelString createSettingsPass() {
		return new SettingsModelString(JIRA_PASS, DEFAULT_VALUE);
	}
	
	static SettingsModelDate createSettingsDateStart() {
		return new SettingsModelDate(JIRA_START_DATE);
	}
	
	static SettingsModelDate createSettingsDateEnd() {
		return new SettingsModelDate(JIRA_END_DATE);
	}
	
	static SettingsModelString createSettingsJQL() {
		return new SettingsModelString(JIRA_JQL, DEFAULT_VALUE);
	}
	
}
