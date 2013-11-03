package org.impressivecode.depress.its.jiraonline;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.filter.HttpBasicAuthFilter;

public class JiraOnlineAdapterRsClient {
	private Client client;
	private String hostname;
	private boolean securedConnection;

	public JiraOnlineAdapterRsClient() {
		createClient();
	}

	public String getIssuesByJql(String jql) throws Exception {
		Response response = getReponseByJql(jql);
		isDataFetchSuccessful(response);

		return reponseToString(response);
	}

	public void registerCredentials(String username, String password) {
		client.register(new HttpBasicAuthFilter(username, password));
	}

	public String getIssueById(String id) throws Exception {
		Response response = getResponseByIssueId(id);
		isDataFetchSuccessful(response);

		return reponseToString(response);
	}

	public String getIssueByDates(Date startDate, Date endDate)
			throws Exception {
		String dateFilter = createJqlDateFilter(startDate, endDate);

		return getIssuesByJql(dateFilter);
	}

	private String createJqlDateFilter(Date startDate, Date endDate) {
		return new JqlDatesFilter().createDatesFilter(startDate, endDate);
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
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

	private Response getReponseByJql(String jql) {
		final URI uri = UriBuilder
				.fromUri("{http}://{host}/rest/api/2/search?jql={jql}")
				.resolveTemplate("http",
						isSecuredConnection() ? "https" : "http")
				.resolveTemplate("host", hostname).resolveTemplate("jql", jql)
				.build();

		return client.target(uri)
				.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get();

	}

	private Response getResponseByIssueId(String id) {
		final URI uri = UriBuilder
				.fromUri("{http}://{host}/rest/api/2/issue/{issueId}")
				.resolveTemplate("http",
						isSecuredConnection() ? "https" : "http")
				.resolveTemplate("host", hostname)
				.resolveTemplate("issueId", id).build();

		return client.target(uri)
				.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get();
	}

	void setClient(Client client) {
		this.client = client;
	}

	public Client getClient() {
		return client;
	}
}

class JqlDatesFilter {

	private static final String JQL_AND = " AND ";
	private static final String JQL_OR = " OR ";
	private static final String JQL_RESOLUTION_DATE = "resolutiondate";
	private static final String JQL_CREATED_DATE = "created";

	private SimpleDateFormat dateFormatter;

	public JqlDatesFilter() {
		setJqlStandardDateFormat();
	}

	public SimpleDateFormat getDateFormatter() {
		return dateFormatter;
	}

	public String createDatesFilter(Date startDate, Date endDate) {
		StringBuilder filter = new StringBuilder();

		createDateFilterBasedOnType(filter, startDate, endDate,
				JQL_CREATED_DATE);

		filter.append(JQL_OR);

		createDateFilterBasedOnType(filter, startDate, endDate,
				JQL_RESOLUTION_DATE);

		return filter.toString();
	}

	private StringBuilder createDateFilterBasedOnType(StringBuilder filter,
			Date startDate, Date endDate, String type) {
		if (startDate != null) {
			filter.append(type).append(" >= ").append(formatDate(startDate));
		}

		if (startDate != null && endDate != null) {
			filter.append(JQL_AND);
		}

		if (endDate != null) {
			filter.append(type).append(" <= ").append(formatDate(endDate));
		}

		return filter;
	}

	String formatDate(Date startDate) {
		return dateFormatter.format(startDate);
	}

	private void setJqlStandardDateFormat() {
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	}

}