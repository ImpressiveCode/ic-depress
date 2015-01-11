package org.impressivecode.depress.its.oschangemanagement.refreshcaller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.impressivecode.depress.its.oschangemanagement.OsChangeManagementAdapterNodeDialog;

public class RefreshCaller implements Callable<List<String>>{
	
		private String propertyName;
	
	    private static final String BLOCKER = "Blocker";
	    private static final String CRITICAL = "Critical";
	    private static final String MAJOR = "Major";
	    private static final String MINOR = "Minor";
	    private static final String TRIVIAL = "Trivial";
	    
	    private static final String OPEN = "Open";
	    private static final String IN_PROGRESS = "In Progress";
	    private static final String REOPENED = "Reopened";
	    private static final String RESOLVED = "Resolved";
	    private static final String CLOSED = "Closed";
	    private static final String DONE = "Done";
	    private static final String TO_DO = "To Do";
	    
	    private static final String NEW_FEATURE = "New Feature";
	    private static final String BUG = "Bug";
	    private static final String SUB_TASK = "Sub-task";
	    private static final String TASK = "Task";
	    private static final String IMPROVEMENT = "Improvement";
	    
	    private static final String FIXED = "Fixed";
	    private static final String WONT_FIX = "Won't Fix";
	    private static final String DUPLICATE = "Duplicate";
	    private static final String INCOMPLETE = "Incomplete";
	    private static final String CANNOT_REPRODUCE = "Cannot Reproduce";

	    public RefreshCaller(String propertyName) {
	        this.propertyName = propertyName;
	    }

		@Override
		public List<String> call() throws Exception {
			switch (propertyName){
			case OsChangeManagementAdapterNodeDialog.PRIORITY:
				return getPriorityList();
			case OsChangeManagementAdapterNodeDialog.STATE:
				return getTypeList();
			case OsChangeManagementAdapterNodeDialog.TYPE:
				return getStateList();
			case OsChangeManagementAdapterNodeDialog.RESOLUTION:
				return getResolutionList();
				default:
					return null;
			}
		}
		
		public List<String> getPriorityList() {
			ArrayList<String> priorityList = new ArrayList<String>();
			priorityList.add(BLOCKER);
			priorityList.add(CRITICAL);
			priorityList.add(MAJOR);
			priorityList.add(MINOR);
			priorityList.add(TRIVIAL); 
			return priorityList;
		}

		public List<String> getTypeList() {
			ArrayList<String> typeList = new ArrayList<String>();
			typeList.add(OPEN);
			typeList.add(IN_PROGRESS);
			typeList.add(REOPENED);
			typeList.add(RESOLVED);
			typeList.add(CLOSED);
			typeList.add(DONE);
			typeList.add(TO_DO);
			return typeList;
		}

		public List<String> getStateList() {
			ArrayList<String> stateList = new ArrayList<String>();
			stateList.add(NEW_FEATURE);
			stateList.add(BUG);
			stateList.add(SUB_TASK);
			stateList.add(TASK);
			stateList.add(IMPROVEMENT);
			return stateList;
		}
		public List<String> getResolutionList() {
			ArrayList<String> resolutionList = new ArrayList<String>();
			resolutionList.add(FIXED);
			resolutionList.add(WONT_FIX);
			resolutionList.add(DUPLICATE);
			resolutionList.add(INCOMPLETE);
			resolutionList.add(CANNOT_REPRODUCE);
			return resolutionList;
		}

}
