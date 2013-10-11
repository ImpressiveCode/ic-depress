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
package org.impressivecode.depress.metric.checkstyle;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 * @author Tomasz Banach
 * @author �ukasz Waga
 * @author Monika Pruszkowska
 * 
 */
public class CheckStyleEntryTest {

    @Test
    public void shouldReturnSetValue() {
        CheckStyleEntry entry = new CheckStyleEntry();

        String className = "package.name.then.ClassName$InternalClassName";
        entry.setFileName(className);
        assertEquals(className, entry.getFileName());

        String expected = "2";
        entry.setLineNumber(expected);
        assertEquals(expected, entry.getLineNumber());

        expected = "3";
        entry.setColumnNumber(expected);
        assertEquals(expected, entry.getColumnNumber());

        expected = "This is a proper text message";
        entry.setMessageText(expected);
        assertEquals(expected, entry.getMessageText());

        expected = "error";
        entry.setSeverityType(expected);
        assertEquals(expected, entry.getSeverityType());

        expected = "file\\sou.rce";
        entry.setSourcePlace(expected);
        assertEquals(expected, entry.getSourcePlace());

    }


}
