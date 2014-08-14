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

package org.impressivecode.depress.mr.pitest;

import static org.impressivecode.depress.mr.pitest.PitestAdapterNodeModel.createFileChooserSettings;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;

/**
 *
 * @author Zuzanna Pacholczyk, Capgemini Polska
 */
public class PitestAdapterNodeDialog extends DefaultNodeSettingsPane {
    
	protected PitestAdapterNodeDialog() {
        super();

        addDialogComponent(new DialogComponentFileChooser(createFileChooserSettings(),
        		PitestAdapterNodeModel.DEFAULT_VALUE, PitestAdapterNodeModel.FILE_EXTENSION));
	}
}

