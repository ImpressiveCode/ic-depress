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
