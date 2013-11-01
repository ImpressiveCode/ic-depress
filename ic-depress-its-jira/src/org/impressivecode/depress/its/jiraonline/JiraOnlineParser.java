package org.impressivecode.depress.its.jiraonline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSPriority;
import org.impressivecode.depress.its.ITSResolution;
import org.impressivecode.depress.its.ITSStatus;
import org.impressivecode.depress.its.ITSType;
import org.impressivecode.depress.its.jiraonline.model.IssueVersion;
import org.impressivecode.depress.its.jiraonline.model.JiraFieldModel.Priority;
import org.impressivecode.depress.its.jiraonline.model.JiraFieldModel.Resolution;
import org.impressivecode.depress.its.jiraonline.model.JiraFieldModel.Status;
import org.impressivecode.depress.its.jiraonline.model.JiraFieldModel.Type;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineIssueModel;
import org.impressivecode.depress.its.jiraonline.model.JiraOnlineIssuesListModel;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

public class JiraOnlineParser {
	public static List<ITSDataType> parse(String source) {

		ObjectMapper objectMapper = new ObjectMapper();
		JsonFactory jsonFactory = new JsonFactory();
		JsonParser jp = null;
		JiraOnlineIssuesListModel issueList = null;

		try {
			jp = jsonFactory.createJsonParser(source);
			issueList = objectMapper.readValue(jp,
					new TypeReference<JiraOnlineIssuesListModel>() {
					});
		} catch (JsonParseException e) {
		} catch (UnrecognizedPropertyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return parseData(issueList);
	}

	private static List<ITSDataType> parseData(
			JiraOnlineIssuesListModel issueList) {
		List<ITSDataType> resultList = new ArrayList<ITSDataType>();

		for (JiraOnlineIssueModel issue : issueList.getIssues()) {
			ITSDataType data = new ITSDataType();
			data.setIssueId(Integer.toString(issue.getId()));
			data.setLink(issue.getLink());

			Set<String> assignees = new HashSet<>();
			if(issue.getFields().getAssignee() != null) {
				assignees.add(issue.getFields().getAssignee().getName());
			}
			data.setAssignees(assignees);

			data.setPriority(parsePriority(issue.getFields().getPriority()));

			data.setType(parseType(issue.getFields().getIssueType()));
			data.setStatus(parseStatus(issue.getFields().getStatus()));

			data.setCreated(issue.getFields().getCreated());
			data.setUpdated(issue.getFields().getUpdated());

			data.setResolution(parseResolution(issue.getFields().getResolution()));

			List<String> versions = new ArrayList<>();
			for (IssueVersion version : issue.getFields().getVersions()) {
				versions.add(version.getName());
			}
			data.setVersion(versions);

			List<String> fixVersions = new ArrayList<>();
			for (IssueVersion version : issue.getFields().getFixVersions()) {
				fixVersions.add(version.getName());
			}
			data.setFixVersion(fixVersions);

			data.setReporter(issue.getFields().getReporter().getName());

			resultList.add(data);
		}

		return resultList;
	}

	private static ITSResolution parseResolution(Resolution resolution) {
		if (resolution == null || resolution.getName() == null) {
			return ITSResolution.UNKNOWN;
		}
		switch (resolution.getName()) {
		case "Unresolved":
			return ITSResolution.UNRESOLVED;
		case "Fixed":
			return ITSResolution.FIXED;
		case "Wont't Fix":
			return ITSResolution.WONT_FIX;
		case "Duplicate":
			return ITSResolution.DUPLICATE;
		case "Invalid":
			return ITSResolution.INVALID;
		case "Incomplete":
			return ITSResolution.INVALID;
		case "Cannot Reproduce":
			return ITSResolution.WONT_FIX;
		case "Later":
			return ITSResolution.WONT_FIX;
		case "Not A Problem":
			return ITSResolution.WONT_FIX;
		case "Implemented":
			return ITSResolution.FIXED;
		default:
			return ITSResolution.UNKNOWN;
		}
	}

	private static ITSStatus parseStatus(Status status) {
		if (status == null || status.getName() == null) {
			return ITSStatus.UNKNOWN;
		}
		switch (status.getName()) {
		case "Open":
			return ITSStatus.OPEN;
		case "Reopen":
			return ITSStatus.REOPEN;
		case "In Progress":
			return ITSStatus.IN_PROGRESS;
		case "Resolved":
			return ITSStatus.RESOLVED;
		case "Closed":
			return ITSStatus.CLOSED;
		default:
			return ITSStatus.UNKNOWN;
		}
	}

	private static ITSType parseType(Type type) {
		if (type == null || type.getName() == null) {
			return ITSType.UNKNOWN;
		}
		switch (type.getName()) {
		case "Bug":
			return ITSType.BUG;
		case "Test":
			return ITSType.TEST;
		case "Improvement":
		case "New Feature":
		case "Task":
		case "Wish":
			return ITSType.ENHANCEMENT;
		default:
			return ITSType.UNKNOWN;
		}
	}

	private static ITSPriority parsePriority(Priority priority) {

		if (priority == null || priority.getName() == null) {
			return ITSPriority.UNKNOWN;
		}
		switch (priority.getName()) {
		case "Trivial":
			return ITSPriority.TRIVIAL;
		case "Minor":
			return ITSPriority.MINOR;
		case "Major":
			return ITSPriority.MAJOR;
		case "Critical":
			return ITSPriority.CRITICAL;
		case "Blocker":
			return ITSPriority.BLOCKER;
		default:
			return ITSPriority.UNKNOWN;
		}
	}
}
