package org.impressivecode.depress.its.bugzilla;

import java.net.URL;
import java.util.HashMap;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 * 
 * @author Micha³ Negacz
 * 
 */
public class BugzillaOnlineXmlRpcClient implements BugzillaOnlineClient {

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

	@Override
	public Object execute(String method, HashMap<String, Object> parameters) throws BugzillaOnlineClientException {
		try {
			return client.execute(method, new Object[] { parameters });
		} catch (XmlRpcException e) {
			throw new BugzillaOnlineClientException(e);
		}
	}

}
