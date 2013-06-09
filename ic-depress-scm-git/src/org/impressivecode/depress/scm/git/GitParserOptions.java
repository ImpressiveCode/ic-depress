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
package org.impressivecode.depress.scm.git;

import java.util.regex.Pattern;

import com.google.common.base.Strings;

/**
 * @author Marek Majchrzak, ImpressiveCode
 */
public class GitParserOptions {
    private Pattern markerRegexp;
    private String packageString;
    private String branch;

    public static GitParserOptions options(final String markerRegexp, final String packageString) {
        return options(markerRegexp, packageString, null);
    }

    public static GitParserOptions options(final String markerRegexp, final String packageString, final String branch) {
        GitParserOptions options = new GitParserOptions();
        if (!Strings.isNullOrEmpty(markerRegexp)) {
            options.markerRegexp = Pattern.compile(markerRegexp);
        }
        options.packageString = Strings.emptyToNull(packageString);
        options.branch = Strings.emptyToNull(branch);
        return options;
    }

    public boolean hasMarkerPattern() {
        return this.markerRegexp != null;
    }

    public Pattern getMarkerPattern() {
        return markerRegexp;
    }

    public boolean hasPackagePrefix() {
        return packageString != null;
    }

    public String getPackagePrefix() {
        return packageString;
    }

    public boolean hasBranch() {
        return this.branch != null;
    }

    public String getBranch() {
        return branch;
    }
}
