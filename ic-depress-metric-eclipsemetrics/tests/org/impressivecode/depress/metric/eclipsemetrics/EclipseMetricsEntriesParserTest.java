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
package org.impressivecode.depress.metric.eclipsemetrics;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * 
 * @author Mateusz Kutyba, Wroclaw University of Technology
 * 
 */
public class EclipseMetricsEntriesParserTest {

    @Test
    public void shouldParseEntries() throws ParserConfigurationException, SAXException, IOException {
        EclipseMetricsEntriesParser parser = new EclipseMetricsEntriesParser();
        List<EclipseMetricsEntry> results = parser.parseEntries("examples/eclipsemetrics.xml");
        assertThat(results).hasSize(699);
        assertEntry(results.get(0));
    }

    private void assertEntry(final EclipseMetricsEntry eclipseMetricsEntry) {
        assertThat(eclipseMetricsEntry.getClassName()).isEqualTo("org.apache.commons.math3.linear.BlockRealMatrix");
        assertThat(eclipseMetricsEntry.getNumberOfOverriddenMethods()).isEqualTo(31.0);
        assertThat(eclipseMetricsEntry.getNumberOfAttributes()).isEqualTo(5.0);
        assertThat(eclipseMetricsEntry.getNumberOfChildren()).isEqualTo(0.0);
        assertThat(eclipseMetricsEntry.getNumberOfMethods()).isEqualTo(52.0);
        assertThat(eclipseMetricsEntry.getDepthOfInheritanceTree()).isEqualTo(3.0);
        assertThat(eclipseMetricsEntry.getLackOfCohesionOfMethods()).isEqualTo(0.365);
        assertThat(eclipseMetricsEntry.getNumberOfStaticMethods()).isEqualTo(2.0);
        assertThat(eclipseMetricsEntry.getSpecializationIndex()).isEqualTo(1.788);
        assertThat(eclipseMetricsEntry.getWeightedMethodsPerClass()).isEqualTo(197.0);
        assertThat(eclipseMetricsEntry.getNumberOfStaticAttributes()).isEqualTo(2.0);
    }
}
