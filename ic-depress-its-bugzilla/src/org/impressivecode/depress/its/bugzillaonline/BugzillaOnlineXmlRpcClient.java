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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 * 
 * @author Michał Negacz, Wrocław University of Technology
 * 
 */
public class BugzillaOnlineXmlRpcClient {

	private static final String SLASH = "/";

	private static final String ENDPOINT_INTERFACE = "xmlrpc.cgi";

	private XmlRpcClient client;

	public BugzillaOnlineXmlRpcClient(String url) throws MalformedURLException {
		client = buildAndConfigureClient(getEndpointURL(url));
	}

	private XmlRpcClient buildAndConfigureClient(URL url) {
		XmlRpcClient client = new XmlRpcClient();

		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(url);

		client.setConfig(config);
		client.setTransportFactory(new XmlRpcTransportFactoryWithCookies(client));

		return client;
	}

	private URL getEndpointURL(String url) throws MalformedURLException {
		if (!url.endsWith(ENDPOINT_INTERFACE)) {
			if (!url.endsWith(SLASH)) {
				url += SLASH;
			}
			url += ENDPOINT_INTERFACE;
		}
		return new URL(url);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> execute(String method, Map<String, Object> parameters) throws XmlRpcException {
		// All Bugzilla functions use named parameters and this is realized by Map object. 
		// To execute method with Map by the client, we need to wrap it into single element array.
		Object[] parametersWrapper = new Object[] { parameters };
		return (Map<String, Object>) client.execute(method, parametersWrapper);
	}

}
