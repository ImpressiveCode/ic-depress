package org.impressivecode.depress.its.jiraonline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.impressivecode.depress.its.ITSPriority;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineFilterListItem;

public class MapperPriority extends MapperAbstractCustomField {

    public MapperPriority(List<JiraOnlineFilterListItem> fieldList) {
        super(fieldList);
    }

    private static final String JIRA_PRIORITY = "depress.its.jiraonline.priorityList";

    @Override
    protected String getMapperModelString() {
        return JIRA_PRIORITY;
    }

    @Override
    protected Collection<String> getImplementedMappings() {
        Collection<String> statuses = new ArrayList<String>();
        for (ITSPriority statusEnum : ITSPriority.values()) {
            statuses.add(statusEnum.toString());
        }
        return statuses;
    }

    @Override
    protected String getParserValue(String priority) {
        return JiraOnlineAdapterParser.parsePriorityFromEnum(priority).toString();
    }
}
