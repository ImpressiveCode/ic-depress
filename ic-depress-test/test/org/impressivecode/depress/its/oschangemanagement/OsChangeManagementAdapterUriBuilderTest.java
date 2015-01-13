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

package org.impressivecode.depress.its.oschangemanagement;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.text.ParseException;

import org.impressivecode.depress.its.oschangemanagement.builder.OsChangeManagementJiraRationalAdapterUriBuilder;
import org.impressivecode.depress.its.oschangemanagement.builder.OsChangeManagementUriBuilder.Mode;
import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for Jira Rational Adapter URI Builder
 * 
 * @author Marcin Cho³uj, Wroclaw University of Technology
 * @author Piotr Malek, Wroclaw University of Technology
 * @author Przemys³aw Trepka, Wroclaw University of Technology
 * @author £ukasz Trojak, Wroclaw University of Technology
 * 
 */

public class OsChangeManagementAdapterUriBuilderTest {
	
	private OsChangeManagementJiraRationalAdapterUriBuilder builder;
	private final String HOSTNAME = "www.hostname.com";

	@Before
	public void setUp() throws ParseException {
		builder = new OsChangeManagementJiraRationalAdapterUriBuilder();
		builder.setHostname(HOSTNAME);
	}

	@Test
	public void should_have_default_mode_set() {
		
		String actualPattern = builder.build().toString();
		String expectedPattern = "https://" + HOSTNAME + "/rest/oslc/latest/projects";
				
		assertThat(actualPattern, is(notNullValue()));
		assertThat(actualPattern, is(equalTo(expectedPattern)));
	}
	
	@Test
	public void should_create_valid_url_with_ending_slash_given() {
		
		//given
		builder.setHostname(HOSTNAME + "/");
		String expectedUri = "https://" + HOSTNAME + "/rest/oslc/latest/projects";
		
		// when
		String result = builder.build().toString();

		// then
		assertThat(result,is(equalTo(expectedUri)));
	}
	
	@Test
	public void should_create_valid_url_without_ending_slash_given() {
		
		// given
		builder.setHostname(HOSTNAME);
		String expectedUri = "https://" + HOSTNAME + "/rest/oslc/latest/projects";

		// when
		String result = builder.build().toString();

		// then
		assertThat(result,is(equalTo(expectedUri)));
	}

	@Test
	public void should_handle_unecured_connection_protocol() {
		
		// given
		builder.setHostname("http://" + HOSTNAME);
		String expectedUri = "http://" + HOSTNAME + "/rest/oslc/latest/projects";

		// when
		String result = builder.build().toString();

		// then
		assertThat(result,is(equalTo(expectedUri)));
	}
	
	@Test
	public void should_handle_secured_connection_protocol() {
		
		// given
		builder.setHostname("https://" + HOSTNAME);
		String expectedUri = "https://" + HOSTNAME + "/rest/oslc/latest/projects";

		// when
		String result = builder.build().toString();

		// then
		assertThat(result,is(equalTo(expectedUri)));
	}

	@Test(expected=RuntimeException.class)
	public void should_throw_exception_given_null_mode() {
		
		// given
		builder.setMode(null);

		// when
		String result = builder.build().toString();
	}

	@Test(expected=RuntimeException.class)
	public void should_throw_exception_given_null_hostname() {
		
		// given
		builder.setMode(null);

		// when
		String result = builder.build().toString();
	}
	
	@Test
	public void reset_should_set_mode_to_default_value() {
		
		//given
		builder = new OsChangeManagementJiraRationalAdapterUriBuilder();
		builder.setHostname(HOSTNAME);
		String expectedUri = "https://" + HOSTNAME + "/rest/oslc/latest/projects";
		
		//when
		builder.setMode(Mode.CHANGE_REQUEST);
		builder.resetMode();
		String result = builder.build().toString();

		// then
		assertThat(result,is(equalTo(expectedUri)));		
	}
	
}