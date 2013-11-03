package org.impressivecode.depress.its.jiraonline;

import static org.junit.Assert.assertTrue;

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
	public void should_download_one_jira_issue() throws Exception {
		String issueId = "HHH-8511";
		String result = client.getIssueById(issueId);

		String id = "\"id\":\"29618\"";
		String summary = "\"summary\":\"\\\"Can't convert fromnull\\\" when inserting clob in Informix\"";
		String priorityBlocker = "\"name\":\"Blocker\"";
		String typeBug = "\"name\":\"Bug\"";
		String reporterName = "\"name\":\"krasig\"";
		String attachement = "\"filename\":\"SQLException.txt\"";

		assertTrue(result.contains(id));
		assertTrue(result.contains(summary));
		assertTrue(result.contains(priorityBlocker));
		assertTrue(result.contains(typeBug));
		assertTrue(result.contains(reporterName));
		assertTrue(result.contains(attachement));

	}

	// TODO: rest of integration tests

}
