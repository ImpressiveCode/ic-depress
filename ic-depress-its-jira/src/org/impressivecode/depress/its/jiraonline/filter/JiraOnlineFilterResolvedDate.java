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
package org.impressivecode.depress.its.jiraonline.filter;

import org.impressivecode.depress.its.ITSTimePeriodFilter;
import org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterUriBuilder;

/**
 * Filter for issue resoluton date
 * 
 * @author Marcin Kunert, Wroclaw University of Technology
 * 
 */
public class JiraOnlineFilterResolvedDate extends ITSTimePeriodFilter {

    private static final String JIRA_RESOLUTIN_DATE = "depress.its.jiraonline.filter.resolution.date";
    private static final String FIELD_NAME = "resolved";

    @Override
    public String getName() {
        return "Resolved date";
    }

    @Override
    public String getFilterValue() {
        StringBuilder value = new StringBuilder();

        if (isFromEnabled()) {
            value.append(FIELD_NAME + " >= " + getFrom());
        }

        if (isToEnabled()) {

            if (value.length() > 0) {
                value.append(" AND ");
            }

            value.append(FIELD_NAME + " <= " + getTo());
        }

        return value.toString();
    }

    public String getFrom() {
        return getFrom(JiraOnlineAdapterUriBuilder.JIRA_DATE_FORMAT);
    }

    public String getTo() {
        return getTo(JiraOnlineAdapterUriBuilder.JIRA_DATE_FORMAT);
    }

    @Override
    public String getFilterModelId() {
        return JIRA_RESOLUTIN_DATE;
    }
}
