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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSPriority;
import org.impressivecode.depress.its.ITSResolution;
import org.impressivecode.depress.its.ITSStatus;
import org.impressivecode.depress.its.ITSType;
import org.junit.Before;
import org.junit.Test;

/**
 * Integration test for RESTful Client and JQL filters.<br />
 * Integration with Hibernate Jira hosted on: https://hibernate.atlassian.net/
 * and Spring Jira hosted on https://jira.springsource.org/
 * 
 * @author Marcin Kunert, Wroclaw University of Technology
 * @author Krzysztof Kwoka, Wroclaw University of Technology
 * @author Dawid Rutowicz, Wroclaw University of Technology
 * @author Maciej Borkowski, Capgemini Poland
 * 
 */
public class JiraOnlineAdapterRsClientIntegrationTest {

    private final String HOSTNAME_HIBERNATE = "hibernate.atlassian.net";
    private final String HOSTNAME_SPRING = "jira.spring.io";

    private JiraOnlineAdapterRsClient client;
    private JiraOnlineAdapterUriBuilder hibernateUriBuilder;
    private JiraOnlineAdapterUriBuilder springUriBuilder;

    @Before
    public void setUp() throws URISyntaxException {
        setupClient();
        setupBuilders();
    }

    @Test
    public void shouldDownloadParseAndCheckSpecificIssue() throws Exception {
        // given
        String jql = "created > 2012-1-1 and created < 2012-1-2";

        springUriBuilder.setJQL(jql);

        HashMap<String, String[]> priority = new HashMap<>();
        priority.put("Major", new String[] { "Major" });
        HashMap<String, String[]> type = new HashMap<>();
        HashMap<String, String[]> resolution = new HashMap<>();
        HashMap<String, String[]> status = new HashMap<>();
        status.put("Resolved", new String[] { "Resolved" });
        JiraOnlineAdapterParser parser = new JiraOnlineAdapterParser(priority, type, resolution, status);

        // when
        List<ITSDataType> entries = parser.parseSingleIssueBatch(client.getJSON(springUriBuilder.build(), "", ""),
                springUriBuilder.getHostname());

        // then
        assertThat(entries.size(), is(2));

        ITSDataType entry = entries.get(0);

        assertThat(entry.getIssueId(), is("SECOAUTH-179"));

        GregorianCalendar calendar = new GregorianCalendar();

        calendar.setTime(entry.getCreated());
        assertThat(calendar.get(GregorianCalendar.DAY_OF_MONTH), is(1));
        assertThat(calendar.get(GregorianCalendar.MONTH), is(0));
        assertThat(calendar.get(GregorianCalendar.YEAR), is(2012));

        calendar.setTime(entry.getUpdated());
        assertThat(calendar.get(GregorianCalendar.DAY_OF_MONTH), is(17));
        assertThat(calendar.get(GregorianCalendar.MONTH), is(0));
        assertThat(calendar.get(GregorianCalendar.YEAR), is(2012));

        calendar.setTime(entry.getResolved());
        assertThat(calendar.get(GregorianCalendar.DAY_OF_MONTH), is(17));
        assertThat(calendar.get(GregorianCalendar.MONTH), is(0));
        assertThat(calendar.get(GregorianCalendar.YEAR), is(2012));

        assertThat(entry.getStatus(), is(ITSStatus.RESOLVED));
        assertThat(entry.getType(), is(ITSType.UNKNOWN));
        assertThat(entry.getVersion().size(), is(0));
        assertThat(entry.getFixVersion().size(), is(1));
        assertThat(entry.getFixVersion().get(0), is("1.0.0.M6"));
        assertThat(entry.getPriority(), is(ITSPriority.MAJOR));
        assertThat(entry.getSummary(),
                is("Improve extendability of RandomValueTokenServices by keeping storage separate from token creation"));
        assertThat(entry.getLink(), is("https://jira.spring.io/browse/SECOAUTH-179"));
        assertNull(entry.getDescription());
        assertThat(entry.getComments().size(), is(0));
        assertThat(entry.getResolution(), is(ITSResolution.UNKNOWN));
        assertThat(entry.getReporter(), is("david_syer"));
        assertThat(entry.getAssignees().size(), is(1));
        assertThat(entry.getAssignees().contains("david_syer"), is(true));
        assertThat(entry.getComments().size(), is(0));
    }

    private void setupClient() {
        client = new JiraOnlineAdapterRsClient();
    }

    private void setupBuilders() {
        hibernateUriBuilder = new JiraOnlineAdapterUriBuilder();
        springUriBuilder = new JiraOnlineAdapterUriBuilder();

        hibernateUriBuilder.setHostname(HOSTNAME_HIBERNATE);
        springUriBuilder.setHostname(HOSTNAME_SPRING);
    }

}
