package org.impressivecode.depress.its.jiraonline;

import java.util.ArrayList;
import java.util.List;

import org.impressivecode.depress.its.jiraonline.model.JiraOnlineFilterListItem;
import org.knime.core.node.defaultnodesettings.DialogComponent;

public class MapperManager {

    private MapperState mapperState;

    // private MapperPriority mapperPriority;
    // private MapperResolution mapperResolution;
    // private MapperType mapperType;

    public List<DialogComponent> getDialogComponents() {
        ArrayList<DialogComponent> componentList = new ArrayList<DialogComponent>();
        componentList.addAll(getComponentsMapperState());
        return componentList;
    }

    public void createMapperState(List<JiraOnlineFilterListItem> fieldList) {
        mapperState = new MapperState(fieldList);
        mapperState.createDialogComponents();
    }

    public void createMapperPrioryty(List<JiraOnlineFilterListItem> fieldList) {

    }

    public void createMapperResolution(List<JiraOnlineFilterListItem> fieldList) {

    }

    public void createMapperType(List<JiraOnlineFilterListItem> fieldList) {

    }

    public List<DialogComponent> getComponentsMapperState() {
        return mapperState == null ? new ArrayList<DialogComponent>() : mapperState.getDialogComponents();
    }

    public List<DialogComponent> getComponentsMapperPriority() {
        return null;
        // return mapperPriority.createDialogComponents();
    }

    public List<DialogComponent> getComponentsMapperResolution() {
        return null;
        // return mapperResolution.createDialogComponents();
    }

    public List<DialogComponent> getComponentsMapperType() {
        return null;
        // return mapperType.createDialogComponents();
    }
}
