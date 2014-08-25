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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.impressivecode.depress.common.MultiFilterComponent;
import org.impressivecode.depress.its.ITSFilter;
import org.impressivecode.depress.its.ITSNodeDialog;
import org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterUriBuilder.Mode;
import org.impressivecode.depress.its.jiraonline.filter.JiraOnlineFilterCreationDate;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineFilterListItem;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentMultiLineString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.knime.core.node.port.PortObjectSpec;

/**
 * 
 * @author Marcin Kunert, Wroclaw University of Technology
 * @author Krzysztof Kwoka, Wroclaw University of Technology
 * @author Dawid Rutowicz, Wroclaw University of Technology
 * @author Maciej Borkowski, Capgemini Poland
 * 
 */
public class JiraOnlineAdapterNodeDialog extends ITSNodeDialog {
    private static final String JQL = "JQL:";
    private static final String DOWNLOAD_HISTORY = "Download issue history (this will make the processing A LOT longer)";
    private static final String MAPPING = "Mapping";
    
    private static final String STATUS = "Status";
    private static final String PRIORITY = "Priority";
    private static final String RESOLUTION = "Resolution";
    private static final String TYPE = "Type";

    private SettingsModelString hostnameComponent;
    private DialogComponentBoolean history;
    private DialogComponentMultiLineString jql;
    private JTabbedPane mappingTab;
    
    private JiraOnlineMapperManager mapperManager = new JiraOnlineMapperManager();
    
    public JiraOnlineAdapterNodeDialog() {
        super();
        addTab(MAPPING, createMappersTab());
    }

    @Override
    protected Component createAdvancedTab() {
        JPanel panel = (JPanel) super.createAdvancedTab();
        history = new DialogComponentBoolean(JiraOnlineAdapterNodeModel.createSettingsHistory(), DOWNLOAD_HISTORY);
        jql = new DialogComponentMultiLineString(JiraOnlineAdapterNodeModel.createSettingsJQL(), JQL, false, 100, 10);

        panel.add(history.getComponentPanel());
        panel.add(jql.getComponentPanel());

        return panel;
    }

    private Component createMappersTab() {
        mappingTab = new JTabbedPane();
        mapperManager.createFilterPriority(new refreshPriorityCaller());
        mapperManager.createFilterType(new refreshTypeCaller());
        mapperManager.createFilterResolution(new refreshResolutionCaller());
        mapperManager.createFilterStatus(new refreshStatusCaller());

        mappingTab.addTab(PRIORITY, mapperManager.getMultiFilterPriority().getPanel());
        mappingTab.addTab(TYPE, mapperManager.getMultiFilterType().getPanel());
        mappingTab.addTab(RESOLUTION, mapperManager.getMultiFilterResolution().getPanel());
        mappingTab.addTab(STATUS, mapperManager.getMultiFilterStatus().getPanel());
        return mappingTab;
    }
    
    private class refreshPriorityCaller implements Callable<List<String>> {
        @Override
        public List<String> call() throws Exception {
            List<String> list = new ArrayList<>();
            for(JiraOnlineFilterListItem item : getMapperList(Mode.PRIORITY_LIST)) {
                list.add(item.getName());
            }
            return list;
        }
    }
    
    private class refreshTypeCaller implements Callable<List<String>> {
        @Override
        public List<String> call() throws Exception {
            List<String> list = new ArrayList<>();
            for(JiraOnlineFilterListItem item : getMapperList(Mode.TYPE_LIST)) {
                list.add(item.getName());
            }
            return list;
        }
    }
    
    private class refreshResolutionCaller implements Callable<List<String>> {
        @Override
        public List<String> call() throws Exception {
            List<String> list = new ArrayList<>();
            for(JiraOnlineFilterListItem item : getMapperList(Mode.RESOLUTION_LIST)) {
                list.add(item.getName());
            }
            return list;
        }
    }
    
    private class refreshStatusCaller implements Callable<List<String>> {
        @Override
        public List<String> call() throws Exception {
            List<String> list = new ArrayList<>();
            for(JiraOnlineFilterListItem item : getMapperList(Mode.STATE_LIST)) {
                list.add(item.getName());
            }
            return list;
        }
    }
    
    private List<JiraOnlineFilterListItem> getMapperList(Mode mode) {
        JiraOnlineAdapterUriBuilder  builder = new JiraOnlineAdapterUriBuilder();
        builder.setHostname(hostnameComponent.getStringValue());
        builder.setMode(mode);
        JiraOnlineAdapterRsClient client = new JiraOnlineAdapterRsClient();
        String rawData = null;
        try {
            rawData = client.getJSON(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JiraOnlineAdapterParser.getCustomFieldList(rawData);
    }
    
    
    @Override
    protected SettingsModelString createURLSettings() {
        hostnameComponent = JiraOnlineAdapterNodeModel.createSettingsURL();
        return hostnameComponent;
    }

    @Override
    protected SettingsModelString createProjectSettings() {
        return new SettingsModelString("createProjectSettings", "");
    }

    @Override
    protected ActionListener getButtonConnectionCheckListener() {
        return new CheckConnectionButtonListener();
    }

    @Override
    protected void addLargestFilter(JPanel panel) {
        for (DialogComponent component : new JiraOnlineFilterCreationDate().getDialogComponents()) {
            panel.add(component.getComponentPanel());
        }
    }

    class CheckConnectionButtonListener implements ActionListener {
        private static final String TESTING_CONNECTION = "Testing connection...";

        @Override
        public void actionPerformed(ActionEvent e) {
            checkProjectsLabel.setText(TESTING_CONNECTION);
            checkProjectsButton.getModel().setEnabled(false);
            checkProjectsButton.getModel().setEnabled(true);
        }
    }

    @Override
    protected SettingsModelString createLoginSettings() {
        return JiraOnlineAdapterNodeModel.createSettingsLogin();
    }

    @Override
    protected SettingsModelString createPasswordSettings() {
        return JiraOnlineAdapterNodeModel.createSettingsPass();
    }

    @Override
    protected SettingsModelStringArray createFilterSettings() {
        return JiraOnlineAdapterNodeModel.createSettingsFilters();
    }

    @Override
    protected Collection<ITSFilter> getFilters() {
        return JiraOnlineAdapterNodeModel.getFilters();
    }

    @Override
    protected void loadSpecificSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
            throws NotConfigurableException {
        for (MultiFilterComponent component : mapperManager.getComponents()) {
            component.loadSettingsFrom(settings, specs);
        }
        history.loadSettingsFrom(settings, specs);
        jql.loadSettingsFrom(settings, specs);
    }

    @Override
    protected void saveSpecificSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
        for (MultiFilterComponent component : mapperManager.getComponents()) {
            component.saveSettingsTo(settings);
        }
        history.saveSettingsTo(settings);
        jql.saveSettingsTo(settings);
    }

}
