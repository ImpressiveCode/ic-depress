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
package org.impressivecode.depress.mr.googleaudit;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.impressivecode.depress.mr.googleaudit.GoogleAuditXmlResult.Resource;

import com.google.common.base.Preconditions;

/**
 * @author Jadwiga Wozna, Wroclaw University of Technology
 */
public class GoogleAuditEntriesParser {
	  
	public static List<Resource> unmarshalResults(final String path) throws JAXBException {
	        Preconditions.checkArgument(!isNullOrEmpty(path), "Path has to be set.");
	        JAXBContext jaxbContext = JAXBContext.newInstance(GoogleAuditXmlResult.class);
	        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

	        GoogleAuditXmlResult result = (GoogleAuditXmlResult) unmarshaller.unmarshal(new File(path));
	        return result.getResource();
	    }
}
