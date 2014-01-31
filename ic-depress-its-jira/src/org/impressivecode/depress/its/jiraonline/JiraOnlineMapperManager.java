/*
 ImpressiveCode Depress Framework
 Copyright (C) 2013  ImpressiveCode contributors

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.impressivecode.depress.its.jiraonline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.impressivecode.depress.its.jiraonline.model.JiraOnlineFilterListItem;
import org.knime.core.node.defaultnodesettings.DialogComponent;

/**
 * 
 * @author Krzysztof Kwoka, Wroclaw University of Technology
 * 
 */
public class JiraOnlineMapperManager {

    private JiraOnlineMapperState mapperState;
    private JiraOnlineMapperPriority mapperPriority;
    private JiraOnlineMapperResolution mapperResolution;
    private JiraOnlineMapperType mapperType;

    private HashMap<String, String> mapperStateMap;
    private HashMap<String, String> mapperPriorityMap;
    private HashMap<String, String> mapperResolutionMap;
    private HashMap<String, String> mapperTypeMap;

    public JiraOnlineMapperManager() {
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
        mapperState = new JiraOnlineMapperState(fieldList);
        mapperState.createDialogComponents();
    }

    public void createMapperPrioryty(List<JiraOnlineFilterListItem> fieldList) {
        mapperPriority = new JiraOnlineMapperPriority(fieldList);
        mapperPriority.createDialogComponents();
    }

    public void createMapperResolution(List<JiraOnlineFilterListItem> fieldList) {
        mapperResolution = new JiraOnlineMapperResolution(fieldList);
        mapperResolution.createDialogComponents();
    }

    public void createMapperType(List<JiraOnlineFilterListItem> fieldList) {
        mapperType = new JiraOnlineMapperType(fieldList);
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
        mapperResolutionMap = mapperResolution == null ? new HashMap<String, String>() : mapperResolution
                .getMappingMap();
    }
}
