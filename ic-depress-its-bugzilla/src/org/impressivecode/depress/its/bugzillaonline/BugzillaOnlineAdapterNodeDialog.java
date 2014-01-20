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
package org.impressivecode.depress.its.bugzillaonline;

import static com.google.common.collect.Lists.newArrayList;
import static org.impressivecode.depress.its.bugzillaonline.BugzillaOnlineAdapterNodeModel.DEFAULT_COMBOBOX_ANY_VALUE;

import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;

import org.impressivecode.depress.its.ITSNodeDialog;
import org.impressivecode.depress.its.ITSPriority;
import org.knime.core.node.defaultnodesettings.DialogComponentDate;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.DialogComponentOptionalString;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
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

	public static final String UNKNOWN_ENUM_NAME = "UNKNOWN";

	public static final String BUGS_PER_TASK_LABEL = "Bugs per thread:";

	public static final String DATE_FROM_LABEL = "Date from:";

	public static final String ASSIGNED_TO_LABEL = "Assigned to:";

	public static final String LIMIT_LABEL = "Limit:";
	
	public static final String OFFSET_LABEL = "Offset:";

	public static final String REPORTER_LABEL = "Reporter:";

	public static final String PRIORITY_LABEL = "Priority:";
	
	public static final String VERSION_LABEL = "Version:";
	
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
		createAndAddOffsetFilter();
		createAndAddDateFromFilter();
		createAndAddAssignedToFilter();
		createAndAddReporterFilter();
		createAndAddVersionFilter();
		createAndAddPriorityFilter();
	}

	private void createAndAddLimitFilter() {
		addDialogComponent(new DialogComponentOptionalString(BugzillaOnlineAdapterNodeModel.createLimitSettings(), LIMIT_LABEL, COMPONENT_WIDTH));
	}
	
	private void createAndAddOffsetFilter() {
		addDialogComponent(new DialogComponentOptionalString(BugzillaOnlineAdapterNodeModel.createOffsetSettings(), OFFSET_LABEL, COMPONENT_WIDTH));
	}

	private void createAndAddDateFromFilter() {
		addDialogComponent(new DialogComponentDate(BugzillaOnlineAdapterNodeModel.createDateSettings(), DATE_FROM_LABEL));
	}

	private void createAndAddAssignedToFilter() {
		addDialogComponent(new DialogComponentOptionalString(BugzillaOnlineAdapterNodeModel.createAssignedToSettings(), ASSIGNED_TO_LABEL));
	}

	private void createAndAddReporterFilter() {
		addDialogComponent(new DialogComponentOptionalString(BugzillaOnlineAdapterNodeModel.createReporterSettings(), REPORTER_LABEL));
	}
	
	private void createAndAddVersionFilter() {
		addDialogComponent(new DialogComponentOptionalString(BugzillaOnlineAdapterNodeModel.createVersionSettings(), VERSION_LABEL));
	}
	
	private void createAndAddPriorityFilter() {
		addDialogComponent(new DialogComponentStringSelection(BugzillaOnlineAdapterNodeModel.createPrioritySettings(), PRIORITY_LABEL, prepareEnumValuesToComboBox(ITSPriority.values())));
	}
	
	private Collection<String> prepareEnumValuesToComboBox(Enum<?>[] enums) {
		List<String> strings = newArrayList();
		
		for (Enum<?> value : enums) {
			if (UNKNOWN_ENUM_NAME.equals(value.name())) {
				strings.add(DEFAULT_COMBOBOX_ANY_VALUE);
			} else {
				strings.add(value.name());
			}
		}
		
		return strings;
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
