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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		
		
		JiraOnlineConnector connector = new JiraOnlineConnector();
		
		//JiraOnlineAdapterUriFactory.createJiraUriByJql("hibernate.atlassian.net", "");
		
		String jiraExampleUri = "https://hibernate.atlassian.net/rest/api/latest/search?jql=";
		WebResource service = client.resource(jiraExampleUri);
		String pageContent = service.get(String.class);
		System.out.println(pageContent);
		
		//String pageContent = readFileAsString("exampleJiraAnswer.txt");
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonFactory jsonFactory = new JsonFactory();
		JsonParser jp = null;
		JiraOnlineIssuesListModel issueList = null;

		try {
			jp = jsonFactory.createJsonParser(pageContent);
			issueList = objectMapper.readValue(jp, new TypeReference<JiraOnlineIssuesListModel>() {
			});
		} catch (JsonParseException e) {
		} catch (UnrecognizedPropertyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(issueList);
		
	}
}
