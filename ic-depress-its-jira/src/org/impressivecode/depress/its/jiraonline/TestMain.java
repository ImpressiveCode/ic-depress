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
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		
		//JiraOnlineAdapterUriFactory.createJiraUriByJql("hibernate.atlassian.net", "");
		
//		String jiraExampleUri = "https://hibernate.atlassian.net/rest/api/latest/search?jql=";
//		WebResource service = client.resource(jiraExampleUri);
//		String pageContent = service.get(String.class);
		//System.out.println(pageContent);
		
		String pageContent = readFileAsString("exampleJiraAnswer.txt");
		
		
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
	
	private static String readFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

}
