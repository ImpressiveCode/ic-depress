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

import static com.google.common.collect.Maps.newHashMap;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSPriority;
import org.impressivecode.depress.its.ITSResolution;
import org.impressivecode.depress.its.ITSStatus;
import org.impressivecode.depress.its.ITSType;
import org.junit.Test;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.NodeProgressMonitor;

/**
 * 
 * @author Michał Negacz, Wrocław University of Technology
 * 
 */
public class BugzillaOnlineClientAdapterIntegrationTest {

    @Test
    public void shouldFetchBugsFromFirefoxProduct() throws Exception {
        // given
        ExecutionMonitor monitor = mock(ExecutionMonitor.class);
        BugzillaOnlineClientAdapter clientAdapter = new BugzillaOnlineClientAdapter(
                "https://bugzilla.mozilla.org/xmlrpc.cgi", monitor);
        Map<String, Object> parameters = newHashMap();
        parameters.put(BugzillaOnlineClientAdapter.PRODUCT_NAME, "Firefox");
        parameters.put(BugzillaOnlineClientAdapter.LIMIT, 10);

        // when
        Object[] result = clientAdapter.searchBugs(parameters);

        // then
        assertNotNull(result);
        assertThat(result).isNotEmpty();
    }

    @Test
    public void shouldFetchBugsDetailFromFirefoxWithBugId820167() throws Exception {
        // given
        ExecutionMonitor monitor = mock(ExecutionMonitor.class);
        BugzillaOnlineClientAdapter clientAdapter = new BugzillaOnlineClientAdapter(
                "https://bugzilla.mozilla.org/xmlrpc.cgi", monitor);
        Map<String, Object> parameters = newHashMap();
        parameters.put(BugzillaOnlineClientAdapter.IDS, new String[] { "820167" });

        // when
        Object[] result = clientAdapter.getBugs(parameters);

        // then
        assertNotNull(result);
        assertThat(result).isNotEmpty();
    }

    @Test
    public void shouldFetchBugsCommentFromFirefoxWithBugId820167() throws Exception {
        // given
        ExecutionMonitor monitor = mock(ExecutionMonitor.class);
        BugzillaOnlineClientAdapter clientAdapter = new BugzillaOnlineClientAdapter(
                "https://bugzilla.mozilla.org/xmlrpc.cgi", monitor);
        Map<String, Object> parameters = newHashMap();
        parameters.put(BugzillaOnlineClientAdapter.IDS, new String[] { "820167" });

        // when
        Map<String, Object> result = clientAdapter.getBugsComments(parameters);

        // then
        assertNotNull(result);
        assertThat(result.get("820167")).isNotNull();
    }

    @Test
    public void shouldFetchBugsHistoryFromFirefoxWithBugId820167() throws Exception {
        // given
        ExecutionMonitor monitor = mock(ExecutionMonitor.class);
        BugzillaOnlineClientAdapter clientAdapter = new BugzillaOnlineClientAdapter(
                "https://bugzilla.mozilla.org/xmlrpc.cgi", monitor);
        Map<String, Object> parameters = newHashMap();
        parameters.put(BugzillaOnlineClientAdapter.IDS, new String[] { "820167" });

        // when
        Object[] result = clientAdapter.getBugsHistory(parameters);

        // then
        assertNotNull(result);
        assertThat(result).isNotEmpty();
    }

    @Test
    public void shouldFetchAndParseBugFromFirefoxWithBugCreationDateFilter() throws Exception {
        // given
        NodeProgressMonitor nodeProgressMonitor = mock(NodeProgressMonitor.class);
        ExecutionMonitor executionMonitor = new ExecutionMonitor(nodeProgressMonitor);
        BugzillaOnlineClientAdapter clientAdapter = new BugzillaOnlineClientAdapter(
                "https://bugzilla.mozilla.org/xmlrpc.cgi", executionMonitor);
        BugzillaOnlineOptions options = new BugzillaOnlineOptions();
        options.setProductName("Socorro");
        options.setDateFrom(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse("10-02-2010 19:19:59"));
        options.setLimit(1);

        // when
        List<ITSDataType> entries = clientAdapter.listEntries(options);

        // then
        assertThat(entries).isNotEmpty();
        ITSDataType firstEntry = entries.get(0);
        assertThat(firstEntry.getIssueId()).isEqualTo("545454");
        assertThat(firstEntry.getCreated().toString()).isEqualTo("Wed Feb 10 19:20:56 CET 2010");
        assertThat(firstEntry.getUpdated().toString()).isEqualTo("Wed Dec 28 18:40:11 CET 2011");
        assertThat(firstEntry.getStatus()).isEqualTo(ITSStatus.RESOLVED);
        assertThat(firstEntry.getType()).isEqualTo(ITSType.BUG);
        assertThat(firstEntry.getVersion()).containsOnly("Trunk");
        assertThat(firstEntry.getFixVersion()).containsOnly("1.8");
        assertThat(firstEntry.getPriority()).isEqualTo(ITSPriority.MINOR);
        assertThat(firstEntry.getSummary()).isEqualTo(
                "\"More Versions\" topcrash page doesn't load (connection reset after timing out)");
        assertThat(firstEntry.getLink()).isEqualTo("http://crash-stats.mozilla.com/topcrasher/");
        assertThat(firstEntry.getResolution()).isEqualTo(ITSResolution.WONT_FIX);
        assertThat(firstEntry.getReporter()).isEqualTo("dbaron@dbaron.org");
        assertThat(firstEntry.getAssignees()).containsOnly("laura@mozilla.com");
        assertThat(firstEntry.getCommentAuthors()).hasSize(5);
        assertThat(firstEntry.getComments()).hasSize(6);
        assertThat(firstEntry.getResolved().toString()).isEqualTo("Wed Aug 04 18:11:09 CEST 2010");
        assertThat(firstEntry.getDescription()).startsWith(
                "If I go to http://crash-stats.mozilla.com/ , hover the \"Firefox\" menu at the top of the page");
        assertThat(firstEntry.getDescription()).endsWith(
                "it seems like something timed out before the page could actually be displayed.");
    }

}
