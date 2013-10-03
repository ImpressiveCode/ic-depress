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

import static org.impressivecode.depress.its.bugzilla.BugzillaOnlineAdapterNodeModel.createURLSettings;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentString;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * @author Micha³ Negacz
 * 
 */
public class BugzillaOnlineAdapterNodeDialog extends DefaultNodeSettingsPane {

    private static final String BUGZILLA_URL_LABEL = "Bugzilla URL:";

	protected BugzillaOnlineAdapterNodeDialog() {
        addDialogComponent(getURLComponent());
    }

    private DialogComponent getURLComponent() {
        return new DialogComponentString(createURLSettings(), BUGZILLA_URL_LABEL);
    }
}
