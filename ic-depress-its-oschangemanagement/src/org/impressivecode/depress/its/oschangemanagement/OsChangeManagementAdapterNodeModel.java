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
package org.impressivecode.depress.its.oschangemanagement;

import static org.impressivecode.depress.its.ITSAdapterTableFactory.createDataColumnSpec;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.impressivecode.depress.its.ITSAdapterTransformer;
import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSMappingManager;
import org.impressivecode.depress.its.ITSOnlineNodeModel;
import org.impressivecode.depress.its.oschangemanagement.builder.OsChangeManagementJiraRationalAdapterUriBuilder;
import org.impressivecode.depress.its.oschangemanagement.builder.OsChangeManagementUriBuilder.Mode;
import org.impressivecode.depress.its.oschangemanagement.model.OsChangeManagementProject;
import org.impressivecode.depress.its.oschangemanagement.parser.OsChangeManagementRationalAdapterParser;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;

/**
 * 
 * @author Marcin Cho³uj, Wroclaw University of Technology
 * @author Piotr Malek, Wroclaw University of Technology
 * @author Przemys³aw Trepka, Wroclaw University of Technology
 * @author £ukasz Trojak, Wroclaw University of Technology
 * 
 */

public class OsChangeManagementAdapterNodeModel extends ITSOnlineNodeModel {

	private static final int NUMBER_OF_INPUT_PORTS = 0;
	private static final int NUMBER_OF_OUTPUT_PORTS = 1;
	private static final String CFG_ITS_PLUGIN = "plugin";
	private final SettingsModelString pluginSettings = createPluginSettings();

	private OsChangeManagementRestClient client = new OsChangeManagementRestClient();
	private OsChangeManagementJiraRationalAdapterUriBuilder builder = new OsChangeManagementJiraRationalAdapterUriBuilder();
	private ExecutionContext exec;

	protected OsChangeManagementAdapterNodeModel() {
		super(NUMBER_OF_INPUT_PORTS, NUMBER_OF_OUTPUT_PORTS);
	}

	public static ITSMappingManager createMapping() {
		return new ITSMappingManager();
	}

	public static SettingsModelString createPluginSettings() {
		return new SettingsModelString(CFG_ITS_PLUGIN, DEFAULT_STRING_VALUE);
	}

	@Override
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
			throws InvalidSettingsException {
		return new PortObjectSpec[NUMBER_OF_OUTPUT_PORTS];
	}

	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {

		ArrayList<String> projectList = (ArrayList<String>) getProjectList();
		ArrayList<URI> uriList = new ArrayList<URI>();
		this.exec = exec;

		builder.setHostname(getURL());
		builder.setMode(Mode.CHANGE_REQUEST);

		for (String project : projectList) {
			if (project == null) {
				continue;
			}
			builder.setProject(project);
			builder.setStartIndex(0);

			// executorService = Executors.newFixedThreadPool(THREAD_COUNT);
			uriList.addAll(getIssueList());
		}
		List<ITSDataType> issues = executeIssuesLinks(uriList);

		BufferedDataTable out = transform(issues, exec);
		return new BufferedDataTable[] { out };
	}

	private List<URI> getIssueList() {
		int totalIssues = 0;
		List<URI> issueLinks = new ArrayList<>();
		try {
			totalIssues = getIssuesCount();
		} catch (Exception e) {
			Logger.getLogger("Error").severe(e.getMessage());
		}
		if (totalIssues > builder.getPageSize()) {
			while (totalIssues > 0) {
				issueLinks.add(builder.build());
				builder.prepareNextLink();
				totalIssues -= builder.getPageSize();
			}
		} else {
			issueLinks.add(builder.build());
		}

		return issueLinks;
	}

	private int getIssuesCount() throws Exception {
		String login = getLogin();
		String password = getPassword();
		String pluginName = pluginSettings.getStringValue();
		String rawData = client.getJSON(builder.build(), login, password);
		switch (pluginName) {
		case OsChangeManagementAdapterNodeDialog.IMB_RATIONAL_ADAPTER:
			return new OsChangeManagementRationalAdapterParser()
					.getIssueCount(rawData);
		default:
			return 0;
		}
	}

	protected List<String> getProjectList() {
		ArrayList<String> projectList = new ArrayList<String>();
		if (getProductName() != null) {

			List<OsChangeManagementProject> projects;
			try {
				projects = getList(Mode.PROJECT_LIST);
				for (OsChangeManagementProject item : projects) {
					if (item.getName().equals(getProductName()))
						projectList.add(getLastPathFragment(item.getUri()));
				}
			} catch (Exception e) {
				Logger.getLogger("Error")
						.severe("Error during connection, list could not be downloaded");
			}
		} else {
			List<OsChangeManagementProject> projects;
			try {
				projects = getList(Mode.PROJECT_LIST);
				for (OsChangeManagementProject item : projects) {
					projectList.add(getLastPathFragment(item.getUri()));
				}
			} catch (Exception e) {
				Logger.getLogger("Error")
						.severe("Error during connection, list could not be downloaded");
			}
		}
		return projectList;
	}

	private String getLastPathFragment(String path) {
		return path.substring(path.lastIndexOf('/') + 1);
	}

	private List<ITSDataType> executeIssuesLinks(final List<URI> issuesLinks)
			throws InterruptedException, ExecutionException {
		List<ITSDataType> list = new ArrayList<ITSDataType>();
		for (URI uri : issuesLinks) {
			try {
				list.addAll(call(uri));
			} catch (Exception e) {
				Logger.getLogger("Error").severe(e.getMessage());
			}
		}
		return list;
	}

	private <T> List<T> getList(Mode mode) throws Exception {
		String urlString = getURL();
		String login = getLogin();
		String password = getPassword();
		String pluginName = pluginSettings.getStringValue();
		builder.setHostname(urlString);
		builder.setMode(mode);
		String rawData = client.getJSON(builder.build(), login, password);
		switch (pluginName) {
		case OsChangeManagementAdapterNodeDialog.IMB_RATIONAL_ADAPTER:
			return (List<T>) new OsChangeManagementRationalAdapterParser()
					.getProjectList(rawData);
		default:
			return null;
		}
	}

	private BufferedDataTable transform(final List<ITSDataType> entries,
			final ExecutionContext exec) throws CanceledExecutionException {
		ITSAdapterTransformer transformer = new ITSAdapterTransformer(
				createDataColumnSpec());
		return transformer.transform(entries, exec);
	}

	private void checkForCancel() throws CanceledExecutionException {
		exec.checkCanceled();
	}

	@Override
	protected void saveSpecificSettingsTo(NodeSettingsWO settings) {
		pluginSettings.saveSettingsTo(settings);
	}

	@Override
	protected void validateSpecificSettings(NodeSettingsRO settings)
			throws InvalidSettingsException {
		pluginSettings.validateSettings(settings);
	}

	@Override
	protected void loadSpecificSettingsFrom(NodeSettingsRO settings)
			throws InvalidSettingsException {
		pluginSettings.loadSettingsFrom(settings);
	}

	public List<ITSDataType> call(URI uri) throws InvalidKeyException,
			BadPaddingException, IllegalBlockSizeException,
			UnsupportedEncodingException, IOException, Exception {
		checkForCancel();

		String rawData = client.getJSON(uri, getLogin(), getPassword());
		checkForCancel();
		String pluginName = pluginSettings.getStringValue();
		switch (pluginName) {
		case OsChangeManagementAdapterNodeDialog.IMB_RATIONAL_ADAPTER:
			return new OsChangeManagementRationalAdapterParser(mappingManager
					.getPriorityModel().getIncluded(), mappingManager
					.getTypeModel().getIncluded(), mappingManager
					.getResolutionModel().getIncluded(), mappingManager
					.getStatusModel().getIncluded()).getIssues(rawData);
		default:
			return new ArrayList<ITSDataType>();
		}
		// markProgressForIssue();
	}

}
