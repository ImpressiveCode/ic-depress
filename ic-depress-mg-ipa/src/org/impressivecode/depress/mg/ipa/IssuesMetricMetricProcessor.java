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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Map;

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.support.commonmarker.MarkerDataType;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class IssuesMetricMetricProcessor {
    private Map<String, ITSDataType> issues;
    private List<MarkerDataType> changes;
    private Map<String, IssuesMetricType> metricResult;

    public IssuesMetricMetricProcessor(final List<ITSDataType> issues, final List<MarkerDataType> changes) {
        this.issues = convert2Map(checkNotNull(issues, "Issues has to be set"));
        this.changes = checkNotNull(changes, "Changes has to be set");
        this.metricResult = Maps.newHashMap();
    }

    public List<IssuesMetricType> computeMetric() {
        processIntern();
        return Lists.newArrayList(metricResult.values());
    }

    private void processIntern() {
        for (MarkerDataType marker : changes) {
            IssuesMetricType noi = get(marker);
            updateNoI(marker, noi);
        }
    }

    private void updateNoI(final MarkerDataType markers, final IssuesMetricType noi) {
        for (String marker : markers.getAllMarkers()) {
            if (issues.containsKey(marker)) {
                noi.getIssues().add(issues.get(marker.toString()));
            }
        }
    }

    private IssuesMetricType get(final MarkerDataType scm) {
        IssuesMetricType noi = metricResult.get(scm.getResourceName());
        if (noi == null) {
            noi = new IssuesMetricType();
            noi.setResourceName(scm.getResourceName());
            List<ITSDataType> items = newArrayList();
            noi.setIssues(items);
            metricResult.put(scm.getResourceName(), noi);
        }
        return noi;
    }

    private static Map<String, ITSDataType> convert2Map(final List<ITSDataType> listOfIssues) {
        Map<String, ITSDataType> issuesMap = Maps.newHashMap();//WithExpectedSize(250);
        for (ITSDataType its : listOfIssues) {
            issuesMap.put(its.getIssueId().trim(), its);
        }

        return issuesMap;
    }
}
