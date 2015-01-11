package org.impressivecode.depress.its.oschangemanagement.model.rationaladapter;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OsChangeManagementRationalAdapterPagingInfo {
	
	@JsonProperty("oslc:totalCount")
	private int totalCount;
	
	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public OsChangeManagementRationalAdapterNextPageUrl getResults() {
		return results;
	}

	public void setResults(OsChangeManagementRationalAdapterNextPageUrl results) {
		this.results = results;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	@JsonProperty("oslc:nextPage")
	private OsChangeManagementRationalAdapterNextPageUrl results;
	
	@JsonProperty("jira:startIndex")
	private int startIndex;
}
