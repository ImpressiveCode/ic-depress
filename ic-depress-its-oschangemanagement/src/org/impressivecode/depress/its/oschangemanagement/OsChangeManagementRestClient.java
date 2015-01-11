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
package org.impressivecode.depress.its.oschangemanagement;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

public class OsChangeManagementRestClient {

	private Client client;
	private HttpURLConnection con;
	private static boolean flag = false;
	private static String cookie;
	public OsChangeManagementRestClient() {
		createClient();
	}

    private void createClient() {
        client = ClientBuilder.newClient();
    }
    
    public void registerCredentials(final String username, final String password) {
        Authenticator.setDefault(new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        });
    }

	public String getJSON(URI uri, String login, String password)
			throws Exception {
		registerCredentials(login, password);
		return sendGet(uri);
//		Response response = client.target(uri).request("application/json").get();		
//		isDataFetchSuccessful(response);
//		return response.readEntity(String.class);
	}
	
	private final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0";
	
    private String sendGet(URI uri) throws Exception {
    		 
    		String url = uri.toString();
     
    		URL obj = new URL(url);
    		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    		// optional default is GET
    		con.setRequestMethod("GET");
     
    		//add request header
    		con.setRequestProperty("User-Agent", "Mozilla/6.0 (Windows NT 6.2; WOW64; rv:16.0.1) Gecko/20121011 Firefox/16.0.1");
    		con.setRequestProperty("Accept", "application/json");
    		if(flag){
    			con.setRequestProperty("Cookie", cookie);
    		}
    		
     
    		
    		
     
    		BufferedReader in = new BufferedReader(
    		        new InputStreamReader(con.getInputStream(),"UTF-8"));
    		String inputLine;
    		StringBuffer response = new StringBuffer();
     
    		while ((inputLine = in.readLine()) != null) {
    			response.append(inputLine);
    		}
    		in.close();
    		Map<String, List<String>> fieldList = con.getHeaderFields();
    		if(!flag){

        		cookie = fieldList.get("Set-Cookie").get(1);
        		cookie = cookie.substring(0,cookie.indexOf(';'));
        		flag = true;
    		}
    		
    		//print result
    		return response.toString();    

	}

	private boolean isDataFetchSuccessful(Response response) throws Exception {
        if (response.getStatus() == 401) {
            throw new SecurityException("Unauthorized.");
        }
        if (response.getStatus() != 200) {
            throw new Exception("Failed to fetch data.");
        }
        return true;
    }

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}
