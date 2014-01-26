package org.impressivecode.depress.its.jiraonline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.impressivecode.depress.its.ITSResolution;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineFilterListItem;

public class MapperResolution extends MapperAbstractCustomField {

    public MapperResolution(List<JiraOnlineFilterListItem> fieldList) {
        super(fieldList);
    }

    private static final String JIRA_RESOLUTION = "depress.its.jiraonline.resolutionList";

    @Override
    protected String getMapperModelString() {
        return JIRA_RESOLUTION;
    }

    @Override
    protected Collection<String> getImplementedMappings() {
        Collection<String> statuses = new ArrayList<String>();
        for (ITSResolution statusEnum : ITSResolution.values()) {
            statuses.add(statusEnum.toString());
        }
        return statuses;
    }

    @Override
    protected String getParserValue(String resolution) {
        return JiraOnlineAdapterParser.parseResolution(resolution).toString();
    }
}
