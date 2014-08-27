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
import java.util.List;
import java.util.concurrent.Callable;

import org.impressivecode.depress.common.MultiFilterComponent;
import org.impressivecode.depress.common.SettingsModelMultiFilter;
import org.impressivecode.depress.its.ITSPriority;
import org.impressivecode.depress.its.ITSType;
import org.impressivecode.depress.its.ITSResolution;
import org.impressivecode.depress.its.ITSStatus;

/**
 * @author Krzysztof Kwoka, Wroclaw University of Technology
 * @author Maciej Borkowski, Capgemini Poland
 */
public class JiraOnlineMapperManager {
    private SettingsModelMultiFilter priorityModel;
    private SettingsModelMultiFilter statusModel;
    private SettingsModelMultiFilter typeModel;
    private SettingsModelMultiFilter resolutionModel;

    private MultiFilterComponent multiFilterPriority;
    private MultiFilterComponent multiFilterStatus;
    private MultiFilterComponent multiFilterType;
    private MultiFilterComponent multiFilterResolution;

    public JiraOnlineMapperManager() {
        String configName = "depress.its.jiraonline.priority";
        priorityModel = new SettingsModelMultiFilter(configName, false, ITSPriority.labels());
        configName = "depress.its.jiraonline.status";
        statusModel = new SettingsModelMultiFilter(configName, false, ITSStatus.labels());
        configName = "depress.its.jiraonline.type";
        typeModel = new SettingsModelMultiFilter(configName, false, ITSType.labels());
        configName = "depress.its.jiraonline.resolution";
        resolutionModel = new SettingsModelMultiFilter(configName, false, ITSResolution.labels());
    }

    public void createFilterPriority(final Callable<List<String>> refreshCall) {
        multiFilterPriority = new MultiFilterComponent(priorityModel, refreshCall);
    }

    public void createFilterStatus(final Callable<List<String>> refreshCall) {
        multiFilterStatus = new MultiFilterComponent(statusModel, refreshCall);
    }

    public void createFilterType(final Callable<List<String>> refreshCall) {
        multiFilterType = new MultiFilterComponent(typeModel, refreshCall);
    }

    public void createFilterResolution(final Callable<List<String>> refreshCall) {
        multiFilterResolution = new MultiFilterComponent(resolutionModel, refreshCall);
    }

    public List<MultiFilterComponent> getComponents() {
        List<MultiFilterComponent> list = new ArrayList<>();
        list.add(multiFilterPriority);
        list.add(multiFilterStatus);
        list.add(multiFilterType);
        list.add(multiFilterResolution);
        return list;
    }

    public List<SettingsModelMultiFilter> getModels() {
        List<SettingsModelMultiFilter> list = new ArrayList<>();
        list.add(priorityModel);
        list.add(statusModel);
        list.add(typeModel);
        list.add(resolutionModel);
        return list;
    }

    public MultiFilterComponent getMultiFilterPriority() {
        return multiFilterPriority;
    }

    public MultiFilterComponent getMultiFilterStatus() {
        return multiFilterStatus;
    }

    public MultiFilterComponent getMultiFilterType() {
        return multiFilterType;
    }

    public MultiFilterComponent getMultiFilterResolution() {
        return multiFilterResolution;
    }

    public SettingsModelMultiFilter getPriorityModel() {
        return priorityModel;
    }

    public SettingsModelMultiFilter getStatusModel() {
        return statusModel;
    }

    public SettingsModelMultiFilter getTypeModel() {
        return typeModel;
    }

    public SettingsModelMultiFilter getResolutionModel() {
        return resolutionModel;
    }

}
