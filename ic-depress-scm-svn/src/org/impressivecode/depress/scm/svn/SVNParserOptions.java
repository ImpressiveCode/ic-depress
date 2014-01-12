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
package org.impressivecode.depress.scm.svn;
import java.util.ArrayList;

/**
 * @author Marek Majchrzak, ImpressiveCode
 * @author Krystian Dabrowski, Capgemini Poland
 */
public class SVNParserOptions {

    private String packageString;
    private ArrayList<String> extensionsNamesToFilter;

    public static SVNParserOptions options(final String packageString, final ArrayList<String> extensionsNamesToFilter) {
        SVNParserOptions options = new SVNParserOptions();
        options.packageString = packageString;
        options.extensionsNamesToFilter = extensionsNamesToFilter;
        return options;
    }

    public boolean hasPackagePrefix() {
        return packageString != null;
    }

    public String getPackagePrefix() {
        return packageString;
    }

	public ArrayList<String> getExtensionsNamesToFilter() {
		return extensionsNamesToFilter;
	}

}
