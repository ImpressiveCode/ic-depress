package org.impressivecode.depress.its.jira;

import javax.ws.rs.core.UriBuilder;

public class JiraOnlineAdapterUriFactory {

	public static String createJiraUriByJql(String hostname, String jql) {
		// FIXME consider https?
		boolean https = false;

		UriBuilder builder = setUriHostnameAndJql(hostname, jql);
		setUriProtocol(https, builder);

		return builder.build().toString();
	}

	// TODO what types are dates?
	public static String createJiraUriByDateFilters(String hostname,
			String startDate, String endDate) {
		String jql = createDateJql(startDate, endDate);

		return createJiraUriByJql(hostname, jql);
	}

	private static UriBuilder setUriHostnameAndJql(String hostname, String jql) {
		return UriBuilder
				.fromUri("{http}://{host}/rest/api/2/search?jql={jira-query}")
				.resolveTemplate("host", hostname)
				.resolveTemplate("jira-query", jql);
	}

	private static void setUriProtocol(boolean secure, UriBuilder builder) {
		builder.resolveTemplate("http", (secure ? "https" : "http"));
	}

	private static String createDateJql(String startDate, String endDate) {
		// FIXME prepare JQL date filter
		return "";
	}

}
