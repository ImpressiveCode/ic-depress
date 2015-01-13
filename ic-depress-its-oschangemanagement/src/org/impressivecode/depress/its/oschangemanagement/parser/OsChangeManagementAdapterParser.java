/*
 ImpressiveCode Depress Framework
 Copyright (C) 2013  ImpressiveCode contributors

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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