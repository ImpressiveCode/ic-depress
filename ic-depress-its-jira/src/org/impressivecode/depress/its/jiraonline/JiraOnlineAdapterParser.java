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

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSPriority;
import org.impressivecode.depress.its.ITSResolution;
import org.impressivecode.depress.its.ITSStatus;
import org.impressivecode.depress.its.ITSType;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineComment;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineFilterListItem;
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
    private static final String UNKNOWN_NAME = "unknown";

    public static List<ITSDataType> parseSingleIssueBatch(String source, String hostname) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = null;
        JiraOnlineIssuesList issueList = null;

        try {
            jp = jsonFactory.createJsonParser(source);
            issueList = objectMapper.readValue(jp, new TypeReference<JiraOnlineIssuesList>() {
            });
        } catch (JsonParseException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        } catch (UnrecognizedPropertyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (UnrecognizedPropertyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return issueList.getTotal();
    }

    public static List<JiraOnlineFilterListItem> getCustomFieldList(String source) {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = null;
        List<JiraOnlineFilterListItem> fieldList = null;

        try {
            jp = jsonFactory.createJsonParser(source);
            fieldList = objectMapper.readValue(jp, new TypeReference<List<JiraOnlineFilterListItem>>() {
            });
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (UnrecognizedPropertyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fieldList;
    }

    private static List<ITSDataType> parseData(final JiraOnlineIssuesList issueList, final String hostname) {
        List<ITSDataType> resultList = new ArrayList<>();
        JiraOnlineMapperManager mm = JiraOnlineAdapterNodeModel.getMapperManager();
        HashMap<String, String> priorityMap = mm.getMapMapperPriority();
        HashMap<String, String> resolutionMap = mm.getMapMapperResolution();
        HashMap<String, String> statusMap = mm.getMapMapperState();
        HashMap<String, String> typeMap = mm.getMapMapperType();
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

                String valueToParse = issue.getFields().getPriority().getName();
                data.setPriority(parsePriorityFromMapper(priorityMap.get(valueToParse), valueToParse));
                valueToParse = issue.getFields().getIssueType().getName();
                data.setType(parseTypeFromMapper(typeMap.get(valueToParse), valueToParse));
                valueToParse = issue.getFields().getStatus().getName();
                data.setStatus(parseStatusFromMapper(statusMap.get(valueToParse), valueToParse));
                valueToParse = issue.getFields().getResolution().getName();
                data.setResolution(parseResolutionFromMapper(resolutionMap.get(valueToParse), valueToParse));

                data.setCreated(issue.getFields().getCreated());
                data.setUpdated(issue.getFields().getUpdated());
                data.setResolved(issue.getFields().getResolved());

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
            } catch (Exception e) {
                System.out.println("Failed to parse issue: " + issue.getKey());
            }
        }

        return resultList;
    }

    public static ITSResolution parseResolutionFromMapper(String mapValue, String value) {
        return mapValue == null ? parseResolutionFromEnum(value) : parseResolutionFromEnum(mapValue);
    }

    public static ITSResolution parseResolutionFromEnum(String resolution) {
        ITSResolution parsed = ITSResolution.UNKNOWN;

        for (ITSResolution itsResolution : ITSResolution.values()) {
            if (resolution.equalsIgnoreCase(itsResolution.toString())) {
                parsed = itsResolution;
                break;
            }
        }

        return parsed;
    }

    public static ITSStatus parseStatusFromMapper(String mapValue, String value) {
        return mapValue == null ? parseStatusFromEnum(value) : parseStatusFromEnum(mapValue);
    }

    public static ITSStatus parseStatusFromEnum(String status) {
        ITSStatus parsed = ITSStatus.UNKNOWN;

        for (ITSStatus itsStatus : ITSStatus.values()) {
            if (status.equalsIgnoreCase(itsStatus.toString())) {
                parsed = itsStatus;
                break;
            }
        }

        return parsed;
    }

    public static ITSType parseTypeFromMapper(String mapValue, String value) {
        return mapValue == null ? parseTypeFromEnum(value) : parseTypeFromEnum(mapValue);
    }

    public static ITSType parseTypeFromEnum(String type) {
        ITSType parsed = ITSType.UNKNOWN;

        for (ITSType itsType : ITSType.values()) {
            if (type.equalsIgnoreCase(itsType.toString())) {
                parsed = itsType;
                break;
            }
        }

        return parsed;
    }

    public static ITSPriority parsePriorityFromMapper(String mapValue, String value) {
        return mapValue == null ? parsePriorityFromEnum(value) : parsePriorityFromEnum(mapValue);
    }

    public static ITSPriority parsePriorityFromEnum(String priority) {
        ITSPriority parsed = ITSPriority.UNKNOWN;

        for (ITSPriority itsPriority : ITSPriority.values()) {
            if (priority.equalsIgnoreCase(itsPriority.toString())) {
                parsed = itsPriority;
                break;
            }
        }

        return parsed;
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
