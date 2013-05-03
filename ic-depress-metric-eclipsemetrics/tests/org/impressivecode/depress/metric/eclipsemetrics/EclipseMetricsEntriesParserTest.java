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

    EclipseMetricsEntriesParser parser = new EclipseMetricsEntriesParser();

    @Test
    public void shouldParseEntries() throws ParserConfigurationException, SAXException, IOException {
        List<EclipseMetricsEntry> results = parser.parseEntriesClassLevel("examples/eclipsemetrics.xml");
        assertThat(results).hasSize(699);
        assertEntry(results.get(582));
        assertEntry2(results.get(195));
    }

    @Test(expected = SAXException.class)
    public void shouldThrowExceptionWhenWrongFileStructure() throws ParserConfigurationException, SAXException,
            IOException {
        parser.parseEntriesClassLevel("tests/wrongFile.xml");
    }

    private void assertEntry(final EclipseMetricsEntry eclipseMetricsEntry) {
        assertThat(eclipseMetricsEntry.getClassName()).isEqualTo(
                "org.apache.commons.math3.linear.OpenMapRealVector$OpenMapEntry");
        assertThat(eclipseMetricsEntry.getNumberOfOverriddenMethods()).isEqualTo(3.0);
        assertThat(eclipseMetricsEntry.getNumberOfAttributes()).isEqualTo(1.0);
        assertThat(eclipseMetricsEntry.getNumberOfChildren()).isEqualTo(0.0);
        assertThat(eclipseMetricsEntry.getNumberOfMethods()).isEqualTo(4.0);
        assertThat(eclipseMetricsEntry.getDepthOfInheritanceTree()).isEqualTo(2.0);
        assertThat(eclipseMetricsEntry.getLackOfCohesionOfMethods()).isEqualTo(0.0);
        assertThat(eclipseMetricsEntry.getNumberOfStaticMethods()).isEqualTo(0.0);
        assertThat(eclipseMetricsEntry.getSpecializationIndex()).isEqualTo(1.5);
        assertThat(eclipseMetricsEntry.getWeightedMethodsPerClass()).isEqualTo(4.0);
        assertThat(eclipseMetricsEntry.getNumberOfStaticAttributes()).isEqualTo(0.0);
    }

    private void assertEntry2(final EclipseMetricsEntry eclipseMetricsEntry) {
        assertThat(eclipseMetricsEntry.getClassName()).isEqualTo(
                "org.apache.commons.math3.analysis.interpolation.BicubicSplineFunction");
        assertThat(eclipseMetricsEntry.getNumberOfOverriddenMethods()).isEqualTo(0.0);
        assertThat(eclipseMetricsEntry.getNumberOfAttributes()).isEqualTo(6.0);
        assertThat(eclipseMetricsEntry.getNumberOfChildren()).isEqualTo(0.0);
        assertThat(eclipseMetricsEntry.getNumberOfMethods()).isEqualTo(9.0);
        assertThat(eclipseMetricsEntry.getDepthOfInheritanceTree()).isEqualTo(1.0);
        assertThat(eclipseMetricsEntry.getLackOfCohesionOfMethods()).isEqualTo(0.833);
        assertThat(eclipseMetricsEntry.getNumberOfStaticMethods()).isEqualTo(0.0);
        assertThat(eclipseMetricsEntry.getSpecializationIndex()).isEqualTo(0.0);
        assertThat(eclipseMetricsEntry.getWeightedMethodsPerClass()).isEqualTo(24.0);
        assertThat(eclipseMetricsEntry.getNumberOfStaticAttributes()).isEqualTo(1.0);
    }
}
