package org.impressivecode.depress.its.jira;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		String jiraExampleUri = "https://hibernate.atlassian.net/rest/api/2/issue/HHH-100";
		WebResource service = client.resource(jiraExampleUri);
		System.out.println(service.get(String.class));
	}

}
