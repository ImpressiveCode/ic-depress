/*
 ImpressiveCode Depress Framework
 Copyright (C) 2013  ImpressiveCode contributors

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.impressivecode.depress.its.jiraonline;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.core.UriBuilder;

/**
 * Builder for Jira Uri
 * 
 * @author Marcin Kunert
 * 
 */
public class JiraOnlineAdapterUriBuilder {

	private static final String URI_PATH = "https://{hostname}/rest/api/latest/search";
	private static final String QUERY_PARAM = "jql";
	private static final String CONJUNCTION = " AND ";
	public static final String CREATED = "created";
	public static final String RESOLUTION_DATE = "resolutiondate";

	private SimpleDateFormat dateFormatter;
	private String hostname;
	private String jql;
	private Date dateFrom;
	private Date dateTo;
	private String dateFilterStatus;

	public JiraOnlineAdapterUriBuilder() {
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	}

	// public JiraOnlineAdapterUriBuilder(String hostname) {
	// dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	// this.hostname = hostname;
	// }

	public JiraOnlineAdapterUriBuilder setHostname(String hostname) {
		this.hostname = hostname;
		return this;
	}

	public JiraOnlineAdapterUriBuilder setJQL(String jql) {
		this.jql = jql;
		return this;
	}

	public JiraOnlineAdapterUriBuilder setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
		return this;
	}

	public JiraOnlineAdapterUriBuilder setDateTo(Date dateTo) {
		this.dateTo = dateTo;
		return this;
	}

	public JiraOnlineAdapterUriBuilder setDateFilterStatus(
			String dateFilterStatus) {
		this.dateFilterStatus = dateFilterStatus;
		return this;
	}

	public URI build() {

		StringBuilder jqlBuilder = new StringBuilder();

		if (jql != null) {
			jqlBuilder.append(jql);
			jqlBuilder.append(CONJUNCTION);
		}

		if (dateFrom != null) {
			jqlBuilder.append(dateFilterStatus);
			jqlBuilder.append(" >= " + dateFormatter.format(dateFrom));
			jqlBuilder.append(CONJUNCTION);
		}

		if (dateTo != null) {
			jqlBuilder.append(dateFilterStatus);
			jqlBuilder.append(" <= " + dateFormatter.format(dateTo));
			jqlBuilder.append(CONJUNCTION);
		}

		String uriJQL = null;
		if (jqlBuilder.toString().endsWith(CONJUNCTION)) {
			uriJQL = jqlBuilder.substring(0, jqlBuilder.length() - 5);
		} else {
			uriJQL = jqlBuilder.toString();
		}

		URI result = UriBuilder.fromPath(URI_PATH)
				.resolveTemplate("hostname", hostname)
				.queryParam(QUERY_PARAM, uriJQL).build();
		// TODO setting issue limit, see issue#15 for more details
		// result += "&maxResults=300";
		System.out.println(result.toString());
		return result;
	}

	public URI testHost() {
		// todo
		return null;
	}
}
