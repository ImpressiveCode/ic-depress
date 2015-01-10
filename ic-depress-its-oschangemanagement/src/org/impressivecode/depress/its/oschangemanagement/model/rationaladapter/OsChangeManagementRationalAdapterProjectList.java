package org.impressivecode.depress.its.oschangemanagement.model.rationaladapter;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class OsChangeManagementRationalAdapterProjectList {

	@JsonProperty("rdfs:member")
	private ArrayList<OsChangeManagementRationalAdapterProjectListItem> member;
	
	public ArrayList<OsChangeManagementRationalAdapterProjectListItem> getMember() {
		return member;
	}
	public void setMember(ArrayList<OsChangeManagementRationalAdapterProjectListItem> member) {
		this.member = member;
	}
}
