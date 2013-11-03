package org.impressivecode.depress.its.jiraonline;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSPriority;
import org.impressivecode.depress.its.ITSResolution;
import org.impressivecode.depress.its.ITSType;
import org.junit.Before;
import org.junit.Test;

/**
 * Integration test for RESTful Client and JQL filters.<br />
 * Integration with Hibernate Jira hosted on: https://hibernate.atlassian.net/
 * 
 * @author Dawid
 * 
 */
public class JiraOnlineAdapterRsClientIntegrationTest {

	private final String HOSTNAME = "hibernate.atlassian.net";

	private JiraOnlineAdapterRsClient client;

	@Before
	public void setUp() {
		client = new JiraOnlineAdapterRsClient();
		client.setHostname(HOSTNAME);
		client.setSecuredConnection(true);
	}

	@Test
	public void should_download_and_parse_one_jira_issue() throws Exception {
		String issueId = "HHH-8511";
		String jiraEntry = wrapOneIssueToJSONArray(client.getIssueById(issueId));

		List<ITSDataType> entries = JiraOnlineParser.parse(jiraEntry);

		assertThat(entries, is(notNullValue()));
		assertThat(entries.size(), is(equalTo(1)));

		ITSDataType entry = entries.get(0);
		assertThat(entry, is(notNullValue()));

		String expectedId = "29618";
		assertThat(entry.getIssueId(), is(equalTo(expectedId)));

		String expectedSummary = "\"Can't convert fromnull\" when inserting clob in Informix";
		assertThat(entry.getSummary(), is(equalTo(expectedSummary)));

		ITSPriority expectedPriority = ITSPriority.BLOCKER;
		assertThat(entry.getPriority(), is(equalTo(expectedPriority)));

		ITSType expectedType = ITSType.BUG;

		assertThat(entry.getType(), is(equalTo(expectedType)));
		
		String expectedReporter = "krasig";
		assertThat(entry.getReporter(), is(equalTo(expectedReporter)));

		String expectedLink = "https://hibernate.atlassian.net/rest/api/2/issue/29618";
		assertThat(entry.getLink(), is(equalTo(expectedLink)));
		
		ITSResolution expectedResolution = ITSResolution.UNKNOWN;
		assertThat(entry.getResolution(), is(equalTo(expectedResolution)));

	}

	private String wrapOneIssueToJSONArray(String jiraEntry) {
		String pre = "{\"expand\":\"schema,names\",\"startAt\":0,\"maxResults\":50,\"total\":1,\"issues\":[";
		String post = "]}";

		return pre + jiraEntry + post;

	}

	// TODO: rest of integration tests

}
