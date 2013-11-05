package org.impressivecode.depress.its.jiraonline;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.filter.HttpBasicAuthFilter;

public class JiraOnlineAdapterRsClient {

	private Client client;
	private boolean securedConnection;
	private JiraOnlineAdapterUriBuilder uriBuilder;

	public JiraOnlineAdapterRsClient() {
		this(new JiraOnlineAdapterUriBuilder());
	}

	public JiraOnlineAdapterRsClient(JiraOnlineAdapterUriBuilder uriBuilder) {
		createClient();
		this.uriBuilder = uriBuilder;
	}

	public void registerCredentials(String username, String password) {
		client.register(new HttpBasicAuthFilter(username, password));
	}

	public String getIssues() throws Exception {
		Response response = getReponse();
		isDataFetchSuccessful(response);

		return reponseToString(response);
	}

	public boolean isSecuredConnection() {
		return securedConnection;
	}

	public void setSecuredConnection(boolean securedConnection) {
		this.securedConnection = securedConnection;
	}

	private void createClient() {
		client = ClientBuilder.newClient();
	}

	private void isDataFetchSuccessful(Response response) throws Exception {
		if (response.getStatus() != 200) {
			throw new Exception("Failed to fetch data.");
		}
	}

	private String reponseToString(Response response) {
		return response.readEntity(String.class);
	}

	private Response getReponse() {
		final URI uri = uriBuilder.build();

		return client.target(uri)
				.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get();

	}

	public JiraOnlineAdapterUriBuilder getUriBuilder() {
		return uriBuilder;
	}

	public void setUriBuilder(JiraOnlineAdapterUriBuilder uriBuilder) {
		this.uriBuilder = uriBuilder;
	}
}
