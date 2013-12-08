package org.impressivecode.depress.its;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentPasswordField;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * @author Micha³‚ Negacz, Wroc³aw University of Technology
 * @author Piotr Wróblewski, Wroc³aw University of Technology
 * @author Bartosz Skuza, Wroc³aw University of Technology
 * 
 */
public class ITSAuthenticationPane extends DefaultNodeSettingsPane {
	private static final int STRING_FIELD_WIDTH = 32;
	
	private static final String AUTHENTICATION_TAB_TITILE = "Authentication";
	
	private static final String USERNAME_LABEL = "Username:";

	private static final String PASSWORD_LABEL = "Password:";
	
	public void addAuthTab(SettingsModelString username,SettingsModelString password){
		createNewTab(AUTHENTICATION_TAB_TITILE);
		addDialogComponent(new DialogComponentString(username, USERNAME_LABEL, false, STRING_FIELD_WIDTH));
		addDialogComponent(new DialogComponentPasswordField(password, PASSWORD_LABEL, STRING_FIELD_WIDTH));
	}
}
