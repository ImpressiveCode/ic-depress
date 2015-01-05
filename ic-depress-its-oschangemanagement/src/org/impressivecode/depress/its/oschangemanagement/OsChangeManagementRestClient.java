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

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;

public class OsChangeManagementRestClient {

	private Client client;

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

	public String getXML(URI uri, String login, String password)
			throws Exception {
		registerCredentials(login, password);
		Response response = client.target(uri).request("application/rdf+xml").get();		
		isDataFetchSuccessful(response);
		return response.readEntity(String.class);
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
