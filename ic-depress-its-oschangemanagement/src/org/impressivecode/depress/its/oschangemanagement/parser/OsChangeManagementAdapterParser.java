package org.impressivecode.depress.its.oschangemanagement.parser;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.oschangemanagement.model.OsChangeManagementProject;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Marcin Cho³uj, Wroclaw University of Technology
 * @author Piotr Malek, Wroclaw University of Technology
 * @author Przemys³aw Trepka, Wroclaw University of Technology
 * @author £ukasz Trojak, Wroclaw University of Technology
 * 
 */

public abstract class OsChangeManagementAdapterParser {

	@SuppressWarnings("unchecked")
	public static <T> T parseJSON(String source, Class<?> elem) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = null;
        T field = null;

        try {
            jp = jsonFactory.createJsonParser(source);
            field = (T) objectMapper.readValue(jp, elem); 
        } catch (IOException e) {
            Logger.getLogger("Error").severe(e.getMessage());
        }
        
        return field;
	}
	
	public abstract List<OsChangeManagementProject> getProjectList(String source);
	public abstract int getIssueCount(String source);
	public abstract List<ITSDataType> getIssues(String source);
}