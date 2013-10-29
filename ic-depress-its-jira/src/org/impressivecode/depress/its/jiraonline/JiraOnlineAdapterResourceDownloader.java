package org.impressivecode.depress.its.jiraonline;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 * 
 * @author Marcin Kunert, Krzysztof Kwoka, Dawid Rutowicz
 * 
 */
public class JiraOnlineAdapterResourceDownloader {

	public static String getResource(Client client, String uri) {
		WebResource service = client.resource(uri);

		return service.get(String.class);
	}

}
