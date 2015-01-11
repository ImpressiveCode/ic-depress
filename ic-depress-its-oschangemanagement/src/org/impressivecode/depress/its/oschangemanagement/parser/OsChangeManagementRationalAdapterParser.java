package org.impressivecode.depress.its.oschangemanagement.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSPriority;
import org.impressivecode.depress.its.oschangemanagement.model.OsChangeManagementProject;
import org.impressivecode.depress.its.oschangemanagement.model.rationaladapter.OsChangeManagementRationalAdapterIssueListItem;
import org.impressivecode.depress.its.oschangemanagement.model.rationaladapter.OsChangeManagementRationalAdapterIssuesList;
import org.impressivecode.depress.its.oschangemanagement.model.rationaladapter.OsChangeManagementRationalAdapterProjectList;
import org.impressivecode.depress.its.oschangemanagement.model.rationaladapter.OsChangeManagementRationalAdapterProjectListItem;

public class OsChangeManagementRationalAdapterParser extends
		OsChangeManagementAdapterParser {
	
	@Override
	public List<OsChangeManagementProject> getProjectList(String source) {
		
		List<OsChangeManagementProject> projects = new ArrayList<OsChangeManagementProject>();
		OsChangeManagementRationalAdapterProjectList rationalAdapterProjectList = parseJSON(source, OsChangeManagementRationalAdapterProjectList.class);
		
		for(OsChangeManagementRationalAdapterProjectListItem item : rationalAdapterProjectList.getMember())
		{
			OsChangeManagementProject project = new OsChangeManagementProject();
			project.setName(item.getName());
			project.setUri(item.getUri());
			projects.add(project);
		}
		
		return projects;
	}

	@Override
	public int getIssueCount(String source) {
		OsChangeManagementRationalAdapterIssuesList issues = parseJSON(source, OsChangeManagementRationalAdapterIssuesList.class);
		return issues.getResponseInfo().getTotalCount();
	}
	
	@Override
	public List<ITSDataType> getIssues(String source) {
		OsChangeManagementRationalAdapterIssuesList issues = parseJSON(source, OsChangeManagementRationalAdapterIssuesList.class);
		ArrayList<ITSDataType> ret = new ArrayList<ITSDataType>();
		
		for(OsChangeManagementRationalAdapterIssueListItem item : issues.getResults()){
			ITSDataType its = new ITSDataType();
			its.setAssignees(item.getContributorAsSet());
			try {
				its.setCreated(dateStringToObject(item.getCreated()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			its.setDescription(item.getTitle());
			
			its.setReporter(item.getFirstCreator());
			its.setStatus(item.getStatusAsITS());
			its.setType(item.getTypeAsITS());
			its.setPriority(item.getPriorityAsITS());
			its.setResolution(item.getResolutionAsITS());
			try {
				its.setUpdated(dateStringToObject(item.getModified()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			its.setIssueId(item.getShortTitle());
			
			ret.add(its);
		}
		
		return ret;
	}
	
	private Date dateStringToObject(String date) throws ParseException{
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy:HH:mm:SSZ");
		Date parsedDate = formatter.parse(date);
		return parsedDate;
	}
}