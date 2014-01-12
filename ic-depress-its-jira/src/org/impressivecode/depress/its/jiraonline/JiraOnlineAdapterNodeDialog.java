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

import static org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterNodeModel.createSettingsDateEnd;
import static org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterNodeModel.createSettingsDateFilterStatusChooser;
import static org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterNodeModel.createSettingsDateStart;
import static org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterNodeModel.createSettingsHistory;
import static org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterNodeModel.createSettingsJQL;
import static org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterNodeModel.createSettingsLogin;
import static org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterNodeModel.createSettingsPass;
import static org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterNodeModel.createSettingsThreadCount;
import static org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterNodeModel.createSettingsURL;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingWorker;

import org.knime.core.node.FlowVariableModel;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentButton;
import org.knime.core.node.defaultnodesettings.DialogComponentDate;
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;
import org.knime.core.node.defaultnodesettings.DialogComponentMultiLineString;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.DialogComponentPasswordField;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * 
 * @author Marcin Kunert, Wroclaw University of Technology
 * @author Krzysztof Kwoka, Wroclaw University of Technology
 * @author Dawid Rutowicz, Wroclaw University of Technology
 * 
 */
public class JiraOnlineAdapterNodeDialog extends DefaultNodeSettingsPane {

    private static final String CONNECTION = "Connection";
    private static final String JIRA_URL_LABEL = "Jira URL: ";
    private static final String BUTTON_CEHCK = "Check";
    private static final String FILTERS = "Filters";
    private static final String LOGIN_DATA = "Login data";
    private static final String ADVANCED = "Advanced";
    private static final String PASSWORD = "Password: ";
    private static final String LOGIN = "Login: ";
    private static final String JQL = "JQL:";
    private static final String DATE_TO = "Date to:";
    private static final String DATE_FROM = "Date from:";
    private static final String STATUS = "Status:";
    private static final String NOT_TESTED_YET = "Not tested yet...";
    private static final String THREAD_COUNT_LABEL = "Thread count";
    private static final String DOWNLOAD_HISTORY = "Download issue history (this will make the processing A LOT longer)";
    private static final int DEFAULT_FIELD_WIDTH = 32;

    private final String[] DATE_FILTER_STATUSES = new String[] { "Created", "Resolution" };

    private DialogComponentLabel connectionTestLabel;
    private DialogComponentButton checkConnectionButton;
    private SettingsModelString hostnameComponent;
    private ActionListener checkConnectionButtonListener;

    protected JiraOnlineAdapterNodeDialog() {
        initConnectionTab();
        initLoginDataTab();
    }

    private void initConnectionTab() {
        createConnectionGroup();
        createFiltersGroup();
        createAdvancedGroup();
    }

    private void initLoginDataTab() {
        createNewTab(LOGIN_DATA);
        createNewGroup(LOGIN_DATA);
        addDialogComponent(new DialogComponentString(createSettingsLogin(), LOGIN, false, 32));
        addDialogComponent(new DialogComponentPasswordField(createSettingsPass(), PASSWORD, 32));
    }

    private void createConnectionGroup() {
        createNewGroup(CONNECTION);
        createHostnameComponent();
        createCheckConnectionButton();
        createTestConnectionLabel();
    }

    private void createAdvancedGroup() {
        createNewGroup(ADVANCED);
        addDialogComponent(new DialogComponentNumberEdit(createSettingsThreadCount(), THREAD_COUNT_LABEL, DEFAULT_FIELD_WIDTH));
        addDialogComponent(new DialogComponentBoolean(createSettingsHistory(), DOWNLOAD_HISTORY));
        addDialogComponent(new DialogComponentMultiLineString(createSettingsJQL(), JQL, false, 100, 10));
    }

    private void createHostnameComponent() {
        hostnameComponent = createSettingsURL();
        addDialogComponent(new DialogComponentString(hostnameComponent, JIRA_URL_LABEL, true, 32));
    }

    private void createCheckConnectionButton() {
        checkConnectionButton = new DialogComponentButton(BUTTON_CEHCK);
        checkConnectionButtonListener = new CheckConnectionButtonListener();
        checkConnectionButton.addActionListener(checkConnectionButtonListener);

        addDialogComponent(checkConnectionButton);
    }

    private void createTestConnectionLabel() {
        connectionTestLabel = new DialogComponentLabel(NOT_TESTED_YET);
        addDialogComponent(connectionTestLabel);
    }

    private void createFiltersGroup() {
        createNewGroup(FILTERS);
        addDialogComponent(new DialogComponentStringSelection(createSettingsDateFilterStatusChooser(), STATUS,
                DATE_FILTER_STATUSES));
        addDialogComponent(new DialogComponentDate(createSettingsDateStart(), DATE_FROM, true));
        addDialogComponent(new DialogComponentDate(createSettingsDateEnd(), DATE_TO, true));
    }

    class CheckConnectionButtonListener implements ActionListener {
        private static final String TESTING_CONNECTION = "Testing connection...";

        private ConnectionTestWorker worker;

        public CheckConnectionButtonListener() {
            worker = new ConnectionTestWorker();
        }

        @SuppressWarnings("deprecation")
        @Override
        public void actionPerformed(ActionEvent e) {
            connectionTestLabel.setText(TESTING_CONNECTION);
            checkConnectionButton.setEnabled(false);
            worker.execute();
        }
    }

    class ConnectionTestWorker extends SwingWorker<Boolean, Void> {
        private static final String CONNECTION_FAILED = "Connection failed";
        private static final String CONNECTION_OK = "Connection ok";

        @Override
        protected Boolean doInBackground() throws Exception {
            JiraOnlineAdapterRsClient client = new JiraOnlineAdapterRsClient();

            JiraOnlineAdapterUriBuilder testBuilder = new JiraOnlineAdapterUriBuilder();
            testBuilder.setHostname(hostnameComponent.getStringValue()).setIsTest(true);

            return client.testConnection(testBuilder.build());
        }

        @SuppressWarnings("deprecation")
        @Override
        public void done() {
            try {
                get();
                connectionTestLabel.setText(CONNECTION_OK);
            } catch (Exception e) {
                connectionTestLabel.setText(CONNECTION_FAILED);
            }

            checkConnectionButton.setEnabled(true);
        }

    }

}
