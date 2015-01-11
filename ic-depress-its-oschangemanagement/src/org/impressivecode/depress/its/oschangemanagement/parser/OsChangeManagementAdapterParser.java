package org.impressivecode.depress.its.oschangemanagement.parser;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.impressivecode.depress.its.oschangemanagement.model.OsChangeManagementProject;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class OsChangeManagementAdapterParser {

	public static <T> T parseJSON(String source, Class<?> elem) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = null;

        T field = null;

        try {
            jp = jsonFactory.createJsonParser(source);
            field = (T) objectMapper.readValue(jp, elem); 
                    //objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, elem));

        } catch (IOException e) {
            Logger.getLogger("Error").severe(e.getMessage());
        }
        return field;
	}
	
	
	
	public abstract List<OsChangeManagementProject> getProjectList(String source);
	public abstract int getIssueCount(String source);
	
}