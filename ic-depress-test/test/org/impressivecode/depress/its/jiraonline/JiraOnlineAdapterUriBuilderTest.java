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

import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for {@link JiraOnlineAdapterUriBuilder}
 * 
 * @author Marcin Kunert, Wroclaw University of Technology
 * @author Krzysztof Kwoka, Wroclaw University of Technology
 * @author Dawid Rutowicz, Wroclaw University of Technology
 * 
 */

public class JiraOnlineAdapterUriBuilderTest {
    private JiraOnlineAdapterUriBuilder builder;
    private final String HOSTNAME = "hostname";
    private final String FIELDS = "fields=*all";
    private final String START_AT = "startAt=0";
    private final String MAX_RESULTS = "maxResults=";

    @Before
    public void setUp() throws ParseException {
        builder = new JiraOnlineAdapterUriBuilder();
        builder.setHostname(HOSTNAME);
    }

    @Test
    public void should_have_default_link_pattern() {
        String actualPattern = builder.build().toString();
        String expectedPattern = "https://" + HOSTNAME + "/rest/api/latest/search?" + createLinkPart() + "&jql=";

        assertThat(actualPattern, is(notNullValue()));
        assertThat(actualPattern, is(equalTo(expectedPattern)));
    }

    @Test
    public void shoudCreateLinkWithJqlFilter() throws UnsupportedEncodingException {
        String jql = "labels=metamodel";
        String actual = builder.setJQL(jql).build().toString();
        String expected = "https://" + HOSTNAME + "/rest/api/latest/search?" + createLinkPart()
                + "&jql=labels%3Dmetamodel";

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
        assertThat(result,
                is(equalTo("https://dummyhostname.com/rest/api/latest/search?" + createLinkPart() + "&jql=")));
    }

    @Test
    public void should_create_valid_url_without_ending_slash_given() {
        // given
        JiraOnlineAdapterUriBuilder builder = new JiraOnlineAdapterUriBuilder();
        builder.setHostname("dummyhostname.com");

        // when
        String result = builder.build().toString();

        // then
        assertThat(result,
                is(equalTo("https://dummyhostname.com/rest/api/latest/search?" + createLinkPart() + "&jql=")));
    }

    @Test
    public void shouldDetectUnecuredConnectionProtocol() {
        // given
        JiraOnlineAdapterUriBuilder builder = new JiraOnlineAdapterUriBuilder();
        builder.setHostname("http://dummyhostname.com/");

        // when
        String result = builder.build().toString();

        // then
        assertThat(result, is(equalTo("http://dummyhostname.com/rest/api/latest/search?" + createLinkPart() + "&jql=")));
    }

    @Test
    public void shouldDetectSecuredConnectionProtocol() {
        // given
        JiraOnlineAdapterUriBuilder builder = new JiraOnlineAdapterUriBuilder();
        builder.setHostname("https://dummyhostname.com/");

        // when
        String result = builder.build().toString();

        // then
        assertThat(result,
                is(equalTo("https://dummyhostname.com/rest/api/latest/search?" + createLinkPart() + "&jql=")));
    }

    private String createLinkPart() {
        return FIELDS + "&" + START_AT + "&" + MAX_RESULTS + JiraOnlineAdapterUriBuilder.ISSUES_PER_BATCH;
    }

}
