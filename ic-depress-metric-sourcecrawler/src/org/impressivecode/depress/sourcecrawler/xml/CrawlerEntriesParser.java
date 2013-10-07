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
package org.impressivecode.depress.sourcecrawler.xml;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.base.Preconditions.*;
import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.impressivecode.depress.sourcecrawler.model.SourceCrawlerOutput;

/**
 * 
 * @author Marek Majchrzak, modiefied by Pawel Nosal, ImpressiveCode
 * 
 */
public class CrawlerEntriesParser {

	public SourceCrawlerOutput parseSourceCrawlerResult(final String path)
			throws JAXBException {
		checkArgument(!isNullOrEmpty(path), "Path has to be set.");
		JAXBContext jaxbContext = JAXBContext
				.newInstance(SourceCrawlerOutput.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		SourceCrawlerOutput result = (SourceCrawlerOutput) unmarshaller
				.unmarshal(new File(path));
		return result;
	}

}
