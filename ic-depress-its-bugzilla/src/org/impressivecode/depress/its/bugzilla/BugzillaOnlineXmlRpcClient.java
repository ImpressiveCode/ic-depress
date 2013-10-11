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

import java.net.URL;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 * 
 * @author Michał Negacz
 * 
 */
public class BugzillaOnlineXmlRpcClient {

	private XmlRpcClient client;

	public BugzillaOnlineXmlRpcClient(URL url) {
		client = buildAndConfigureClient(url);
	}

	private XmlRpcClient buildAndConfigureClient(URL url) {
		XmlRpcClient client = new XmlRpcClient();

		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(url);

		client.setConfig(config);
		client.setTransportFactory(new XmlRpcTransportFactoryWithCookies(client));

		return client;
	}

	public Object execute(String method, Map<String, Object> parameters) throws BugzillaOnlineClientException {
		try {
			// All Bugzilla functions use named parameters and this is realized by Map object. 
			// To execute method with Map by the client, we need to wrap it into single element array.
			Object[] parametersWrapper = new Object[] { parameters };
			return client.execute(method, parametersWrapper);
		} catch (XmlRpcException e) {
			throw new BugzillaOnlineClientException(e);
		}
	}

}
