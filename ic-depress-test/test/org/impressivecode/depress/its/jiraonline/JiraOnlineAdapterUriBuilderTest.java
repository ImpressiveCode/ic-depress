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
import static org.junit.Assert.assertThat;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.EndsWith;

/**
 * @author Marcin Kunert, Wroclaw University of Technology
 * @author Krzysztof Kwoka, Wroclaw University of Technology
 * @author Dawid Rutowicz, Wroclaw University of Technology
 * 
 */

public class JiraOnlineAdapterUriBuilderTest {
    private JiraOnlineAdapterUriBuilder builder;
    private Date startDate;
    private Date endDate;
    private final String HOSTNAME = "hostname";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Before
    public void setUp() throws ParseException {
        builder = new JiraOnlineAdapterUriBuilder();
        builder.setHostname(HOSTNAME);
        startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2013-12-01");
        endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2013-12-31");
    }

    @Test
    public void should_have_jira_date_format_pattern() {
        String actualPattern = builder.getDateFormatter().toPattern();
        String expectedPattern = "yyyy-MM-dd";

        assertThat(actualPattern, is(notNullValue()));
        assertThat(actualPattern, is(equalTo(expectedPattern)));
    }

    @Test
    public void should_have_default_link_pattern() {
        String actualPattern = builder.build().toString();
        String expectedPattern = "https://" + HOSTNAME + "/rest/api/latest/search?jql=";

        assertThat(actualPattern, is(notNullValue()));
        assertThat(actualPattern, is(equalTo(expectedPattern)));
    }

    @Test
    public void should_create_link_with_JQL_filter() throws UnsupportedEncodingException {
        String jql = "labels=metamodel";
        String actual = builder.setJQL(jql).build().toString();
        String expected = "https://" + HOSTNAME + "/rest/api/latest/search?jql=labels%3Dmetamodel";

        assertThat(actual, is(notNullValue()));
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void should_create_start_and_end_dates_with_status_created_filter() {
        String actual = builder.setDateFrom(startDate).setDateTo(endDate)
                .setDateFilterStatus(JiraOnlineAdapterUriBuilder.CREATED).build().toString();
        String expected = "https://" + HOSTNAME + "/rest/api/latest/search?jql="
                + createBothDatesStatusCreatedExpectedFilterResult();

        assertThat(actual, is(notNullValue()));
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void should_create_start_and_end_dates_with_status_resolution_filter() {
        String actual = builder.setDateFrom(startDate).setDateTo(endDate)
                .setDateFilterStatus(JiraOnlineAdapterUriBuilder.RESOLUTION_DATE).build().toString();
        String expected = "https://" + HOSTNAME + "/rest/api/latest/search?jql="
                + createBothDatesStatusResolutionExpectedFilterResult();

        assertThat(actual, is(notNullValue()));
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void should_create_start_only_date_with_status_created_filter() {
        String actual = builder.setDateFrom(startDate).setDateFilterStatus(JiraOnlineAdapterUriBuilder.CREATED).build()
                .toString();
        String expected = "https://" + HOSTNAME + "/rest/api/latest/search?jql="
                + createStartDateOnlyStatusCreatedExpectedFilterResult();

        assertThat(actual, is(notNullValue()));
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void should_create_start_only_date_with_status_resolution_filter() {
        String actual = builder.setDateFrom(startDate).setDateFilterStatus(JiraOnlineAdapterUriBuilder.RESOLUTION_DATE)
                .build().toString();
        String expected = "https://" + HOSTNAME + "/rest/api/latest/search?jql="
                + createStartDateOnlyStatusResolutionExpectedFilterResult();

        assertThat(actual, is(notNullValue()));
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void should_create_end_only_date_with_status_created_filter() {
        String actual = builder.setDateTo(endDate).setDateFilterStatus(JiraOnlineAdapterUriBuilder.CREATED).build()
                .toString();
        String expected = "https://" + HOSTNAME + "/rest/api/latest/search?jql="
                + createEndDateOnlyStatusCreatedExpectedFilterResult();

        assertThat(actual, is(notNullValue()));
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void should_create_end_only_date_with_status_resolution_filter() {
        String actual = builder.setDateTo(endDate).setDateFilterStatus(JiraOnlineAdapterUriBuilder.RESOLUTION_DATE)
                .build().toString();
        String expected = "https://" + HOSTNAME + "/rest/api/latest/search?jql="
                + createEndDateOnlyStatusResolutionExpectedFilterResult();

        assertThat(actual, is(notNullValue()));
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void should_create_valid_url_with_ending_slash_given() {
        // given
        JiraOnlineAdapterUriBuilder builder = new JiraOnlineAdapterUriBuilder();
        builder.setHostname("dummyhostname.com/");

        // when
        String result = builder.build().toString();

        // then
        assertThat(result, is(equalTo("https://dummyhostname.com/rest/api/latest/search?jql=")));
    }

    @Test
    public void should_create_valid_url_without_ending_slash_given() {
        // given
        JiraOnlineAdapterUriBuilder builder = new JiraOnlineAdapterUriBuilder();
        builder.setHostname("dummyhostname.com");

        // when
        String result = builder.build().toString();

        // then
        assertThat(result, is(equalTo("https://dummyhostname.com/rest/api/latest/search?jql=")));
    }

    @Test
    public void should_detect_unsecured_connection_protocol() {
        // given
        JiraOnlineAdapterUriBuilder builder = new JiraOnlineAdapterUriBuilder();
        builder.setHostname("http://dummyhostname.com/");

        // when
        String result = builder.build().toString();

        // then
        assertThat(result, is(equalTo("http://dummyhostname.com/rest/api/latest/search?jql=")));
    }

    @Test
    public void should_detect_secured_connection_protocol() {
        // given
        JiraOnlineAdapterUriBuilder builder = new JiraOnlineAdapterUriBuilder();
        builder.setHostname("https://dummyhostname.com/");

        // when
        String result = builder.build().toString();

        // then
        assertThat(result, is(equalTo("https://dummyhostname.com/rest/api/latest/search?jql=")));
    }

    private String createBothDatesStatusCreatedExpectedFilterResult() {
        StringBuilder expectedResult = new StringBuilder();
        expectedResult.append(createStartDateOnlyStatusCreatedExpectedFilterResult());
        expectedResult.append("+AND+");
        expectedResult.append(createEndDateOnlyStatusCreatedExpectedFilterResult());

        return expectedResult.toString();
    }

    private String createBothDatesStatusResolutionExpectedFilterResult() {
        StringBuilder expectedResult = new StringBuilder();
        expectedResult.append(createStartDateOnlyStatusResolutionExpectedFilterResult());
        expectedResult.append("+AND+");
        expectedResult.append(createEndDateOnlyStatusResolutionExpectedFilterResult());

        return expectedResult.toString();
    }

    private String createStartDateOnlyStatusCreatedExpectedFilterResult() {
        StringBuilder expectedResult = new StringBuilder("created+%3E%3D+");
        expectedResult.append(dateFormat.format(startDate));
        return expectedResult.toString();
    }

    private String createStartDateOnlyStatusResolutionExpectedFilterResult() {
        StringBuilder expectedResult = new StringBuilder("resolutiondate+%3E%3D+");
        expectedResult.append(dateFormat.format(startDate));

        return expectedResult.toString();
    }

    private String createEndDateOnlyStatusCreatedExpectedFilterResult() {
        StringBuilder expectedResult = new StringBuilder("created+%3C%3D+");
        expectedResult.append(dateFormat.format(endDate));

        return expectedResult.toString();
    }

    private String createEndDateOnlyStatusResolutionExpectedFilterResult() {
        StringBuilder expectedResult = new StringBuilder("resolutiondate+%3C%3D+");
        expectedResult.append(dateFormat.format(endDate));

        return expectedResult.toString();
    }
}
