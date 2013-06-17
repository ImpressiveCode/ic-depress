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
package org.impressivecode.depress.support.activitymatcher;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class ActivityMatcherNodeDialog extends DefaultNodeSettingsPane {

    protected ActivityMatcherNodeDialog() {
        super();
        createNewGroup("Matcher Settings");
        addDialogComponent(new DialogComponentNumberEdit(new SettingsModelInteger(ActivityMatcherParserNodeModel.CFG_INTERVAL,
                ActivityMatcherParserNodeModel.INTERVAL_DEFAULT), 
                "Interval(min): ", 33));

        addDialogComponent(new DialogComponentString(new SettingsModelString(ActivityMatcherParserNodeModel.CFG_IDBUILDER,
                ActivityMatcherParserNodeModel.IDBUILDER_DEFAULT), 
                "ID Builder:     ", false, 33));

        createNewGroup("Confidence");
        addDialogComponent(new DialogComponentString(new SettingsModelString(ActivityMatcherParserNodeModel.CFG_REGEXP_KEYWORDS,
                ActivityMatcherParserNodeModel.REGEXP_KEYWORDS_DEFAULT), 
                "Keywords Regexp:", false, 33));

        addDialogComponent(new DialogComponentString(new SettingsModelString(ActivityMatcherParserNodeModel.CFG_KEYWORDS,
                ActivityMatcherParserNodeModel.KEYWORDS_DEFAULT), 
                "Keywords(,):    ", false, 33));

    }
}
