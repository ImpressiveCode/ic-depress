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
package org.impressivecode.depress.support.activitymatcher;

import org.impressivecode.depress.common.Cells;
import org.impressivecode.depress.its.ITSDataType;
import org.knime.base.data.append.column.AppendedCellFactory;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.date.DateAndTimeCell;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class ActivityMarkerCellFactory implements AppendedCellFactory {

    private final Configuration cfg;
    private final int commitDateCellIndex;

    public ActivityMarkerCellFactory(final Configuration configuration, final int commitDateCellIndex) {
        this.cfg = configuration;
        this.commitDateCellIndex = commitDateCellIndex;
    }

    @Override
    public DataCell[] getAppendedCell(final DataRow row) {
        long commitTimeInMillis = ((DateAndTimeCell) row.getCell(commitDateCellIndex)).getUTCTimeInMillis();

        // naive approach
        final Iterable<ITSDataType> markers = findIssuesFromGivenInterval(commitTimeInMillis);

        return new DataCell[] { Cells.stringListCell(applyBuilder(markers))};
    }

    private Iterable<ITSDataType> findIssuesFromGivenInterval(final long commitTimeInMillis) {
        final long min = commitTimeInMillis - this.cfg.getIntervalInMillis();
        final long max = commitTimeInMillis + this.cfg.getIntervalInMillis();
        return Iterables.filter(this.cfg.getIssues(), new Predicate<ITSDataType>() {
            @Override
            public boolean apply(final ITSDataType issue) {
                return min < issue.getResolved().getTime() && issue.getResolved().getTime() < max;
            }
        });
    }

    private Iterable<String> applyBuilder(final Iterable<ITSDataType> markers) {
        return Iterables.transform(markers, new Function<ITSDataType, String>() {
            @Override
            public String apply(final ITSDataType id) {
                String transformedId = null;
                if (cfg.getBuilderFormat() != null) {
                    transformedId = String.format(cfg.getBuilderFormat(), id.getIssueId());
                } else {
                    transformedId = id.getIssueId();
                }
                return transformedId;
            }
        });
    }
}