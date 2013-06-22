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
package org.impressivecode.depress.test.metric.pmd;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.impressivecode.depress.metric.pmd.PMDEntriesParser;
import org.impressivecode.depress.metric.pmd.PMDEntry;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * 
 * @author Tomasz Banach
 * @author £ukasz Waga
 * @author Monika Pruszkowska
 * 
 */
public class PMDEntriesParserTest {

    PMDEntriesParser parser = new PMDEntriesParser();

    @Test
    public void shouldParseEntries() throws ParserConfigurationException, SAXException, IOException {
        List<PMDEntry> results = parser.parseEntries("pmd.xml");
        assertEntry(results.get(1));
        assertEntry2(results.get(12));
    }

    @Test(expected = SAXException.class)
    public void shouldThrowExceptionWhenWrongFileStructure() throws ParserConfigurationException, SAXException,
            IOException {
        parser.parseEntries("wrongFile.xml");
    }

    private void assertEntry(final PMDEntry pMDEntry) {
        assertThat(pMDEntry.getFileName()).isEqualTo(
                "Yang.java");
        assertThat(pMDEntry.getBeginLine()).isEqualTo("2");
        assertThat(pMDEntry.getEndLine()).isEqualTo("2");
        assertThat(pMDEntry.getBeginColumn()).isEqualTo("0");
        assertThat(pMDEntry.getEndColumn()).isEqualTo("0");
        assertThat(pMDEntry.getRule()).isEqualTo("MethodArgumentCouldBeFinal");
        assertThat(pMDEntry.getRuleSet()).isEqualTo("Optimization Rules");
        assertThat(pMDEntry.getPackageName()).isEqualTo("(default)");
        assertThat(pMDEntry.getClassName()).isEqualTo("Yang");
        assertThat(pMDEntry.getInfoUrl()).isEqualTo("http://pmd.sourceforge.net/rules/optimizations.html#MethodArgumentCouldBeFinal");
        assertThat(pMDEntry.getPriority()).isEqualTo("3");
        assertThat(pMDEntry.getMessageText()).isEqualTo("Parameter 'str' is not assigned and could be declared final");
    }

    private void assertEntry2(final PMDEntry pMDEntry) {
    	assertThat(pMDEntry.getFileName()).isEqualTo(
                "Ying.java");
        assertThat(pMDEntry.getBeginLine()).isEqualTo("3");
        assertThat(pMDEntry.getEndLine()).isEqualTo("3");
        assertThat(pMDEntry.getBeginColumn()).isEqualTo("0");
        assertThat(pMDEntry.getEndColumn()).isEqualTo("0");
        assertThat(pMDEntry.getRule()).isEqualTo("UnusedPrivateField");
        assertThat(pMDEntry.getRuleSet()).isEqualTo("Unused Code Rules");
        assertThat(pMDEntry.getPackageName()).isEqualTo("(default)");
        assertThat(pMDEntry.getClassName()).isEqualTo("Ying");
        assertThat(pMDEntry.getInfoUrl()).isEqualTo("http://pmd.sourceforge.net/rules/unusedcode.html#UnusedPrivateField");
        assertThat(pMDEntry.getPriority()).isEqualTo("3");
        assertThat(pMDEntry.getMessageText()).isEqualTo("Avoid unused private fields such as 'gOOD'.");
    }

}
