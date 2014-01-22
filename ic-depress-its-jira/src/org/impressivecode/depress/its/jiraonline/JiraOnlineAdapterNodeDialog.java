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
import static org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterNodeModel.getFilters;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingWorker;

import org.impressivecode.depress.its.ITSFilter;
import org.impressivecode.depress.its.ITSFiltersDialogComponent;
import org.impressivecode.depress.its.ITSNodeDialog;
import org.impressivecode.depress.its.jiraonline.filter.CreationDateFilter;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentButton;
import org.knime.core.node.defaultnodesettings.DialogComponentDate;
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;
import org.knime.core.node.defaultnodesettings.DialogComponentMultiLineString;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.DialogComponentPasswordField;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;

/**
 * 
 * @author Marcin Kunert, Wroclaw University of Technology
 * @author Krzysztof Kwoka, Wroclaw University of Technology
 * @author Dawid Rutowicz, Wroclaw University of Technology
 * 
 */
public class JiraOnlineAdapterNodeDialog extends ITSNodeDialog {

    private static final String CONNECTION = "Connection";
    private static final String JIRA_URL_LABEL = "Jira URL: ";
    private static final String BUTTON_CEHCK = "Check";
    private static final String FILTERS = "Filters";
    private static final String JQL = "JQL:";
    private static final String DATE_TO = "Date to:";
    private static final String DATE_FROM = "Date from:";
    private static final String STATUS = "Status:";
    private static final String NOT_TESTED_YET = "Not tested yet...";
    private static final String DOWNLOAD_HISTORY = "Download issue history (this will make the processing A LOT longer)";

    private final String[] DATE_FILTER_STATUSES = new String[] { "Created", "Resolution" };

    private DialogComponentLabel connectionTestLabel;
    private DialogComponentButton checkConnectionButton;
    private SettingsModelString hostnameComponent;
    private ActionListener checkConnectionButtonListener;

    @Override
    protected void createAdvancedTab() {
        super.createAdvancedTab();
        addDialogComponent(new DialogComponentBoolean(createSettingsHistory(), DOWNLOAD_HISTORY));
        addDialogComponent(new DialogComponentMultiLineString(createSettingsJQL(), JQL, false, 100, 10));
    }

    private ITSFilter currentFilter;

    @Override
    protected void createAvailableFiltersComponent() {
        createNewGroup("Filters");
        final ITSFiltersDialogComponent filtersComp = new ITSFiltersDialogComponent(getFilters(), new SettingsModelStringArray(
                "conf", new String[1]));

        filtersComp.addListItemSelectionListener(new ITSFiltersDialogComponent.ListItemSelectedListener() {

            @Override
            public void listItemSelected(ITSFilter filter, int tableId) {

                if (currentFilter != null) {
                    currentFilter.removeComponents(JiraOnlineAdapterNodeDialog.this);
                }

                if (filter != null) {
                    if (tableId == 2) {
                        filter.addComponents(JiraOnlineAdapterNodeDialog.this);
                        currentFilter = filter;
                        JiraOnlineAdapterNodeDialog.this.invalidate();
                    }
                }
            }
        });

        addDialogComponent(filtersComp);
    }

    @Override
    protected void createFilterOptionsComponent() {
        createNewGroup("Filter settings");

        currentFilter = new CreationDateFilter();
        currentFilter.addComponents(this);
        // filter.removeComponents(this);
    }

    @Override
    protected void createHostnameComponent() {
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

    @Override
    protected SettingsModelString createURLSettings() {
        return JiraOnlineAdapterNodeModel.createSettingsURL();
    }

    @Override
    protected void createProjectChooser() {
        // NOOP
    }

    @Override
    protected SettingsModelString createProjectSettings() {
        return null;
    }

    @Override
    protected ActionListener getButtonConnectionCheckListener() {
        return null;
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

}
