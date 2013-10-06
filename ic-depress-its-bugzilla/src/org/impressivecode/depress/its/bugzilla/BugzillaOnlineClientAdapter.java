package org.impressivecode.depress.its.bugzilla;

import java.util.List;

import org.impressivecode.depress.its.ITSDataType;

/**
 * 
 * @author Micha³ Negacz
 * 
 */
public interface BugzillaOnlineClientAdapter {
	
	List<ITSDataType> listEntries();
	
}
