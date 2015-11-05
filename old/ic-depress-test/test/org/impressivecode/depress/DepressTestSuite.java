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
package org.impressivecode.depress;

import org.impressivecode.depress.data.anonymisation.DataAnonymisationTest;
import org.impressivecode.depress.its.bugzilla.BugzillaEntriesParserTest;
import org.impressivecode.depress.its.bugzillaonline.BugzillaOnlineClientAdapterIntegrationTest;
import org.impressivecode.depress.its.bugzillaonline.BugzillaOnlineParserTest;
import org.impressivecode.depress.its.bugzillaonline.BugzillaOnlineXmlRpcClientIntegrationTest;
import org.impressivecode.depress.its.clearquest.ClearQuestEntriesParserTest;
import org.impressivecode.depress.its.hpqc.HPQCEntriesParserTest;
import org.impressivecode.depress.its.jira.JiraEntriesParserTest;
import org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterParserTest;
import org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterRsClientTest;
import org.impressivecode.depress.mg.ipa.IssuesMetricMetricProcessorTest;
import org.impressivecode.depress.mg.po.PeopleOrganizationMetricProcessorTest;
import org.impressivecode.depress.mr.checkstyle.CheckStyleEntriesParserTest;
import org.impressivecode.depress.mr.checkstyle.CheckStyleEntryTest;
import org.impressivecode.depress.mr.eclipsemetrics.EclipseMetricsEntryTest;
import org.impressivecode.depress.mr.jacoco.JaCoCoEntriesParserTest;
import org.impressivecode.depress.mr.judy.JudyEntriesParserTest;
import org.impressivecode.depress.mr.pmd.PMDEntriesParserTest;
import org.impressivecode.depress.mr.pmd.PMDEntryTest;
import org.impressivecode.depress.scm.git.GitOfflineLogParserTest;
import org.impressivecode.depress.scm.git.GitOnlineLogParserTest;
import org.impressivecode.depress.scm.svn.SVNOfflineParserTest;
import org.impressivecode.depress.scm.svn.SVNOnlineParserTest;
import org.impressivecode.depress.support.sematicanalysis.SimilarityMatcherTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite for basic JUnit tests. Should be run as "JUnit Test". For plugin
 * tests use {@link DepressPluginTestSuite}
 * 
 * @author Marcin Kunert, Wroclaw University of Technology
 * 
 */
@RunWith(Suite.class)
// @formatter:off
@Suite.SuiteClasses({ 
    DataAnonymisationTest.class, 
    BugzillaEntriesParserTest.class,
    BugzillaOnlineClientAdapterIntegrationTest.class,
    BugzillaOnlineParserTest.class,
    BugzillaOnlineXmlRpcClientIntegrationTest.class,
    ClearQuestEntriesParserTest.class,
    HPQCEntriesParserTest.class,
    JiraEntriesParserTest.class,
    JiraOnlineAdapterParserTest.class,
    JiraOnlineAdapterRsClientTest.class,
    IssuesMetricMetricProcessorTest.class,
    PeopleOrganizationMetricProcessorTest.class,
    CheckStyleEntryTest.class,
    CheckStyleEntriesParserTest.class,
    EclipseMetricsEntryTest.class,
    JaCoCoEntriesParserTest.class,
    JudyEntriesParserTest.class,
    PMDEntriesParserTest.class,
    PMDEntryTest.class,
    GitOfflineLogParserTest.class,
    SimilarityMatcherTest.class,
    GitOnlineLogParserTest.class,
    SVNOfflineParserTest.class,
    SVNOnlineParserTest.class,
})
//@formatter:on
public class DepressTestSuite {
    
}
