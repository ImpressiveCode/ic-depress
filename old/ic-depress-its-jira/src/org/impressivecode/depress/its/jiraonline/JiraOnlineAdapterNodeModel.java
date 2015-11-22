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
import org.impressivecode.depress.its.ITSMappingManager;
import org.impressivecode.depress.its.ITSOnlineNodeModel;
import org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterUriBuilder.Mode;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineIssueChangeRowItem;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;

import com.google.common.base.Preconditions;

/**
 * @author Marcin Kunert, Wroclaw University of Technology
 * @author Krzysztof Kwoka, Wroclaw University of Technology
 * @author Dawid Rutowicz, Wroclaw University of Technology
 * @author Maciej Borkowski, Capgemini Poland
 */
public class JiraOnlineAdapterNodeModel extends ITSOnlineNodeModel {
    private static final String DEFAULT_VALUE = "";
    private static final int INPUT_NODE_COUNT = 0;
    private static final int OUTPUT_NODE_COUNT = 2;
    private static final int STEPS_PER_TASK = 2;
    private static final int THREAD_COUNT = 10;

    private static final String JIRA_JQL = "jql";
    private static final String JIRA_HISTORY = "history";

    private final SettingsModelString jqlSettings = createSettingsJQL();
    private final SettingsModelBoolean historySettings = createSettingsHistory();

    private JiraOnlineAdapterUriBuilder builder;
    private JiraOnlineAdapterRsClient client;

    private ExecutionContext exec;
    private ExecutionMonitor issueCountMonitor;
    private ExecutionMonitor issueListMonitor;
    private ExecutionMonitor issueHistoryMonitor;

    private ExecutorService executorService;

    private int issueTaskStepsSum;
    private int historyTaskStepsSum;
    private int issueTaskStepsCompleted;
    private int historyTaskStepsCompleted;

    protected JiraOnlineAdapterNodeModel() {
        super(INPUT_NODE_COUNT, OUTPUT_NODE_COUNT);
    }

    @Override
    protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs) throws InvalidSettingsException {
        return new PortObjectSpec[OUTPUT_NODE_COUNT];
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {
        long startTime = System.currentTimeMillis();
        this.exec = exec;
        prepareProgressMonitors();

        builder = prepareBuilder();
        builder.setProjectName(getProductName());

        client = new JiraOnlineAdapterRsClient();
        executorService = Executors.newFixedThreadPool(getThreadCount());

        List<URI> issueBatchLinks = prepareIssueBatchesLinks();

        issueListMonitor.setProgress(0);
        issueTaskStepsSum = issueBatchLinks.size() * STEPS_PER_TASK;

        List<ITSDataType> issues = executeIssueTasks(issueBatchLinks);
        List<JiraOnlineIssueChangeRowItem> issuesHistory = executeHistoryTasks(issues);

        executorService.shutdown();

        BufferedDataTable out = transform(issues, exec);
        BufferedDataTable outHistory = transformHistory(issuesHistory, exec);

        long endTime = System.currentTimeMillis();
        LOGGER.warn("Finished in " + ((endTime - startTime) / 1000) + " seconds.");
        return new BufferedDataTable[] { out, outHistory };
    }

    private int getThreadCount() {
        return THREAD_COUNT;
    }

    private List<URI> prepareIssueBatchesLinks() throws Exception {
        issueCountMonitor.setProgress(0);
        final int totalIssues = getIssuesCount();
        issueCountMonitor.setProgress(0.5);
        List<URI> issueBatchLinks = new ArrayList<>();

        while (totalIssues > builder.getStartingIndex()) {
            issueBatchLinks.add(builder.build());
            builder.prepareForNextBatch();
        }
        issueCountMonitor.setProgress(1);
        return issueBatchLinks;
    }

    private int getIssuesCount() throws Exception {
        String rawData = null;
        rawData = client.getJSON(builder.build(), getLogin(), getPassword());
        return JiraOnlineAdapterParser.getTotalIssuesCount(rawData);
    }

    private List<ITSDataType> executeIssueTasks(final List<URI> issueBatchLinks) throws InterruptedException,
            ExecutionException {
        List<Callable<List<ITSDataType>>> tasks = newArrayList();

        for (URI uri : issueBatchLinks) {
            tasks.add(new DownloadAndParseIssuesTask(uri, mappingManager));
        }
        List<Future<List<ITSDataType>>> partialResults = executorService.invokeAll(tasks);
        List<ITSDataType> issues = combinePartialIssueResults(partialResults);
        return issues;
    }

    private List<JiraOnlineIssueChangeRowItem> executeHistoryTasks(final List<ITSDataType> issues)
            throws InterruptedException, ExecutionException {
        List<JiraOnlineIssueChangeRowItem> issuesHistory;
        List<Callable<List<JiraOnlineIssueChangeRowItem>>> historyTasks = newArrayList();
        if (shouldDownloadHistory()) {
            historyTaskStepsSum = issues.size() * STEPS_PER_TASK;
            issueHistoryMonitor.setProgress(0);
            builder.setMode(Mode.SINGLE_ISSUE_WITH_HISTORY);
            for (ITSDataType issue : issues) {
                builder.setIssueKey(issue.getIssueId());
                historyTasks.add(new DownloadAndParseIssueHistoryTask(builder.build()));
            }
            List<Future<List<JiraOnlineIssueChangeRowItem>>> partialHistoryResults = executorService
                    .invokeAll(historyTasks);
            issuesHistory = combinePartialIssueHistoryResults(partialHistoryResults);
            builder.setMode(Mode.MULTIPLE_ISSUES);
        } else {
            issuesHistory = newArrayList();
        }
        return issuesHistory;
    }

    private void prepareProgressMonitors() {
        double issueListProgressPart = 0.9;
        double issueHistoryProgressPart = 0;

        if (shouldDownloadHistory()) {
            issueListProgressPart = 0.2;
            issueHistoryProgressPart = 0.7;
        }

        issueCountMonitor = exec.createSubProgress(0.1);

        issueListMonitor = exec.createSubProgress(issueListProgressPart);
        issueHistoryMonitor = exec.createSubProgress(issueHistoryProgressPart);

        issueTaskStepsCompleted = 0;
        historyTaskStepsCompleted = 0;

    }

    private List<ITSDataType> combinePartialIssueResults(final List<Future<List<ITSDataType>>> partialResults)
            throws InterruptedException, ExecutionException {
        List<ITSDataType> result = new ArrayList<>();
        for (Future<List<ITSDataType>> partialResult : partialResults) {
            result.addAll(partialResult.get());
        }

        return result;
    }

    private List<JiraOnlineIssueChangeRowItem> combinePartialIssueHistoryResults(
            final List<Future<List<JiraOnlineIssueChangeRowItem>>> partialResults) throws InterruptedException,
            ExecutionException {
        List<JiraOnlineIssueChangeRowItem> result = newArrayList();

        for (Future<List<JiraOnlineIssueChangeRowItem>> partialResult : partialResults) {
            result.addAll(partialResult.get());
        }

        return result;
    }

    private boolean shouldDownloadHistory() {
        return historySettings.getBooleanValue();
    }

    private void markProgressForIssue() {
        issueListMonitor.setProgress((double) ++issueTaskStepsCompleted / (double) issueTaskStepsSum);
    }

    private void markProgressForHistory() {
        issueHistoryMonitor.setProgress((double) ++historyTaskStepsCompleted / (double) historyTaskStepsSum);
    }

    private void checkForCancel() throws CanceledExecutionException {
        exec.checkCanceled();
    }

    private JiraOnlineAdapterUriBuilder prepareBuilder() {
        JiraOnlineAdapterUriBuilder builder = new JiraOnlineAdapterUriBuilder();
        builder.setHostname(getURL());
        if (jqlSettings.getStringValue() != null && !jqlSettings.getStringValue().equals("")) {
            builder.setJQL(jqlSettings.getStringValue());
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
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        Preconditions.checkArgument(inSpecs.length == 0);
        return new DataTableSpec[] { createDataColumnSpec() };
    }

    @Override
    protected void saveSpecificSettingsTo(final NodeSettingsWO settings) {
        jqlSettings.saveSettingsTo(settings);
        historySettings.saveSettingsTo(settings);
    }

    @Override
    protected void loadSpecificSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        jqlSettings.loadSettingsFrom(settings);
        historySettings.loadSettingsFrom(settings);
    }

    @Override
    protected void validateSpecificSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        jqlSettings.validateSettings(settings);
        historySettings.validateSettings(settings);
    }

    static SettingsModelString createSettingsJQL() {
        return new SettingsModelString(JIRA_JQL, DEFAULT_VALUE);
    }

    static SettingsModelBoolean createSettingsHistory() {
        return new SettingsModelBoolean(JIRA_HISTORY, false);
    }

    private class DownloadAndParseIssuesTask implements Callable<List<ITSDataType>> {
        private URI uri;
        private ITSMappingManager mappingManager;

        public DownloadAndParseIssuesTask(final URI uri, ITSMappingManager mappingManager) {
            this.uri = uri;
            this.mappingManager = mappingManager;
        }

        @Override
        public List<ITSDataType> call() throws Exception {
            checkForCancel();
            String rawData = client.getJSON(uri, getLogin(), getPassword());
            String hostname = builder.getHostname();

            markProgressForIssue();
            checkForCancel();
            JiraOnlineAdapterParser parser = new JiraOnlineAdapterParser(mappingManager.getPriorityModel()
                    .getIncluded(), mappingManager.getTypeModel().getIncluded(), mappingManager.getResolutionModel()
                    .getIncluded(), mappingManager.getStatusModel().getIncluded());
            List<ITSDataType> list = parser.parseSingleIssueBatch(rawData, hostname);
            markProgressForIssue();

            return list;
        }
    }

    private class DownloadAndParseIssueHistoryTask implements Callable<List<JiraOnlineIssueChangeRowItem>> {
        private URI uri;

        public DownloadAndParseIssueHistoryTask(final URI uri) {
            this.uri = uri;
        }

        @Override
        public List<JiraOnlineIssueChangeRowItem> call() throws Exception {
            checkForCancel();

            String rawIssue = client.getJSON(uri, getLogin(), getPassword());

            markProgressForHistory();
            checkForCancel();

            List<JiraOnlineIssueChangeRowItem> list = JiraOnlineAdapterParser.parseSingleIssue(rawIssue);
            markProgressForHistory();

            return list;
        }
    }

}
