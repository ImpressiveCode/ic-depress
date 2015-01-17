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
package org.impressivecode.depress.its.oschangemanagement.model.rationaladapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author Marcin Cho³uj, Wroclaw University of Technology
 * @author Piotr Malek, Wroclaw University of Technology
 * @author Przemys³aw Trepka, Wroclaw University of Technology
 * @author £ukasz Trojak, Wroclaw University of Technology
 * 
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class OsChangeManagementRationalAdapterIssueListItem {

	@JsonProperty("dcterms:contributor")
	private ArrayList<OsChangeManagementRationalAdapterDetails> contributor;

	@JsonProperty("dcterms:created")
	private String created;

	@JsonProperty("dcterms:creator")
	private ArrayList<OsChangeManagementRationalAdapterDetails> creator;

	@JsonProperty("dcterms:description")
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("dcterms:identifier")
	private String identifier;

	@JsonProperty("jira:issueType")
	private OsChangeManagementRationalAdapterDetails issueType;

	@JsonProperty("dcterms:modified")
	private String modified;

	@JsonProperty("oslc_cmx:priority")
	private OsChangeManagementRationalAdapterDetails priority;

	@JsonProperty("jira:resolution")
	private String resolution;

	@JsonProperty("oslc:shortTitle")
	private String shortTitle;

	@JsonProperty("oslc_cm:status")
	private String status;

	@JsonProperty("dcterms:title")
	private String title;

	@JsonProperty("dcterms:type")
	private String type;

	public ArrayList<OsChangeManagementRationalAdapterDetails> getContributor() {
		return contributor;
	}

	public Set<String> getContributorAsSet() {
		Set<String> ret = new HashSet<String>();
		for (OsChangeManagementRationalAdapterDetails item : getContributor()) {
			ret.add(getLastPathFragment(item.getAboutUrl()));
		}
		return ret;
	}

	public String getCreated() {
		return created;
	}

	public ArrayList<OsChangeManagementRationalAdapterDetails> getCreator() {
		return creator;
	}

	public String getFirstCreator() {
		return getLastPathFragment(getCreator().get(0).getAboutUrl());
	}

	public String getIdentifier() {
		return identifier;
	}

	public OsChangeManagementRationalAdapterDetails getIssueType() {
		return issueType;
	}

	private String getLastPathFragment(String path) {
		return path.substring(path.lastIndexOf('/') + 1);
	}

	public String getModified() {
		return modified;
	}

	public OsChangeManagementRationalAdapterDetails getPriority() {
		return priority;
	}

	public String getPriorityAsString() {
		return parsePriority(getLastPathFragment(getPriority().getAboutUrl()));
	}

	public String getResolution() {
		return resolution;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public String getStatus() {
		return status;
	}

	public String getTitle() {
		return title;
	}

	public String getType() {
		return type;
	}

	private String parsePriority(String priority) {
		switch (priority) {
		case "1":
			return "Blocker";
		case "2":
			return "Critical";
		case "3":
			return "Major";
		case "4":
			return "Minor";
		case "5":
			return "Trivial";
		default:
			return priority;
		}
	}

	public void setContributor(ArrayList<OsChangeManagementRationalAdapterDetails> contributor) {
		this.contributor = contributor;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public void setCreator(ArrayList<OsChangeManagementRationalAdapterDetails> creator) {
		this.creator = creator;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public void setIssueType(OsChangeManagementRationalAdapterDetails issueType) {
		this.issueType = issueType;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public void setPriority(OsChangeManagementRationalAdapterDetails priority) {
		this.priority = priority;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setType(String type) {
		this.type = type;
	}

}
