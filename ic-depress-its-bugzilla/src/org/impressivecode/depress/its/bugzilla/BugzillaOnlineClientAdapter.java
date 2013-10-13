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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.impressivecode.depress.its.ITSDataType;

import com.google.common.collect.Lists;

/**
 * 
 * @author Michał Negacz, Wrocław University of Technology
 * 
 */
public class BugzillaOnlineClientAdapter {

	private BugzillaOnlineXmlRpcClient bugzillaClient;

	public BugzillaOnlineClientAdapter(String urlAddress) throws MalformedURLException {
		bugzillaClient = buildClient(urlAddress);
	}

	private BugzillaOnlineXmlRpcClient buildClient(String urlAddress) throws MalformedURLException {
		return BugzillaOnlineXmlRpcClient.buildAndConfigureClient(new URL(urlAddress));
	}

	public List<ITSDataType> listEntries() throws XmlRpcException {
		return Lists.newArrayList(); // TODO implement fetching entries
	}
	
	public void login(String login, String password) throws XmlRpcException{
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("login", login);
		params.put("password", password);
		Object sessionId=bugzillaClient.execute("User.login", params);
		System.out.println(sessionId);
	}

}
