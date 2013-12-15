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
package org.impressivecode.depress.its;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentDate;
import org.knime.core.node.defaultnodesettings.DialogComponentPasswordField;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelDate;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * @author Michał Negacz, Wrocław University of Technology
 * @author Piotr Wróblewski, Wrocław University of Technology
 * @author Bartosz Skuza, Wrocław University of Technology
 * 
 */
public class ITSCommonDialog extends DefaultNodeSettingsPane {
	
	public static final int STRING_FIELD_WIDTH = 32;

	private static final String URL_LABEL = "URL:";
	
	private static final String DATE_FROM_LABEL = "Date from:";
	
	public static final String AUTHENTICATION_TAB_TITLE = "Authentication";

	public static final String USERNAME_LABEL = "Username:";

	public static final String PASSWORD_LABEL = "Password:";

	public void addAuthTab(SettingsModelString username, SettingsModelString password) {
		createNewTab(AUTHENTICATION_TAB_TITLE);
		addDialogComponent(new DialogComponentString(username, USERNAME_LABEL, false, STRING_FIELD_WIDTH));
		addDialogComponent(new DialogComponentPasswordField(password, PASSWORD_LABEL, STRING_FIELD_WIDTH));
	}
	
	public void addUrlComponent(SettingsModelString url){
		addDialogComponent(new DialogComponentString(url, URL_LABEL, true, STRING_FIELD_WIDTH));
	}
	
	public void addDateFromFilter(SettingsModelDate dateFrom){
		addDialogComponent(new DialogComponentDate(dateFrom,DATE_FROM_LABEL));
	}
	
}
