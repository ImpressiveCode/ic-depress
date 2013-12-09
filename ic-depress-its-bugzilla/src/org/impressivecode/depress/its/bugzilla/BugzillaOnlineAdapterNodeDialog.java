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

import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineAdapterNodeModel.createBugsPerTaskSettings;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineAdapterNodeModel.createDateSettings;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineAdapterNodeModel.createLimitSettings;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineAdapterNodeModel.createPasswordSettings;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineAdapterNodeModel.createProductSettings;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineAdapterNodeModel.createThreadsCountSettings;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineAdapterNodeModel.createURLSettings;
import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineAdapterNodeModel.createUsernameSettings;

import org.impressivecode.depress.its.ITSAuthenticationPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.DialogComponentString;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * @author Michał Negacz, Wrocław University of Technology
 * @author Piotr Wróblewski, Wrocław University of Technology
 * @author Bartosz Skuza, Wrocław University of Technology
 * 
 */
public class BugzillaOnlineAdapterNodeDialog extends ITSAuthenticationPane {

	
	public static final String PRODUCT_LABEL = "Product:";

	public static final String LIMIT_LABEL = "Limit:";
	
	public static final String ADVANCED_TAB_TITILE = "Advanced";
	
	public static final String THREADS_COUNT_LABEL = "Threads count:";
	
	public static final String BUGS_PER_TASK_LABEL = "Bugs per thread:";

	protected BugzillaOnlineAdapterNodeDialog() {
		addUrlComponent(createURLSettings());
		addDialogComponent(getProductComponent());
		addDateFromFilter(createDateSettings());
		addDialogComponent(getLimitComponent());
		
		addAuthTab(createUsernameSettings(), createPasswordSettings());
		
		createNewTab(ADVANCED_TAB_TITILE);
		addDialogComponent(getThreadsCountComponent());
		addDialogComponent(getBugsPerTaskComponent());
	}
	
	private DialogComponent getProductComponent() {
		return new DialogComponentString(createProductSettings(), PRODUCT_LABEL, true, STRING_FIELD_WIDTH);
	}


	private DialogComponent getLimitComponent() {
		return new DialogComponentNumberEdit(createLimitSettings(), LIMIT_LABEL);
	}

	private DialogComponent getThreadsCountComponent() {
		return new DialogComponentNumberEdit(createThreadsCountSettings(), THREADS_COUNT_LABEL, STRING_FIELD_WIDTH);
	}
	
	private DialogComponent getBugsPerTaskComponent() {
		return new DialogComponentNumberEdit(createBugsPerTaskSettings(), BUGS_PER_TASK_LABEL, STRING_FIELD_WIDTH);
	}
	
}
