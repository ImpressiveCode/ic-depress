package org.impressivecode.depress.its.oschangemanagement.parser;

import java.util.ArrayList;
import java.util.List;

import org.impressivecode.depress.its.oschangemanagement.model.OsChangeManagementProject;
import org.impressivecode.depress.its.oschangemanagement.model.rationaladapter.OsChangeManagementRationalAdapterProjectList;
import org.impressivecode.depress.its.oschangemanagement.model.rationaladapter.OsChangeManagementRationalAdapterProjectListItem;

public class OsChangeManagementRationalAdapterParser extends
		OsChangeManagementAdapterParser {
	
	@Override
	public List<OsChangeManagementProject> getProjectList(String source) {
		
		List<OsChangeManagementProject> projects = new ArrayList<OsChangeManagementProject>();
		OsChangeManagementRationalAdapterProjectList rationalAdapterProjectList = getCustomList(source, OsChangeManagementRationalAdapterProjectList.class);
		
		for(OsChangeManagementRationalAdapterProjectListItem item : rationalAdapterProjectList.getMember())
		{
			OsChangeManagementProject project = new OsChangeManagementProject();
			project.setName(item.getName());
			project.setUri(item.getUri());
			projects.add(project);
		}
		
		return projects;
	}
}