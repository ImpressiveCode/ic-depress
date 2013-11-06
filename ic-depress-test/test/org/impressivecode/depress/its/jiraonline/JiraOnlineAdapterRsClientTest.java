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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author Dawid Rutowicz, Wroclaw University of Technology
 * @author Krzysztof Kwoka, Wroclaw University of Technology
 * 
 */

public class JiraOnlineAdapterRsClientTest {

	private JiraOnlineAdapterRsClient jiraOnlineClient;
	private Client mockedRsClient;
	private WebTarget mockedWebTarget;
	private Builder mockedBuilder;
	private Response mockedResponse;
	private final static int HTTP_OK = 200;
	private final static int HTTP_NO_CONNECTION = 404;

	@Before
	public void setUp() {
		prepareMocks();
		prepareJiraTestClient();
	}

	@After
	public void tearDown() {
		verifyMocksCalls();
	}

	@Test
	public void should_not_throw_exception_when_get_issues_by_default_no_secure_protocol()
			throws Exception {
		jiraOnlineClient.setSecuredConnection(false);
		
		when(mockedResponse.getStatus()).thenReturn(HTTP_OK);
		
		jiraOnlineClient.getIssues();
	}

	@Test(expected = Exception.class)
	public void should_throw_exception_when_get_issues_by_default_no_secure_protocol()
			throws Exception {
		jiraOnlineClient.setSecuredConnection(false);

		when(mockedResponse.getStatus()).thenReturn(HTTP_NO_CONNECTION);

		jiraOnlineClient.getIssues();
	}

	@Test
	public void should_not_throw_exception_when_get_issues_by_default_with_secure_protocol()
			throws Exception {
		jiraOnlineClient.setSecuredConnection(true);

		when(mockedResponse.getStatus()).thenReturn(HTTP_OK);

		jiraOnlineClient.getIssues();
	}

	@Test(expected = Exception.class)
	public void should_throw_exception_when_get_get_issues_by_default_with_secure_protocol()
			throws Exception {
		jiraOnlineClient.setSecuredConnection(true);

		when(mockedResponse.getStatus()).thenReturn(HTTP_NO_CONNECTION);

		jiraOnlineClient.getIssues();
	}

	private void prepareMocks() {
		mockedRsClient = Mockito.mock(Client.class);
		mockedWebTarget = Mockito.mock(WebTarget.class);
		mockedBuilder = Mockito.mock(Builder.class);
		mockedResponse = Mockito.mock(Response.class);

		when(mockedRsClient.target(any(URI.class))).thenReturn(mockedWebTarget);
		when(mockedWebTarget.request(anyString())).thenReturn(mockedBuilder);
		when(mockedBuilder.get()).thenReturn(mockedResponse);
		when(mockedResponse.readEntity(String.class)).thenReturn(null);
	}

	private void prepareJiraTestClient() {
		jiraOnlineClient = new JiraOnlineAdapterRsClient();
		jiraOnlineClient.getUriBuilder().setHostname("dummy_hostname");
		jiraOnlineClient.setClient(mockedRsClient);
	}

	private void verifyMocksCalls() {
		verify(mockedRsClient, times(1)).target(any(URI.class));
		verify(mockedWebTarget, times(1)).request(anyString());
		verify(mockedBuilder, times(1)).get();
	}
}