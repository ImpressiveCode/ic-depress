package org.impressivecode.depress.its.oschangemanagement.model.rationaladapter;

import java.util.ArrayList;

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
