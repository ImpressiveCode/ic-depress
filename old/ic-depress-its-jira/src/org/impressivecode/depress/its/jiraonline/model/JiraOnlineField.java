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
package org.impressivecode.depress.its.jiraonline.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model class used by Jackson for Jira JSON parsing. Contains issue fields
 * 
 * @author Marcin Kunert, Wroclaw University of Technology
 * @author Maciej Borkowski, Capgemini Poland
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraOnlineField {
    private Date created;
    private Date updated;
    @JsonProperty("resolutiondate")
    private Date resolved;
    private Status status;
    @JsonProperty("issuetype")
    private Type issueType;
    private List<JiraOnlineIssueVersion> versions;
    private List<JiraOnlineIssueVersion> fixVersions;
    private Priority priority;
    private String summary;
    private String description;
    private Resolution resolution;
    private JiraOnlineUser reporter;
    private JiraOnlineUser assignee;
    private JiraOnlineComments comment;
    @JsonProperty("timetracking")
    private TimeTracking timeTracking;

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getResolved() {
        return resolved;
    }

    public void setResolved(Date resolved) {
        this.resolved = resolved;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getIssueType() {
        return issueType;
    }

    public void setIssueType(Type issuetype) {
        this.issueType = issuetype;
    }

    public List<JiraOnlineIssueVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<JiraOnlineIssueVersion> versions) {
        this.versions = versions;
    }

    public List<JiraOnlineIssueVersion> getFixVersions() {
        return fixVersions;
    }

    public void setFixVersions(List<JiraOnlineIssueVersion> fixVersions) {
        this.fixVersions = fixVersions;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public JiraOnlineUser getReporter() {
        return reporter;
    }

    public void setReporter(JiraOnlineUser reporter) {
        this.reporter = reporter;
    }

    public JiraOnlineUser getAssignee() {
        return assignee;
    }

    public void setAssignee(JiraOnlineUser assignee) {
        this.assignee = assignee;
    }


    public JiraOnlineComments getComment() {
        return comment;
    }

    public void setComment(JiraOnlineComments comment) {
        this.comment = comment;
    }


    public TimeTracking getTimeTracking() {
        return timeTracking;
    }

    public void setTimeTracking(TimeTracking timeTracking) {
        this.timeTracking = timeTracking;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Status {
        private String description;
        private String name;
        private int id;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Priority {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Type {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Resolution {
        private int id;
        private String name;
        private String description;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class TimeTracking {
        @JsonProperty("originalEstimateSeconds")
        private int estimate;
        @JsonProperty("timeSpentSeconds")
        private int spent;
        
        public int getEstimate() {
            return estimate;
        }
        public void setEstimate(int estimate) {
            this.estimate = estimate;
        }
        public int getSpent() {
            return spent;
        }
        public void setSpent(int spent) {
            this.spent = spent;
        }
    }
}
