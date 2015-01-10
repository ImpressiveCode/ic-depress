package org.impressivecode.depress.its.oschangemanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class OsChangeManagementProjectListItem {
	
	@JsonProperty("dcterms:title")
	private String name;
	@JsonProperty("rdf:resource")
	private String uri;
	@JsonProperty("rdf:type")
	private String[] type;
	
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
	public String[] getType() {
		return type;
	}
	public void setType(String[] type) {
		this.type = type;
	}	
}