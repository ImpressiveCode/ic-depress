package org.impressivecode.depress.its.oschangemanagement.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.impressivecode.depress.its.oschangemanagement.model.OsChangeManagementProjectList;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class OsChangeManagementAdapterParser {

	public static <T> T getCustomList(String source, Class<?> elem) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = null;
        T fieldList = null;

        try {
            jp = jsonFactory.createJsonParser(source);
           fieldList = (T) objectMapper.readValue(jp,elem);
      //      OsChangeManagementProjectList p = (OsChangeManagementProjectList) objectMapper.readValue(jp,elem);
        } catch (IOException e) {
            Logger.getLogger("Error").severe(e.getMessage());
        }
        return fieldList;
	}
}
