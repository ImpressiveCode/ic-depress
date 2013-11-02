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

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.core.UriBuilder;


/**
 * Builder for Jira Uri
 * @author Marcin Kunert
 * 
 */
public class JiraOnlineAdapterUriBuilder {
	
	private static final String URI_PATH = "{hostname}/rest/api/latest/search";
	private static final String QUERY_PARAM = "jql";
	
	private SimpleDateFormat dateFormatter;
	private String hostname;
	private String jql;
	private Date dateFrom;
	private Date dateTo;
	
	public JiraOnlineAdapterUriBuilder(String hostname) {
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		this.hostname = hostname;
	}
	
	public JiraOnlineAdapterUriBuilder setHostname(String hostname) {
		this.hostname = hostname;
		return this;
	}
	
	public JiraOnlineAdapterUriBuilder setJQL(String jql) {
		resetAllData();
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
	
	
	private void resetAllData() {
		dateFrom = null;
		dateTo = null;
	}
	
	
	public String build() {
		
		StringBuilder jqlBuilder = new StringBuilder();
		
		if(jql != null) {
			jqlBuilder.append(jql);
			jqlBuilder.append(" AND ");
		}
		
		if(dateFrom != null) {
			jqlBuilder.append("created >= " + dateFormatter.format(dateFrom));
			jqlBuilder.append(" AND ");
		}
		
		if(dateTo != null) {
			jqlBuilder.append("created <= " + dateFormatter.format(dateTo));
			jqlBuilder.append(" AND ");
		}
		
		//trim last " AND "
		String uriJQL = jqlBuilder.substring(0, jqlBuilder.length()-5);
		
		return UriBuilder.fromPath(URI_PATH).queryParam(QUERY_PARAM, uriJQL).build(hostname).toString();
	}
}
