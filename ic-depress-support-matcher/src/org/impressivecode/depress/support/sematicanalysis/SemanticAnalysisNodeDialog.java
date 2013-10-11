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
package org.impressivecode.depress.support.sematicanalysis;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class SemanticAnalysisNodeDialog extends DefaultNodeSettingsPane {

    protected SemanticAnalysisNodeDialog() {
        super();
        createNewGroup("Weight");
        addDialogComponent(new DialogComponentNumberEdit(new SettingsModelInteger(SemanticAnalysisNodeModel.CFG_AUTHOR_WEIGHT,
                SemanticAnalysisNodeModel.AUTHOR_WEIGHT_DEFAULT), 
                "Author: ", 33));

        addDialogComponent(new DialogComponentNumberEdit(new SettingsModelInteger(SemanticAnalysisNodeModel.CFG_RESOLUTION_WEIGHT,
                SemanticAnalysisNodeModel.RESOLUTION_WEIGHT_DEFAULT), 
                "Resolution(FIXED): ", 25));

    }
}
