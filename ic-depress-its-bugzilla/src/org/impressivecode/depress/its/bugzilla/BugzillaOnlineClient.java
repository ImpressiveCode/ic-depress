package org.impressivecode.depress.its.bugzilla;

import java.util.HashMap;

/**
 * 
 * @author Micha³ Negacz
 * 
 */
public interface BugzillaOnlineClient {
	
	Object execute(String method, HashMap<String, Object> parameters) throws BugzillaOnlineClientException;
	
}
