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
package org.impressivecode.depress.its.jiraonline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSPriority;
import org.impressivecode.depress.its.ITSResolution;
import org.impressivecode.depress.its.ITSStatus;
import org.impressivecode.depress.its.ITSType;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineComment;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineField.Priority;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineField.Resolution;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineField.Status;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineField.Type;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineIssue;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineIssueChange;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineIssueChangeRowItem;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineIssueChanges;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineIssueHistory;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineIssueVersion;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineIssuesList;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

/**
 * Parser from Jira JSON answer to {@link ITSDataType}
 * 
 * @author Marcin Kunert, Wroclaw University of Technology
 * @author Krzysztof Kwoka, Wroclaw University of Technology
 * 
 */
public class JiraOnlineAdapterParser {

    private static final String LINK_PATH = "browse/";

    public static List<ITSDataType> parseSingleIssueBatch(final String source, final String hostname) {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = null;
        JiraOnlineIssuesList issueList = null;

        try {
            jp = jsonFactory.createJsonParser(source);
            issueList = objectMapper.readValue(jp, new TypeReference<JiraOnlineIssuesList>() {
            });
        } catch (JsonParseException e) {
        } catch (UnrecognizedPropertyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parseData(issueList, hostname);
    }

    public static List<JiraOnlineIssueChangeRowItem> parseSingleIssue(final String json) {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = null;
        JiraOnlineIssueHistory issue = null;

        try {
            jp = jsonFactory.createJsonParser(json);
            issue = objectMapper.readValue(jp, new TypeReference<JiraOnlineIssueHistory>() {
            });
        } catch (JsonParseException e) {
        } catch (UnrecognizedPropertyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parseIssueHistory(issue);
    }

    public static List<ITSDataType> parseMultipleIssueBatches(final List<String> sources, final String hostname) {
        List<ITSDataType> combinedIssues = new ArrayList<>();

        for (String source : sources) {
            combinedIssues.addAll(parseSingleIssueBatch(source, hostname));
        }

        return combinedIssues;
    }

    public static int getTotalIssuesCount(final String source) {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = null;
        JiraOnlineIssuesList issueList = null;

        try {
            jp = jsonFactory.createJsonParser(source);
            issueList = objectMapper.readValue(jp, new TypeReference<JiraOnlineIssuesList>() {
            });
        } catch (JsonParseException e) {
        } catch (UnrecognizedPropertyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return issueList.getTotal();
    }



    private static List<ITSDataType> parseData(final JiraOnlineIssuesList issueList, final String hostname) {
        List<ITSDataType> resultList = new ArrayList<>();

        for (JiraOnlineIssue issue : issueList.getIssues()) {
            ITSDataType data = new ITSDataType();
            data.setIssueId(issue.getKey());

            String link = hostname + LINK_PATH + issue.getKey();
            data.setLink(link);

            Set<String> assignees = new HashSet<>();
            if (issue.getFields().getAssignee() != null) {
                assignees.add(issue.getFields().getAssignee().getName());
            }
            data.setAssignees(assignees);

            data.setPriority(parsePriority(issue.getFields().getPriority()));

            data.setType(parseType(issue.getFields().getIssueType()));
            data.setStatus(parseStatus(issue.getFields().getStatus()));

            data.setCreated(issue.getFields().getCreated());
            data.setUpdated(issue.getFields().getUpdated());
            data.setResolved(issue.getFields().getResolved());

            data.setResolution(parseResolution(issue.getFields().getResolution()));

            List<String> versions = new ArrayList<>();
            for (JiraOnlineIssueVersion version : issue.getFields().getVersions()) {
                versions.add(version.getName());
            }
            data.setVersion(versions);

            List<String> fixVersions = new ArrayList<>();
            for (JiraOnlineIssueVersion version : issue.getFields().getFixVersions()) {
                fixVersions.add(version.getName());
            }
            data.setFixVersion(fixVersions);

            data.setReporter(issue.getFields().getReporter().getName());

            data.setSummary(issue.getFields().getSummary());
            data.setDescription(issue.getFields().getDescription());

            List<String> comments = new ArrayList<>();
            Set<String> commentAuthors = new HashSet<>();
            for (JiraOnlineComment comment : issue.getFields().getComment().getComments()) {
                comments.add(comment.getBody());
                commentAuthors.add(comment.getAuthor().getName());
            }

            data.setComments(comments);
            data.setCommentAuthors(commentAuthors);

            resultList.add(data);
        }

        return resultList;
    }

    private static ITSResolution parseResolution(final Resolution resolution) {
        if (resolution == null || resolution.getName() == null) {
            return ITSResolution.UNKNOWN;
        }
        switch (resolution.getName()) {
        case "Unresolved":
            return ITSResolution.UNRESOLVED;
        case "Fixed":
            return ITSResolution.FIXED;
        case "Wont't Fix":
            return ITSResolution.WONT_FIX;
        case "Duplicate":
            return ITSResolution.DUPLICATE;
        case "Invalid":
            return ITSResolution.INVALID;
        case "Incomplete":
            return ITSResolution.INVALID;
        case "Cannot Reproduce":
            return ITSResolution.WONT_FIX;
        case "Later":
            return ITSResolution.WONT_FIX;
        case "Not A Problem":
            return ITSResolution.WONT_FIX;
        case "Implemented":
            return ITSResolution.FIXED;
        case "Complete":
            return ITSResolution.FIXED;
        default:
            return ITSResolution.UNKNOWN;
        }
    }

    private static ITSStatus parseStatus(final Status status) {
        if (status == null || status.getName() == null) {
            return ITSStatus.UNKNOWN;
        }
        switch (status.getName()) {
        case "Open":
            return ITSStatus.OPEN;
        case "Reopen":
            return ITSStatus.REOPEN;
        case "In Progress":
            return ITSStatus.IN_PROGRESS;
        case "Resolved":
            return ITSStatus.RESOLVED;
        case "Closed":
            return ITSStatus.CLOSED;
        default:
            return ITSStatus.UNKNOWN;
        }
    }

    private static ITSType parseType(final Type type) {
        if (type == null || type.getName() == null) {
            return ITSType.UNKNOWN;
        }
        switch (type.getName()) {
        case "Bug":
            return ITSType.BUG;
        case "Test":
            return ITSType.TEST;
        case "Improvement":
        case "New Feature":
        case "Task":
        case "Wish":
            return ITSType.ENHANCEMENT;
        default:
            return ITSType.UNKNOWN;
        }
    }

    private static ITSPriority parsePriority(final Priority priority) {

        if (priority == null || priority.getName() == null) {
            return ITSPriority.UNKNOWN;
        }
        switch (priority.getName()) {
        case "Trivial":
            return ITSPriority.TRIVIAL;
        case "Minor":
            return ITSPriority.MINOR;
        case "Major":
            return ITSPriority.MAJOR;
        case "Critical":
            return ITSPriority.CRITICAL;
        case "Blocker":
            return ITSPriority.BLOCKER;
        default:
            return ITSPriority.UNKNOWN;
        }
    }

    private static List<JiraOnlineIssueChangeRowItem> parseIssueHistory(final JiraOnlineIssueHistory issue) {

        List<JiraOnlineIssueChangeRowItem> issueList = new ArrayList<>();

        for (int i = 0; i < issue.getChangelog().getHistories().size(); i++) {
            JiraOnlineIssueChanges changes = issue.getChangelog().getHistories().get(i);
            for (int j = 0; j < changes.getItems().size(); j++) {
                JiraOnlineIssueChange change = changes.getItems().get(j);

                JiraOnlineIssueChangeRowItem parsedIssue = new JiraOnlineIssueChangeRowItem();
                parsedIssue.setKey(issue.getKey());
                parsedIssue.setAuthor(changes.getAuthor().getName());
                parsedIssue.setTimestamp(changes.getTimestamp());
                parsedIssue.setField(change.getFieldName());
                parsedIssue.setChangedFrom(change.getFrom());
                parsedIssue.setChangedTo(change.getTo());

                issueList.add(parsedIssue);
            }
        }

        return issueList;
    }
}
