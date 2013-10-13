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
 * @author MichaÅ‚ Negacz, WrocÅ‚aw University of Technology
 * 
 */
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

/**
 * 
 * @author Micha³ Negacz
 * 
 */
public class BugzillaOnlineXmlRpcClient {

	private XmlRpcClient client;

	private static BugzillaOnlineXmlRpcClient instance = null;

	private BugzillaOnlineXmlRpcClient() {
	}

	// chyba lepiej jak bêdzie jeden klient, ¿eby nie tworzyæ nowego co ¿¹danie.
	public static BugzillaOnlineXmlRpcClient buildAndConfigureClient(URL url) {
		if (instance == null) {
			instance = new BugzillaOnlineXmlRpcClient();
			instance.setClient(new XmlRpcClient());

			instance.setConfigUrl(url);
		} else {
			XmlRpcClientConfigImpl config = (XmlRpcClientConfigImpl) instance
					.getClient().getConfig();
			if (!config.getServerURL().equals(url)) {
				instance.setConfigUrl(url);
			}
		}
		return instance;
	}

	private void setConfigUrl(URL url) {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(url);

		instance.getClient().setConfig(config);
		instance.getClient().setTransportFactory(
				new XmlRpcTransportFactoryWithCookies(instance.getClient()));
	}

	public void setClient(XmlRpcClient client) {
		this.client = client;
	}

	public XmlRpcClient getClient() {
		return client;
	}

	public Object execute(String method, Map<String, Object> parameters)
			throws XmlRpcException {
		// All Bugzilla functions use named parameters and this is realized by
		// Map object.
		// To execute method with Map by the client, we need to wrap it into
		// single element array.
		Object[] parametersWrapper = new Object[] { parameters };
		return client.execute(method, parametersWrapper);
	}

}
