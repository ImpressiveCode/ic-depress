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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSPriority;
import org.impressivecode.depress.its.ITSResolution;
import org.impressivecode.depress.its.ITSStatus;
import org.impressivecode.depress.its.ITSType;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineComment;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineIssue;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineIssueChange;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineIssueChangeRowItem;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineIssueChanges;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineIssueHistory;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineIssueVersion;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineIssuesList;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Parser from Jira JSON answer to {@link ITSDataType}
 * 
 * @author Marcin Kunert, Wroclaw University of Technology
 * @author Krzysztof Kwoka, Wroclaw University of Technology
 * @author Maciej Borkowski, Capgemini Poland
 * 
 */
public class JiraOnlineAdapterParser {
    private static final String LINK_PATH = "browse/";
    private static final String UNKNOWN_NAME = "unknown";
    private final HashMap<String, String[]> priorityMap;
    private final HashMap<String, String[]> typeMap;
    private final HashMap<String, String[]> resolutionMap;
    private final HashMap<String, String[]> statusMap;

    public JiraOnlineAdapterParser(HashMap<String, String[]> priority, HashMap<String, String[]> type,
            HashMap<String, String[]> resolution, HashMap<String, String[]> status) {
        super();
        this.priorityMap = priority;
        this.typeMap = type;
        this.resolutionMap = resolution;
        this.statusMap = status;
    }

    public List<ITSDataType> parseSingleIssueBatch(String source, String hostname) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = null;
        JiraOnlineIssuesList issueList = null;

        try {
            jp = jsonFactory.createJsonParser(source);
            issueList = objectMapper.readValue(jp, new TypeReference<JiraOnlineIssuesList>() {
            });
        } catch (IOException e) {
            Logger.getLogger("Error").severe(e.getMessage());
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
        } catch (IOException e) {
            Logger.getLogger("Error").severe(e.getMessage());
        }

        return parseIssueHistory(issue);
    }

    public static int getTotalIssuesCount(String source) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = null;
        JiraOnlineIssuesList issueList = null;

        try {
            jp = jsonFactory.createJsonParser(source);
            issueList = objectMapper.readValue(jp, new TypeReference<JiraOnlineIssuesList>() {
            });
        } catch (IOException e) {
            Logger.getLogger("Error").severe(e.getMessage());
        }

        return issueList.getTotal();
    }

    public static <T> List<T> getCustomList(String source, Class<?> elem) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = null;
        List<T> fieldList = null;

        try {
            jp = jsonFactory.createJsonParser(source);
            fieldList = objectMapper.readValue(jp,
                    objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, elem));
        } catch (IOException e) {
            Logger.getLogger("Error").severe(e.getMessage());
        }
        return fieldList;
    }

    private List<ITSDataType> parseData(final JiraOnlineIssuesList issueList, final String hostname) {
        List<ITSDataType> resultList = new ArrayList<>();
        for (JiraOnlineIssue issue : issueList.getIssues()) {
            try {
                ITSDataType data = new ITSDataType();
                data.setIssueId(issue.getKey());

                String link = hostname + LINK_PATH + issue.getKey();
                data.setLink(link);

                Set<String> assignees = new HashSet<>();
                if (issue.getFields().getAssignee() != null) {
                    assignees.add(issue.getFields().getAssignee().getName());
                }
                data.setAssignees(assignees);

                String valueToParse = null == issue.getFields().getPriority() ? null : issue.getFields().getPriority()
                        .getName();
                data.setPriority(parsePriorityFromMap(valueToParse));

                valueToParse = null == issue.getFields().getIssueType() ? null : issue.getFields().getIssueType()
                        .getName();
                data.setType(parseTypeFromMap(valueToParse));

                valueToParse = null == issue.getFields().getStatus() ? null : issue.getFields().getStatus().getName();
                data.setStatus(parseStatusFromMap(valueToParse));

                valueToParse = null == issue.getFields().getResolution() ? null : issue.getFields().getResolution()
                        .getName();
                data.setResolution(parseResolutionFromMap(valueToParse));

                if (issue.getFields().getTimeTracking() != null) {
                    data.setTimeEstimate(issue.getFields().getTimeTracking().getEstimate() / 60);
                    data.setTimeSpent(issue.getFields().getTimeTracking().getSpent() / 60);
                }

                data.setCreated(issue.getFields().getCreated());
                data.setUpdated(issue.getFields().getUpdated());
                data.setResolved(issue.getFields().getResolved());

                List<String> versions = new ArrayList<>();
                if (issue.getFields().getVersions() != null) {
                    for (JiraOnlineIssueVersion version : issue.getFields().getVersions()) {
                        versions.add(version.getName());
                    }
                }
                data.setVersion(versions);

                List<String> fixVersions = new ArrayList<>();
                if (issue.getFields().getFixVersions() != null) {
                    for (JiraOnlineIssueVersion version : issue.getFields().getFixVersions()) {
                        fixVersions.add(version.getName());
                    }
                }
                data.setFixVersion(fixVersions);

                if (issue.getFields().getReporter() != null) {
                    data.setReporter(issue.getFields().getReporter().getName());
                }

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
            } catch (Exception e) {
                System.out.println("Failed to parse issue: " + issue.getLink());
            }
        }
        return resultList;
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

    private static List<JiraOnlineIssueChangeRowItem> parseIssueHistory(final JiraOnlineIssueHistory issue) {
        List<JiraOnlineIssueChangeRowItem> issueList = new ArrayList<>();

        for (int i = 0; i < issue.getChangelog().getHistories().size(); i++) {
            JiraOnlineIssueChanges changes = issue.getChangelog().getHistories().get(i);
            for (int j = 0; j < changes.getItems().size(); j++) {
                try {
                    JiraOnlineIssueChange change = changes.getItems().get(j);

                    JiraOnlineIssueChangeRowItem parsedIssue = new JiraOnlineIssueChangeRowItem();
                    parsedIssue.setKey(issue.getKey());
                    if (changes.getAuthor() != null && changes.getAuthor().getName() != null) {
                        parsedIssue.setAuthor(changes.getAuthor().getName());
                    } else {
                        parsedIssue.setAuthor(UNKNOWN_NAME);
                    }
                    parsedIssue.setTimestamp(changes.getTimestamp());
                    parsedIssue.setField(change.getFieldName());
                    parsedIssue.setChangedFrom(change.getFrom());
                    parsedIssue.setChangedTo(change.getTo());

                    issueList.add(parsedIssue);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }

        return issueList;
    }
}
