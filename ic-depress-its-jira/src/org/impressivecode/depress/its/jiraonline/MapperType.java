package org.impressivecode.depress.its.jiraonline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.impressivecode.depress.its.ITSType;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineFilterListItem;

public class MapperType extends MapperAbstractCustomField {

    public MapperType(List<JiraOnlineFilterListItem> fieldList) {
        super(fieldList);
    }

    private static final String JIRA_TYPE = "depress.its.jiraonline.typeList";

    @Override
    protected String getMapperModelString() {
        return JIRA_TYPE;
    }

    @Override
    protected Collection<String> getImplementedMappings() {
        Collection<String> statuses = new ArrayList<String>();
        for (ITSType statusEnum : ITSType.values()) {
            statuses.add(statusEnum.toString());
        }
        return statuses;
    }

    @Override
    protected String getParserValue(String type) {
        return JiraOnlineAdapterParser.parseTypeFromEnum(type).toString();
    }
}
