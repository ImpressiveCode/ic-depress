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
import static com.google.common.collect.Maps.newHashMap;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineClientAdapter.Progress.GET_BUGS;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineClientAdapter.Progress.GET_BUGS_COMMENTS;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineClientAdapter.Progress.GET_BUGS_HISTORY;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineClientAdapter.Progress.PARSE_ENTRIES;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineClientAdapter.Progress.SEARCH_BUGS;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.impressivecode.depress.its.ITSDataType;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;

/**
 * 
 * @author Michał Negacz, Wrocław University of Technology
 * 
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

	private final int minChunkSize = 200;

	private final int maxThreadsNumber = 20;

	private volatile List<ITSDataType> result = new ArrayList<ITSDataType>();

	public enum Progress {
		SEARCH_BUGS(0.0, "Searching bugs"), GET_BUGS(0.2,
				"Getting bugs details"), GET_BUGS_HISTORY(0.4,
				"Getting bugs history"), GET_BUGS_COMMENTS(0.6,
				"Getting bugs comments"), PARSE_ENTRIES(0.8, "Parsing bugs");

		private double value;

		private String message;

		private Progress(double value, String message) {
			this.value = value;
			this.message = message;
		}

		public double getValue() {
			return value;
		}

		public String getMessage() {
			return message;
		}

	}

	private BugzillaOnlineXmlRpcClient bugzillaClient;

	private BugzillaOnlineParser parser;

	private ExecutionMonitor monitor;

	public BugzillaOnlineClientAdapter(String url, ExecutionMonitor monitor)
			throws MalformedURLException {
		checkNotNull(url);
		checkNotNull(monitor);
		this.monitor = monitor;
		bugzillaClient = buildClient(url);
		parser = buildParser();
	}

	private BugzillaOnlineXmlRpcClient buildClient(String url)
			throws MalformedURLException {
		return new BugzillaOnlineXmlRpcClient(url);
	}

	private BugzillaOnlineParser buildParser() {
		return new BugzillaOnlineParser();
	}

	public List<ITSDataType> listEntries(BugzillaOnlineFilter filter,
			boolean parallel) throws XmlRpcException,
			CanceledExecutionException {
		checkNotNull(filter.getProductName());

		checkIfIsCanceledAndMarkProgress(SEARCH_BUGS);
		Object[] simpleBugsInformation = searchBugs(
				prepareSearchBugsParameters(filter), 0, filter.getLimit());

		int chunkSize;
		if (parallel) {
			chunkSize = getChunkSize(simpleBugsInformation.length);
		} else {
			chunkSize = simpleBugsInformation.length;
		}

		BigDecimal bLength = new BigDecimal(simpleBugsInformation.length);
		BigDecimal bchunk = new BigDecimal(chunkSize);
		int iterNumber = bLength.divide(bchunk, 2, RoundingMode.CEILING)
				.intValue();
		Thread[] threads = new Thread[iterNumber];
		for (int i = 0; i < iterNumber; i++) {
			int from = i * chunkSize;
			int to = from + chunkSize;
			if (to > simpleBugsInformation.length) {
				to = simpleBugsInformation.length;
			}
			threads[i] = runSingleThread(Arrays.copyOfRange(
					simpleBugsInformation, from, to));
		}

		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	private int getChunkSize(int length) {
		if (length < minChunkSize) {
			return length;
		} else {
			BigDecimal bLength = new BigDecimal(length);
			BigDecimal threadsNumber = new BigDecimal(maxThreadsNumber);
			threadsNumber.setScale(0);
			BigDecimal minChunkS = new BigDecimal(minChunkSize);
			BigDecimal threads = bLength.divide(minChunkS, 0,
					RoundingMode.CEILING);
			if (threads.compareTo(threadsNumber) == 1) {
				BigDecimal scope = bLength.divide(threadsNumber, 0,
						RoundingMode.CEILING);
				return scope.intValue();
			} else {
				return minChunkSize;
			}
		}
	}

	private Thread runSingleThread(Object[] chunk) {
		BugInfoGetter getter = new BugInfoGetter(chunk);
		Thread worker = new Thread(getter);
		worker.start();
		return worker;
	}

	private void checkIfIsCanceledAndMarkProgress(Progress progress)
			throws CanceledExecutionException {
		monitor.checkCanceled();
		monitor.setProgress(progress.getValue(), progress.getMessage());
	}

	Object[] searchBugs(Map<String, Object> parameters, int offset, int limit)
			throws XmlRpcException {
		parameters.put(OFFSET, offset);
		parameters.put(LIMIT, limit);

		Map<String, Object> result = bugzillaClient.execute(BUG_SEARCH_METHOD,
				parameters);

		return (Object[]) result.get(BUGS);
	}

	Object[] getBugs(Map<String, Object> parameters) throws XmlRpcException {
		Map<String, Object> result = bugzillaClient.execute(BUG_GET_METHOD,
				parameters);

		return (Object[]) result.get(BUGS);
	}

	Object[] getBugsHistory(Map<String, Object> parameters)
			throws XmlRpcException {
		Map<String, Object> result = bugzillaClient.execute(BUG_HISTORY_METHOD,
				parameters);

		return (Object[]) result.get(BUGS);
	}

	@SuppressWarnings("unchecked")
	Map<String, Object> getBugsComments(Map<String, Object> parameters)
			throws XmlRpcException {
		Map<String, Object> result = bugzillaClient.execute(BUG_COMMENT_METHOD,
				parameters);

		return (Map<String, Object>) result.get(BUGS);
	}

	private Map<String, Object> prepareSearchBugsParameters(
			BugzillaOnlineFilter filter) {
		Map<String, Object> parameters = newHashMap();
		parameters.put(PRODUCT_NAME, filter.getProductName());
		parameters
				.put(INCLUDE_FIELDS, new String[] { BugzillaOnlineParser.ID });
		if (creationTimeIsProvided(filter)) {
			parameters.put(DATE_FROM, filter.getDateFrom());
		}
		return parameters;
	}

	private Map<String, Object> prepareGetBugsParameters(List<String> ids) {
		Map<String, Object> parameters = prepareBugsIdsParameters(ids);
		parameters.put(INCLUDE_FIELDS, new String[] { BugzillaOnlineParser.ID,
				BugzillaOnlineParser.CREATED, BugzillaOnlineParser.UPDATED,
				BugzillaOnlineParser.STATUS, BugzillaOnlineParser.ASSIGNEE,
				BugzillaOnlineParser.FIX_VERSION, BugzillaOnlineParser.VERSION,
				BugzillaOnlineParser.REPORTER, BugzillaOnlineParser.PRIORITY,
				BugzillaOnlineParser.SUMMARY, BugzillaOnlineParser.LINK,
				BugzillaOnlineParser.RESOLUTION });
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

	private class BugInfoGetter implements Runnable {

		private Object[] simpleBugsInformation;

		public BugInfoGetter(Object[] simpleBugsInformation) {
			this.simpleBugsInformation = simpleBugsInformation;
		}

		@Override
		public void run() {
			try {
				List<ITSDataType> tab = getBugsInformation();
				result.addAll(tab);
			} catch (XmlRpcException e) {
				e.printStackTrace();
			} catch (CanceledExecutionException e) {
				e.printStackTrace();
			}
		}

		private List<ITSDataType> getBugsInformation() throws XmlRpcException,
				CanceledExecutionException {

			List<String> bugsIds = parser.extractBugsIds(simpleBugsInformation);

			Object[] bugs = getBugs(prepareGetBugsParameters(bugsIds));

			Object[] histories = getBugsHistory(prepareBugsIdsParameters(bugsIds));

			Map<String, Object> comments = getBugsComments(prepareBugsIdsParameters(bugsIds));

			return parser.parseEntries(bugs, histories, comments);
		}

	}

}
