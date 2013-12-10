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
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.GregorianCalendar;
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
 * 
 * @author Marcin Kunert, Wroclaw University of Technology
 * @author Krzysztof Kwoka, Wroclaw University of Technology
 * @author Dawid Rutowicz, Wroclaw University of Technology
 * 
 */
public class JiraOnlineAdapterRsClientIntegrationTest {

    private final String HOSTNAME_HIBERNATE = "hibernate.atlassian.net";
    private final String HOSTNAME_JIRA = "jira.springsource.org";

    private JiraOnlineAdapterRsClient client;

    @Before
    public void setUp() {
        client = new JiraOnlineAdapterRsClient();
        client.setSecuredConnection(true);
        client.getUriBuilder().setHostname(HOSTNAME_HIBERNATE);
    }

    @Test
    public void should_download_and_parse_one_jira_issues() throws Exception {
        String jiraEntry = client.getIssues();

        List<ITSDataType> entries = JiraOnlineAdapterParser.parse(jiraEntry, client.getUriBuilder().getHostname());

        assertThat(entries, is(notNullValue()));
        assertThat(entries.size(), is(equalTo(100)));

        for (ITSDataType entry : entries) {
            assertThat(entry, is(notNullValue()));
            assertThat(entry.getSummary(), is(notNullValue()));
            assertThat(entry.getPriority(), is(notNullValue()));
            assertThat(entry.getType(), is(notNullValue()));
            assertThat(entry.getReporter(), is(notNullValue()));
            assertTrue("Starts with: " + entry.getLink(),
                    entry.getLink().startsWith("https://hibernate.atlassian.net/browse/"));
            assertThat(entry.getResolution(), is(notNullValue()));
        }

    }

    @Test
    public void should_download_parse_and_check_specific_issues() throws Exception {
        // given
        JiraOnlineAdapterUriBuilder builder = new JiraOnlineAdapterUriBuilder();
        builder.setHostname(HOSTNAME_JIRA);
        builder.setDateFilterStatus(JiraOnlineAdapterUriBuilder.DateFilterType.CREATED);

        GregorianCalendar dateFrom = new GregorianCalendar();
        dateFrom.set(2012, 0, 1);
        builder.setDateFrom(dateFrom.getTime());

        GregorianCalendar dateTo = new GregorianCalendar();
        dateTo.set(2012, 0, 2);
        builder.setDateTo(dateTo.getTime());

        client.setUriBuilder(builder);

        // when
        List<ITSDataType> entries = JiraOnlineAdapterParser.parse(client.getIssues(), builder.getHostname());

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
        assertThat(entry.getType(), is(ITSType.ENHANCEMENT));
        assertThat(entry.getVersion().size(), is(0));
        assertThat(entry.getFixVersion().size(), is(1));
        assertThat(entry.getFixVersion().get(0), is("1.0.0.M6"));
        assertThat(entry.getPriority(), is(ITSPriority.MAJOR));
        assertThat(entry.getSummary(),
                is("Improve extendability of RandomValueTokenServices by keeping storage separate from token creation"));
        assertThat(entry.getLink(), is("https://jira.springsource.org/browse/SECOAUTH-179"));
        assertNull(entry.getDescription());
        assertThat(entry.getComments().size(), is(0));
        assertThat(entry.getResolution(), is(ITSResolution.FIXED));
        assertThat(entry.getReporter(), is("david_syer"));
        assertThat(entry.getAssignees().size(), is(1));
        assertThat(entry.getAssignees().contains("david_syer"), is(true));
        assertThat(entry.getComments().size(), is(0));
    }

}
