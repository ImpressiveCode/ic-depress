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
import java.util.Collection;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingWorker;

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
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.knime.core.node.port.PortObjectSpec;

/**
 * 
 * @author Marcin Kunert, Wroclaw University of Technology
 * @author Krzysztof Kwoka, Wroclaw University of Technology
 * @author Dawid Rutowicz, Wroclaw University of Technology
 * 
 */
public class JiraOnlineAdapterNodeDialog extends ITSNodeDialog {

    private static final String JQL = "JQL:";
    private static final String DOWNLOAD_HISTORY = "Download issue history (this will make the processing A LOT longer)";
    private static final String MAPPING = "Mapping";

    private SettingsModelString hostnameComponent;
    private DialogComponentBoolean history;
    private DialogComponentMultiLineString jql;
    private JPanel mappingTab;

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
        mappingTab = new JPanel();
        // JTabbedPane mapTabs = new JTabbedPane();
        // JScrollPane scrollPane = new JScrollPane();
        // mapTabs.add(MAPPING, scrollPane);
        //
        // mappingTab.add(mapTabs);

        mappingTab.setLayout(new BoxLayout(mappingTab, BoxLayout.Y_AXIS));
        // mappingTab.
        // repaintMappersTab();

        return mappingTab;
    }

    private void repaintMappersTab(JTabbedPane tabs) {
        mappingTab.removeAll();
        mappingTab.add(tabs);
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

    @Override
    protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
        for (DialogComponent component : JiraOnlineAdapterNodeModel.getMapperManager().getDialogComponents()) {
            try {
                component.getModel().saveSettingsTo(settings);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        super.saveSettingsTo(settings);
    }

    class CheckConnectionButtonListener implements ActionListener {
        private static final String TESTING_CONNECTION = "Testing connection...";

        private ConnectionChecker worker;

        @SuppressWarnings("deprecation")
        @Override
        public void actionPerformed(ActionEvent e) {
            worker = new ConnectionChecker();
            checkProjectsLabel.setText(TESTING_CONNECTION);
            checkProjectsButton.setEnabled(false);
            worker.execute();
        }
    }

    class ConnectionChecker extends SwingWorker<JTabbedPane, Void> {
        private static final String CONNECTION_FAILED = "Connection failed.";
        private static final String CONNECTION_OK = "Connection ok, Jira filters updated.";

        private static final String STATE = "State";
        private static final String PRIORITY = "Priority";
        private static final String RESOLUTION = "Resolution";
        private static final String TYPE = "Type";

        private JiraOnlineAdapterUriBuilder builder;
        private JiraOnlineAdapterRsClient client;
        private JiraOnlineMapperManager mm;

        public ConnectionChecker() {
            mm = JiraOnlineAdapterNodeModel.getMapperManager();
            builder = new JiraOnlineAdapterUriBuilder();
            client = new JiraOnlineAdapterRsClient();
        }

        @Override
        protected JTabbedPane doInBackground() throws Exception {
            builder.setHostname(hostnameComponent.getStringValue());

            mm.createMapperState(getMapperList(Mode.STATE_LIST));
            mm.createMapperPrioryty(getMapperList(Mode.PRIORITY_LIST));
            mm.createMapperResolution(getMapperList(Mode.RESOLUTION_LIST));
            mm.createMapperType(getMapperList(Mode.TYPE_LIST));

            return createTabs();
        }

        private List<JiraOnlineFilterListItem> getMapperList(Mode mode) {
            builder.setMode(mode);
            String rawData = null;
            try {
                rawData = client.getJSON(builder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return JiraOnlineAdapterParser.getCustomFieldList(rawData);
        }

        private JTabbedPane createTabs() {
            JTabbedPane mapTabs = new JTabbedPane();
            mapTabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
            mapTabs.add(STATE, createTab(mm.getComponentsMapperState()));
            mapTabs.add(PRIORITY, createTab(mm.getComponentsMapperPriority()));
            mapTabs.add(RESOLUTION, createTab(mm.getComponentsMapperResolution()));
            mapTabs.add(TYPE, createTab(mm.getComponentsMapperType()));
            return mapTabs;
        }

        private Component createTab(List<DialogComponent> componentList) {
            JPanel tabPanel = new JPanel();
            JScrollPane scrollPane = new JScrollPane();
            tabPanel.setLayout(new BoxLayout(tabPanel, BoxLayout.Y_AXIS));
            for (DialogComponent dialogComponent : componentList) {
                tabPanel.add(dialogComponent.getComponentPanel());
            }
            scrollPane.setViewportView(tabPanel);
            return scrollPane;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void done() {
            try {
                JTabbedPane tabs = get();
                repaintMappersTab(tabs);
                checkProjectsLabel.setText(CONNECTION_OK);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                checkProjectsLabel.setText(CONNECTION_FAILED);
            }

            checkProjectsButton.setEnabled(true);
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
    protected SettingsModelInteger createThreadsCountSettings() {
        return JiraOnlineAdapterNodeModel.createSettingsThreadCount();
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
        history.loadSettingsFrom(settings, specs);
        jql.loadSettingsFrom(settings, specs);
    }

    @Override
    protected void saveSpecificSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
        history.saveSettingsTo(settings);
        jql.saveSettingsTo(settings);
    }

}
