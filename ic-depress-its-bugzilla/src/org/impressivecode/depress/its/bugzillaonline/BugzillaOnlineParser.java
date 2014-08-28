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
package org.impressivecode.depress.its.bugzillaonline;

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
import org.impressivecode.depress.its.bugzilla.BugzillaCommonUtils;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * @author Piotr Wróblewski, Wrocław University of Technology
 * @author Michał Negacz, Wrocław University of Technology
 * @author Maciej Borkowski, Capgemini Poland
 */
public class BugzillaOnlineParser {
    public static final String ID = "id";
    public static final String CREATED = "creation_time";
    public static final String UPDATED = "last_change_time";
    public static final String STATUS = "status";
    public static final String ASSIGNEE = "assigned_to";
    public static final String FIX_VERSION = "target_milestone";
    public static final String VERSION = "version";
    public static final String REPORTER = "creator";
    public static final String PRIORITY = "severity";
    public static final String SUMMARY = "summary";
    public static final String LINK = "url";
    public static final String RESOLUTION = "resolution";
    public static final String TEXT = "text";
    public static final String COMMENTS = "comments";
    public static final String ADDED = "added";
    public static final String FIELD_NAME = "field_name";
    public static final String WHEN = "when";
    public static final String CHANGES = "changes";
    public static final String HISTORY = "history";
    public static final String AUTHOR = "author";
    public static final String ESTIMATED_TIME = "estimated_time";
    public static final String ACTUAL_TIME = "actual_time";

    public List<ITSDataType> parseEntries(final Object[] bugs, final Object[] histories,
            final Map<String, Object> comments) {
        List<ITSDataType> entries = newArrayListWithCapacity(bugs.length);

        for (Object bug : bugs) {
            ITSDataType entry = parse(bug);
            fillHistoryData(entry, findBugHistory(histories, entry.getIssueId()));
            fillCommentsData(entry, getBugsComments(comments, entry.getIssueId()));
            fillDescription(entry);
            entries.add(entry);
        }

        return entries;
    }

    @SuppressWarnings("unchecked")
    ITSDataType parse(Object bug) {
        ITSDataType entry = new ITSDataType();
        Map<String, Object> details = (Map<String, Object>) bug;

        entry.setIssueId(getId(details));
        entry.setCreated(getCreated(details));
        entry.setUpdated(getUpdated(details));
        entry.setStatus(getStatus(details));
        entry.setType(getType());
        entry.setVersion(getVersion(details));
        entry.setFixVersion(getFixVersion(details));
        entry.setPriority(getPriority(details));
        entry.setSummary(getSummary(details));
        entry.setLink(getLink(details));
        entry.setResolution(getResolution(details));
        entry.setReporter(getReporter(details));
        entry.setAssignees(getAssignee(details));
        entry.setTimeEstimate(getTimeEstimate(details));
        entry.setTimeSpent(getTimeSpent(details));

        return entry;
    }

    private String getId(Map<String, Object> details) {
        return details.get(ID).toString();
    }

    private Date getCreated(Map<String, Object> details) {
        return (Date) details.get(CREATED);
    }

    private Date getUpdated(Map<String, Object> details) {
        return (Date) details.get(UPDATED);
    }

    private ITSStatus getStatus(Map<String, Object> details) {
        return BugzillaCommonUtils.getStatus(details.get(STATUS).toString());
    }

    private ITSType getType() {
        return ITSType.BUG;
    }

    private ArrayList<String> getFixVersion(Map<String, Object> details) {
        ArrayList<String> fixVersions = newArrayList();
        if (details.containsKey(FIX_VERSION)) {
            fixVersions.add(details.get(FIX_VERSION).toString());
        }
        return fixVersions;
    }

    private ArrayList<String> getVersion(Map<String, Object> details) {
        ArrayList<String> versions = newArrayList();
        if (details.containsKey(VERSION)) {
            versions.add(details.get(VERSION).toString());
        }
        return versions;
    }

    private ITSPriority getPriority(Map<String, Object> details) {
        return details.containsKey(PRIORITY) ? BugzillaCommonUtils.getPriority(details.get(PRIORITY).toString()) : null;
    }

    private String getSummary(Map<String, Object> details) {
        return details.get(SUMMARY).toString();
    }

    private String getLink(Map<String, Object> details) {
        return details.containsKey(LINK) ? details.get(LINK).toString() : null;
    }

    private ITSResolution getResolution(Map<String, Object> details) {
        return BugzillaCommonUtils.getResolution(details.get(RESOLUTION).toString());
    }

    private String getReporter(Map<String, Object> details) {
        return details.containsKey(REPORTER) ? details.get(REPORTER).toString() : null;
    }

    private HashSet<String> getAssignee(Map<String, Object> details) {
        return newHashSet(details.get(ASSIGNEE).toString());
    }

    private Integer getTimeEstimate(Map<String, Object> details) {
        return details.containsKey(ESTIMATED_TIME) ? (int) (((double) details.get(ESTIMATED_TIME)) * 60) : null;
    }

    private Integer getTimeSpent(Map<String, Object> details) {
        return details.containsKey(ACTUAL_TIME) ? (int) (((double) details.get(ACTUAL_TIME)) * 60) : null;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> findBugHistory(Object[] histories, String id) {
        for (Object history : histories) {
            Map<String, Object> historyDetails = (Map<String, Object>) history;
            if (id.equals(historyDetails.get(ID).toString())) {
                return historyDetails;
            }
        }
        return newHashMap();
    }

    @SuppressWarnings("unchecked")
    void fillHistoryData(ITSDataType entry, Map<String, Object> historyDetails) {
        Object[] history = (Object[]) historyDetails.get(HISTORY);

        for (Object event : history) {
            Map<String, Object> eventDetails = (Map<String, Object>) event;
            Object[] changes = (Object[]) eventDetails.get(CHANGES);
            for (Object change : changes) {
                Map<String, Object> changeDetails = (Map<String, Object>) change;

                tryToSetResolved(entry, eventDetails, changeDetails);
            }
        }
    }

    private void tryToSetResolved(ITSDataType entry, Map<String, Object> eventDetails, Map<String, Object> changeDetails) {
        if (isEntryResolved(entry) && isFieldStatus(changeDetails) && isValueAdded(changeDetails)
                && isValueChangeToResolved(changeDetails) && isAfterPreviouslyResolvedDate(entry, eventDetails)) {
            entry.setResolved((Date) eventDetails.get(WHEN));
        }
    }

    private boolean isEntryResolved(ITSDataType entry) {
        return ITSStatus.RESOLVED.equals(entry.getStatus());
    }

    private boolean isFieldStatus(Map<String, Object> changeDetails) {
        return STATUS.equals(changeDetails.get(FIELD_NAME));
    }

    private boolean isValueAdded(Map<String, Object> changeDetails) {
        return !isNullOrEmpty(changeDetails.get(ADDED).toString());
    }

    private boolean isValueChangeToResolved(Map<String, Object> changeDetails) {
        return ITSStatus.RESOLVED.equals(BugzillaCommonUtils.getStatus(changeDetails.get(ADDED).toString()));
    }

    private boolean isAfterPreviouslyResolvedDate(ITSDataType entry, Map<String, Object> changeDetails) {
        return entry.getResolved() == null || ((Date) changeDetails.get(WHEN)).after(entry.getResolved());
    }

    @SuppressWarnings("unchecked")
    private Object[] getBugsComments(final Map<String, Object> comments, String id) {
        Map<String, Object> map = (Map<String, Object>) comments.get(id);
        return (Object[]) map.get(COMMENTS);
    }

    @SuppressWarnings("unchecked")
    void fillCommentsData(ITSDataType entry, Object[] comments) {
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
        return map.get(AUTHOR).toString();
    }

    private String getComment(Map<String, Object> map) {
        return map.get(TEXT).toString();
    }

    void fillDescription(ITSDataType entry) {
        // first comment is description, according to api documentation
        if (entry.getComments().size() > 0) {
            entry.setDescription(entry.getComments().get(0));
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> extractIds(final Object[] map) {
        List<String> ids = newArrayList();
        for (Object item : map) {
            Map<String, Object> details = (Map<String, Object>) item;
            ids.add(getId(details));
        }
        return ids;
    }

}
