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

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
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

	private static final int BUGS_FETCH_LIMIT = 10;

	private BugzillaOnlineXmlRpcClient bugzillaClient;

	public BugzillaOnlineClientAdapter(String urlAddress) throws MalformedURLException {
		Preconditions.checkNotNull(urlAddress);
		bugzillaClient = buildClient(urlAddress);
	}

	private BugzillaOnlineXmlRpcClient buildClient(String urlAddress) throws MalformedURLException {
		return new BugzillaOnlineXmlRpcClient(new URL(urlAddress));
	}

	public List<ITSDataType> listEntries(String productName, Date creation_time) throws XmlRpcException {
		Preconditions.checkNotNull(productName);
		Object[] bugs = getBugsFromProduct(productName,creation_time, 0, BUGS_FETCH_LIMIT); // TODO in one worker fetch part of bugs and in other worker transform they into entries (producer consumer pattern)
		BugzillaOnlineAdapterEntriesParser parser=new BugzillaOnlineAdapterEntriesParser();
		return parser.parseEntries(bugs);
	}

	private Object[] getBugsFromProduct(String productName,Date creation_time, int offset, int limit) throws XmlRpcException {
		Map<String, Object> parameters = newHashMap();
		parameters.put("product", productName);
		parameters.put("offset", offset);
		parameters.put("limit", limit);
		if(creation_time!=null){
			parameters.put("cration_time", creation_time);
		}

		Map<String, Object> result = bugzillaClient.execute("Bug.search", parameters);

		return (Object[]) result.get("bugs");
	}

	public void login(String username, String password) throws XmlRpcException {
		Map<String, Object> params = newHashMap();
		params.put("login", username);
		params.put("password", password);

		bugzillaClient.execute("User.login", params);
	}

	private void getBugTest() throws XmlRpcException {
		Map<String, Object> params = newHashMap();
		params.put("ids", "60");
		Object sessionId = bugzillaClient.execute("Bug.get", params);
		Map<String, Object> test = (Map<String, Object>) sessionId;
		Object[] ob = (Object[]) test.get("bugs");
		Map<String, Object> test2 = (Map<String, Object>) ob[0];
		Map<String, Object> creator = (Map<String, Object>) test2.get("creator_detail");
		System.out.println(creator.get("email"));
		System.out.println(test2.get("version"));
	}

}
