package org.impressivecode.depress.its.oschangemanagement;

import java.io.File;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.oschangemanagement.model.OsChangeManagementProject;
import org.impressivecode.depress.its.oschangemanagement.model.rationaladapter.*;
import org.impressivecode.depress.its.oschangemanagement.parser.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * 
 * @author Marcin Cho³uj, Wroclaw University of Technology
 * @author Piotr Malek, Wroclaw University of Technology
 * @author Przemys³aw Trepka, Wroclaw University of Technology
 * @author £ukasz Trojak, Wroclaw University of Technology
 * 
 */

public class OsChangeManagementRationalAdapterParserTest {

	private final String PROJECTS_LIST_PATH = "projects_list.txt";
	private final String ISSUES_LIST_PATH = "issues_list.txt";
	private String projectsListJSON;
	private String issuesListJSON;
	private OsChangeManagementRationalAdapterParser osChangeManagementRationalAdapterParser;
	
	@Before
	public void setUp() throws Exception {
		projectsListJSON = new Scanner( new File(getClass().getResource(PROJECTS_LIST_PATH).getPath()) ).useDelimiter("\\A").next();
		issuesListJSON = new Scanner( new File(getClass().getResource(ISSUES_LIST_PATH).getPath()) ).useDelimiter("\\A").next();
		osChangeManagementRationalAdapterParser = new OsChangeManagementRationalAdapterParser();
    }
	
	@Test(expected = NullPointerException.class)
	public void parseJSON_when_null_source_then_throw_NullPointerException() {
		OsChangeManagementRationalAdapterParser.parseJSON(null, OsChangeManagementRationalAdapterProjectList.class);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void parseJSON_when_null_elem_then_throw_IllegalArgumentException() {
		OsChangeManagementRationalAdapterParser.parseJSON(projectsListJSON, null);
	}
	
	@Test
	public void parseJSON_when_class_math_JSON_then_return_object_of_this_class() {
		assertThat(OsChangeManagementRationalAdapterParser.parseJSON(projectsListJSON, OsChangeManagementRationalAdapterProjectList.class), is(OsChangeManagementRationalAdapterProjectList.class));
	}
	
	@Test(expected = Exception.class)
	public void getProjectList_when_null_source_then_throw_Exception() {
		osChangeManagementRationalAdapterParser.getProjectList(null);
	}
	
	@Test
	public void getProjectList_when_correct_source_then_return_List_of_OsChangeManagementProject_object() {
		assertThat(osChangeManagementRationalAdapterParser.getProjectList(projectsListJSON).get(0), is(OsChangeManagementProject.class));
	}

	@Test(expected = Exception.class)
	public void getIssueCount_when_null_source_then_throw_Exception() {
		osChangeManagementRationalAdapterParser.getIssueCount(null);
	}

	@Test
	public void getIssueCount_when_correct_source_then_return_int() {
		assertTrue(osChangeManagementRationalAdapterParser.getIssueCount(issuesListJSON) >0 );
	}
	
	@Test(expected = Exception.class)
	public void getIssuest_when_null_source_then_throw_Exception() {
		osChangeManagementRationalAdapterParser.getIssueCount(null);
	}

	@Test
	public void getIssues_when_correct_source_then_return_List_of_ITSDataType_object() {
		assertThat(osChangeManagementRationalAdapterParser.getIssues(issuesListJSON).get(0), is(ITSDataType.class));
	}

}