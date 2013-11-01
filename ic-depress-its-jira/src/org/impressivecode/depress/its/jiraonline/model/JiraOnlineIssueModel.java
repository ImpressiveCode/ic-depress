package org.impressivecode.depress.its.jiraonline.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraOnlineIssueModel {
	
	private int id;
	private JiraFieldModel fields;
	
	@JsonProperty("self")
	private String link;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public JiraFieldModel getFields() {
		return fields;
	}

	public void setFields(JiraFieldModel fields) {
		this.fields = fields;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
}
