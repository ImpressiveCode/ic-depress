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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Model class for Jira JSON parsing. Contains issue id, link and field list
 * @author Marcin Kunert
 *
 */
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
