package org.impressivecode.depress.its.jiraonline;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * 
 * @author Marcin Kunert, Krzysztof Kwoka, Dawid Rutowicz
 * 
 */
public class JiraOnlineAdapterClientFactory {

	public static Client createClient() {
		ClientConfig config = new DefaultClientConfig();
		
		return Client.create(config);
	}

	public static Client createClient(String username, String password) throws Exception {
		// TODO: implementation
		
		throw new Exception("not implemented");
	}

}
