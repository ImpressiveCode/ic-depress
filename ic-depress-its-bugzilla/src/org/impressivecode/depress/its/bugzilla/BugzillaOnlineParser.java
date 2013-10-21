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

import static com.google.common.collect.Lists.newLinkedList;
import static org.impressivecode.depress.its.bugzilla.BugzillaCommonUtils.getPriority;
import static org.impressivecode.depress.its.bugzilla.BugzillaCommonUtils.getResolution;
import static org.impressivecode.depress.its.bugzilla.BugzillaCommonUtils.getStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.impressivecode.depress.its.ITSDataType;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * @author Piotr Wróblewski
 * @author Michał Negacz, Wrocław University of Technology
 * 
 */
public class BugzillaOnlineParser {

	public final List<ITSDataType> parseEntries(final Object[] bugs) {
		List<ITSDataType> entries = newLinkedList();
		for (Object element : bugs) {
			entries.add(parse(element));
		}
		return entries;
	}

	@SuppressWarnings("unchecked")
	ITSDataType parse(Object bug) {
		ITSDataType entry = new ITSDataType();
		Map<String, Object> parametersMap = (Map<String, Object>) bug;

		entry.setIssueId(parametersMap.get("id").toString());
		entry.setCreated((Date) parametersMap.get("creation_time"));
		entry.setPriority(getPriority(parametersMap.get("priority").toString()));
		entry.setStatus(getStatus(parametersMap.get("status").toString()));
		entry.setSummary(parametersMap.get("summary").toString());
		entry.setUpdated((Date) parametersMap.get("last_change_time"));
		entry.setResolution(getResolution(parametersMap.get("resolution").toString()));
		entry.setReporter(parametersMap.get("assigned_to").toString());
		entry.setLink(parametersMap.get("url").toString());
		entry.setVersion(getVersion(parametersMap));
		entry.setComments(new ArrayList<String>()); // Bugzilla::Webservice::Bug  comments method
		
		return entry;
	}

	private List<String> getVersion(Map<String, Object> parametersMap) {
		List<String> version = new ArrayList<String>();
		version.add(parametersMap.get("version").toString());
		return version;
	}

}
