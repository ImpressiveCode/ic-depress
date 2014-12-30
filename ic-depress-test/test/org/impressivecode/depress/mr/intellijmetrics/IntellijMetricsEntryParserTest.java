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
package org.impressivecode.depress.mr.intellijmetrics;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import org.impressivecode.depress.mr.intellijmetrics.IntellijMetricsEntry;
import org.impressivecode.depress.mr.intellijmetrics.IntellijMetricsEntriesParser;

/**
 * 
 * @author Maciej Mickiewicz, Wroclaw University of Technology
 * 
 */
public class IntellijMetricsEntryParserTest {

    @Test
    public void shouldParseEntries() throws ParserConfigurationException, SAXException, IOException {
        String path = getClass().getResource("intellijmetrics.xml").getPath();
        IntellijMetricsEntriesParser parser = new IntellijMetricsEntriesParser();
        List<IntellijMetricsEntry> results = parser.parseEntries(path);
        assertThat(results).hasSize(12);
    }

    @Test
    public void shouldHaveValidResults() throws ParserConfigurationException, SAXException, IOException {
        String path = getClass().getResource("intellijmetrics.xml").getPath();
        IntellijMetricsEntriesParser parser = new IntellijMetricsEntriesParser();
        List<IntellijMetricsEntry> results = parser.parseEntries(path);
        assertEntry(results.get(0));
    }
    
    @Test
    public void shouldHaveValidResults2() throws ParserConfigurationException, SAXException, IOException {
        String path = getClass().getResource("intellijmetrics.xml").getPath();
        IntellijMetricsEntriesParser parser = new IntellijMetricsEntriesParser();
        List<IntellijMetricsEntry> results = parser.parseEntries(path);
        assertEntry2(results.get(3));
    }
    
    private void assertEntry(final IntellijMetricsEntry intellijMetricsEntry) {
        assertThat(intellijMetricsEntry.getClassName()).isEqualTo("org.gjt.sp.jedit.jEdit");
        assertThat(intellijMetricsEntry.getSeverityErrorCounter()).isEqualTo(2);
        assertThat(intellijMetricsEntry.getSeverityInfoCounter()).isEqualTo(1);
        assertThat(intellijMetricsEntry.getSeverityServerProblemCounter()).isNotNull();
        assertThat(intellijMetricsEntry.getSeverityTypoCounter()).isEqualTo(1);
        assertThat(intellijMetricsEntry.getSeverityWarningCounter()).isNotNull();
        assertThat(intellijMetricsEntry.getSeverityWeakWarningCounter()).isNotNull();
    }
    
    private void assertEntry2(final IntellijMetricsEntry intellijMetricsEntry) {
        assertThat(intellijMetricsEntry.getClassName()).isEqualTo("org.gjt.sp.jedit.syntax.ModeProvider");
        assertThat(intellijMetricsEntry.getValue("ERROR")).isEqualTo(0);
        assertThat(intellijMetricsEntry.getValue("INFO")).isEqualTo(1);
        assertThat(intellijMetricsEntry.getValue("SERVER PROBLEM")).isEqualTo(0);
        assertThat(intellijMetricsEntry.getValue("TYPO")).isEqualTo(0);
        assertThat(intellijMetricsEntry.getValue("WARNING")).isEqualTo(0);
        assertThat(intellijMetricsEntry.getValue("WEAK WARNING")).isEqualTo(0);
    }
	
}
