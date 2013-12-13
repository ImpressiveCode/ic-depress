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

import static com.google.common.collect.Lists.newArrayList;
import static org.impressivecode.depress.its.ITSAdapterTableFactory.createDataColumnSpec;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    private static final int INPUT_NODE_COUNT = 0;
    private static final int OUTPUT_NODE_COUNT = 2;
    private static final int THREAD_COUNT = 45;

    private static final String JIRA_URL = "depress.its.jiraonline.url";
    private static final String JIRA_LOGIN = "depress.its.jiraonline.login";
    private static final String JIRA_PASS = "depress.its.jiraonline.password";
    private static final String JIRA_START_DATE = "depress.its.jiraonline.startDate";
    private static final String JIRA_END_DATE = "depress.its.jiraonline.endDate";
    private static final String JIRA_JQL = "depress.its.jiraonline.jql";
    private static final String JIRA_STATUS = "depress.its.jiraonline.status";
    private static final String JIRA_HISTORY = "depress.its.jiraonline.history";

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
        super(INPUT_NODE_COUNT, OUTPUT_NODE_COUNT);
    }

    @Override
    protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
        return new PortObjectSpec[OUTPUT_NODE_COUNT];
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {
        long startTime = System.currentTimeMillis();

        builder = prepareBuilder();
        client = new JiraOnlineAdapterRsClient(builder);

        String rawData = client.getIssues();
        final int totalIssues = JiraOnlineAdapterParser.getTotalIssuesNumber(rawData);

        List<URI> issueBatchLinks = new ArrayList<>();

        while (totalIssues > builder.getNextStartingIndex()) {
            builder.prepareForNextBatch();
            issueBatchLinks.add(builder.build());
        }

        List<Callable<List<ITSDataType>>> tasks = newArrayList();

        LOGGER.warn("Created "+issueBatchLinks.size()+ " tasks");
        for (URI uri : issueBatchLinks) {
            tasks.add(new DownloadAndParseIssuesTask(uri));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Future<List<ITSDataType>>> partialResults = executorService.invokeAll(tasks);
        
        
        List<ITSDataType> issues = combinePartialIssueResults(partialResults);
        List<JiraOnlineIssueChangeRowItem> issuesHistory;
        
        List<Callable<List<JiraOnlineIssueChangeRowItem>>> historyTasks = newArrayList();
        if(shouldDownloadHistory()) {
            for(ITSDataType issue : issues) {
                builder.setIssueKey(issue.getIssueId());
                historyTasks.add(new DownloadAndParseIssueHistoryTask(builder.buildIssueHistoryURI()));
            }
            List<Future<List<JiraOnlineIssueChangeRowItem>>> partialHistoryResults = executorService.invokeAll(historyTasks);
            issuesHistory = combinePartialIssueHistoryResults(partialHistoryResults);
        } else {
            issuesHistory = newArrayList();
        }
        
        executorService.shutdown();

        BufferedDataTable out = transform(issues, exec);
        BufferedDataTable outHistory = transformHistory(issuesHistory, exec);

        long endTime = System.currentTimeMillis();
        LOGGER.warn("Finished in " + ((endTime - startTime) / 1000) + " seconds.");
        return new BufferedDataTable[] { out, outHistory };
    }

    private List<ITSDataType> combinePartialIssueResults(List<Future<List<ITSDataType>>> partialResults)
            throws InterruptedException, ExecutionException {
        List<ITSDataType> result = newArrayList();

        for (Future<List<ITSDataType>> partialResult : partialResults) {
            result.addAll(partialResult.get());
        }

        return result;
    }
    
    private List<JiraOnlineIssueChangeRowItem> combinePartialIssueHistoryResults(List<Future<List<JiraOnlineIssueChangeRowItem>>> partialResults)
            throws InterruptedException, ExecutionException {
        List<JiraOnlineIssueChangeRowItem> result = newArrayList();

        for (Future<List<JiraOnlineIssueChangeRowItem>> partialResult : partialResults) {
            result.addAll(partialResult.get());
        }

        return result;
    }
    
    private boolean shouldDownloadHistory() {
        return jiraSettingsHistory.getBooleanValue();
    }
    
    public void checkIfIsCanceledAndMarkProgress() {
        // TODO Auto-generated method stub

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

    private class DownloadAndParseIssuesTask implements Callable<List<ITSDataType>> {

        private URI uri;

        public DownloadAndParseIssuesTask(URI uri) {
            this.uri = uri;
        }

        @Override
        public List<ITSDataType> call() throws Exception {
            checkIfIsCanceledAndMarkProgress();

            String rawData = client.getJSON(uri);
            String hostname = client.getUriBuilder().getHostname();
            
            checkIfIsCanceledAndMarkProgress();
            
            return JiraOnlineAdapterParser.parseSingleIssueBatch(rawData, hostname);
        }
    }
    
    private class DownloadAndParseIssueHistoryTask  implements Callable<List<JiraOnlineIssueChangeRowItem>> {
        
        private URI uri;
        
        public DownloadAndParseIssueHistoryTask(URI uri) {
            this.uri = uri;
        }
        
        @Override
        public List<JiraOnlineIssueChangeRowItem> call() throws Exception {
            checkIfIsCanceledAndMarkProgress();
            
            String rawIssue = client.getJSON(uri);
            
            checkIfIsCanceledAndMarkProgress();
            
            return JiraOnlineAdapterParser.parseSingleIssue(rawIssue);
        }
    }




}
