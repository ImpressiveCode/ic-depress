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

import static com.google.common.collect.Lists.newArrayList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.impressivecode.depress.its.jiraonline.model.JiraOnlineFilterListItem;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * 
 * @author Krzysztof Kwoka, Wroclaw University of Technology
 * 
 */
public abstract class JiraOnlineMapperAbstractCustomField {

    protected List<JiraOnlineFilterListItem> fieldList;
    private List<DialogComponent> dialogComponents;
    private HashMap<String, SettingsModelString> mappingMap;

    public JiraOnlineMapperAbstractCustomField(List<JiraOnlineFilterListItem> fieldList) {
        this.fieldList = fieldList;
        this.mappingMap = new HashMap<>();
    }

    public void createDialogComponents() {
        dialogComponents = newArrayList();
        for (JiraOnlineFilterListItem item : fieldList) {
            SettingsModelString sms = new SettingsModelString(getMapperModelString() + "." + item.getName(), "");
            sms.setStringValue(getParserValue(item.getName()));
            DialogComponentStringSelection comp = new DialogComponentStringSelection(sms, formatName(item.getName()),
                    getImplementedMappings());
            comp.setToolTipText(item.getDescription());
            dialogComponents.add(comp);
            mappingMap.put(item.getName(), sms);
        }
    }

    private String formatName(String name) {
        return "Map \"" + name + "\" to: ";
    }

    public List<DialogComponent> getDialogComponents() {
        return dialogComponents == null ? new ArrayList<DialogComponent>() : dialogComponents;
    }

    public HashMap<String, String> getMappingMap() {
        HashMap<String, String> map = new HashMap<>();
        for (String key : mappingMap.keySet()) {
            map.put(key, mappingMap.get(key).getStringValue());
        }
        return map;
    }

    protected abstract String getMapperModelString();

    protected abstract Collection<String> getImplementedMappings();

    protected abstract String getParserValue(String nameToParse);
}
