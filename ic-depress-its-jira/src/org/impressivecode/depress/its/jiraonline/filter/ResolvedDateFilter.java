package org.impressivecode.depress.its.jiraonline.filter;

import org.impressivecode.depress.its.ITSTimePeriodFilter;

public class ResolvedDateFilter extends ITSTimePeriodFilter {

    private static final String JIRA_RESOLUTIN_DATE = "depress.its.jiraonline.filter.resolution.date";
    
    @Override
    public String getName() {
        return "Resolved date";
    }

	@Override
	public String getFilterValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilterModelId() {
		return JIRA_RESOLUTIN_DATE;
	}
}
