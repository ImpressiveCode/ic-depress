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
 * @author Dawid Rutowicz
 * @author Krzysztof Kwoka
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
