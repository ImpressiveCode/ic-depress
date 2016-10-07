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
package org.impressivecode.depress.matcher.extmarkerparser;

import static com.google.common.base.Strings.emptyToNull;

import java.util.regex.Pattern;

import org.knime.core.node.defaultnodesettings.SettingsModelString;

import com.google.common.base.Strings;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class Configuration {
    private final Pattern idRegexp;
    private final String builderFormat;

    public Configuration(final SettingsModelString regExpID, final SettingsModelString builder) {
        if (emptyToNull(regExpID.getStringValue()) != null) {
            this.idRegexp = Pattern.compile(regExpID.getStringValue(),  Pattern.MULTILINE);
        } else {
            this.idRegexp = null;
        }

        this.builderFormat = Strings.emptyToNull(builder.getStringValue());
    }

    public Pattern getIdRegexp() {
        return idRegexp;
    }

    public String getBuilderFormat() {
        return builderFormat;
    }
}