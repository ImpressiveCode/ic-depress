package org.impressivecode.depress.its.oschangemanagement.model.rationaladapter;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OsChangeManagementRationalAdapterIssuesList {
	
	@JsonProperty("oslc:responseInfo")
	private OsChangeManagementRationalAdapterPagingInfo responseInfo;
	
	public OsChangeManagementRationalAdapterPagingInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(
			OsChangeManagementRationalAdapterPagingInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public ArrayList<OsChangeManagementRationalAdapterIssueListItem> getResults() {
		return results;
	}

	public void setResults(
			ArrayList<OsChangeManagementRationalAdapterIssueListItem> results) {
		this.results = results;
	}

	@JsonProperty("oslc:results")
	private ArrayList<OsChangeManagementRationalAdapterIssueListItem> results;
	
}
