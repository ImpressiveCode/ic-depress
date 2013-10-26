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
package org.impressivecode.depress.mr.eclipsemetrics;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.impressivecode.depress.mr.eclipsemetrics.EclipseMetricsEntriesParser;
import org.impressivecode.depress.mr.eclipsemetrics.EclipseMetricsEntryClassLevel;
import org.impressivecode.depress.mr.eclipsemetrics.EclipseMetricsEntryMethodLevel;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * 
 * @author Mateusz Kutyba, Wroclaw University of Technology
 * 
 */
public class EclipseMetricsEntriesParserTest {

    EclipseMetricsEntriesParser parser = new EclipseMetricsEntriesParser();

    @Test
    public void shouldParseEntries() throws ParserConfigurationException, SAXException, IOException {
        List<EclipseMetricsEntryClassLevel> results = parser.parseEntriesClassLevel("examples/eclipsemetrics.xml");
        assertThat(results).hasSize(956);
        assertEntry(results.get(6));
        assertEntry2(results.get(7));
    }

    @Test(expected = SAXException.class)
    public void shouldThrowExceptionWhenWrongFileStructure() throws ParserConfigurationException, SAXException,
            IOException {
        parser.parseEntriesClassLevel("tests/wrongFile.xml");
    }

    private void assertEntry(final EclipseMetricsEntryClassLevel eclipseMetricsEntry) {
        assertThat(eclipseMetricsEntry.getClassName()).isEqualTo(
                "org.apache.commons.math3.stat.Frequency$NaturalComparator");
        assertThat(eclipseMetricsEntry.getNumberOfOverriddenMethods()).isEqualTo(0.0);
        assertThat(eclipseMetricsEntry.getNumberOfAttributes()).isEqualTo(0.0);
        assertThat(eclipseMetricsEntry.getNumberOfChildren()).isEqualTo(0.0);
        assertThat(eclipseMetricsEntry.getNumberOfMethods()).isEqualTo(1.0);
        assertThat(eclipseMetricsEntry.getDepthOfInheritanceTree()).isEqualTo(1.0);
        assertThat(eclipseMetricsEntry.getLackOfCohesionOfMethods()).isEqualTo(0.0);
        assertThat(eclipseMetricsEntry.getNumberOfStaticMethods()).isEqualTo(0.0);
        assertThat(eclipseMetricsEntry.getSpecializationIndex()).isEqualTo(0.0);
        assertThat(eclipseMetricsEntry.getWeightedMethodsPerClass()).isEqualTo(1.0);
        assertThat(eclipseMetricsEntry.getNumberOfStaticAttributes()).isEqualTo(1.0);
    }

    private void assertEntry2(final EclipseMetricsEntryClassLevel eclipseMetricsEntry) {
        assertThat(eclipseMetricsEntry.getClassName()).isEqualTo(
                "org.apache.commons.math3.stat.Frequency");
        assertThat(eclipseMetricsEntry.getNumberOfOverriddenMethods()).isEqualTo(0.0);
        assertThat(eclipseMetricsEntry.getNumberOfAttributes()).isEqualTo(1.0);
        assertThat(eclipseMetricsEntry.getNumberOfChildren()).isEqualTo(0.0);
        assertThat(eclipseMetricsEntry.getNumberOfMethods()).isEqualTo(33.0);
        assertThat(eclipseMetricsEntry.getDepthOfInheritanceTree()).isEqualTo(1.0);
        assertThat(eclipseMetricsEntry.getLackOfCohesionOfMethods()).isEqualTo(0.0);
        assertThat(eclipseMetricsEntry.getNumberOfStaticMethods()).isEqualTo(0.0);
        assertThat(eclipseMetricsEntry.getSpecializationIndex()).isEqualTo(0.0);
        assertThat(eclipseMetricsEntry.getWeightedMethodsPerClass()).isEqualTo(59.0);
        assertThat(eclipseMetricsEntry.getNumberOfStaticAttributes()).isEqualTo(1.0);
    }

    @Test
    public void shouldParseEntriesMethodLevel() throws ParserConfigurationException, SAXException, IOException {
        List<EclipseMetricsEntryMethodLevel> results = parser.parseEntriesMethodLevel("examples/eclipsemetrics.xml");
        assertThat(results).hasSize(7100);
        assertEntry3(results.get(0));
    }

    private void assertEntry3(final EclipseMetricsEntryMethodLevel eclipseMetricsEntry) {
        assertThat(eclipseMetricsEntry.getMethodName()).isEqualTo(
                "org.apache.commons.math3.stat.descriptive.SummaryStatistics~clear");
        assertThat(eclipseMetricsEntry.getMethodLinesOfCode()).isEqualTo(14.0);
        assertThat(eclipseMetricsEntry.getNestedBlockDepth()).isEqualTo(2.0);
        assertThat(eclipseMetricsEntry.getMcCabeCyclomaticComplexity()).isEqualTo(3.0);
        assertThat(eclipseMetricsEntry.getNumberOfParameters()).isEqualTo(0.0);
    }
}
