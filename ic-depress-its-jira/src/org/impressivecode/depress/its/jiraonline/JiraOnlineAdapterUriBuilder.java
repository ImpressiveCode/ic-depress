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
 * Builder for JIRA REST API
 * 
 * @author Marcin Kunert, Wroclaw University of Technology
 * @author Krzysztof Kwoka, Wroclaw University of Technology
 * 
 */
public class JiraOnlineAdapterUriBuilder {

    public static final int ISSUES_PER_BATCH = 100;

    private static final String SIMPLE_URI_PATH = "{protocol}://{hostname}/";
    private static final String TEST_URI_PATH = "{protocol}://{hostname}/rest/api/2/serverInfo";
    private static final String QUERY_URI_PATH = "{protocol}://{hostname}/rest/api/latest/search";
    private static final String ISSUE_HISTORY_URI_PATH = "{protocol}://{hostname}/rest/api/latest/issue/{issueKey}";
    private static final String QUERY_PARAM = "jql";
    private static final String FIELDS_PARAM = "fields";
    private static final String EXPAND_PARAM = "expand";
    private static final String CONJUNCTION = " AND ";
    private static final String GREATER_EQUAL = " >= ";
    private static final String LESS_EQUAL = " <= ";
    private static final String JIRA_DATE_FORMAT = "yyyy-MM-dd";
    private static final String MAX_RESULTS = "maxResults";
    private static final String START_AT = "startAt";

    private Mode mode = Mode.MULTI;
    private int startingIndex = 0;
    private String issueKey;

    public static enum DateFilterType {

        CREATED("created"), RESOLUTION_DATE("resolutiondate");

        public final String value;

        private DateFilterType(String value) {
            this.value = value;
        }

        public String toString() {
            return value;
        }
    }

    private SimpleDateFormat dateFormatter;
    private String hostname;
    private String jql;
    private String protocol;
    private Date dateFrom;
    private Date dateTo;
    private JiraOnlineAdapterUriBuilder.DateFilterType dateFilterStatus;
    private boolean isTest;

    public JiraOnlineAdapterUriBuilder() {
        dateFormatter = new SimpleDateFormat(JIRA_DATE_FORMAT);
    }

    public JiraOnlineAdapterUriBuilder setHostname(String hostname) {
        this.hostname = hostname;
        filterInputedHostname();

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

    public JiraOnlineAdapterUriBuilder setDateFilterStatus(JiraOnlineAdapterUriBuilder.DateFilterType dateFilterStatus) {
        this.dateFilterStatus = dateFilterStatus;
        return this;
    }

    public JiraOnlineAdapterUriBuilder setIsTest(boolean isTest) {
        this.isTest = isTest;
        return this;
    }

    public JiraOnlineAdapterUriBuilder prepareForNextBatch() {
        this.startingIndex += JiraOnlineAdapterUriBuilder.ISSUES_PER_BATCH;
        return this;
    }

    public URI build() {

        if (isTest) {
            return testHost();
        }

        if (mode == Mode.MULTI) {
            return buildMultiIssuesURI();
        }

        if (mode == Mode.HISTORY) {
            return buildIssueHistoryURI();
        }

        throw new RuntimeException("This should never happen. URI builder failed");
    }

    private URI buildMultiIssuesURI() {
        StringBuilder jqlBuilder = new StringBuilder();

        if (jql != null) {
            jqlBuilder.append(jql);
            jqlBuilder.append(CONJUNCTION);
        }

        if (dateFilterStatus != null) {
            if (dateFrom != null) {
                jqlBuilder.append(dateFilterStatus);
                jqlBuilder.append(GREATER_EQUAL + dateFormatter.format(dateFrom));
                jqlBuilder.append(CONJUNCTION);
            }

            if (dateTo != null) {
                jqlBuilder.append(dateFilterStatus);
                jqlBuilder.append(LESS_EQUAL + dateFormatter.format(dateTo));
                jqlBuilder.append(CONJUNCTION);
            }
        }

        String uriJQL = null;
        if (jqlBuilder.toString().endsWith(CONJUNCTION)) {
            uriJQL = jqlBuilder.substring(0, jqlBuilder.length() - 5);
        } else {
            uriJQL = jqlBuilder.toString();
        }

        // @formatter:off
        URI result = UriBuilder.fromPath(QUERY_URI_PATH)
                .resolveTemplate("protocol", protocol)
                .resolveTemplate("hostname", hostname)
                .queryParam(FIELDS_PARAM, "*all")
                .queryParam(START_AT, startingIndex)
                .queryParam(MAX_RESULTS, ISSUES_PER_BATCH)
                .queryParam(QUERY_PARAM, uriJQL)
                .build();
        // @formatter:on

        return result;
    }

    public URI buildIssueHistoryURI() {
        
        // @formatter:off
        URI result = UriBuilder.fromPath(ISSUE_HISTORY_URI_PATH)
                .resolveTemplate("protocol", protocol)
                .resolveTemplate("hostname", hostname)
                .resolveTemplate("issueKey", issueKey)
                .queryParam(FIELDS_PARAM, "changelog")
                .queryParam(EXPAND_PARAM, "changelog")
                .build();
        // @formatter:on
        
        return result;
    }

    public SimpleDateFormat getDateFormatter() {
        return (SimpleDateFormat) dateFormatter.clone();
    }

    public URI testHost() {
        return UriBuilder.fromPath(TEST_URI_PATH).resolveTemplate("protocol", getProtocol())
                .resolveTemplate("hostname", hostname).build();
    }

    private String getProtocol() {
        return protocol;
    }

    private void filterInputedHostname() {
        checkForProtocol();
        removeLastSlash();
    }

    private void checkForProtocol() {
        int index = hostname.indexOf("://");

        if (index != -1) {
            protocol = hostname.substring(0, index);
            hostname = hostname.substring(index + 3);
        } else {
            protocol = "https";
        }
    }

    private void removeLastSlash() {
        if (hostname.endsWith("/")) {
            hostname = hostname.substring(0, hostname.length() - 1);
        }
    }

    public String getHostname() {
        return UriBuilder.fromPath(SIMPLE_URI_PATH).resolveTemplate("protocol", getProtocol())
                .resolveTemplate("hostname", hostname).build().toASCIIString();
    }

    public int getStartingIndex() {
        return startingIndex;
    }

    public void setStartingIndex(int startingIndex) {
        this.startingIndex = startingIndex;
    }

    public int getNextStartingIndex() {
        return startingIndex + JiraOnlineAdapterUriBuilder.ISSUES_PER_BATCH;
    }

    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public enum Mode {
        HISTORY, MULTI;
    }
}
