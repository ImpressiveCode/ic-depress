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
package org.impressivecode.depress.test.metric.checkstyle;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.impressivecode.depress.metric.checkstyle.CheckStyleEntriesParser;
import org.impressivecode.depress.metric.checkstyle.CheckStyleEntry;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * 
 * @author Tomasz Banach
 * @author £ukasz Waga
 * @author Monika Pruszkowska
 * 
 */
public class CheckStyleEntriesParserTest {

    CheckStyleEntriesParser parser = new CheckStyleEntriesParser();

    @Test
    public void shouldParseEntries() throws ParserConfigurationException, SAXException, IOException {
        List<CheckStyleEntry> results = parser.parseEntries("checkstyle-test.xml");
        assertEntry(results.get(6));
        assertEntry2(results.get(89));
    }

    @Test(expected = SAXException.class)
    public void shouldThrowExceptionWhenWrongFileStructure() throws ParserConfigurationException, SAXException,
            IOException {
        parser.parseEntries("wrongFile.xml");
    }

    private void assertEntry(final CheckStyleEntry checkStyleEntry) {
        assertThat(checkStyleEntry.getFileName()).isEqualTo(
                "com\\virtusa\\gto\\locc\\FileParser.html");
        assertThat(checkStyleEntry.getLineNumber()).isEqualTo("40");
        assertThat(checkStyleEntry.getColumnNumber()).isEqualTo("9");
        assertThat(checkStyleEntry.getSeverityType()).isEqualTo("error");
        assertThat(checkStyleEntry.getMessageText()).isEqualTo("Missing a Javadoc comment.");
        assertThat(checkStyleEntry.getSourcePlace()).isEqualTo("com.puppycrawl.tools.checkstyle.checks.javadoc.JavadocVariableCheck");
    }

    private void assertEntry2(final CheckStyleEntry checkStyleEntry) {
    	assertThat(checkStyleEntry.getFileName()).isEqualTo(
                "com\\virtusa\\gto\\locc\\MetricsPrinter.java");
        assertThat(checkStyleEntry.getLineNumber()).isEqualTo("33");
        assertThat(checkStyleEntry.getColumnNumber()).isEqualTo("5");
        assertThat(checkStyleEntry.getSeverityType()).isEqualTo("error");
        assertThat(checkStyleEntry.getMessageText()).isEqualTo("Missing a Javadoc comment.");
        assertThat(checkStyleEntry.getSourcePlace()).isEqualTo("com.puppycrawl.tools.checkstyle.checks.javadoc.JavadocVariableCheck");
    }

}
