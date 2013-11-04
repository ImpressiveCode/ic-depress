package org.impressivecode.depress.its.jiraonline;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.filter.HttpBasicAuthFilter;

public class JiraOnlineAdapterRsClient {

	private Client client;
//	private String hostname;
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

	//do we need it?
	public String getIssueById(String id) throws Exception {
		Response response = getResponseByIssueId(id);
		isDataFetchSuccessful(response);

		return reponseToString(response);
	}
//
//	public String getIssueByDates(Date startDate, Date endDate)
//			throws Exception {
//		String dateFilter = createJqlDateFilter(startDate, endDate);
//
//		return getIssuesByJql(dateFilter);
//	}
//
//	private String createJqlDateFilter(Date startDate, Date endDate) {
//		return new JqlDateFilter().createDatesFilter(startDate, endDate);
//	}
//
//	public String getHostname() {
//		return hostname;
//	}
//
//	public void setHostname(String hostname) {
//		this.hostname = hostname;
//	}

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

	private Response getResponseByIssueId(String id) {
		//TODO remade/finish if we need this
//		final URI uri = uriBuilder.setId(id).buildById();
//		
//		return client.target(uri)
//				.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get();
		return null;
	}

	//get/set client needed?
	void setClient(Client client) {
		this.client = client;
	}

	public Client getClient() {
		return client;
	}
}

//class JqlDateFilter {
//
//	private static final String JQL_AND = " AND ";
//	private static final String JQL_OR = " OR ";
//	private static final String JQL_RESOLUTION_DATE = "resolutiondate";
//	private static final String JQL_CREATED_DATE = "created";
//
//	private SimpleDateFormat dateFormatter;
//
//	public JqlDateFilter() {
//		setJqlStandardDateFormat();
//	}
//
//	public SimpleDateFormat getDateFormatter() {
//		return dateFormatter;
//	}
//
//	public String createDatesFilter(Date startDate, Date endDate) {
//		StringBuilder filter = new StringBuilder();
//
//		createDateFilterBasedOnType(filter, startDate, endDate,
//				JQL_CREATED_DATE);
//
//		filter.append(JQL_OR);
//
//		createDateFilterBasedOnType(filter, startDate, endDate,
//				JQL_RESOLUTION_DATE);
//
//		return filter.toString();
//	}
//
//	private StringBuilder createDateFilterBasedOnType(StringBuilder filter,
//			Date startDate, Date endDate, String type) {
//		if (startDate != null) {
//			filter.append(type).append(" >= ").append(formatDate(startDate));
//		}
//
//		if (startDate != null && endDate != null) {
//			filter.append(JQL_AND);
//		}
//
//		if (endDate != null) {
//			filter.append(type).append(" <= ").append(formatDate(endDate));
//		}
//
//		return filter;
//	}
//
//	String formatDate(Date startDate) {
//		return dateFormatter.format(startDate);
//	}
//
//	private void setJqlStandardDateFormat() {
//		dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
//	}
//
//}