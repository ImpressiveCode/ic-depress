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
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.impressivecode.depress.its.ITSOnlineNodeDialog;
import org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterUriBuilder.Mode;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineFilterListItem;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineProjectListItem;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentMultiLineString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;

/**
 * @author Marcin Kunert, Wroclaw University of Technology
 * @author Krzysztof Kwoka, Wroclaw University of Technology
 * @author Dawid Rutowicz, Wroclaw University of Technology
 * @author Maciej Borkowski, Capgemini Poland
 */
public class JiraOnlineAdapterNodeDialog extends ITSOnlineNodeDialog {
    private static final String JQL = "JQL:";
    private static final String DOWNLOAD_HISTORY = "Download issue history (this will make the processing A LOT longer)";

    private DialogComponentBoolean history;
    private DialogComponentMultiLineString jql;

    @Override
    protected Component createAdvancedTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        history = new DialogComponentBoolean(JiraOnlineAdapterNodeModel.createSettingsHistory(), DOWNLOAD_HISTORY);
        jql = new DialogComponentMultiLineString(JiraOnlineAdapterNodeModel.createSettingsJQL(), JQL, false, 50, 10);

        panel.add(history.getComponentPanel());
        panel.add(jql.getComponentPanel());
        return panel;
    }

    @Override
    protected void createMappingManager() {
        mappingManager = JiraOnlineAdapterNodeModel.createMapping();
        mappingManager.createFilterPriority(new RefreshCaller(Mode.PRIORITY_LIST));
        mappingManager.createFilterType(new RefreshCaller(Mode.TYPE_LIST));
        mappingManager.createFilterResolution(new RefreshCaller(Mode.RESOLUTION_LIST));
        mappingManager.createFilterStatus(new RefreshCaller(Mode.STATE_LIST));
    }

    private class RefreshCaller implements Callable<List<String>> {
        private final Mode mode;

        RefreshCaller(final Mode mode) {
            this.mode = mode;
        }

        @Override
        public List<String> call() throws Exception {
            List<String> list = new ArrayList<>();
            List<JiraOnlineFilterListItem> items = getList(mode, JiraOnlineFilterListItem.class);
            for (JiraOnlineFilterListItem item : items) {
                list.add(item.getName());
            }
            return list;
        }
    }

    private <T> List<T> getList(Mode mode, Class<?> elem) throws Exception {
        JiraOnlineAdapterUriBuilder builder = new JiraOnlineAdapterUriBuilder();
        String urlString = ((SettingsModelString) (url.getModel())).getStringValue();
        builder.setHostname(urlString);
        builder.setMode(mode);
        JiraOnlineAdapterRsClient client = new JiraOnlineAdapterRsClient();
        URI uri = builder.build();
        String login = ((SettingsModelString) (loginComponent.getModel())).getStringValue();
        String password = ((SettingsModelString) (passwordComponent.getModel())).getStringValue();
        String rawData = null;

        rawData = client.getJSON(uri, login, password);
        return JiraOnlineAdapterParser.getCustomList(rawData, elem);
    }

    @Override
    protected void updateProjectsList() {
        ArrayList<String> projects = new ArrayList<String>();
        List<JiraOnlineProjectListItem> list;
        try {
            list = getList(Mode.PROJECT_LIST, JiraOnlineProjectListItem.class);
            projectSelection.getModel().setEnabled(true);
            for (JiraOnlineProjectListItem item : list) {
                projects.add(item.getName());
            }
            projectSelection.replaceListItems(projects, null);
        } catch (Exception e) {
            Logger.getLogger("Error").severe("Error during connection, list could not be downloaded");
        }
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
