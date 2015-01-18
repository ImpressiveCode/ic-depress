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
package org.impressivecode.depress.its.oschangemanagement.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSPriority;
import org.impressivecode.depress.its.ITSResolution;
import org.impressivecode.depress.its.ITSStatus;
import org.impressivecode.depress.its.ITSType;
import org.impressivecode.depress.its.oschangemanagement.model.OsChangeManagementProject;
import org.impressivecode.depress.its.oschangemanagement.model.rationaladapter.OsChangeManagementRationalAdapterIssueListItem;
import org.impressivecode.depress.its.oschangemanagement.model.rationaladapter.OsChangeManagementRationalAdapterIssuesList;
import org.impressivecode.depress.its.oschangemanagement.model.rationaladapter.OsChangeManagementRationalAdapterProjectList;
import org.impressivecode.depress.its.oschangemanagement.model.rationaladapter.OsChangeManagementRationalAdapterProjectListItem;

/**
 * 
 * @author Marcin Cho³uj, Wroclaw University of Technology
 * @author Piotr Malek, Wroclaw University of Technology
 * @author Przemys³aw Trepka, Wroclaw University of Technology
 * @author £ukasz Trojak, Wroclaw University of Technology
 * 
 */

public class OsChangeManagementRationalAdapterParser extends OsChangeManagementAdapterParser {
	private static final int START_HOSTNAME_INDEX = 0;
	private static final int END_HOSTNAME_INDEX = 7;
	private final HashMap<String, String[]> priorityMap;
	private final HashMap<String, String[]> typeMap;
	private final HashMap<String, String[]> resolutionMap;
	private final HashMap<String, String[]> statusMap;

	public OsChangeManagementRationalAdapterParser(HashMap<String, String[]> priority, HashMap<String, String[]> type,
			HashMap<String, String[]> resolution, HashMap<String, String[]> status) {
		super();
		this.priorityMap = priority;
		this.typeMap = type;
		this.resolutionMap = resolution;
		this.statusMap = status;
	}

	public OsChangeManagementRationalAdapterParser() {
		super();
		this.priorityMap = new HashMap<>();
		this.typeMap = new HashMap<>();
		this.resolutionMap = new HashMap<>();
		this.statusMap = new HashMap<>();
	}

	private ITSResolution parseResolutionFromMap(final String valueToParse) {
		if (valueToParse == null) {
			return ITSResolution.UNKNOWN;
		}
		for (String key : resolutionMap.keySet()) {
			for (String value : resolutionMap.get(key)) {
				if (valueToParse.equalsIgnoreCase(value))
					return ITSResolution.get(key);
			}
		}
		return ITSResolution.UNKNOWN;
	}

	private ITSStatus parseStatusFromMap(final String valueToParse) {
		if (valueToParse == null) {
			return ITSStatus.UNKNOWN;
		}
		for (String key : statusMap.keySet()) {
			for (String value : statusMap.get(key)) {
				if (valueToParse.equalsIgnoreCase(value))
					return ITSStatus.get(key);
			}
		}
		return ITSStatus.UNKNOWN;
	}

	private ITSType parseTypeFromMap(final String valueToParse) {
		if (valueToParse == null) {
			return ITSType.UNKNOWN;
		}
		for (String key : typeMap.keySet()) {
			for (String value : typeMap.get(key)) {
				if (valueToParse.equalsIgnoreCase(value))
					return ITSType.get(key);
			}
		}
		return ITSType.UNKNOWN;
	}

	private ITSPriority parsePriorityFromMap(final String valueToParse) {
		if (valueToParse == null) {
			return ITSPriority.UNKNOWN;
		}
		for (String key : priorityMap.keySet()) {
			for (String value : priorityMap.get(key)) {
				if (valueToParse.equalsIgnoreCase(value))
					return ITSPriority.get(key);
			}
		}
		return ITSPriority.UNKNOWN;
	}

	@Override
	public List<OsChangeManagementProject> getProjectList(String source) {

		List<OsChangeManagementProject> projects = new ArrayList<OsChangeManagementProject>();
		OsChangeManagementRationalAdapterProjectList rationalAdapterProjectList = parseJSON(source,
				OsChangeManagementRationalAdapterProjectList.class);

		for (OsChangeManagementRationalAdapterProjectListItem item : rationalAdapterProjectList.getMember()) {
			OsChangeManagementProject project = new OsChangeManagementProject();
			project.setName(item.getName());
			project.setUri(item.getUri());
			projects.add(project);
		}

		return projects;
	}

	@Override
	public int getIssueCount(String source) {
		OsChangeManagementRationalAdapterIssuesList issues = parseJSON(source,
				OsChangeManagementRationalAdapterIssuesList.class);
		return issues.getResponseInfo().getTotalCount();
	}

	@Override
	public List<ITSDataType> getIssues(String source) {
		OsChangeManagementRationalAdapterIssuesList issues = parseJSON(source,
				OsChangeManagementRationalAdapterIssuesList.class);
		ArrayList<ITSDataType> ret = new ArrayList<ITSDataType>();

		for (OsChangeManagementRationalAdapterIssueListItem item : issues.getResults()) {
			ITSDataType its = new ITSDataType();
			its.setAssignees(item.getContributorAsSet());
			try {
				its.setCreated(dateStringToObject(item.getCreated()));
			} catch (ParseException e) {
				Logger.getLogger("Error").severe(e.getMessage());
			}
			its.setDescription(item.getTitle());
			its.setSummary(item.getDescription());
			its.setReporter(item.getFirstCreator());
			its.setStatus(parseStatusFromMap(item.getStatus()));
			its.setType(parseTypeFromMap(item.getType()));
			its.setPriority(parsePriorityFromMap(item.getPriorityAsString()));
			its.setResolution(parseResolutionFromMap(item.getResolution()));
			its.setLink(item.getPriority().getAboutUrl()
					.substring(START_HOSTNAME_INDEX, item.getPriority().getAboutUrl().indexOf("/", END_HOSTNAME_INDEX))
					+ "/browse/" + item.getIdentifier());
			try {
				its.setUpdated(dateStringToObject(item.getModified()));
			} catch (ParseException e) {
				Logger.getLogger("Error").severe(e.getMessage());
			}
			its.setIssueId(item.getShortTitle());

			ret.add(its);
		}

		return ret;
	}

	private Date dateStringToObject(String date) throws ParseException {
		// String sub = date.substring(0,10);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		Date parsedDate = formatter.parse(date);
		return parsedDate;
	}
}