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
package org.impressivecode.depress.its.jiraonline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSPriority;
import org.impressivecode.depress.its.ITSResolution;
import org.impressivecode.depress.its.ITSStatus;
import org.impressivecode.depress.its.ITSType;
import org.impressivecode.depress.its.jiraonline.model.IssueVersion;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineIssueModel;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineIssuesListModel;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * 
 * @author Marcin Kunert, Krzysztof Kwoka, Dawid Rutowicz
 * 
 */
public class JiraOnlineConnector {

	public static String getData() {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		// JiraOnlineAdapterUriFactory.createJiraUriByJql("hibernate.atlassian.net",
		// "");

		String jiraExampleUri = "https://hibernate.atlassian.net/rest/api/latest/search?jql=";
		WebResource service = client.resource(jiraExampleUri);
		return service.get(String.class);
	}

	
}
