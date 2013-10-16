package org.impressivecode.depress.its.jira;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class JiraOnlineConnectorTest {
	
	private static final String EXISTING_JIRA_URL = "https://jira.springsource.org/";
	private static final String NON_EXISTING_JIRA_URL = "https://hereisnojira.com/";
	
	private JiraOnlineConnector mConnector;
	
	@Before
	public void init() {
		mConnector = new JiraOnlineConnector();
	}

	@Test
	public void shouldConnectToExternalSystem() {
		assertThat(mConnector.connect(EXISTING_JIRA_URL)).isNotNull();
	}
	
	@Test
	public void shouldNotConnectToNonExistingSystem() {
		assertThat(mConnector.connect(NON_EXISTING_JIRA_URL)).isNull();
	}
	
	@Test
	public void shouldMapToITSDatatype() {
		assertThat(false);
	}

}
