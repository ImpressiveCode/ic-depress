package org.impressivecode.depress.its.jiraonline.filter;

import org.impressivecode.depress.its.ITSTimePeriodFilter;
import org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterUriBuilder;

public class LastUpdateDateFilter extends ITSTimePeriodFilter {

    private static final String JIRA_LAST_UPDATE_DATE = "depress.its.jiraonline.filter.last.update.date";
    private static final String FIELD_NAME = "updated";

    @Override
    public String getName() {
        return "Last update date";
    }

    @Override
    public String getFilterValue() {
        StringBuilder value = new StringBuilder();

        if (isFromEnabled()) {
            value.append(FIELD_NAME + " >= " + getFrom(JiraOnlineAdapterUriBuilder.JIRA_DATE_FORMAT));
        }

        if (value.length() > 0) {
            value.append("AND");
        }

        if (isToEnabled()) {
            value.append(FIELD_NAME + " <= " + getTo(JiraOnlineAdapterUriBuilder.JIRA_DATE_FORMAT));
        }

        return value.toString();
    }

    @Override
    public String getFilterModelId() {
        return JIRA_LAST_UPDATE_DATE;
    }
}
