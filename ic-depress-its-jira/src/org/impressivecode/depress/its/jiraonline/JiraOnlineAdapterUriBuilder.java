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

import javax.ws.rs.core.UriBuilder;
/**
 * Builder for JIRA REST API
 * 
 * @author Marcin Kunert, Wroclaw University of Technology
 * @author Krzysztof Kwoka, Wroclaw University of Technology
 * @author Maciej Borkowski, Capgemini Poland
 * 
 */
public class JiraOnlineAdapterUriBuilder {
    public static final int ISSUES_PER_BATCH = 50;
    public static final String JIRA_DATE_FORMAT = "yyyy-MM-dd";

    private static final String SIMPLE_URI_PATH = "{protocol}://{hostname}/";
    private static final String BASIC_URI_PATH = "{protocol}://{hostname}/rest/api/latest/{command}";
    private static final String TEST_URI_PATH = "{protocol}://{hostname}/rest/api/latest/serverInfo";
    private static final String QUERY_URI_PATH = "{protocol}://{hostname}/rest/api/latest/search";
    private static final String ISSUE_HISTORY_URI_PATH = "{protocol}://{hostname}/rest/api/latest/issue/{issueKey}";

    private static final String STATE_LIST_PARAM = "status";
    private static final String PRIORITY_LIST_PARAM = "priority";
    private static final String RESOLUTION_LIST_PARAM = "resolution";
    private static final String TYPE_LIST_PARAM = "issuetype";
    private static final String PROJECT_LIST_PARAM = "project";
    private static final String QUERY_PARAM = "jql";
    private static final String FIELDS_PARAM = "fields";
    private static final String EXPAND_PARAM = "expand";
    private static final String CONJUNCTION = " AND ";
    private static final String MAX_RESULTS = "maxResults";
    private static final String START_AT = "startAt";

    private Mode mode = Mode.MULTIPLE_ISSUES;
    private int startingIndex = 0;
    private String issueKey;
    private String projectName;

    private String hostname;
    private String jql;
    private String protocol;
    private boolean isTest;

    public JiraOnlineAdapterUriBuilder setHostname(String hostname) {
        this.hostname = hostname;
        filterInputedHostname();

        return this;
    }

    public JiraOnlineAdapterUriBuilder setJQL(String jql) {
        this.jql = jql;
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

        switch (mode) {
        case MULTIPLE_ISSUES:
            return buildMultiIssuesURI();
        case SINGLE_ISSUE_WITH_HISTORY:
            return buildIssueHistoryURI();
        case PRIORITY_LIST:
            return buildPriorityListURI();
        case RESOLUTION_LIST:
            return buildResolutionListURI();
        case STATE_LIST:
            return buildStateListURI();
        case TYPE_LIST:
            return buildTypeListURI();
        case PROJECT_LIST:
            return buildProjectListURI();
        default:
            throw new RuntimeException("This should never happen. URI builder failed");
        }
    }

    private URI buildMultiIssuesURI() {
        StringBuilder jqlBuilder = new StringBuilder();

        if (jql != null) {
            jqlBuilder.append(jql);
            jqlBuilder.append(CONJUNCTION);
        }

        if (projectName != null) {
            jqlBuilder.append("project = \"" + projectName + "\"");
            jqlBuilder.append(CONJUNCTION);
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
                .resolveTemplateFromEncoded("hostname", hostname)
                .queryParam(FIELDS_PARAM, "*all")
                .queryParam(START_AT, startingIndex)
                .queryParam(MAX_RESULTS, ISSUES_PER_BATCH)
                .queryParam(QUERY_PARAM, uriJQL)
                .build();
        // @formatter:on
        return result;
    }

    private URI buildIssueHistoryURI() {
        // @formatter:off
        URI result = UriBuilder.fromPath(ISSUE_HISTORY_URI_PATH)
                .resolveTemplate("protocol", protocol)
                .resolveTemplateFromEncoded("hostname", hostname)
                .resolveTemplate("issueKey", issueKey)
                .queryParam(FIELDS_PARAM, "changelog")
                .queryParam(EXPAND_PARAM, "changelog")
                .build();
        // @formatter:on

        return result;
    }

    private URI buildListURI(String jiraCommand) {
        // @formatter:off
        URI result = UriBuilder.fromPath(BASIC_URI_PATH)
                .resolveTemplate("protocol", protocol)
                .resolveTemplateFromEncoded("hostname", hostname)
                .resolveTemplate("command", jiraCommand)
                .build();
        // @formatter:on

        return result;
    }

    private URI buildTypeListURI() {
        return buildListURI(TYPE_LIST_PARAM);
    }

    private URI buildStateListURI() {
        return buildListURI(STATE_LIST_PARAM);
    }

    private URI buildResolutionListURI() {
        return buildListURI(RESOLUTION_LIST_PARAM);
    }

    private URI buildPriorityListURI() {
        return buildListURI(PRIORITY_LIST_PARAM);
    }

    private URI buildProjectListURI() {
        return buildListURI(PROJECT_LIST_PARAM);
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void resetMode() {
        this.mode = Mode.MULTIPLE_ISSUES;
    }

    public enum Mode {
        SINGLE_ISSUE_WITH_HISTORY, MULTIPLE_ISSUES, STATE_LIST, PRIORITY_LIST, RESOLUTION_LIST, TYPE_LIST, PROJECT_LIST;
    }
}
