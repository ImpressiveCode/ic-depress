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
import static com.google.common.collect.Lists.newLinkedList;

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

	private BugzillaOnlineBuilder builder;

	public BugzillaOnlineParser(BugzillaOnlineBuilder builder) {
		this.builder = builder;
	}

	public final List<ITSDataType> parseEntries(final Object[] bugs) {
		List<ITSDataType> entries = newLinkedList();
		for (Object element : bugs) {
			entries.add(builder.parse(element));
		}
		return entries;
	}

	public final List<ITSDataType> parseEntries(final Object[] bugs, final Object[] history, final Map<String, Object> comments) {
		List<ITSDataType> entries = newLinkedList();
		for (int i = 0; i < bugs.length; i++) {
			// i made internal test and the order of bugs history and bugs is
			// the
			// same, but there is no information about returned history order in
			// bugzilla api, so this is risky solution
			ITSDataType entry = builder.parse(bugs[i]);

			if (history != null) {
				builder.buildHistory(entry, history[i]);
			}
			if (comments != null) {
				builder.buildComments(entry, comments.get(entry.getIssueId()));
			}

			entries.add(entry);
		}
		return entries;
	}

	@SuppressWarnings("unchecked")
	public List<String> extractBugsIds(final Object[] bugs) {
		List<String> bugsIds = newArrayList();

		for (Object bug : bugs) {
			Map<String, Object> map = (Map<String, Object>) bug;
			bugsIds.add(map.get("id").toString());
		}

		return bugsIds;
	}

	public void setBuilder(BugzillaOnlineBuilder builder) {
		this.builder = builder;
	}

}
