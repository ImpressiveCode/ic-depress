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

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class SVNSettings {

	public final static SettingsModelString SVN_PATH = new SettingsModelString(
			SVNSettings.SVN_PATH_CFG, SVNSettings.SVN_DEFAULT_FILE_PATH);

	public static final SettingsModelString SVN_ISSUE_MARKER = new SettingsModelString(
			SVNSettings.SVN_ISSUE_REGEX_CFG, SVNSettings.SVN_DEFAULT_ISSUE_REGEX);

	public static final SettingsModelString SVN_PACKAGE = new SettingsModelString(
			SVNSettings.SVN_PACKAGE_CFG, SVNSettings.SVN_DEFAULT_PACKAGE);

	public static final String SVN_DEFAULT_FILE_PATH = "";

	public static final String SVN_DEFAULT_ISSUE_REGEX = "";

	public static final String SVN_DEFAULT_PACKAGE = "";

	public static final String SVN_PATH_CFG = "svnPath";

	public static final String SVN_PACKAGE_CFG = "package";

	public static final String SVN_ISSUE_REGEX_CFG = "issueMarker";

	public static void loadSettingsFrom(final NodeSettingsRO settings) {

		try {
			SVN_PATH.loadSettingsFrom(settings);
			SVN_ISSUE_MARKER.loadSettingsFrom(settings);
			SVN_PACKAGE.loadSettingsFrom(settings);
		} catch (InvalidSettingsException e) {
			Logger.instance().error(SVNLocale.eLoadSettings(), e);
		}
	}

	public static void saveSettingsTo(final NodeSettingsWO settings) {
		SVN_PATH.saveSettingsTo(settings);
		SVN_ISSUE_MARKER.saveSettingsTo(settings);
		SVN_PACKAGE.saveSettingsTo(settings);
	}

	public static void validateSettings(final NodeSettingsRO settings) {
		try {
			SVN_PATH.validateSettings(settings);
			SVN_ISSUE_MARKER.validateSettings(settings);
			SVN_PACKAGE.validateSettings(settings);
		} catch (InvalidSettingsException e) {
			Logger.instance().error(SVNLocale.eValidateSettings(), e);
		}
	}
}
