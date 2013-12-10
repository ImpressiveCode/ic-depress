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
package org.impressivecode.depress.its.jiraonline;

import static org.impressivecode.depress.its.ITSAdapterTableFactory.createDataColumnSpec;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.impressivecode.depress.its.ITSAdapterTableFactory;
import org.impressivecode.depress.its.ITSAdapterTransformer;
import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.jiraonline.historymodel.JiraOnlineIssueChangeRowItem;
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
import org.knime.core.node.port.PortObjectSpec;

import com.google.common.base.Preconditions;

/**
 * 
 * @author Marcin Kunert, Wroclaw University of Technology
 * @author Krzysztof Kwoka, Wroclaw University of Technology
 * @author Dawid Rutowicz, Wroclaw University of Technology
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
    private static final String JIRA_STATUS = "depress.its.jiraonline.status";
    private static final String JIRA_HISTORY = "depress.its.jiraonline.historz";

    private final SettingsModelString jiraSettingsURL = createSettingsURL();
    private final SettingsModelString jiraSettingsLogin = createSettingsLogin();
    private final SettingsModelString jiraSettingsPass = createSettingsPass();
    private final SettingsModelDate jiraSettingsDateStart = createSettingsDateStart();
    private final SettingsModelDate jiraSettingsDateEnd = createSettingsDateEnd();
    private final SettingsModelString jiraSettingsJQL = createSettingsJQL();
    private final SettingsModelString jiraSettingsStatus = createSettingsDateFilterStatusChooser();
    private final SettingsModelBoolean jiraSettingsHistory = createSettingsHistory();

    private static final NodeLogger LOGGER = NodeLogger.getLogger(JiraOnlineAdapterNodeModel.class);

    private JiraOnlineAdapterUriBuilder builder;
    private JiraOnlineAdapterRsClient client;

    protected JiraOnlineAdapterNodeModel() {
        super(0, 2);
    }
    
    @Override
    protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
        return new PortObjectSpec[2];
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {
        LOGGER.info("Preparing to download JIRA entries.");
        
        builder = prepareBuilder();
        client = new JiraOnlineAdapterRsClient(builder);
        client.setSecuredConnection(true);

        if (shouldDownloadHistory()) {
            return executeWithHistory(exec);
        } else {
            return executeWithoutHistory(exec);
        }
    }

    private BufferedDataTable[] executeWithoutHistory(final ExecutionContext exec) throws Exception {
        LOGGER.info("Downloading JIRA entries.");

        ArrayList<String> rawSources = new ArrayList<>();
        String rawData = client.getIssues();
        rawSources.add(rawData);
        final int totalIssues = JiraOnlineAdapterParser.getTotalIssuesNumber(rawData);
        while (totalIssues > builder.getNextStartingIndex()) {
            LOGGER.info("Downloaded " + builder.getNextStartingIndex() + " out of " + totalIssues + " JIRA entries.");
            exec.setProgress((builder.getNextStartingIndex() + 0.0) / (totalIssues + 0.0));
            builder.prepareForNextBatch();
            rawSources.add(client.getIssues());
        }
        LOGGER.info("Downloaded " + totalIssues + " out of " + totalIssues + " JIRA entries.");
        exec.setProgress(1);
        LOGGER.info("Transforming JIRA entries.");

        List<ITSDataType> parsedData = JiraOnlineAdapterParser.parseMultipleIssueBatches(rawSources, client
                .getUriBuilder().getHostname());
        BufferedDataTable out = transform(parsedData, exec);

        BufferedDataTable outHistory = transformHistory(new ArrayList<JiraOnlineIssueChangeRowItem>(), exec);

        return new BufferedDataTable[] { out, outHistory };
    }

    private BufferedDataTable[] executeWithHistory(final ExecutionContext exec) throws Exception {
        LOGGER.info("Downloading JIRA entries with history.");

        ArrayList<String> rawSources = new ArrayList<>();
        String rawData = client.getIssues();
        rawSources.add(rawData);
        final int totalIssues = JiraOnlineAdapterParser.getTotalIssuesNumber(rawData);
        while (totalIssues > builder.getNextStartingIndex()) {
            LOGGER.info("Downloaded " + builder.getNextStartingIndex() + " out of " + totalIssues + " JIRA entries.");
            builder.prepareForNextBatch();
            rawSources.add(client.getIssues());
        }
        LOGGER.info("Downloaded " + totalIssues + " out of " + totalIssues + " JIRA entries.");
        LOGGER.info("Transforming JIRA entries.");

        List<ITSDataType> parsedData = JiraOnlineAdapterParser.parseMultipleIssueBatches(rawSources, client
                .getUriBuilder().getHostname());

        List<JiraOnlineIssueChangeRowItem> history = new ArrayList<JiraOnlineIssueChangeRowItem>();

        for (ITSDataType issue : parsedData) {
            history.addAll(downloadAndParseIssueHistory(issue));
        }

        BufferedDataTable out = transform(parsedData, exec);
        BufferedDataTable outHistory = transformHistory(history, exec);

        return new BufferedDataTable[] { out, outHistory };
    }

    private List<JiraOnlineIssueChangeRowItem> downloadAndParseIssueHistory(ITSDataType issue) throws Exception {
        builder.setIssueKey(issue.getIssueId());
        String rawIssue = client.getIssueHistory();
        return JiraOnlineAdapterParser.parseSingleIssue(rawIssue);
    }

    private boolean shouldDownloadHistory() {
        return jiraSettingsHistory.getBooleanValue();
    }

    private JiraOnlineAdapterUriBuilder prepareBuilder() {
        JiraOnlineAdapterUriBuilder builder = new JiraOnlineAdapterUriBuilder();

        builder.setHostname(jiraSettingsURL.getStringValue());
        if (jiraSettingsJQL.getStringValue() != null && !jiraSettingsJQL.getStringValue().equals("")) {
            builder.setJQL(jiraSettingsJQL.getStringValue());
        }
        if (jiraSettingsDateStart.getSelectedFields() > 0) {
            builder.setDateFrom(jiraSettingsDateStart.getDate());
        }
        if (jiraSettingsDateEnd.getSelectedFields() > 0) {
            builder.setDateTo(jiraSettingsDateEnd.getDate());
        }

        switch (jiraSettingsStatus.getStringValue().toLowerCase()) {
        case "created":
            builder.setDateFilterStatus(JiraOnlineAdapterUriBuilder.DateFilterType.CREATED);
            break;
        case "resolutiondate":
            builder.setDateFilterStatus(JiraOnlineAdapterUriBuilder.DateFilterType.RESOLUTION_DATE);
            break;
        }
        return builder;
    }

    private BufferedDataTable transform(final List<ITSDataType> entries, final ExecutionContext exec)
            throws CanceledExecutionException {
        ITSAdapterTransformer transformer = new ITSAdapterTransformer(ITSAdapterTableFactory.createDataColumnSpec());
        return transformer.transform(entries, exec);
    }

    private BufferedDataTable transformHistory(final List<JiraOnlineIssueChangeRowItem> entries,
            final ExecutionContext exec) throws CanceledExecutionException {
        JiraOnlineAdapterHistoryTransformer transformer = new JiraOnlineAdapterHistoryTransformer(
                JiraOnlineAdapterHistoryTableFactory.createDataColumnSpec());
        return transformer.transform(entries, exec);
    }

    @Override
    protected void reset() {
        // NOOP
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        Preconditions.checkArgument(inSpecs.length == 0);
        return new DataTableSpec[] { createDataColumnSpec() };
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        jiraSettingsURL.saveSettingsTo(settings);
        jiraSettingsLogin.saveSettingsTo(settings);
        jiraSettingsPass.saveSettingsTo(settings);
        jiraSettingsDateStart.saveSettingsTo(settings);
        jiraSettingsDateEnd.saveSettingsTo(settings);
        jiraSettingsJQL.saveSettingsTo(settings);
        jiraSettingsStatus.saveSettingsTo(settings);
        jiraSettingsHistory.saveSettingsTo(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        jiraSettingsURL.loadSettingsFrom(settings);
        jiraSettingsLogin.loadSettingsFrom(settings);
        jiraSettingsPass.loadSettingsFrom(settings);
        jiraSettingsDateStart.loadSettingsFrom(settings);
        jiraSettingsDateEnd.loadSettingsFrom(settings);
        jiraSettingsJQL.loadSettingsFrom(settings);
        jiraSettingsStatus.loadSettingsFrom(settings);
        jiraSettingsHistory.loadSettingsFrom(settings);
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        jiraSettingsURL.validateSettings(settings);
        jiraSettingsLogin.validateSettings(settings);
        jiraSettingsPass.validateSettings(settings);
        jiraSettingsDateStart.validateSettings(settings);
        jiraSettingsDateEnd.validateSettings(settings);
        jiraSettingsJQL.validateSettings(settings);
        jiraSettingsStatus.validateSettings(settings);
        jiraSettingsHistory.validateSettings(settings);
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

    static SettingsModelString createSettingsDateFilterStatusChooser() {
        return new SettingsModelString(JIRA_STATUS, DEFAULT_VALUE);
    }

    static SettingsModelBoolean createSettingsHistory() {
        return new SettingsModelBoolean(JIRA_HISTORY, false);
    }
}
