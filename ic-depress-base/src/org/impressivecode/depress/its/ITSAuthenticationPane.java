package org.impressivecode.depress.its;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentPasswordField;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * @author Michał Negacz, Wrocław University of Technology
 * @author Piotr Wróblewski, Wrocław University of Technology
 * @author Bartosz Skuza, Wrocław University of Technology
 * 
 */
public class ITSAuthenticationPane extends DefaultNodeSettingsPane {
	
	public static final int STRING_FIELD_WIDTH = 32;

	public static final String AUTHENTICATION_TAB_TITILE = "Authentication";

	public static final String USERNAME_LABEL = "Username:";

	public static final String PASSWORD_LABEL = "Password:";

	public void addAuthTab(SettingsModelString username, SettingsModelString password) {
		createNewTab(AUTHENTICATION_TAB_TITILE);
		addDialogComponent(new DialogComponentString(username, USERNAME_LABEL, false, STRING_FIELD_WIDTH));
		addDialogComponent(new DialogComponentPasswordField(password, PASSWORD_LABEL, STRING_FIELD_WIDTH));
	}
	
}
