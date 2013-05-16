package org.impressivecode.depress.scm.svn;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

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
public class SVNSettings {

	public final static SettingsModelString SVN_PATH_MODEL = new SettingsModelString(
			SVNSettings.CFG_SVN_PATH, SVNSettings.DEFAULT_PATH);

	public static final SettingsModelString ISSUE_MARKER_MODEL = new SettingsModelString(
			SVNSettings.CFG_ISSUE_REGEX, SVNSettings.DEFAULT_ISSUE);

	public static final SettingsModelString PACKAGE_MODEL = new SettingsModelString(
			SVNSettings.CFG_PACKAGE, SVNSettings.DEFAULT_PACKAGE);

	public static final String DEFAULT_PATH = "";

	public static final String DEFAULT_ISSUE = "";

	public static final String DEFAULT_PACKAGE = "";

	public static final String CFG_SVN_PATH = "svnPath";

	public static final String CFG_PACKAGE = "package";

	public static final String CFG_ISSUE_REGEX = "issueMarker";

	public static void loadSettingsFrom(final NodeSettingsRO settings) {

		try {
			SVN_PATH_MODEL.loadSettingsFrom(settings);
			ISSUE_MARKER_MODEL.loadSettingsFrom(settings);
			PACKAGE_MODEL.loadSettingsFrom(settings);
		} catch (InvalidSettingsException e) {
			Logger.instance().error(SVNLocale.eLoadSettings(), e);
		}
	}

	public static void saveSettingsTo(final NodeSettingsWO settings) {
		SVN_PATH_MODEL.saveSettingsTo(settings);
		ISSUE_MARKER_MODEL.saveSettingsTo(settings);
		PACKAGE_MODEL.saveSettingsTo(settings);
	}

	public static void validateSettings(final NodeSettingsRO settings) {
		try {
			SVN_PATH_MODEL.validateSettings(settings);
			ISSUE_MARKER_MODEL.validateSettings(settings);
			PACKAGE_MODEL.validateSettings(settings);
		} catch (InvalidSettingsException e) {
			Logger.instance().error(SVNLocale.eValidateSettings(), e);
		}
	}
}
