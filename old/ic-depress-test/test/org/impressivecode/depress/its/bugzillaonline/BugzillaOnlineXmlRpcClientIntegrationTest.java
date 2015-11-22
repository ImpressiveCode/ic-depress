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
package org.impressivecode.depress.its.bugzillaonline;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.junit.Test;

import com.google.common.collect.Maps;

/**
 * 
 * @author Michał Negacz, Wrocław University of Technology
 * 
 */
public class BugzillaOnlineXmlRpcClientIntegrationTest {

    @Test(expected = XmlRpcException.class)
    public void shouldNotConnectToNotExistingInstallation() throws Exception {
        // given
        BugzillaOnlineXmlRpcClient client = new BugzillaOnlineXmlRpcClient("http://fakebugzilla/xmlrpc.cgi");
        Map<String, Object> parameters = Collections.<String, Object> emptyMap();

        // when
        client.execute("Bugzilla.version", parameters);

        // then
        // Exception expected
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldConnectToMozillaOfficialInstallationWithoutLoginAndFetchVersion() throws Exception {
        // given
        BugzillaOnlineXmlRpcClient client = new BugzillaOnlineXmlRpcClient("https://bugzilla.mozilla.org/xmlrpc.cgi");
        Map<String, Object> parameters = Collections.<String, Object> emptyMap();

        // when
        Object result = client.execute("Bugzilla.version", parameters);

        // then
        assertNotNull(result);
        assertThat(result, instanceOf(Map.class));
        Map<String, Object> resultMap = (Map<String, Object>) result;
        assertFalse(resultMap.isEmpty());
        assertNotNull(resultMap.get("version"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldConnectToBugzillaDemoInstallationAndLogin() throws Exception {
        // given
        BugzillaOnlineXmlRpcClient client = new BugzillaOnlineXmlRpcClient(
                "https://landfill.bugzilla.org/bugzilla-tip/xmlrpc.cgi");
        Map<String, Object> parameters = Maps.newHashMap();
        // login and password comes from
        // http://bugmenot.com/view/landfill.bugzilla.org
        client.setAuthentificator("partner55581521@aravensoft.com", "landfill");
        parameters.put("names", "partner55581521@aravensoft.com");
        
        // when
        Object result = client.execute("User.get", parameters);

        // then
        assertNotNull(result);
        assertThat(result, instanceOf(Map.class));
        Map<String, Object> resultMap = (Map<String, Object>) result;
        assertFalse(resultMap.isEmpty());
        assertNotNull(resultMap.get("users"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldConnectToMozillaWithoutEndpointInLinkSpecified() throws Exception {
        // given
        BugzillaOnlineXmlRpcClient client = new BugzillaOnlineXmlRpcClient("https://bugzilla.mozilla.org/");
        Map<String, Object> parameters = Collections.<String, Object> emptyMap();

        // when
        Object result = client.execute("Bugzilla.version", parameters);

        // then
        assertNotNull(result);
        assertThat(result, instanceOf(Map.class));
        Map<String, Object> resultMap = (Map<String, Object>) result;
        assertFalse(resultMap.isEmpty());
        assertNotNull(resultMap.get("version"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldConnectToMozillaWithoutLastSlashAndEndpointInLinkSpecified() throws Exception {
        // given
        BugzillaOnlineXmlRpcClient client = new BugzillaOnlineXmlRpcClient("https://bugzilla.mozilla.org");
        Map<String, Object> parameters = Collections.<String, Object> emptyMap();

        // when
        Object result = client.execute("Bugzilla.version", parameters);

        // then
        assertNotNull(result);
        assertThat(result, instanceOf(Map.class));
        Map<String, Object> resultMap = (Map<String, Object>) result;
        assertFalse(resultMap.isEmpty());
        assertNotNull(resultMap.get("version"));
    }

}
