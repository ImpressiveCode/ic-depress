package org.impressivecode.depress.its.oschangemanagement.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class OsChangeManagementAdapterParser {

	public static <T> List<T> getCustomList(String source, Class<?> elem) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = null;
        List<T> fieldList = null;

        try {
            jp = jsonFactory.createJsonParser(source);
            fieldList = objectMapper.readValue(jp,
                    objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, elem));
        } catch (IOException e) {
            Logger.getLogger("Error").severe(e.getMessage());
        }
        return fieldList;
	}
}
