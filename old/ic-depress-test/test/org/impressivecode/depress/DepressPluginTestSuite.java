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

import org.impressivecode.depress.its.ITSAdapterTableFactoryTest;
import org.impressivecode.depress.its.ITSAdapterTransformerTest;
import org.impressivecode.depress.its.ITSInputTransformerTest;
import org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterRsClientIntegrationTest;
import org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterUriBuilderTest;
import org.impressivecode.depress.mg.ipa.IssuesMetricOutputTransformerTest;
import org.impressivecode.depress.mg.ipa.IssuesMetricTableFactoryTest;
import org.impressivecode.depress.mr.checkstyle.CheckStyleTransformerTest;
import org.impressivecode.depress.mr.eclipsemetrics.EclipseMetricsEntriesParserTest;
import org.impressivecode.depress.mr.eclipsemetrics.EclipseMetricsTransformerTest;
import org.impressivecode.depress.mr.jacoco.JaCoCoAdapterTransformerTest;
import org.impressivecode.depress.mr.judy.JudyAdapterTableFactoryPluginTest;
import org.impressivecode.depress.mr.pmd.PMDTransformerTest;
import org.impressivecode.depress.scm.SCMAdapterTableFactoryTest;
import org.impressivecode.depress.scm.SCMAdapterTransformerTest;
import org.impressivecode.depress.scm.SCMInputTransformerTest;
import org.impressivecode.depress.support.extmarkerparser.ExtMarkerCellFactoryTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite for plugin JUnit tests. Should be run as "JUnit Plug-in Test"
 * 
 * @author Marcin Kunert, Wroclaw University of Technology. For basic JUnits
 *         tests use {@link DepressTestSuite}
 */
@RunWith(Suite.class)
// @formatter:off
@Suite.SuiteClasses({ 
    ITSAdapterTableFactoryTest.class, 
    ITSAdapterTransformerTest.class,
    ITSInputTransformerTest.class ,
    IssuesMetricOutputTransformerTest.class,
    IssuesMetricTableFactoryTest.class,
    EclipseMetricsEntriesParserTest.class,
    EclipseMetricsTransformerTest.class,
    JiraOnlineAdapterRsClientIntegrationTest.class,
    JiraOnlineAdapterUriBuilderTest.class,
    CheckStyleTransformerTest.class,
    JaCoCoAdapterTransformerTest.class,
    JudyAdapterTableFactoryPluginTest.class,
    PMDTransformerTest.class,
    SCMAdapterTableFactoryTest.class,
    SCMAdapterTransformerTest.class,
    SCMInputTransformerTest.class,
    ExtMarkerCellFactoryTest.class,
})
//@formatter:on
public class DepressPluginTestSuite {

}
