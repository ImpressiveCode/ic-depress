package org.impressivecode.depress.its.jiraonline.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraOnlineIssuesListModel {
	
	private List<JiraOnlineIssueModel> issues;

	public List<JiraOnlineIssueModel> getIssues() {
		return issues;
	}

	public void setIssues(List<JiraOnlineIssueModel> issues) {
		this.issues = issues;
	}
}
