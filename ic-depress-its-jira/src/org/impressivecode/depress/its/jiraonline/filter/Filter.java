package org.impressivecode.depress.its.jiraonline.filter;

import javax.swing.JPanel;

import org.knime.core.node.defaultnodesettings.SettingsModel;

public abstract class Filter {

	abstract SettingsModel[] getSettingModels();
	abstract JPanel getPanel();
	abstract String getName();
	abstract String getJQL();

	@Override
	public String toString() {
		return getName();
	}

}
