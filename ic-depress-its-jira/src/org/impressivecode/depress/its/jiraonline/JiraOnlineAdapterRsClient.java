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

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.filter.HttpBasicAuthFilter;

/**
 * @author Marcin Kunert, Wroclaw University of Technology
 * @author Dawid Rutowicz, Wroclaw University of Technology
 * 
 */
public class JiraOnlineAdapterRsClient {

    private Client client;

    public JiraOnlineAdapterRsClient() {
        createClient();
    }

    public void registerCredentials(String username, String password) {
        client.register(new HttpBasicAuthFilter(username, password));
    }

    public String getJSON(URI uri) throws Exception {
        Response response = getReponse(uri);
        isDataFetchSuccessful(response);

        return reponseToString(response);
    }

    public boolean testConnection(URI uri) throws Exception {
        Response response = getReponse(uri);

        return isDataFetchSuccessful(response);
    }

    private void createClient() {
        client = ClientBuilder.newClient();
    }

    private boolean isDataFetchSuccessful(Response response) throws Exception {
        if (response.getStatus() != 200) {
            throw new Exception("Failed to fetch data.");
        }

        return true;
    }

    private String reponseToString(Response response) {
        return response.readEntity(String.class);
    }

    private Response getReponse(URI uri) {
        return client.target(uri).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get();
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
