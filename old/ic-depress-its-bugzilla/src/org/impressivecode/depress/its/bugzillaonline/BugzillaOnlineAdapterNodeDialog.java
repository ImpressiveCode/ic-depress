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
package org.impressivecode.depress.its.bugzillaonline;

import static com.google.common.collect.Lists.newArrayList;
import static org.impressivecode.depress.its.bugzillaonline.BugzillaOnlineAdapterNodeModel.DEFAULT_COMBOBOX_ANY_VALUE;

import java.awt.Component;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.apache.xmlrpc.XmlRpcException;
import org.impressivecode.depress.its.ITSOnlineNodeDialog;
import org.impressivecode.depress.its.ITSPriority;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponentDate;
import org.knime.core.node.defaultnodesettings.DialogComponentOptionalString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;

/**
 * @author Marek Majchrzak, ImpressiveCode
 * @author Michał‚ Negacz, Wrocław University of Technology
 * @author Piotr Wróblewski, Wrocław University of Technology
 * @author Bartosz Skuza, Wrocław University of Technology
 * @author Maciej Borkowski, Capgemini Poland
 */
public class BugzillaOnlineAdapterNodeDialog extends ITSOnlineNodeDialog {
    public static final String UNKNOWN_ENUM_NAME = "UNKNOWN";
    public static final String DATE_FROM_LABEL = "Date from:";
    public static final String ASSIGNED_TO_LABEL = "Assigned to:";
    public static final String LIMIT_LABEL = "Limit:";
    public static final String OFFSET_LABEL = "Offset:";
    public static final String REPORTER_LABEL = "Reporter:";
    public static final String PRIORITY_LABEL = "Priority:";
    public static final String VERSION_LABEL = "Version:";
    public static final String BUG = "Bug";

    private DialogComponentOptionalString limit;
    private DialogComponentOptionalString offset;
    private DialogComponentDate date;
    private DialogComponentOptionalString assignedTo;
    private DialogComponentOptionalString reporter;
    private DialogComponentOptionalString version;
    private DialogComponentStringSelection priority;

    @Override
    protected Component createAdvancedTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(createAndAddLimitFilter());
        panel.add(createAndAddOffsetFilter());
        panel.add(createAndAddDateFromFilter());
        panel.add(createAndAddAssignedToFilter());
        panel.add(createAndAddReporterFilter());
        panel.add(createAndAddVersionFilter());
        panel.add(createAndAddPriorityFilter());
        return panel;
    }

    @Override
    protected void createMappingManager() {
        mappingManager = BugzillaOnlineAdapterNodeModel.createMapping();
        mappingManager.createFilterPriority(new RefreshCaller(BugzillaOnlineParser.PRIORITY));
        mappingManager.createFilterType(new RefreshCaller(BUG));
        mappingManager.createFilterResolution(new RefreshCaller(BugzillaOnlineParser.RESOLUTION));
        mappingManager.createFilterStatus(new RefreshCaller(BugzillaOnlineParser.STATUS));
    }

    private class RefreshCaller implements Callable<List<String>> {
        private final String propertyName;

        RefreshCaller(final String propertyName) {
            this.propertyName = propertyName;
        }

        @Override
        public List<String> call() throws Exception {
            List<String> properties = new ArrayList<>();
            if (propertyName.equals(BUG)) {
                properties.add(BUG);
            } else {
                BugzillaOnlineClientAdapter adapter = new BugzillaOnlineClientAdapter(
                        ((SettingsModelString) (url.getModel())).getStringValue());
                String login = ((SettingsModelString) loginComponent.getModel()).getStringValue();
                String password = ((SettingsModelString) passwordComponent.getModel()).getStringValue();
                adapter.setCredentials(login, password);
                BugzillaOnlineOptions options = new BugzillaOnlineOptions();
                options.setProductName(((SettingsModelString) (projectSelection.getModel())).getStringValue());
                properties = adapter.listProperties(options, propertyName);
                Set<String> propertiesSet = new HashSet<String>(properties);
                properties = new ArrayList<String>(propertiesSet);
                properties.remove("");
            }
            return properties;
        }
    }

    @Override
    protected void updateProjectsList() {
        try {
            BugzillaOnlineClientAdapter adapter = new BugzillaOnlineClientAdapter(
                    ((SettingsModelString) (url.getModel())).getStringValue());
            String login = ((SettingsModelString) loginComponent.getModel()).getStringValue();
            String password = ((SettingsModelString) passwordComponent.getModel()).getStringValue();
            adapter.setCredentials(login, password);
            List<String> projects = adapter.listProjects();
            projectSelection.replaceListItems(projects, null);
        } catch (MalformedURLException | XmlRpcException e) {
            Logger.getLogger("Error").severe(e.getMessage());
        }
    }

    private Collection<String> prepareEnumValuesToComboBox(Enum<?>[] enums) {
        List<String> strings = newArrayList();

        for (Enum<?> value : enums) {
            if (UNKNOWN_ENUM_NAME.equals(value.name())) {
                strings.add(DEFAULT_COMBOBOX_ANY_VALUE);
            } else {
                strings.add(value.name());
            }
        }

        return strings;
    }

    private Component createAndAddLimitFilter() {
        limit = new DialogComponentOptionalString(BugzillaOnlineAdapterNodeModel.createLimitSettings(), LIMIT_LABEL,
                COMPONENT_WIDTH);
        return limit.getComponentPanel();
    }

    private Component createAndAddOffsetFilter() {
        offset = new DialogComponentOptionalString(BugzillaOnlineAdapterNodeModel.createOffsetSettings(), OFFSET_LABEL,
                COMPONENT_WIDTH);
        return offset.getComponentPanel();
    }

    private Component createAndAddDateFromFilter() {
        date = new DialogComponentDate(BugzillaOnlineAdapterNodeModel.createDateSettings(), DATE_FROM_LABEL);
        return date.getComponentPanel();
    }

    private Component createAndAddAssignedToFilter() {
        assignedTo = new DialogComponentOptionalString(BugzillaOnlineAdapterNodeModel.createAssignedToSettings(),
                ASSIGNED_TO_LABEL);
        return assignedTo.getComponentPanel();
    }

    private Component createAndAddReporterFilter() {
        reporter = new DialogComponentOptionalString(BugzillaOnlineAdapterNodeModel.createReporterSettings(),
                REPORTER_LABEL);
        return reporter.getComponentPanel();
    }

    private Component createAndAddVersionFilter() {
        version = new DialogComponentOptionalString(BugzillaOnlineAdapterNodeModel.createVersionSettings(),
                VERSION_LABEL);
        return version.getComponentPanel();
    }

    private Component createAndAddPriorityFilter() {
        priority = new DialogComponentStringSelection(BugzillaOnlineAdapterNodeModel.createPrioritySettings(),
                PRIORITY_LABEL, prepareEnumValuesToComboBox(ITSPriority.values()));
        return priority.getComponentPanel();
    }

    @Override
    protected void saveSpecificSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
        limit.saveSettingsTo(settings);
        offset.saveSettingsTo(settings);
        date.saveSettingsTo(settings);
        assignedTo.saveSettingsTo(settings);
        reporter.saveSettingsTo(settings);
        version.saveSettingsTo(settings);
        priority.saveSettingsTo(settings);
    }

    @Override
    protected void loadSpecificSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
            throws NotConfigurableException {
        limit.loadSettingsFrom(settings, specs);
        offset.loadSettingsFrom(settings, specs);
        date.loadSettingsFrom(settings, specs);
        assignedTo.loadSettingsFrom(settings, specs);
        reporter.loadSettingsFrom(settings, specs);
        version.loadSettingsFrom(settings, specs);
        priority.loadSettingsFrom(settings, specs);
    }

}
