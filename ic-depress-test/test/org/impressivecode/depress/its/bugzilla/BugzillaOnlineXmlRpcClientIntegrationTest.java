package org.impressivecode.depress.its.bugzilla;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.net.URL;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;

public class BugzillaOnlineXmlRpcClientIntegrationTest {

	@Test(expected = BugzillaOnlineClientException.class)
    public void shouldNotConnectToNotExistingInstallation() throws Exception {
        // given
        BugzillaOnlineClient client = new BugzillaOnlineXmlRpcClient(new URL("http://fakebugzilla/xmlrpc.cgi"));
        Map<String, Object> parameters = Collections.<String, Object>emptyMap();
		
        // when
		client.execute("Bugzilla.version", parameters);

        // then
        // Exception expected
    }
	
	@SuppressWarnings("unchecked")
	@Test
    public void shouldConnectToMozillaOfficialInstallationWithoutLoginAndFetchVersion() throws Exception {
        // given
        BugzillaOnlineClient client = new BugzillaOnlineXmlRpcClient(new URL("https://bugzilla.mozilla.org/xmlrpc.cgi"));
        Map<String, Object> parameters = Collections.<String, Object>emptyMap();
        
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
        BugzillaOnlineClient client = new BugzillaOnlineXmlRpcClient(new URL("https://landfill.bugzilla.org/bugzilla-tip/xmlrpc.cgi"));
        Map<String, Object> parameters = Maps.newHashMap();
        // login and password comes from http://bugmenot.com/view/landfill.bugzilla.org
        parameters.put("login", "partner55581521@aravensoft.com");
        parameters.put("password", "landfill");
		
        // when
        Object result = client.execute("User.login", parameters);

        // then
        assertNotNull(result);
        assertThat(result, instanceOf(Map.class));
        Map<String, Object> resultMap = (Map<String, Object>) result;
		assertFalse(resultMap.isEmpty());
        assertNotNull(resultMap.get("id"));
        assertNotNull(resultMap.get("token"));
    }
	
}
