package org.impressivecode.depress.its.oschangemanagement.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OsChangeManagementProjectList {

	
	@JsonProperty("rdfs:member")
	private List<OsChangeManagementProjectListItem> member;
	
	
	public List<OsChangeManagementProjectListItem> getMember() {
		return member;
	}
	public void setMember(List<OsChangeManagementProjectListItem> member) {
		this.member = member;
	}
	@Override
	public String toString(){
		return "";
	}
}
