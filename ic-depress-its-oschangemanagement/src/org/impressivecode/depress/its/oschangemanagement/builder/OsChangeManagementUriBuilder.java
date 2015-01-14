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

package org.impressivecode.depress.its.oschangemanagement.builder;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.ws.rs.core.UriBuilder;

/**
 * Builder for JIRA REST API
 * 
 * @author Marcin Cho³uj, Wroclaw University of Technology
 * @author Piotr Malek, Wroclaw University of Technology
 * @author Przemys³aw Trepka, Wroclaw University of Technology
 * @author £ukasz Trojak, Wroclaw University of Technology
 * 
 */

public abstract class OsChangeManagementUriBuilder {

	public enum Mode {
		PROJECT_LIST, CHANGE_REQUEST;
	}

	protected static final int PAGE_SIZE = 50;

	protected Mode mode = Mode.PROJECT_LIST;
	protected String hostname;
	protected String protocol;
	protected String project;
	protected int startIndex = 0;

	public OsChangeManagementUriBuilder setHostname(String hostname) {
		this.hostname = hostname;
		filterInputedHostname();

		return this;
	}
	

	public OsChangeManagementUriBuilder setProject(String project) {
		this.project = project;
		return this;
	}

	public URI build() {

		switch (mode) {
		case PROJECT_LIST:
			return buildProjectListURI();
		case CHANGE_REQUEST:
			return buildChangeRequestsURI(startIndex);
		default:
			throw new RuntimeException(
					"This should never happen! URI builder failed!");
		}
	}

	public void resetMode() {
		this.mode = Mode.PROJECT_LIST;
	}

	protected URI buildListURI(String command, String basicUriPath) {
		// @formatter:off
		URI result = UriBuilder.fromPath(basicUriPath)
				.resolveTemplateFromEncoded("protocol", protocol)
				.resolveTemplateFromEncoded("hostname", hostname)
				.resolveTemplateFromEncoded("command", command).build();
		// @formatter:on
		
		return result;
	}

	protected void filterInputedHostname() {
		checkForProtocol();
		removeLastSlash();
	}

	protected void checkForProtocol() {
		int index = hostname.indexOf("://");

		if (index != -1) {
			protocol = hostname.substring(0, index);
			hostname = hostname.substring(index + 3);
		} else {
			protocol = "https";
		}
	}

	protected void removeLastSlash() {
		if (hostname.endsWith("/")) {
			hostname = hostname.substring(0, hostname.length() - 1);
		}
	}

	protected String getProtocol() {
		return protocol;
	}

	protected String getProject() {
		return project;
	}
	
	 public void setMode(Mode mode) {
	        this.mode = mode;
	    }
	 
	 public int getPageSize(){
		 return PAGE_SIZE;
	 }
	 
	 public void setStartIndex(int startIndex){
		 this.startIndex=startIndex;
	 }

	protected abstract URI buildProjectListURI();
	
	protected abstract URI buildChangeRequestsURI(int startIndex);
}