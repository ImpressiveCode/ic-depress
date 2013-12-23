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

import java.awt.event.ActionListener;

import org.impressivecode.depress.its.ITSNodeDialog;
import org.knime.core.node.defaultnodesettings.DialogComponentDate;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * @author Michał Negacz, Wrocław University of Technology
 * @author Piotr Wróblewski, Wrocław University of Technology
 * @author Bartosz Skuza, Wrocław University of Technology
 * 
 */
public class BugzillaOnlineAdapterNodeDialog extends ITSNodeDialog {

	public static final String BUGS_PER_TASK_LABEL = "Bugs per thread:";
	
	public static final String DATE_FROM_LABEL = "Date from:";
	
	public static final String LIMIT_LABEL = "Limit:";
	
	@Override
	protected SettingsModelString createURLSettings() {
		return BugzillaOnlineAdapterNodeModel.createURLSettings();
	}

	@Override
	protected SettingsModelString createProjectSettings() {
		return BugzillaOnlineAdapterNodeModel.createProductSettings();
	}

	@Override
	protected ActionListener getButtonConnectionCheckListener() {
		return null;
	}
	
	@Override
	protected void createProjectChooser() {
		addDialogComponent(new DialogComponentString(createProjectSettings(), PROJECT_LABEL, true, COMPONENT_WIDTH));
	};
	
	@Override
    protected void createCheckProjectsButton() {
	};
    
    @Override
    protected void createCheckProjectsLabel() {
    }

	@Override
	protected SettingsModelString createLoginSettings() {
		return BugzillaOnlineAdapterNodeModel.createUsernameSettings();
	}

	@Override
	protected SettingsModelString createPasswordSettings() {
		return BugzillaOnlineAdapterNodeModel.createPasswordSettings();
	}
	
	@Override
	protected void createFiltersTab() {
		super.createFiltersTab();
		createAndAddLimitFilter();
		createAndAddDateFromFilter();
	}
	
	private void createAndAddLimitFilter() {
        addDialogComponent(new DialogComponentNumberEdit(BugzillaOnlineAdapterNodeModel.createLimitSettings(), LIMIT_LABEL, COMPONENT_WIDTH));
	}
	
	private void createAndAddDateFromFilter() {
       addDialogComponent(new DialogComponentDate(BugzillaOnlineAdapterNodeModel.createDateSettings(), DATE_FROM_LABEL));
	}

	@Override
	protected SettingsModelInteger createThreadsCountSettings() {
		return BugzillaOnlineAdapterNodeModel.createThreadsCountSettings();
	}

	@Override
	protected void createAdvancedTab() {
		super.createAdvancedTab();
		createAndAddBugsPerTaskComponent();
	}
	
	private void createAndAddBugsPerTaskComponent() {
        addDialogComponent(new DialogComponentNumberEdit(BugzillaOnlineAdapterNodeModel.createBugsPerTaskSettings(), BUGS_PER_TASK_LABEL, COMPONENT_WIDTH));
	}
	
}
