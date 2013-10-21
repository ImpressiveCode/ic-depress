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

import static com.google.common.collect.Maps.newHashMap;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.impressivecode.depress.its.ITSDataType;

import com.google.common.base.Preconditions;

/**
 * 
 * @author Michał Negacz, Wrocław University of Technology
 * 
 */
public class BugzillaOnlineClientAdapter {

	public static final String USER_LOGIN_METHOD = "User.login";

	public static final String BUG_SEARCH_METHOD = "Bug.search";

	public static final String LIMIT = "limit";

	public static final String OFFSET = "offset";

	public static final String PRODUCT_NAME = "product";

	public static final String DATE_FROM = "creation_time";

	public static final String PASSWORD = "password";

	public static final String LOGIN = "login";

	public static final String BUGS = "bugs";

	public static final int BUGS_FETCH_LIMIT = 10;

	private BugzillaOnlineXmlRpcClient bugzillaClient;

	private BugzillaOnlineParser parser;

	public BugzillaOnlineClientAdapter(String urlAddress) throws MalformedURLException {
		Preconditions.checkNotNull(urlAddress);
		bugzillaClient = buildClient(urlAddress);
		parser = buildParser();
	}

	private BugzillaOnlineXmlRpcClient buildClient(String urlAddress) throws MalformedURLException {
		return new BugzillaOnlineXmlRpcClient(new URL(urlAddress));
	}

	private BugzillaOnlineParser buildParser() {
		return new BugzillaOnlineParser();
	}

	public List<ITSDataType> listEntries(BugzillaOnlineFilter filter) throws XmlRpcException {
		Preconditions.checkNotNull(filter.getProductName());
		// TODO in one worker fetch part of bugs and in other worker transform they into entries (producer consumer pattern)
		Object[] bugs = getBugs(getParametersMap(filter), 0, BUGS_FETCH_LIMIT);
		return parser.parseEntries(bugs);
	}

	Object[] getBugs(Map<String, Object> parameters, int offset, int limit) throws XmlRpcException {
		parameters.put(OFFSET, offset);
		parameters.put(LIMIT, limit);

		Map<String, Object> result = bugzillaClient.execute(BUG_SEARCH_METHOD, parameters);

		return (Object[]) result.get(BUGS);
	}

	private Map<String, Object> getParametersMap(BugzillaOnlineFilter filter) {
		Map<String, Object> parameters = newHashMap();
		parameters.put(PRODUCT_NAME, filter.getProductName());
		if (creationTimeIsProvided(filter)) {
			parameters.put(DATE_FROM, filter.getDateFrom());
		}
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

}
