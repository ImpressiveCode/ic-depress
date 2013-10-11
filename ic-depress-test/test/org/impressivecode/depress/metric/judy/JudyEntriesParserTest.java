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
package org.impressivecode.depress.metric.judy;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.impressivecode.depress.metric.judy.JudyXmlResult.Classes.Class;
import org.junit.Test;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class JudyEntriesParserTest {

    @Test
    public void shouldUnmarshalResults() throws JAXBException {
        List<Class> results = JudyEntriesParser.unmarshalResults(getClass().getResource("judy.xml").getPath());
        assertEquals(results.size(), 2);
    }
}
