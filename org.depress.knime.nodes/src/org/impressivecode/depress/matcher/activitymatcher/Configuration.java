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
package org.impressivecode.depress.matcher.activitymatcher;

import java.util.List;

import org.impressivecode.depress.its.common.ITSDataType;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import com.google.common.base.Strings;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class Configuration {
    private final String builderFormat;
    private final List<ITSDataType> issues;
    private final int interval;

    public Configuration(final SettingsModelInteger interval,
            final SettingsModelString builder, final List<ITSDataType> issues) {

        this.builderFormat = Strings.emptyToNull(builder.getStringValue());
        this.issues = issues;
        this.interval = interval.getIntValue();
    }

    public int getInterval() {
        return interval;
    }

    public long getIntervalInMillis() {
        return interval * 60000;
    }

    public List<ITSDataType> getIssues() {
        return issues;
    }

    public String getBuilderFormat() {
        return builderFormat;
    }
}