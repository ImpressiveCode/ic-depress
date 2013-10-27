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

import static com.google.common.collect.Lists.newArrayList;
import static org.impressivecode.depress.its.bugzilla.BugzillaCommonUtils.getPriority;
import static org.impressivecode.depress.its.bugzilla.BugzillaCommonUtils.getResolution;
import static org.impressivecode.depress.its.bugzilla.BugzillaCommonUtils.getStatus;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.impressivecode.depress.its.ITSDataType;

/**
 * 
 * @author Piotr Wróblewski
 * 
 */
public abstract class BugzillaOnlineBuilder {

	public void buildHistory(ITSDataType entry, Object bug) {
		//TODO
		//http://www.bugzilla.org/docs/3.6/en/html/api/Bugzilla/WebService/Bug.html -> history
	}

	public void buildComments(ITSDataType entry, Object bug) {
		//TODO
		//http://www.bugzilla.org/docs/3.6/en/html/api/Bugzilla/WebService/Bug.html -> history
	}

	public void buildAttachments(ITSDataType entry, Object bug) {
		//TODO
		//http://www.bugzilla.org/docs/3.6/en/html/api/Bugzilla/WebService/Bug.html -> history
	}
	
	public abstract ITSDataType parse(Object bug);

	public void buildBasicField(ITSDataType entry, Map<String, Object> parametersMap) {
		entry.setIssueId(parametersMap.get("id").toString());
		entry.setCreated((Date) parametersMap.get("creation_time"));
		entry.setPriority(getPriority(parametersMap.get("priority").toString()));
		entry.setStatus(getStatus(parametersMap.get("status").toString()));
		entry.setSummary(parametersMap.get("summary").toString());
		entry.setUpdated((Date) parametersMap.get("last_change_time"));
		entry.setResolution(getResolution(parametersMap.get("resolution").toString()));
		entry.setReporter(parametersMap.get("assigned_to").toString());

		List<String> versions = newArrayList();
		versions.add(parametersMap.get("version").toString());
		entry.setVersion(versions);
	}
	
}
