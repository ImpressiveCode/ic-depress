package org.impressivecode.depress.its.jiraonline.filter;

import org.impressivecode.depress.its.ITSTimePeriodFilter;
import org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterUriBuilder;

public class CreationDateFilter extends ITSTimePeriodFilter {

    private static final String JIRA_CREATION_DATE = "depress.its.jiraonline.filter.creation.date";
    private static final String FIELD_NAME = "created";

    @Override
    public String getName() {
        return "Creation date";
    }
    
    
    @Override
    public String getFilterValue() {
        StringBuilder value = new StringBuilder();

        if (isFromEnabled()) {
            value.append(FIELD_NAME + " >= " + getFrom(JiraOnlineAdapterUriBuilder.JIRA_DATE_FORMAT));
        }

        if (isToEnabled()) {

            if (value.length() > 0) {
                value.append(" AND ");
            }
            
            value.append(FIELD_NAME + " <= " + getTo(JiraOnlineAdapterUriBuilder.JIRA_DATE_FORMAT));
        }

        return value.toString();
    }

    @Override
    public String getFilterModelId() {
        return JIRA_CREATION_DATE;
    }

}
