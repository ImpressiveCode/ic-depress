package org.impressivecode.depress.its.bugzilla;

import static org.impressivecode.depress.its.bugzilla.BugzillaCommonUtils.getPriority;
import static org.impressivecode.depress.its.bugzilla.BugzillaCommonUtils.getResolution;
import static org.impressivecode.depress.its.bugzilla.BugzillaCommonUtils.getStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.impressivecode.depress.its.ITSDataType;

public abstract class BugzillaOnlineBuilderImpl {

	public void buildHistory(ITSDataType entry, Object bug) {
		//TODO
		//http://www.bugzilla.org/docs/3.6/en/html/api/Bugzilla/WebService/Bug.html -> history
	}
	
	public void buildComments(ITSDataType entry, Object bug) {
		//TODO
		//http://www.bugzilla.org/docs/3.6/en/html/api/Bugzilla/WebService/Bug.html -> history
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public void buildAttachments(ITSDataType entry, Object bug) {
		//TODO
		//http://www.bugzilla.org/docs/3.6/en/html/api/Bugzilla/WebService/Bug.html -> history
	}

	public void buildBasicField(ITSDataType entry,
			Map<String, Object> parametersMap) {
		entry.setIssueId(parametersMap.get("id").toString());
		entry.setCreated((Date) parametersMap.get("creation_time"));
		entry.setPriority(getPriority(parametersMap.get("priority").toString()));
		entry.setStatus(getStatus(parametersMap.get("status").toString()));
		entry.setSummary(parametersMap.get("summary").toString());
		entry.setUpdated((Date) parametersMap.get("last_change_time"));
		entry.setResolution(getResolution(parametersMap.get("resolution")
				.toString()));
		entry.setReporter(parametersMap.get("assigned_to").toString());

		List<String> versions = new ArrayList<String>();
		versions.add(parametersMap.get("version").toString());
		entry.setVersion(versions);
	}
}
