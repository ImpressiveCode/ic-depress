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
package org.impressivecode.depress.its.bugzilla;

import static com.google.common.collect.Maps.newHashMap;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;

/**
 * 
 * @author Michał Negacz, Wrocław University of Technology
 * 
 */
public class BugzillaOnlineClientAdapterIntegrationTest {

	@Test
	public void shouldFetchBugsFromFirefoxProduct() throws Exception {
		// given
		BugzillaOnlineClientAdapter clientAdapter = new BugzillaOnlineClientAdapter("https://bugzilla.mozilla.org/xmlrpc.cgi");
		Map<String, Object> parameters = newHashMap();
		parameters.put(BugzillaOnlineClientAdapter.PRODUCT_NAME, "Firefox");
		Integer offset = 0;
		Integer limit = 10;

		// when
		Object[] result = clientAdapter.searchBugs(parameters, offset, limit);

		// then
		assertNotNull(result);
		assertThat(result).isNotEmpty();
	}

	@Test
	public void shouldFetchBugsDetailFromFirefoxWithBugId820167() throws Exception {
		// given
		BugzillaOnlineClientAdapter clientAdapter = new BugzillaOnlineClientAdapter("https://bugzilla.mozilla.org/xmlrpc.cgi");
		Map<String, Object> parameters = newHashMap();
		parameters.put(BugzillaOnlineClientAdapter.IDS, new String[] {"820167"});

		// when
		Object[] result = clientAdapter.getBugs(parameters);

		// then
		assertNotNull(result);
		assertThat(result).isNotEmpty();
	}
	
	@Test
	public void shouldFetchBugsCommentFromFirefoxWithBugId820167() throws Exception {
		// given
		BugzillaOnlineClientAdapter clientAdapter = new BugzillaOnlineClientAdapter("https://bugzilla.mozilla.org/xmlrpc.cgi");
		Map<String, Object> parameters = newHashMap();
		parameters.put(BugzillaOnlineClientAdapter.IDS, new String[] {"820167"});

		// when
		Map<String, Object> result = clientAdapter.getBugsComments(parameters);

		// then
		assertNotNull(result);
		assertThat(result.get("820167")).isNotNull();
	}

	@Test
	public void shouldFetchBugsHistoryFromFirefoxWithBugId820167() throws Exception {
		// given
		BugzillaOnlineClientAdapter clientAdapter = new BugzillaOnlineClientAdapter("https://bugzilla.mozilla.org/xmlrpc.cgi");
		Map<String, Object> parameters = newHashMap();
		parameters.put(BugzillaOnlineClientAdapter.IDS, new String[] {"820167"});

		// when
		Object[] result = clientAdapter.getBugsHistory(parameters);

		// then
		assertNotNull(result);
		assertThat(result).isNotEmpty();
	}
	
}
