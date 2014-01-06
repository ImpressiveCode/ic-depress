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
package org.impressivecode.depress.its.bugzillaonline;

import static org.fest.assertions.Assertions.assertThat;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;
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
	public void shouldParseOneBugInformation() throws Exception {
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
	public void shouldParseOneBugComments() throws Exception {
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
	public void shouldParseOneBugHistory() throws Exception {
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

	@SuppressWarnings("unchecked")
	@Test
	public void shouldParseManyBugs() throws Exception {
		// given
		Object[] details = (Object[]) getBugSample("mozillaOnline545454and115450.dat");
		Map<String, Object> comments = (Map<String, Object>) getBugSample("mozillaOnlineComments545454and115450.dat");
		Object[] histories = (Object[]) getBugSample("mozillaOnlineHistory545454and115450.dat");
		BugzillaOnlineParser parser = new BugzillaOnlineParser();

		// when
		List<ITSDataType> entries = parser.parseEntries(details, histories, comments);

		// then
		assertThat(entries).hasSize(2);
		
		ITSDataType firstEntry = entries.get(0);
		assertThat(firstEntry.getIssueId()).isEqualTo("545454");
		assertThat(firstEntry.getCreated().toString()).isEqualTo("Wed Feb 10 19:20:00 CET 2010");
		assertThat(firstEntry.getUpdated().toString()).isEqualTo("Wed Dec 28 18:40:11 CET 2011");
		assertThat(firstEntry.getStatus()).isEqualTo(ITSStatus.RESOLVED);
		assertThat(firstEntry.getType()).isEqualTo(ITSType.BUG);
		assertThat(firstEntry.getVersion()).containsOnly("Trunk");
		assertThat(firstEntry.getFixVersion()).containsOnly("1.8");
		assertThat(firstEntry.getPriority()).isEqualTo(ITSPriority.MINOR);
		assertThat(firstEntry.getSummary()).isEqualTo("\"More Versions\" topcrash page doesn't load (connection reset after timing out)");
		assertThat(firstEntry.getLink()).isEqualTo("http://crash-stats.mozilla.com/topcrasher/");
		assertThat(firstEntry.getResolution()).isEqualTo(ITSResolution.WONT_FIX);
		assertThat(firstEntry.getReporter()).isEqualTo("dbaron@dbaron.org");
		assertThat(firstEntry.getAssignees()).containsOnly("laura@mozilla.com");
		assertThat(firstEntry.getCommentAuthors()).hasSize(5);
		assertThat(firstEntry.getComments()).hasSize(6);
		assertThat(firstEntry.getResolved().toString()).isEqualTo("Wed Aug 04 18:11:09 CEST 2010");
		assertThat(firstEntry.getDescription()).startsWith("If I go to http://crash-stats.mozilla.com/ , hover the \"Firefox\" menu at the top of the page");
		assertThat(firstEntry.getDescription()).endsWith("it seems like something timed out before the page could actually be displayed.");
		
		ITSDataType secondEntry = entries.get(1);
		assertThat(secondEntry.getIssueId()).isEqualTo("115450");
		assertThat(secondEntry.getCreated().toString()).isEqualTo("Sat Dec 15 23:20:00 CET 2001");
		assertThat(secondEntry.getUpdated().toString()).isEqualTo("Mon Mar 15 14:36:01 CET 2010");
		assertThat(secondEntry.getStatus()).isEqualTo(ITSStatus.RESOLVED);
		assertThat(secondEntry.getType()).isEqualTo(ITSType.BUG);
		assertThat(secondEntry.getVersion()).containsOnly("Trunk");
		assertThat(secondEntry.getFixVersion()).containsOnly("---");
		assertThat(secondEntry.getPriority()).isEqualTo(ITSPriority.MINOR);
		assertThat(secondEntry.getSummary()).isEqualTo("XPCom Plugins should be unloaded when not in use");
		assertThat(secondEntry.getLink()).isEqualTo("http://www.pall.com/investor");
		assertThat(secondEntry.getResolution()).isEqualTo(ITSResolution.INVALID);
		assertThat(secondEntry.getReporter()).isEqualTo("carl@fink.to");
		assertThat(secondEntry.getAssignees()).containsOnly("nobody@mozilla.org");
		assertThat(secondEntry.getCommentAuthors()).hasSize(18);
		assertThat(secondEntry.getComments()).hasSize(25);
		assertThat(secondEntry.getResolved().toString()).isEqualTo("Mon Mar 15 14:36:01 CET 2010");
		assertThat(secondEntry.getDescription()).startsWith("From Bugzilla Helper:");
		assertThat(secondEntry.getDescription()).endsWith("Expected Results:  They should.");
	}
	
}
