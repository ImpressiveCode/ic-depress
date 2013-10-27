package org.impressivecode.depress.its.jira;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import org.glassfish.jersey.client.filter.HttpBasicAuthFilter;

/**
 * 
 * @author Marcin Kunert, Krzysztof Kwoka, Dawid Rutowicz
 * 
 */
public class JiraOnlineAdapterClientFactory {

	public static Client createClient() {
		return ClientBuilder.newClient();
	}

	public static Client createClient(String username, String password) {
		Client client = createClient();

		client.register(new HttpBasicAuthFilter(username, password));

		return client;
	}

}	
