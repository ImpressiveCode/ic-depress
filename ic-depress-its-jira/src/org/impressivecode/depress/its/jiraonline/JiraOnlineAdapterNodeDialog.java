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

import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.impressivecode.depress.its.ITSFilter;
import org.impressivecode.depress.its.ITSFiltersDialogComponent;
import org.impressivecode.depress.its.ITSNodeDialog;
import org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterUriBuilder.Mode;
import org.impressivecode.depress.its.jiraonline.filter.CreationDateFilter;
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

    private SettingsModelString hostnameComponent;

    private DialogComponentBoolean history;
    private DialogComponentMultiLineString jql;

    @Override
    protected Component createAdvancedTab() {
        JPanel panel = (JPanel) super.createAdvancedTab();
        history = new DialogComponentBoolean(JiraOnlineAdapterNodeModel.createSettingsHistory(), DOWNLOAD_HISTORY);
        jql = new DialogComponentMultiLineString(JiraOnlineAdapterNodeModel.createSettingsJQL(), JQL, false, 100, 10);

        panel.add(history.getComponentPanel());
        panel.add(jql.getComponentPanel());

        return panel;
    }

    // @Override
    // protected Component createHostnameComponent() {
    // hostnameComponent = createSettingsURL();
    // addDialogComponent(new DialogComponentString(hostnameComponent,
    // JIRA_URL_LABEL, true, 32));
    // }

    @Override
    protected SettingsModelString createURLSettings() {
        return JiraOnlineAdapterNodeModel.createSettingsURL();
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
        for (DialogComponent component : new CreationDateFilter().getDialogComponents()) {
            panel.add(component.getComponentPanel());
        }
    }

    class CheckConnectionButtonListener implements ActionListener {
        private static final String TESTING_CONNECTION = "Testing connection.";

        private ConnectionChecker worker;

        public CheckConnectionButtonListener() {
            worker = new ConnectionChecker();
        }

        @SuppressWarnings("deprecation")
        @Override
        public void actionPerformed(ActionEvent e) {
            checkProjectsLabel.setText(TESTING_CONNECTION);
            checkProjectsButton.setEnabled(false);
            worker.execute();
        }
    }

    class ConnectionChecker extends SwingWorker<Boolean, Void> {
        private static final String CONNECTION_FAILED = "Connection failed.";
        private static final String CONNECTION_OK = "Connection ok, Jira filters updated.";
        private JiraOnlineAdapterUriBuilder builder;
        private JiraOnlineAdapterRsClient client;

        public ConnectionChecker() {
            builder = new JiraOnlineAdapterUriBuilder();
            client = new JiraOnlineAdapterRsClient();
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            builder.setHostname(hostnameComponent.getStringValue());

            MapperManager mm = new MapperManager();
            mm.createStateMapper(getMapperList(Mode.STATE_LIST));
            mm.createPriorytyMapper(getMapperList(Mode.PRIORITY_LIST));
            mm.createResolutionMapper(getMapperList(Mode.RESOLUTION_LIST));
            mm.createTypeMapper(getMapperList(Mode.TYPE_LIST));

            return true;
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

        @SuppressWarnings("deprecation")
        @Override
        public void done() {
            try {
                get();
                checkProjectsLabel.setText(CONNECTION_OK);
            } catch (Exception e) {
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
