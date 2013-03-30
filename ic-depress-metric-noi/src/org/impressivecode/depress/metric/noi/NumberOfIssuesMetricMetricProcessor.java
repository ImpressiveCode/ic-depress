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
package org.impressivecode.depress.metric.noi;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Map;

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.scm.SCMDataType;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class NumberOfIssuesMetricMetricProcessor {
    private Map<String, ITSDataType> issues;
    private List<SCMDataType> changes;
    private Map<String, NoIMetricType> metricResult;

    public NumberOfIssuesMetricMetricProcessor(final List<ITSDataType> issues, final List<SCMDataType> changes) {
        this.issues = convert2Map(checkNotNull(issues, "Issues has to be set"));
        this.changes = checkNotNull(changes, "Changes has to be set");
        this.metricResult = Maps.newHashMapWithExpectedSize(1500);
    }

    public List<NoIMetricType> computeMetric() {
        processIntern();
        return Lists.newArrayList(metricResult.values());
    }

    private void processIntern() {
        for (SCMDataType scm : changes) {
            NoIMetricType noi = get(scm);
            updateNoI(scm, noi);
        }
    }

    private void updateNoI(final SCMDataType scm, final NoIMetricType noi) {
        for (String marker : scm.getMarkers()) {
            if (issues.containsKey(marker)) {
                noi.getIssues().add(issues.get(marker));
            }
        }
    }

    private NoIMetricType get(final SCMDataType scm) {
        NoIMetricType noi = metricResult.get(scm.getResourceName());
        if (noi == null) {
            noi = new NoIMetricType();
            noi.setResourceName(scm.getResourceName());
            List<ITSDataType> items = newArrayList();
            noi.setIssues(items);
            metricResult.put(scm.getResourceName(), noi);
        }
        return noi;
    }

    private static Map<String, ITSDataType> convert2Map(final List<ITSDataType> listOfIssues) {
        Map<String, ITSDataType> issuesMap = Maps.newHashMapWithExpectedSize(250);
        for (ITSDataType its : listOfIssues) {
            issuesMap.put(its.getIssueId(), its);
        }

        return issuesMap;
    }
}
