package org.impressivecode.depress.its.jiraonline.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.impressivecode.depress.its.ITSStatus;
import org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterUriBuilder.Mode;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineFilterListItem;

public class StatusMapperFilter extends CustomFieldMapperFilter {

    public StatusMapperFilter(List<JiraOnlineFilterListItem> fieldList) {
        super(fieldList);
    }

    private static final String JIRA_STATUS = "depress.its.jiraonline.statusList";

    @Override
    protected Collection<String> getImplementedMappings() {
        Collection<String> statuses = new ArrayList<String>();
        for (ITSStatus statusEnum : ITSStatus.values()) {
            statuses.add(statusEnum.toString());
        }
        return statuses;
    }

    @Override
    public String getFilterValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getFilterModelId() {
        return JIRA_STATUS;
    }

    @Override
    protected Mode getURIMode() {
        return Mode.STATE_LIST;
    }
}
