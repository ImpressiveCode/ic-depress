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
package org.impressivecode.depress.its.bugzilla;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentPasswordField;
import org.knime.core.node.defaultnodesettings.DialogComponentString;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * @author Michał Negacz, Wrocław University of Technology
 * 
 */
public class BugzillaOnlineAdapterNodeDialog extends DefaultNodeSettingsPane {

	private static final String AUTHENTICATION_TAB_TITILE = "Authentication";

	private static final String URL_LABEL = "Bugzilla URL:";

	private static final String USERNAME_LABEL = "Username:";

	private static final String PASSWORD_LABEL = "Password:";

	protected BugzillaOnlineAdapterNodeDialog() {
		addDialogComponent(getURLComponent());
		createNewTab(AUTHENTICATION_TAB_TITILE);
		addDialogComponent(getUsernameComponent());
		addDialogComponent(getPasswordComponent());
	}

	private DialogComponent getURLComponent() {
		return new DialogComponentString(BugzillaOnlineAdapterNodeModel.createURLSettings(), URL_LABEL);
	}

	private DialogComponent getUsernameComponent() {
		return new DialogComponentString(BugzillaOnlineAdapterNodeModel.createUsernameSettings(), USERNAME_LABEL);
	}

	private DialogComponent getPasswordComponent() {
		return new DialogComponentPasswordField(BugzillaOnlineAdapterNodeModel.createPasswordSettings(), PASSWORD_LABEL);
	}

}
