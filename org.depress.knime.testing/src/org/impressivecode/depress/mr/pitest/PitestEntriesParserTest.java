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
package org.impressivecode.depress.mr.pitest;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.impressivecode.depress.mr.pitest.PitestEntriesParser;
import org.impressivecode.depress.mr.pitest.PitestEntry;
import org.junit.Test;
import org.xml.sax.SAXException;
/**
 * 
 * @author Zuzanna Pacholczyk, Capgemini Polska
 * 
 */
public class PitestEntriesParserTest {

    @Test
    public void shouldParseEntries() throws ParserConfigurationException, SAXException, IOException {
        String path = getClass().getResource("pitest.xml").getPath();
        PitestEntriesParser parser = new PitestEntriesParser();
        List<PitestEntry> results = parser.parseEntries(path);
        assertThat(results).hasSize(206);
        assertEntry(results.get(0));
    }

    private void assertEntry(final PitestEntry pitestEntry) {
        assertThat(pitestEntry.getMutatedClass()).isNotNull();
        assertEquals(0.78, 0.001, pitestEntry.getMutationScoreIndicator());
    }
}
