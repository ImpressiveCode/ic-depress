package org.impressivecode.depress.its.jiraonline.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraFieldModel {

	private Date created;
	private Date updated;
	private Status status;
	private Type issueType;
	private List<IssueVersion> versions;
	private List<IssueVersion> fixVersions;
	private Priority priority;
	private String summary;
	private String description;
	private Resolution resolution;
	private JiraUser reporter;
	private JiraUser assignee;

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

	public List<IssueVersion> getVersions() {
		return versions;
	}

	public void setVersions(List<IssueVersion> versions) {
		this.versions = versions;
	}

	public List<IssueVersion> getFixVersions() {
		return fixVersions;
	}

	public void setFixVersions(List<IssueVersion> fixVersions) {
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

	public JiraUser getReporter() {
		return reporter;
	}

	public void setReporter(JiraUser reporter) {
		this.reporter = reporter;
	}

	public JiraUser getAssignee() {
		return assignee;
	}

	public void setAssignee(JiraUser assignee) {
		this.assignee = assignee;
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
	public class JiraUser {
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
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
}
