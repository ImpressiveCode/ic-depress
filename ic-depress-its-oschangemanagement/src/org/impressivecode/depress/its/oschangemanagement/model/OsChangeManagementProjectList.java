package org.impressivecode.depress.its.oschangemanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class OsChangeManagementProjectList {

	@JsonProperty("rdf:type")
	private String[] type;
	@JsonProperty("rdf:about")
	private String about;
	@JsonProperty("rdfs:member")
	private OsChangeManagementProjectListItem[] member;
	
	public String[] getType() {
		return type;
	}
	public void setType(String[] type) {
		this.type = type;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public OsChangeManagementProjectListItem[] getMember() {
		return member;
	}
	public void setMember(OsChangeManagementProjectListItem[] member) {
		this.member = member;
	}
}
