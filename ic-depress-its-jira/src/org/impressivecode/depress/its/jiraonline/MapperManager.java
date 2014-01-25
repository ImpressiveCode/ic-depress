package org.impressivecode.depress.its.jiraonline;

import java.util.ArrayList;
import java.util.List;

import org.impressivecode.depress.its.jiraonline.model.JiraOnlineFilterListItem;
import org.knime.core.node.defaultnodesettings.DialogComponent;

public class MapperManager {

    private List<MapperAbstractCustomField> mapperList;
    private JiraOnlineAdapterUriBuilder uriBuilder;
    
    public List<DialogComponent> getDialogComponents() {
        ArrayList<DialogComponent> componentList = new ArrayList<DialogComponent>();
        return componentList;
    }

    public void createStateMapper(List<JiraOnlineFilterListItem> fieldList) {
        if (uriBuilder != null) {
            mapperList.add(new MapperState(fieldList));
        }
    }

    public void createPriorytyMapper(List<JiraOnlineFilterListItem> fieldList) {

    }

    public void createResolutionMapper(List<JiraOnlineFilterListItem> fieldList) {

    }

    public void createTypeMapper(List<JiraOnlineFilterListItem> fieldList) {

    }
}
