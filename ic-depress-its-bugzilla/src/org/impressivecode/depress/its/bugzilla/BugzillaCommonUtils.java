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
package org.impressivecode.depress.its.bugzilla;

import org.impressivecode.depress.its.ITSPriority;
import org.impressivecode.depress.its.ITSResolution;
import org.impressivecode.depress.its.ITSStatus;

/**
 * 
 * @author Michał Negacz, Wrocław University of Technology
 * 
 */
public class BugzillaCommonUtils {

	public static ITSResolution getResolution(String resolution) {
		if (resolution == null) {
			return ITSResolution.UNKNOWN;
		}
		switch (resolution) {
		case "---":
			return ITSResolution.UNRESOLVED;
		case "FIXED":
			return ITSResolution.FIXED;
		case "WONTFIX":
			return ITSResolution.WONT_FIX;
		case "DUPLICATE":
			return ITSResolution.DUPLICATE;
		case "INVALID":
			return ITSResolution.INVALID;
		case "INCOMPLETE":
			return ITSResolution.INVALID;
		case "WORKSFORME":
			return ITSResolution.INVALID;
		default:
			return ITSResolution.UNKNOWN;
		}
	}

	public static ITSStatus getStatus(String status) {
		if (status == null) {
			return ITSStatus.UNKNOWN;
		}
		switch (status) {
		case "UNCONFIRMED":
			return ITSStatus.OPEN;
		case "NEW":
			return ITSStatus.OPEN;
		case "REOPENED":
			return ITSStatus.REOPENED;
		case "ASSIGN":
			return ITSStatus.IN_PROGRESS;
		case "RESOLVED":
			return ITSStatus.RESOLVED;
		case "VERIFIED":
			return ITSStatus.RESOLVED;
		case "CLOSED":
			return ITSStatus.CLOSED;
		default:
			return ITSStatus.UNKNOWN;
		}
	}

	public static ITSPriority getPriority(String priority) {
		if (priority == null) {
			return ITSPriority.UNKNOWN;
		}
		switch (priority) {
		case "trivial":
			return ITSPriority.TRIVIAL;
		case "normal":
			return ITSPriority.MINOR;
		case "minor":
			return ITSPriority.MINOR;
		case "major":
			return ITSPriority.MAJOR;
		case "critical":
			return ITSPriority.CRITICAL;
		case "blocker":
			return ITSPriority.BLOCKER;
		default:
			return ITSPriority.UNKNOWN;
		}
	}

}
