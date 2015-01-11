package org.impressivecode.depress.its.oschangemanagement.model.rationaladapter;

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
public class OsChangeManagementRationalAdapterProjectListItem {
	
	@JsonProperty("dcterms:title")
	private String name;
	@JsonProperty("rdf:resource")
	private String uri;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}	
}