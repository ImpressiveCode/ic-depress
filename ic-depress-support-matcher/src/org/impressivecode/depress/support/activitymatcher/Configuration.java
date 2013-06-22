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

import static com.google.common.base.Strings.emptyToNull;

import java.util.List;
import java.util.regex.Pattern;

import org.impressivecode.depress.its.ITSDataType;
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
    private final String[] keywords;
    private final Pattern keywordsRegexp;
    private final List<ITSDataType> issues;
    private final int interval;

    public Configuration(final SettingsModelInteger interval, final SettingsModelString regExpKeywords, final SettingsModelString keywords,
            final SettingsModelString builder, final List<ITSDataType> issues) {

        if (emptyToNull(regExpKeywords.getStringValue()) != null) {
            this.keywordsRegexp = Pattern.compile(regExpKeywords.getStringValue());
        } else {
            this.keywordsRegexp = null;
        }
        if (emptyToNull(keywords.getStringValue()) != null) {
            this.keywords = keywords.getStringValue().split(",");
        } else {
            this.keywords = null;
        }

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

    public String[] getKeywords() {
        return keywords;
    }

    public Pattern getKeywordsRegexp() {
        return keywordsRegexp;
    }
}