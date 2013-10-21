package org.impressivecode.depress.its.bugzilla;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSPriority;
import org.impressivecode.depress.its.ITSResolution;
import org.impressivecode.depress.its.ITSStatus;

import com.google.common.collect.Lists;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * @author Piotr Wr�blewski
 * 
 */
public class BugzillaOnlineParser {

	public final List<ITSDataType> parseEntries(final Object[] elements) {
		List<ITSDataType> entries = Lists.newLinkedList();
		for (Object element : elements) {
			entries.add(extract(element));
		}
		return entries;
	}

	@SuppressWarnings("unchecked")
	protected ITSDataType extract(Object element) {
		ITSDataType itsElement = new ITSDataType();
		Map<String, Object> paramMap = (Map<String, Object>) element;
		try {
			setSpecificField(itsElement, paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itsElement;
	}

	protected void setSpecificField(ITSDataType itsElement,
			Map<String, Object> paramMap) {
		itsElement.setIssueId(paramMap.get("id").toString());
		itsElement.setCreated((Date) paramMap.get("creation_time"));
		itsElement
				.setPriority(getPriority(paramMap.get("priority").toString()));
		itsElement.setStatus(getStatus(paramMap.get("status").toString()));
		itsElement.setSummary(paramMap.get("summary").toString());
		itsElement.setUpdated((Date) paramMap.get("last_change_time"));
		itsElement.setResolution(getResolution(paramMap.get("resolution")
				.toString()));
		itsElement.setReporter(paramMap.get("assigned_to").toString());
		itsElement.setLink(paramMap.get("url").toString());
		List<String> version = new ArrayList<String>();
		version.add(paramMap.get("version").toString());
		itsElement.setVersion(version);

		itsElement.setComments(new ArrayList<String>()); // Bugzilla::Webservice::Bug
															// comments method

	}

	private ITSResolution getResolution(String resolution) {
		if (resolution == null) {
			return ITSResolution.UNKNOWN;
		}
		switch (resolution) {
		case "---":
			return ITSResolution.UNRESOLVED;
		case "FIXED":
			return ITSResolution.FIXED;
		case "WONTFIX":
			return ITSResolution.WONT_FIX;
		case "DUPLICATE":
			return ITSResolution.DUPLICATE;
		case "INVALID":
			return ITSResolution.INVALID;
		case "INCOMPLETE":
			return ITSResolution.INVALID;
		case "WORKSFORME":
			return ITSResolution.INVALID;
		default:
			return ITSResolution.UNKNOWN;
		}
	}

	private ITSStatus getStatus(String status) {
		if (status == null) {
			return ITSStatus.UNKNOWN;
		}
		switch (status) {
		case "UNCONFIRMED":
			return ITSStatus.OPEN;
		case "NEW":
			return ITSStatus.OPEN;
		case "REOPENED":
			return ITSStatus.REOPEN;
		case "ASSIGN":
			return ITSStatus.IN_PROGRESS;
		case "RESOLVED":
			return ITSStatus.RESOLVED;
		case "VERIFIED":
			return ITSStatus.RESOLVED;
		case "CLOSED":
			return ITSStatus.CLOSED;
		default:
			return ITSStatus.UNKNOWN;
		}
	}

	private ITSPriority getPriority(String priority) {
		if (priority == null) {
			return ITSPriority.UNKNOWN;
		}
		switch (priority) {
		case "trivial":
			return ITSPriority.TRIVIAL;
		case "normal":
			return ITSPriority.MINOR;
		case "minor":
			return ITSPriority.MINOR;
		case "major":
			return ITSPriority.MAJOR;
		case "critical":
			return ITSPriority.CRITICAL;
		case "blocker":
			return ITSPriority.BLOCKER;
		default:
			return ITSPriority.UNKNOWN;
		}
	}

}
