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
package org.impressivecode.depress.mg.ipa;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.mg.ipa.IssuesMetricMetricProcessor;
import org.impressivecode.depress.mg.ipa.IssuesMetricType;
import org.impressivecode.depress.support.commonmarker.MarkerDataType;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class IssuesMetricMetricProcessorTest {

    @Test
    public void shouldComputeMetricWhenIssues() {
        // given
        List<MarkerDataType> changes = Lists.newArrayList(scm("ClassA", "i1"), scm("ClassA", "i1"), scm("ClassA", "i2"),
                scm("ClassB", "i1"), scm("ClassB", "i3"));
        List<ITSDataType> issues = Lists.newArrayList(its("i1"), its("i2"));
        IssuesMetricMetricProcessor noim = new IssuesMetricMetricProcessor(issues, changes);
        // when
        List<IssuesMetricType> results = noim.computeMetric();
        // then
        assertThat(results).hasSize(2);
        IssuesMetricType noiA = Iterables.find(results, new NoiPredicate("ClassA"));
        assertThat(noiA.getIssues()).onProperty("issueId").containsOnly("i1", "i1", "i2");
        IssuesMetricType noiB = Iterables.find(results, new NoiPredicate("ClassB"));
        assertThat(noiB.getIssues()).onProperty("issueId").containsOnly("i1");
    }

    @Test
    public void shouldComputeMetricWhenNoIssues() {
        // given
        List<MarkerDataType> changes = Lists.newArrayList(scm("ClassA", "i1"));
        IssuesMetricMetricProcessor noim = new IssuesMetricMetricProcessor(
                Collections.<ITSDataType> emptyList(), changes);
        // when
        List<IssuesMetricType> results = noim.computeMetric();
        // then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getResourceName()).isEqualTo("ClassA");
    }

    private MarkerDataType scm(final String resource, final String marker) {
        MarkerDataType scm = new MarkerDataType();
        scm.setResourceName(resource);
        scm.setMarkers(Sets.newHashSet(marker));
        return scm;
    }

    private ITSDataType its(final String id) {
        ITSDataType its = new ITSDataType();
        its.setIssueId(id);
        return its;
    }

    private class NoiPredicate implements Predicate<IssuesMetricType> {

        private String name;

        public NoiPredicate(final String name) {
            this.name = name;
        }

        @Override
        public boolean apply(final IssuesMetricType noi) {
            return noi.getResourceName().equals(name);
        }

    }
}
