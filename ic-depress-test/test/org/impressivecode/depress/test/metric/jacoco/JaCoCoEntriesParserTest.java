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
package org.impressivecode.depress.test.metric.jacoco;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.impressivecode.depress.metric.jacoco.findbugEntriesParser;
import org.impressivecode.depress.metric.jacoco.findbugEntry;
import org.junit.Test;
import org.xml.sax.SAXException;
/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class JaCoCoEntriesParserTest {

    @Test
    public void shouldParseEntries() throws ParserConfigurationException, SAXException, IOException {
        String path = getClass().getResource("jacoco.xml").getPath();
        findbugEntriesParser parser = new findbugEntriesParser();
        List<findbugEntry> results = parser.parseEntries(path);
        assertThat(results).hasSize(3);
        assertEntry(results.get(0));
    }

    private void assertEntry(final findbugEntry findbugEntry) {
        assertThat(findbugEntry.getClassName()).isEqualTo("org.apache.commons.math.optimization.direct.DirectSearchOptimizer");
        assertThat(findbugEntry.getBranchCoverageCounter()).isNotNull();
        assertThat(findbugEntry.getClassCoverageCounter()).isNotNull();
        assertThat(findbugEntry.getComplexityCoverageCounter()).isNotNull();
        assertThat(findbugEntry.getInstructionCoverageCounter()).isNotNull();
        assertThat(findbugEntry.getLineCoverageCounter()).isNotNull();
        assertThat(findbugEntry.getMethodCoverageCounter()).isNotNull();
    }
}
