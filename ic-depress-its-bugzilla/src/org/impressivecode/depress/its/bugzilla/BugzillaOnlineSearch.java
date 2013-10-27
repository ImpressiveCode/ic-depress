package org.impressivecode.depress.its.bugzilla;


import java.util.ArrayList;
import java.util.Map;

import org.impressivecode.depress.its.ITSDataType;

public class BugzillaOnlineSearch extends BugzillaOnlineBuilderImpl implements BugzillaOnlineBuilder{

	@SuppressWarnings("unchecked")
	@Override
	public ITSDataType parse(Object bug) {
		ITSDataType entry = new ITSDataType();
		Map<String, Object> parametersMap = (Map<String, Object>) bug;

		buildBasicField(entry, parametersMap);
		
		entry.setLink(parametersMap.get("url").toString());
		entry.setComments(new ArrayList<String>()); 
		return entry;
	}

}
