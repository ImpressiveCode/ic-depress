package org.impressivecode.depress.its.bugzilla;

import org.impressivecode.depress.its.ITSDataType;

public interface BugzillaOnlineBuilder {

	public ITSDataType parse(Object bug);
	
	public void buildHistory(ITSDataType type, Object bug);
	
	public void buildComments(ITSDataType type, Object bug);
	
	public void buildAttachments(ITSDataType type, Object bug);
}
