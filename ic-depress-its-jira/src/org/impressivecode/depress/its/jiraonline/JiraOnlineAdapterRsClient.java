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
 * 
 * @author Dawid Rutowicz, Wroclaw University of Technology
 * 
 */
public class JiraOnlineAdapterRsClient {

    private Client client;
    private boolean securedConnection;
    private JiraOnlineAdapterUriBuilder uriBuilder;

    public JiraOnlineAdapterRsClient() {
        this(new JiraOnlineAdapterUriBuilder());
    }

    public JiraOnlineAdapterRsClient(JiraOnlineAdapterUriBuilder uriBuilder) {
        createClient();
        this.uriBuilder = uriBuilder;
    }

    public void registerCredentials(String username, String password) {
        client.register(new HttpBasicAuthFilter(username, password));
    }

    public String getIssues() throws Exception {
        Response response = getReponse();
        isDataFetchSuccessful(response);

        return reponseToString(response);
    }

    public boolean testConnection() {
        Response response = getReponse();
        try {
            isDataFetchSuccessful(response);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean isSecuredConnection() {
        return securedConnection;
    }

    public void setSecuredConnection(boolean securedConnection) {
        this.securedConnection = securedConnection;
    }

    private void createClient() {
        client = ClientBuilder.newClient();
    }

    private void isDataFetchSuccessful(Response response) throws Exception {
        if (response.getStatus() != 200) {
            throw new Exception("Failed to fetch data.");
        }
    }

    private String reponseToString(Response response) {
        return response.readEntity(String.class);
    }

    private Response getReponse() {
        final URI uri = uriBuilder.build();

        return client.target(uri).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get();

    }

    public JiraOnlineAdapterUriBuilder getUriBuilder() {
        return uriBuilder;
    }

    public void setUriBuilder(JiraOnlineAdapterUriBuilder uriBuilder) {
        this.uriBuilder = uriBuilder;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
