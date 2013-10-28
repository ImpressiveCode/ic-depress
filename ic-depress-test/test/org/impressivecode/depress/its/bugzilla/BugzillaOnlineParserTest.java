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

import static org.fest.assertions.Assertions.assertThat;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Map;

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSPriority;
import org.impressivecode.depress.its.ITSResolution;
import org.impressivecode.depress.its.ITSStatus;
import org.impressivecode.depress.its.ITSType;
import org.junit.Test;

/**
 * 
 * @author Michał Negacz, Wrocław University of Technology
 * 
 */
public class BugzillaOnlineParserTest {

	public Object getBugSample(String sample) throws Exception {
		FileInputStream fin = new FileInputStream(BugzillaOnlineParserTest.class.getResource(sample).getPath());
		ObjectInputStream ois = new ObjectInputStream(fin);
		Object object = ois.readObject();
		ois.close();
		return object;
	}

	@Test
	public void shouldParseBugInformation() throws Exception {
		// given
		Object bug = ((Object[]) getBugSample("mozillaOnline820167.dat"))[0];
		BugzillaOnlineParser parser = new BugzillaOnlineParser();

		// when
		ITSDataType its = parser.parse(bug);

		// then
		assertThat(its.getIssueId()).isEqualTo("820167");
		assertThat(its.getCreated().toString()).isEqualTo("Mon Dec 10 23:18:00 CET 2012");
		assertThat(its.getUpdated().toString()).isEqualTo("Mon Jul 01 12:56:51 CEST 2013");
		assertThat(its.getStatus()).isEqualTo(ITSStatus.RESOLVED);
		assertThat(its.getType()).isEqualTo(ITSType.BUG);
		assertThat(its.getVersion()).containsOnly("unspecified");
		assertThat(its.getFixVersion()).containsOnly("Firefox 20");
		assertThat(its.getPriority()).isEqualTo(ITSPriority.MINOR);
		assertThat(its.getSummary()).isEqualTo("Enable performance measurement of tab animation");
		assertThat(its.getLink()).isEqualTo("");
		assertThat(its.getResolution()).isEqualTo(ITSResolution.FIXED);
		assertThat(its.getReporter()).isEqualTo("avihpit@yahoo.com");
		assertThat(its.getAssignees()).containsOnly("avihpit@yahoo.com");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldParseBugComments() throws Exception {
		// given
		Map<String, Object> allBugCommentsMap = (Map<String, Object>) getBugSample("mozillaOnlineComments820167.dat");
		Map<String, Object> oneBugCommentsMap = (Map<String, Object>) allBugCommentsMap.get("820167");
		Object[] comments = (Object[]) oneBugCommentsMap.get("comments");
		BugzillaOnlineParser parser = new BugzillaOnlineParser();
		ITSDataType its = new ITSDataType();

		// when
		parser.fillCommentsData(its, comments);

		// then
		assertThat(its.getCommentAuthors()).hasSize(7);
		assertThat(its.getComments()).hasSize(23);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldParseBugHistory() throws Exception {
		// given
		Map<String, Object> history = (Map<String, Object>) ((Object[]) getBugSample("mozillaOnlineHistory820167.dat"))[0];
		BugzillaOnlineParser parser = new BugzillaOnlineParser();
		ITSDataType its = new ITSDataType();
		its.setStatus(ITSStatus.RESOLVED);

		// when
		parser.fillHistoryData(its, history);

		// then
		assertThat(its.getResolved().toString()).isEqualTo("Fri Jan 04 17:51:50 CET 2013");
	}

}
