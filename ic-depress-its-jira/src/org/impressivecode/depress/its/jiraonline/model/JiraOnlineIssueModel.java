package org.impressivecode.depress.its.jiraonline.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraOnlineIssueModel {
	
	private JiraFieldModel fields;

	public JiraFieldModel getFields() {
		return fields;
	}

	public void setFields(JiraFieldModel fields) {
		this.fields = fields;
	}
}
