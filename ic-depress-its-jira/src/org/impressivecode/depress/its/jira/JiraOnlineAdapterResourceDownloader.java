package org.impressivecode.depress.its.jira;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 
 * @author Marcin Kunert, Krzysztof Kwoka, Dawid Rutowicz
 * 
 */
public class JiraOnlineAdapterResourceDownloader {

	public static Response getResource(Client client, String uri) throws Exception {
		WebTarget target = client.target(uri);
		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
		// TODO consider handling no connection error
		Response response = invocationBuilder.get();

		// TODO check HTTP statuses that gives positive response
		if (response.getStatus() != 200) {
			Exception up = new Exception("a proper place to handle exceptions as wrong creditials etc.");
			
			throw up; // ;-)
		}
		
		return response;
	}

}
