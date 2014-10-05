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
package org.impressivecode.depress.its.bugzillaonline;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.lang.Integer.parseInt;
import static java.lang.Math.ceil;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.impressivecode.depress.its.bugzillaonline.BugzillaOnlineParser.ASSIGNEE;
import static org.impressivecode.depress.its.bugzillaonline.BugzillaOnlineParser.CREATED;
import static org.impressivecode.depress.its.bugzillaonline.BugzillaOnlineParser.FIX_VERSION;
import static org.impressivecode.depress.its.bugzillaonline.BugzillaOnlineParser.ID;
import static org.impressivecode.depress.its.bugzillaonline.BugzillaOnlineParser.LINK;
import static org.impressivecode.depress.its.bugzillaonline.BugzillaOnlineParser.PRIORITY;
import static org.impressivecode.depress.its.bugzillaonline.BugzillaOnlineParser.REPORTER;
import static org.impressivecode.depress.its.bugzillaonline.BugzillaOnlineParser.RESOLUTION;
import static org.impressivecode.depress.its.bugzillaonline.BugzillaOnlineParser.STATUS;
import static org.impressivecode.depress.its.bugzillaonline.BugzillaOnlineParser.SUMMARY;
import static org.impressivecode.depress.its.bugzillaonline.BugzillaOnlineParser.UPDATED;
import static org.impressivecode.depress.its.bugzillaonline.BugzillaOnlineParser.VERSION;
import static org.impressivecode.depress.its.bugzillaonline.BugzillaOnlineParser.ACTUAL_TIME;
import static org.impressivecode.depress.its.bugzillaonline.BugzillaOnlineParser.ESTIMATED_TIME;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.xmlrpc.XmlRpcException;
import org.impressivecode.depress.its.ITSDataType;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;

/**
 * @author Michał Negacz, Wrocław University of Technology
 * @author Piotr Wróblewski, Wrocław University of Technology
 * @author Maciej Borkowski, Cagepmini Poland
 */
public class BugzillaOnlineClientAdapter {
    public static final String BUGZILLA_VERSION_THAT_SUPPORT_INCLUSION_IN_GET = "3.6";
    public static final String BUGZILLA_VERSION_THAT_SUPPORT_INCLUSION_IN_SEARCH = "4.0";

    public static final String VERSION_SEPARATOR = "\\.";
    public static final String BUGZILLA_VERSION_METHOD = "Bugzilla.version";
    public static final String BUG_SEARCH_METHOD = "Bug.search";
    public static final String PRODUCT_SEARCH_METHOD = "Product.get_accessible_products";
    public static final String PRODUCT_GET_METHOD = "Product.get";
    public static final String BUG_GET_METHOD = "Bug.get";
    public static final String BUG_HISTORY_METHOD = "Bug.history";
    public static final String BUG_COMMENT_METHOD = "Bug.comments";
    public static final String BUG_ATTACHMENT_METHOD = "Bug.attachments";
    public static final String LIMIT = "limit";
    public static final String OFFSET = "offset";
    public static final String PRODUCT_NAME = "product";
    public static final String PASSWORD = "password";
    public static final String BUGS = "bugs";
    public static final String IDS = "ids";
    public static final String PRODUCTS = "products";
    public static final String INCLUDE_FIELDS = "include_fields";
    public static final String NAME = "name";

    private static final int TASK_STEPS_COUNT = 4;
    private static final int BUGS_PER_TASK = 1000;
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

    private BugzillaOnlineXmlRpcClient bugzillaClient;
    private BugzillaOnlineParser parser;
    private ExecutionMonitor monitor;

    private double progressStep;
    private String bugzillaVersion;

    public BugzillaOnlineClientAdapter(String url) throws MalformedURLException {
        checkNotNull(url);
        bugzillaClient = buildClient(url);
        parser = buildParser();
    }

    public BugzillaOnlineClientAdapter(String url, ExecutionMonitor monitor) throws MalformedURLException {
        checkNotNull(url);
        checkNotNull(monitor);
        this.monitor = monitor;
        bugzillaClient = buildClient(url);
        parser = buildParser();
    }

    private BugzillaOnlineXmlRpcClient buildClient(String url) throws MalformedURLException {
        return new BugzillaOnlineXmlRpcClient(url);
    }

    private BugzillaOnlineParser buildParser() {
        return new BugzillaOnlineParser();
    }

    public List<ITSDataType> listEntries(BugzillaOnlineOptions options) throws XmlRpcException,
            CanceledExecutionException, InterruptedException, ExecutionException {
        checkIfIsCanceledAndMarkProgress(0);

        bugzillaVersion = getBugzillaVersion();
        List<String> bugsIds = listBugsIds(options);
        List<Callable<List<ITSDataType>>> tasks = partitionTasks(bugsIds);
        setProgressStep(tasks.size());
        List<Future<List<ITSDataType>>> partialResults = executeTasks(tasks);
        return combinePartialResults(partialResults);
    }

    public List<String> listProjects() throws XmlRpcException {
        Map<String, Object> info = new HashMap<String, Object>();
        Object[] ids = listProjectsIds();
        info.put(IDS, ids);
        info.put(INCLUDE_FIELDS, new String[] { NAME });
        Object[] products = getProjects(info);

        List<String> projects = new ArrayList<String>();
        for (Object object : products) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) object;
            projects.add((String) map.get(NAME));
        }
        return projects;
    }

    public List<String> listProperties(BugzillaOnlineOptions options, String propertyName) throws XmlRpcException {
        bugzillaVersion = getBugzillaVersion();
        List<String> bugsIds = listBugsIds(options);
        List<String> propertyList = new ArrayList<String>();
        Object[] bugs = getBugs(prepareGetBugsPropertiesParameters(bugsIds, propertyName));
        for (Object object : bugs) {
            @SuppressWarnings("unchecked")
            Map<String, String> map = (Map<String, String>) object;
            propertyList.add(map.get(propertyName));
        }
        return propertyList;
    }

    private Object[] listProjectsIds() throws XmlRpcException {
        return searchProjects(new HashMap<String, Object>());
    }

    private List<String> listBugsIds(BugzillaOnlineOptions options) throws XmlRpcException {
        List<String> bugsIds = new ArrayList<String>();
        if (options.getProductName() == null || options.getProductName() == "") {
            for (String name : listProjects()) {
                options.setProductName(name);
                Object[] simpleBugsInformation = searchBugs(prepareSearchBugsParameters(options));
                bugsIds.addAll(parser.extractIds(simpleBugsInformation));
            }
        } else {
            Object[] simpleBugsInformation = searchBugs(prepareSearchBugsParameters(options));
            bugsIds = parser.extractIds(simpleBugsInformation);
        }
        return bugsIds;
    }

    private void checkIfIsCanceledAndMarkProgress(double value) throws CanceledExecutionException {
        monitor.checkCanceled();
        monitor.setProgress(value);
    }

    public String getBugzillaVersion() throws XmlRpcException {
        Map<String, Object> result = bugzillaClient.execute(BUGZILLA_VERSION_METHOD,
                Collections.<String, Object> emptyMap());
        return result.get(VERSION).toString();
    }

    Object[] searchBugs(Map<String, Object> parameters) throws XmlRpcException {
        Map<String, Object> result = bugzillaClient.execute(BUG_SEARCH_METHOD, parameters);
        return (Object[]) result.get(BUGS);
    }

    Object[] getBugs(Map<String, Object> parameters) throws XmlRpcException {
        Map<String, Object> result = bugzillaClient.execute(BUG_GET_METHOD, parameters);
        return (Object[]) result.get(BUGS);
    }

    Object[] getBugsHistory(Map<String, Object> parameters) throws XmlRpcException {
        Map<String, Object> result = bugzillaClient.execute(BUG_HISTORY_METHOD, parameters);
        return (Object[]) result.get(BUGS);
    }

    Object[] searchProjects(Map<String, Object> parameters) throws XmlRpcException {
        Map<String, Object> result = bugzillaClient.execute(PRODUCT_SEARCH_METHOD, parameters);
        return (Object[]) result.get(IDS);
    }

    Object[] getProjects(Map<String, Object> parameters) throws XmlRpcException {
        Map<String, Object> result = bugzillaClient.execute(PRODUCT_GET_METHOD, parameters);
        return (Object[]) result.get(PRODUCTS);
    }

    @SuppressWarnings("unchecked")
    Map<String, Object> getBugsComments(Map<String, Object> parameters) throws XmlRpcException {
        Map<String, Object> result = bugzillaClient.execute(BUG_COMMENT_METHOD, parameters);
        return (Map<String, Object>) result.get(BUGS);
    }

    private List<Callable<List<ITSDataType>>> partitionTasks(List<String> bugsIds) {
        List<Callable<List<ITSDataType>>> tasks = newArrayList();

        int taskCount = (int) ceil((double) bugsIds.size() / BUGS_PER_TASK);

        for (int taskNo = 0; taskNo < taskCount; taskNo++) {
            int chunkLowerIndex = taskNo * BUGS_PER_TASK;
            int chunkUpperIndex = (taskNo + 1) * BUGS_PER_TASK;
            if (chunkUpperIndex > bugsIds.size()) {
                chunkUpperIndex = bugsIds.size();
            }

            tasks.add(new Task(bugsIds.subList(chunkLowerIndex, chunkUpperIndex)));
        }

        return tasks;
    }

    private List<Future<List<ITSDataType>>> executeTasks(List<Callable<List<ITSDataType>>> tasks)
            throws InterruptedException {
        ExecutorService executorService = newFixedThreadPool(THREAD_COUNT);
        List<Future<List<ITSDataType>>> partialResults = executorService.invokeAll(tasks);
        executorService.shutdown();
        return partialResults;
    }

    private List<ITSDataType> combinePartialResults(List<Future<List<ITSDataType>>> partialResults)
            throws InterruptedException, ExecutionException {
        List<ITSDataType> result = newArrayList();

        for (Future<List<ITSDataType>> partialResult : partialResults) {
            result.addAll(partialResult.get());
        }

        return result;
    }

    private void setProgressStep(int tasksCount) {
        progressStep = (1.0 / tasksCount / TASK_STEPS_COUNT);
    }

    private void checkIfIsCanceledAndMarkProgress() throws CanceledExecutionException {
        synchronized (monitor) {
            double progress = monitor.getProgressMonitor().getProgress();
            progress += progressStep;
            checkIfIsCanceledAndMarkProgress(progress);
        }
    }

    private Map<String, Object> prepareSearchBugsParameters(BugzillaOnlineOptions filter) {
        Map<String, Object> parameters = newHashMap();
        parameters.put(PRODUCT_NAME, filter.getProductName());

        if (bugzillaInstanceSupportFieldsInclusionInSearch()) {
            parameters.put(INCLUDE_FIELDS, new String[] { ID });
        }
        if (isFilterProvided(filter.getLimit())) {
            parameters.put(LIMIT, filter.getLimit());
        }
        if (isFilterProvided(filter.getOffset())) {
            parameters.put(OFFSET, filter.getOffset());
        }
        if (isFilterProvided(filter.getDateFrom())) {
            parameters.put(CREATED, filter.getDateFrom());
        }
        if (isFilterProvided(filter.getPriority())) {
            parameters.put(PRIORITY, filter.getPriority());
        }
        if (isFilterProvided(filter.getAssignedTo())) {
            parameters.put(ASSIGNEE, filter.getAssignedTo());
        }
        if (isFilterProvided(filter.getReporter())) {
            parameters.put(REPORTER, filter.getReporter());
        }
        if (isFilterProvided(filter.getVersion())) {
            parameters.put(VERSION, filter.getVersion());
        }
        return parameters;
    }

    private boolean bugzillaInstanceSupportFieldsInclusionInSearch() {
        return isVersionGreaterOrEqual(BUGZILLA_VERSION_THAT_SUPPORT_INCLUSION_IN_SEARCH, bugzillaVersion);
    }

    private boolean isFilterProvided(Object value) {
        return value != null;
    }

    private boolean isVersionGreaterOrEqual(String firstVersion, String secondVersion) {
        String[] splittedFirstVersion = firstVersion.split(VERSION_SEPARATOR);
        String[] splittedSecondVersion = secondVersion.split(VERSION_SEPARATOR);

        int firstMajorVersion = parseInt(splittedFirstVersion[0]);
        int secondMajorVersion = parseInt(splittedSecondVersion[0]);
        int firstMinorVersion = parseInt(splittedFirstVersion[1]);
        int secondMinorVersion = parseInt(splittedSecondVersion[1]);

        return (firstMajorVersion < secondMajorVersion)
                || (firstMajorVersion == secondMajorVersion && firstMinorVersion <= secondMinorVersion);
    }

    private Map<String, Object> prepareGetBugsParameters(List<String> ids) {
        Map<String, Object> parameters = prepareBugsIdsParameters(ids);
        if (bugzillaInstanceSupportFieldsInclusionInGet()) {
            parameters.put(INCLUDE_FIELDS, new String[] { ID, CREATED, UPDATED, STATUS, ASSIGNEE, FIX_VERSION, VERSION,
                    REPORTER, PRIORITY, SUMMARY, LINK, RESOLUTION, ACTUAL_TIME, ESTIMATED_TIME });
        }
        return parameters;
    }

    private Map<String, Object> prepareGetBugsPropertiesParameters(List<String> ids, String property) {
        Map<String, Object> parameters = prepareBugsIdsParameters(ids);
        if (bugzillaInstanceSupportFieldsInclusionInGet()) {
            parameters.put(INCLUDE_FIELDS, new String[] { property });
        }
        return parameters;
    }

    private boolean bugzillaInstanceSupportFieldsInclusionInGet() {
        return isVersionGreaterOrEqual(BUGZILLA_VERSION_THAT_SUPPORT_INCLUSION_IN_GET, bugzillaVersion);
    }

    private Map<String, Object> prepareBugsIdsParameters(List<String> ids) {
        Map<String, Object> parameters = newHashMap();
        parameters.put(IDS, ids);
        return parameters;
    }

    private class Task implements Callable<List<ITSDataType>> {
        private List<String> bugsIds;

        public Task(List<String> bugsIds) {
            this.bugsIds = bugsIds;
        }

        @Override
        public List<ITSDataType> call() throws Exception {
            checkIfIsCanceledAndMarkProgress();
            Object[] bugs = getBugs(prepareGetBugsParameters(bugsIds));

            checkIfIsCanceledAndMarkProgress();
            Object[] histories = getBugsHistory(prepareBugsIdsParameters(bugsIds));

            checkIfIsCanceledAndMarkProgress();
            Map<String, Object> comments = getBugsComments(prepareBugsIdsParameters(bugsIds));

            checkIfIsCanceledAndMarkProgress();
            return parser.parseEntries(bugs, histories, comments);
        }

    }

    public void setCredentials(final String login, final String password) {
        bugzillaClient.setAuthentificator(login, password);
    }

}
