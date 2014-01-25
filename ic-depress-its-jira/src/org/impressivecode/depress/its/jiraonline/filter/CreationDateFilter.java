package org.impressivecode.depress.its.jiraonline.filter;

import org.impressivecode.depress.its.ITSTimePeriodFilter;

public class CreationDateFilter extends ITSTimePeriodFilter {

    private static final String JIRA_CREATION_DATE = "depress.its.jiraonline.filter.creation.date";

    @Override
    public String getFilterValue() {
        return "created" + getFrom("dd-mm-yyyy");
    }

    @Override
    public String getName() {
        return "Creation date";
    }

    @Override
    public String getFilterModelId() {
        return JIRA_CREATION_DATE;
    }

}
