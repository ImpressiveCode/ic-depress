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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.lang.Math.ceil;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineParser.ASSIGNEE;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineParser.CREATED;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineParser.FIX_VERSION;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineParser.ID;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineParser.LINK;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineParser.PRIORITY;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineParser.REPORTER;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineParser.RESOLUTION;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineParser.STATUS;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineParser.SUMMARY;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineParser.UPDATED;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineParser.VERSION;

import java.net.MalformedURLException;
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
 * 
 * @author Michał Negacz, Wrocław University of Technology
 * @author Piotr Wróblewski, Wrocław University of Technology
 */
public class BugzillaOnlineClientAdapter {

	public static final String USER_LOGIN_METHOD = "User.login";

	public static final String BUG_SEARCH_METHOD = "Bug.search";

	public static final String BUG_GET_METHOD = "Bug.get";

	public static final String BUG_HISTORY_METHOD = "Bug.history";

	public static final String BUG_COMMENT_METHOD = "Bug.comments";

	public static final String BUG_ATTACHMENT_METHOD = "Bug.attachments";

	public static final String LIMIT = "limit";

	public static final String OFFSET = "offset";

	public static final String PRODUCT_NAME = "product";

	public static final String DATE_FROM = "creation_time";

	public static final String PASSWORD = "password";

	public static final String LOGIN = "login";

	public static final String BUGS = "bugs";

	public static final String IDS = "ids";

	public static final String INCLUDE_FIELDS = "include_fields";

	private static final int CHUNK_SIZE = 200;

	private static final int STEPS_NUMBER = 4;

	private BugzillaOnlineXmlRpcClient bugzillaClient;

	private BugzillaOnlineParser parser;

	private ExecutionMonitor monitor;

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

	private int getThreadsLimit() {
		return Runtime.getRuntime().availableProcessors() + 1;
	}

	public List<ITSDataType> listEntries(BugzillaOnlineFilter filter) throws XmlRpcException, CanceledExecutionException, InterruptedException, ExecutionException {
		checkNotNull(filter.getProductName());
		checkIfIsCanceledAndMarkProgress(0);

		Object[] simpleBugsInformation = searchBugs(prepareSearchBugsParameters(filter), 0, filter.getLimit());
		List<String> bugsIds = parser.extractBugsIds(simpleBugsInformation);

		List<Callable<List<ITSDataType>>> tasks = partitionTasks(bugsIds);

		ExecutorService executorService = newFixedThreadPool(getThreadsLimit());
		List<Future<List<ITSDataType>>> partialResults = executorService.invokeAll(tasks);
		executorService.shutdown();

		return combinePartialResults(partialResults);
	}

	private List<Callable<List<ITSDataType>>> partitionTasks(List<String> bugsIds) {
		List<Callable<List<ITSDataType>>> tasks = newArrayList();

		int taskCount = (int) ceil((double) bugsIds.size() / CHUNK_SIZE);

		for (int taskNo = 0; taskNo < taskCount; taskNo++) {
			int chunkLowerIndex = taskNo * CHUNK_SIZE;
			int chunkUpperIndex = (taskNo + 1) * CHUNK_SIZE;
			if (chunkUpperIndex > bugsIds.size()) {
				chunkUpperIndex = bugsIds.size();
			}

			tasks.add(new Worker(bugsIds.subList(chunkLowerIndex, chunkUpperIndex)));
		}

		return tasks;
	}

	private List<ITSDataType> combinePartialResults(List<Future<List<ITSDataType>>> partialResults) throws InterruptedException, ExecutionException {
		List<ITSDataType> result = newArrayList();

		for (Future<List<ITSDataType>> partialResult : partialResults) {
			result.addAll(partialResult.get());
		}

		return result;
	}

	private void checkIfIsCanceledAndMarkProgress(double value) throws CanceledExecutionException {
		monitor.checkCanceled();
		monitor.setProgress(value);
	}

	private void checkIfIsCanceledAndMarkProgress() throws CanceledExecutionException {
		synchronized (monitor) {
			double progress = monitor.getProgressMonitor().getProgress();
			// TODO calculate next progress value
			checkIfIsCanceledAndMarkProgress(progress);
		}
	}

	Object[] searchBugs(Map<String, Object> parameters, int offset, int limit) throws XmlRpcException {
		parameters.put(OFFSET, offset);
		parameters.put(LIMIT, limit);

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

	@SuppressWarnings("unchecked")
	Map<String, Object> getBugsComments(Map<String, Object> parameters) throws XmlRpcException {
		Map<String, Object> result = bugzillaClient.execute(BUG_COMMENT_METHOD, parameters);

		return (Map<String, Object>) result.get(BUGS);
	}

	private Map<String, Object> prepareSearchBugsParameters(BugzillaOnlineFilter filter) {
		Map<String, Object> parameters = newHashMap();
		parameters.put(PRODUCT_NAME, filter.getProductName());
		parameters.put(INCLUDE_FIELDS, new String[] { ID });
		if (creationTimeIsProvided(filter)) {
			parameters.put(DATE_FROM, filter.getDateFrom());
		}
		return parameters;
	}

	private Map<String, Object> prepareGetBugsParameters(List<String> ids) {
		Map<String, Object> parameters = prepareBugsIdsParameters(ids);
		parameters.put(INCLUDE_FIELDS, new String[] { ID, CREATED, UPDATED, STATUS, ASSIGNEE, FIX_VERSION, VERSION, REPORTER, PRIORITY, SUMMARY, LINK, RESOLUTION });
		return parameters;
	}

	private Map<String, Object> prepareBugsIdsParameters(List<String> ids) {
		Map<String, Object> parameters = newHashMap();
		parameters.put(IDS, ids);
		return parameters;
	}

	private boolean creationTimeIsProvided(BugzillaOnlineFilter filter) {
		return filter.getDateFrom() != null;
	}

	public void login(String username, String password) throws XmlRpcException {
		Map<String, Object> params = newHashMap();
		params.put(LOGIN, username);
		params.put(PASSWORD, password);

		bugzillaClient.execute(USER_LOGIN_METHOD, params);
	}

	private class Worker implements Callable<List<ITSDataType>> {

		private List<String> bugsIds;

		public Worker(List<String> bugsIds) {
			this.bugsIds = bugsIds;
		}

		@Override
		public List<ITSDataType> call() throws Exception {
			Object[] bugs = getBugs(prepareGetBugsParameters(bugsIds));
			checkIfIsCanceledAndMarkProgress();
			Object[] histories = getBugsHistory(prepareBugsIdsParameters(bugsIds));
			checkIfIsCanceledAndMarkProgress();
			Map<String, Object> comments = getBugsComments(prepareBugsIdsParameters(bugsIds));
			checkIfIsCanceledAndMarkProgress();
			return parser.parseEntries(bugs, histories, comments);
		}

	}

}
