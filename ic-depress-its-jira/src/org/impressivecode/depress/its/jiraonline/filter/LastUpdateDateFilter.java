package org.impressivecode.depress.its.jiraonline.filter;

import org.impressivecode.depress.its.ITSTimePeriodFilter;

public class LastUpdateDateFilter extends ITSTimePeriodFilter {
    
    private static final String JIRA_LAST_UPDATE_DATE = "depress.its.jiraonline.filter.last.update.date";

    @Override
    public String getName() {
        return "Last update date";
    }

	@Override
	public String getFilterValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilterModelId() {
		return JIRA_LAST_UPDATE_DATE;
	}
}
