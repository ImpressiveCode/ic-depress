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
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.impressivecode.depress.its.ITSDataType;
import org.junit.Before;
import org.junit.Test;

/**
 * Integration test for RESTful Client and JQL filters.<br />
 * Integration with Hibernate Jira hosted on: https://hibernate.atlassian.net/
 * 
 * @author Dawid Rutowicz, Wroclaw University of Technology
 * @author Krzysztof Kwoka, Wroclaw University of Technology
 * 
 */
public class JiraOnlineAdapterRsClientIntegrationTest {

	private final String HOSTNAME = "hibernate.atlassian.net";

	private JiraOnlineAdapterRsClient client;

	@Before
	public void setUp() {
		client = new JiraOnlineAdapterRsClient();
		client.setSecuredConnection(true);
		client.getUriBuilder().setHostname(HOSTNAME);
	}

	@Test
	public void should_download_and_parse_one_jira_issues() throws Exception {
		String jiraEntry = client.getIssues();

		List<ITSDataType> entries = JiraOnlineParser.parse(jiraEntry);

		assertThat(entries, is(notNullValue()));
		assertThat(entries.size(), is(equalTo(50)));

		for (ITSDataType entry : entries) {
			assertThat(entry, is(notNullValue()));
			
			assertThat(entry.getSummary(), is(notNullValue()));

			assertThat(entry.getPriority(), is(notNullValue()));

			assertThat(entry.getType(), is(notNullValue()));
			
			assertThat(entry.getReporter(), is(notNullValue()));

			assertTrue(entry.getLink().startsWith("https://hibernate.atlassian.net/rest/api/"));
			
			assertThat(entry.getResolution(), is(notNullValue()));
		}

	}

}
