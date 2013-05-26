package org.impressivecode.depress.scm.svn;

import java.nio.file.Paths;

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

/*
 * Method prefix meaning
 * - "n" - native name
 * - "i" - long information
 * - "e" - error information
 * */
public class SVNLocale {

	public static String eLoadSettings() {
		return "Error durning load setting";
	}

	public static String eValidateSettings() {
		return "Error durninig validate setting";
	}

	public static String iCurrentProgress(double inProgres) {
		return String.format("Current progress %d", inProgres);
	}

	public static Object iEndLoading() {
		return "End loading";
	}

	public static String iSettingsLoaded() {
		return "Settings loaded";
	}

	public static String iSettingsSaved() {
		return "Settings saved";
	}

	public static String iStartLoading() {
		return "Start loading";
	}

	public static String nIssueMarker() {
		return "Issue Marker";
	}

	public static String nPackage() {
		return "Package";
	}

	public static String iIvalidRepoPath() {
		return "Invalid repo path";
	}

	public static String iInitOnlineRepo(String url) {
		return "Init : " + url;
	}

	public static String iStartLoadOnlineRepo() {
		return "Start loading repository";
	}

	public static String iEndLoadOnlineRepo() {
		return "End loading repository";
	}

	public static String iInitLocalRepo(String inPath) {
		return "Init : " + Paths.get(inPath).getFileName();
	}

	public static String iEndLoadLocalRepo() {
		return "Start loading file";
	}

	public static String iStartLoadLocalRepo() {
		return "End loading file";
	}

	public static String iCancelLoading() {
		return "Cancel loading";
	}

	public static String iSVNInternalError() {
		return "Svn internal error";
	}

}
