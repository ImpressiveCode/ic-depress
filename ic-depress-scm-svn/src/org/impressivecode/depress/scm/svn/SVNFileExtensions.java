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

/**
 * @author Krystian Dabrowski, Capgemini Poland
 */

public enum SVNFileExtensions {
	JAVA("CFG_JAVA_EXTENSION", true, ".java");
	//XML("CFG_XML_EXTENSION", true, ".xml");
	
	private final String configName;
	private final boolean defaultSelected;
	private final String extension;
	
	SVNFileExtensions(String configName, boolean defaultSelected, String extension) {
		this.configName = configName;
		this.defaultSelected = defaultSelected;
		this.extension = extension;
	}
	
	public String getConfigName() {
		return configName;
	}

	public boolean isDefaultSelected() {
		return defaultSelected;
	}

	public String getExtension() {
		return extension;
	}
	
	public static SVNFileExtensions getSVNFileExtensionsFromConfigName(String configName) {
        for (SVNFileExtensions supportedExtension : SVNFileExtensions.values()) {
            if (supportedExtension.getConfigName().equals(configName)) {
                return supportedExtension;
            }
        }
        return null;
    }
}
