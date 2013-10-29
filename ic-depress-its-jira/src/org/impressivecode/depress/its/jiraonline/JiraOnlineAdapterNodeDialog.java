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
import static org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterNodeModel.createSettingsDateStart;
import static org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterNodeModel.createSettingsJQL;
import static org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterNodeModel.createSettingsLogin;
import static org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterNodeModel.createSettingsPass;
import static org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterNodeModel.createSettingsURL;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingWorker;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentButton;
import org.knime.core.node.defaultnodesettings.DialogComponentDate;
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;
import org.knime.core.node.defaultnodesettings.DialogComponentMultiLineString;
import org.knime.core.node.defaultnodesettings.DialogComponentPasswordField;
import org.knime.core.node.defaultnodesettings.DialogComponentString;

/**
 * 
 * @author Krzysztof Kwoka
 * @author Marcin Kunert
 * @author Dawid Rutowicz
 * 
 */
public class JiraOnlineAdapterNodeDialog extends DefaultNodeSettingsPane {

	// private static final String HISTORY_ID_URL =
	// "depress.its.jiraonline.history.url";
	// private static final String HISTORY_ID_USERNAME =
	// "depress.its.jiraonline.history.username";
	// private static final String HISTORY_ID_PASSWORD =
	// "depress.its.jiraonline.history.password";

	// private static final String HISTORY_ID_START_DATE =
	// "depress.its.jiraonline.history.startdate";
	// private static final String HISTORY_ID_END_DATE =
	// "depress.its.jiraonline.history.enddate";

	private DialogComponentLabel mConnectionTestLabel;

	protected JiraOnlineAdapterNodeDialog() {
		initConnectionTab();
		initLoginDataTab();
	}

	private void initConnectionTab() {
		createNewGroup("Connection");
		addDialogComponent(new DialogComponentString(createSettingsURL(),
				"Jira URL: ", true, 32));

		final DialogComponentButton checkButton = new DialogComponentButton(
				"Check");
		checkButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				mConnectionTestLabel.setText("Testing connection...");
				checkButton.getModel().setEnabled(false);

				// TODO Symulate Jira connection test. Change this to a real
				// test!
				new SwingWorker<Void, Void>() {

					@Override
					protected Void doInBackground() throws Exception {
						Thread.sleep(1000);
						return null;
					}

					@Override
					public void done() {
						mConnectionTestLabel.setText("Connection ok!");
						checkButton.getModel().setEnabled(true);
					}

				}.execute();
			}
		});

		addDialogComponent(checkButton);

		mConnectionTestLabel = new DialogComponentLabel("Not tested yet...");
		addDialogComponent(mConnectionTestLabel);

		createNewGroup("Filters");
		addDialogComponent(new DialogComponentDate(createSettingsDateStart(),
				"Date from:", true));

		addDialogComponent(new DialogComponentDate(createSettingsDateEnd(),
				"Date to:", true));
		
		createNewGroup("Advanced");
		addDialogComponent(new DialogComponentMultiLineString(createSettingsJQL(), "JQL:", false, 100, 10));

	}

	private void initLoginDataTab() {
		createNewTab("Login data");
		createNewGroup("Login data");
		addDialogComponent(new DialogComponentString(createSettingsLogin(),
				"Login: ", false, 32));
		addDialogComponent(new DialogComponentPasswordField(
				createSettingsPass(), "Password: ", 32));
	}

}
