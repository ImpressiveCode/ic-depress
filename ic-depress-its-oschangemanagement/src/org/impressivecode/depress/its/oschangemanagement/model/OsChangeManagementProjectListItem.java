package org.impressivecode.depress.its.oschangemanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OsChangeManagementProjectListItem {
	
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