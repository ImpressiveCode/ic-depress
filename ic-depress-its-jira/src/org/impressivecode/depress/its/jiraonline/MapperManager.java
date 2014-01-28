package org.impressivecode.depress.its.jiraonline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.impressivecode.depress.its.jiraonline.model.JiraOnlineFilterListItem;
import org.knime.core.node.defaultnodesettings.DialogComponent;

public class MapperManager {

    private MapperState mapperState;
    private MapperPriority mapperPriority;
    private MapperResolution mapperResolution;
    private MapperType mapperType;

    private HashMap<String, String> mapperStateMap;
    private HashMap<String, String> mapperPriorityMap;
    private HashMap<String, String> mapperResolutionMap;
    private HashMap<String, String> mapperTypeMap;
    
    public MapperManager() {
        refreshMaps();
    }

    public List<DialogComponent> getDialogComponents() {
        ArrayList<DialogComponent> componentList = new ArrayList<DialogComponent>();
        componentList.addAll(getComponentsMapperState());
        componentList.addAll(getComponentsMapperPriority());
        componentList.addAll(getComponentsMapperResolution());
        componentList.addAll(getComponentsMapperType());
        return componentList;
    }

    public void createMapperState(List<JiraOnlineFilterListItem> fieldList) {
        mapperState = new MapperState(fieldList);
        mapperState.createDialogComponents();
    }

    public void createMapperPrioryty(List<JiraOnlineFilterListItem> fieldList) {
        mapperPriority = new MapperPriority(fieldList);
        mapperPriority.createDialogComponents();
    }

    public void createMapperResolution(List<JiraOnlineFilterListItem> fieldList) {
        mapperResolution = new MapperResolution(fieldList);
        mapperResolution.createDialogComponents();
    }

    public void createMapperType(List<JiraOnlineFilterListItem> fieldList) {
        mapperType = new MapperType(fieldList);
        mapperType.createDialogComponents();
    }

    public List<DialogComponent> getComponentsMapperState() {
        return mapperState == null ? new ArrayList<DialogComponent>() : mapperState.getDialogComponents();
    }

    public List<DialogComponent> getComponentsMapperPriority() {
        return mapperPriority == null ? new ArrayList<DialogComponent>() : mapperPriority.getDialogComponents();
    }

    public List<DialogComponent> getComponentsMapperResolution() {
        return mapperResolution == null ? new ArrayList<DialogComponent>() : mapperResolution.getDialogComponents();
    }

    public List<DialogComponent> getComponentsMapperType() {
        return mapperType == null ? new ArrayList<DialogComponent>() : mapperType.getDialogComponents();
    }

    public HashMap<String, String> getMapMapperState() {
        return mapperStateMap;
    }

    public HashMap<String, String> getMapMapperPriority() {
        return mapperPriorityMap;
    }

    public HashMap<String, String> getMapMapperResolution() {
        return mapperResolutionMap;
    }

    public HashMap<String, String> getMapMapperType() {
        return mapperTypeMap;
    }

    public void refreshMaps() {
        mapperStateMap = mapperState == null ? new HashMap<String, String>() : mapperState.getMappingMap();
        mapperPriorityMap = mapperPriority == null ? new HashMap<String, String>() : mapperPriority.getMappingMap();
        mapperTypeMap = mapperType == null ? new HashMap<String, String>() : mapperType.getMappingMap();
        mapperResolutionMap = mapperResolution == null ? new HashMap<String, String>() : mapperResolution.getMappingMap();
    }
}
