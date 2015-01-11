package org.impressivecode.depress.its.oschangemanagement.model.rationaladapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OsChangeManagementRationalAdapterDetails {
	@JsonProperty("rdf:about")
	private String aboutUrl;

	public String getAboutUrl() {
		return aboutUrl;
	}

	public void setAboutUrl(String aboutUrl) {
		this.aboutUrl = aboutUrl;
	}
}
