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
package org.impressivecode.depress.matcher.syntacticanalysis;

import static com.google.common.base.Strings.emptyToNull;

import java.util.Set;
import java.util.regex.Pattern;

import org.knime.core.node.defaultnodesettings.SettingsModelString;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class Configuration {
    private final Optional<Set<String>> keywords;
    private final Optional<Pattern> keywordsRegexp;
    private final Optional<Pattern> onlyNumbers;

    public Configuration(final SettingsModelString regExpKeywords, final SettingsModelString keywords,
            final SettingsModelString onlyIds) {

        if (emptyToNull(regExpKeywords.getStringValue()) != null) {
            this.keywordsRegexp = Optional.of(Pattern.compile(regExpKeywords.getStringValue()));
        } else {
            this.keywordsRegexp = Optional.absent();
        }
        if (emptyToNull(keywords.getStringValue()) != null) {
            Set<String> set = Sets.newHashSet(keywords.getStringValue().split(","));
            this.keywords = Optional.of(set);
        } else {
            this.keywords = Optional.absent();
        }

        if (emptyToNull(onlyIds.getStringValue()) != null) {
            this.onlyNumbers = Optional.of(Pattern.compile(onlyIds.getStringValue(), Pattern.MULTILINE));
        } else {
            this.onlyNumbers = Optional.absent();
        }
    }

    public Optional<Set<String>> getKeywords() {
        return keywords;
    }

    public Optional<Pattern> getKeywordsRegexp() {
        return keywordsRegexp;
    }

    public Optional<Pattern> getOnlyNumbers() {
        return onlyNumbers;
    }
}