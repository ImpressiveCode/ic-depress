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

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newArrayListWithCapacity;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSPriority;
import org.impressivecode.depress.its.ITSResolution;
import org.impressivecode.depress.its.ITSStatus;
import org.impressivecode.depress.its.ITSType;

import com.google.common.collect.Sets;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * @author Piotr Wróblewski
 * @author Michał Negacz, Wrocław University of Technology
 * 
 */
public class BugzillaOnlineParser {

	public List<ITSDataType> parseEntries(final Object[] bugs, final Object[] bugHistories, final Map<String, Object> bugComments) {
		List<ITSDataType> entries = newArrayListWithCapacity(bugs.length);

		for (Object bug : bugs) {
			ITSDataType entry = parse(bug);
			fillHistoryData(entry, findBugHistory(bugHistories, entry.getIssueId()));
			fillCommentsData(entry, getBugsComments(bugComments, entry.getIssueId()));
			fillDescription(entry);
			entries.add(entry);
		}

		return entries;
	}

	@SuppressWarnings("unchecked")
	ITSDataType parse(Object bug) {
		ITSDataType entry = new ITSDataType();
		Map<String, Object> map = (Map<String, Object>) bug;

		entry.setIssueId(getId(map));
		entry.setCreated(getCreated(map));
		entry.setUpdated(getUpdated(map));
		entry.setStatus(getStatus(map));
		entry.setType(getType());
		entry.setVersion(getVersion(map));
		entry.setFixVersion(getFixVersion(map));
		entry.setPriority(getPriority(map));
		entry.setSummary(getSummary(map));
		entry.setLink(getLink(map));
		entry.setResolution(getResolution(map));
		entry.setReporter(getReporter(map));
		entry.setAssignees(getAssignee(map));

		return entry;
	}

	private String getId(Map<String, Object> map) {
		return map.get("id").toString();
	}

	private Date getCreated(Map<String, Object> map) {
		return (Date) map.get("creation_time");
	}

	private Date getUpdated(Map<String, Object> map) {
		return (Date) map.get("last_change_time");
	}

	private ITSStatus getStatus(Map<String, Object> map) {
		return BugzillaCommonUtils.getStatus(map.get("status").toString());
	}

	private ITSType getType() {
		return ITSType.BUG;
	}

	private ArrayList<String> getFixVersion(Map<String, Object> map) {
		return newArrayList((String) map.get("target_milestone"));
	}

	private ArrayList<String> getVersion(Map<String, Object> map) {
		return newArrayList((String) map.get("version"));
	}

	private ITSPriority getPriority(Map<String, Object> map) {
		// in Bugzilla bug severity contains appropriate values
		return BugzillaCommonUtils.getPriority(map.get("severity").toString());
	}

	private String getSummary(Map<String, Object> map) {
		return map.get("summary").toString();
	}

	private String getLink(Map<String, Object> map) {
		return map.get("url").toString();
	}

	private ITSResolution getResolution(Map<String, Object> map) {
		return BugzillaCommonUtils.getResolution(map.get("resolution").toString());
	}

	private String getReporter(Map<String, Object> map) {
		return map.get("assigned_to").toString();
	}

	private HashSet<String> getAssignee(Map<String, Object> map) {
		return Sets.newHashSet((String) map.get("assigned_to"));
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> findBugHistory(Object[] bugHistories, String issueId) {
		for (Object bugHistory : bugHistories) {
			Map<String, Object> map = (Map<String, Object>) bugHistory;
			if (issueId.equals(map.get("id").toString())) {
				return map;
			}
		}
		return newHashMap();
	}

	@SuppressWarnings("unchecked")
	private void fillHistoryData(ITSDataType entry, Map<String, Object> bugHistory) {
		Object[] history = (Object[]) bugHistory.get("history");

		if (notEmpty(bugHistory)) {
			for (Object event : history) {
				Map<String, Object> map = (Map<String, Object>) event;
				Object[] changes = (Object[]) map.get("changes");
				for (Object change : changes) {
					Map<String, Object> changeMap = (Map<String, Object>) change;

					if (isEntryResolved(entry) && isFieldStatus(changeMap) && isValueAdded(changeMap) && isValueChangeToResolved(changeMap) && isAfterPreviouslyResolvedDate(entry, map)) {
						entry.setResolved((Date) map.get("when"));
					}
				}
			}
		}
	}

	private boolean notEmpty(Map<String, Object> bugHistory) {
		return !bugHistory.isEmpty();
	}

	private boolean isEntryResolved(ITSDataType entry) {
		return ITSStatus.RESOLVED.equals(entry.getStatus());
	}

	private boolean isFieldStatus(Map<String, Object> changeMap) {
		return "status".equals(changeMap.get("field_name"));
	}

	private boolean isValueAdded(Map<String, Object> changeMap) {
		return !isNullOrEmpty((String) changeMap.get("added"));
	}

	private boolean isValueChangeToResolved(Map<String, Object> changeMap) {
		return ITSStatus.RESOLVED.equals(BugzillaCommonUtils.getStatus((String) changeMap.get("added")));
	}

	private boolean isAfterPreviouslyResolvedDate(ITSDataType entry, Map<String, Object> map) {
		return entry.getResolved() == null || ((Date) map.get("when")).after(entry.getResolved());
	}

	@SuppressWarnings("unchecked")
	private Object[] getBugsComments(final Map<String, Object> bugComments, String id) {
		Object[] comments = new Object[0];
		if (bugComments.containsKey(id)) {
			Map<String, Object> map = (Map<String, Object>) bugComments.get(id);
			comments = (Object[]) map.get("comments");
		}
		return comments;
	}

	@SuppressWarnings("unchecked")
	private void fillCommentsData(ITSDataType entry, Object[] comments) {
		List<String> contents = newArrayList();
		Set<String> authors = newHashSet();

		for (Object comment : comments) {
			Map<String, Object> map = (Map<String, Object>) comment;
			contents.add(getComment(map));
			authors.add(getCommentAuthor(map));
		}

		entry.setComments(contents);
		entry.setCommentAuthors(authors);
	}

	private String getCommentAuthor(Map<String, Object> map) {
		return (String) map.get("creator");
	}

	private String getComment(Map<String, Object> map) {
		return (String) map.get("text");
	}

	private void fillDescription(ITSDataType entry) {
		if (notEmpty(entry.getComments())) {
			entry.setDescription(entry.getComments().get(0)); // first comment is a description
		}
	}

	private boolean notEmpty(List<?> list) {
		return !list.isEmpty();
	}

	@SuppressWarnings("unchecked")
	public List<String> extractBugsIds(final Object[] bugs) {
		List<String> bugsIds = newArrayList();

		for (Object bug : bugs) {
			Map<String, Object> map = (Map<String, Object>) bug;
			bugsIds.add(getId(map));
		}

		return bugsIds;
	}

}
