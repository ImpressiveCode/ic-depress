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

import static org.junit.Assert.*;

import org.impressivecode.depress.mr.eclipsemetrics.EclipseMetricsEntryClassLevel;
import org.impressivecode.depress.mr.eclipsemetrics.EclipseMetricsEntryMethodLevel;
import org.junit.Test;

/**
 * 
 * @author Mateusz Kutyba, Wroclaw University of Technology
 * 
 */
public class EclipseMetricsEntryTest {

    @Test
    public void shouldReturnSetValue() {
        EclipseMetricsEntryClassLevel entry = new EclipseMetricsEntryClassLevel();

        String className = "package.name.then.ClassName$InternalClassName";
        entry.setClassName(className);
        assertEquals(className, entry.getClassName());

        Double expected = 2.5;
        entry.setNumberOfOverriddenMethods(expected);
        assertEquals(expected, entry.getNumberOfOverriddenMethods());

        expected = 3.5;
        entry.setNumberOfAttributes(expected);
        assertEquals(expected, entry.getNumberOfAttributes());

        expected = 4.5;
        entry.setNumberOfChildren(expected);
        assertEquals(expected, entry.getNumberOfChildren());

        expected = 5.5;
        entry.setNumberOfMethods(expected);
        assertEquals(expected, entry.getNumberOfMethods());

        expected = 6.5;
        entry.setDepthOfInheritanceTree(expected);
        assertEquals(expected, entry.getDepthOfInheritanceTree());

        expected = 7.5;
        entry.setLackOfCohesionOfMethods(expected);
        assertEquals(expected, entry.getLackOfCohesionOfMethods());

        expected = 8.5;
        entry.setNumberOfStaticMethods(expected);
        assertEquals(expected, entry.getNumberOfStaticMethods());

        expected = 9.5;
        entry.setSpecializationIndex(expected);
        assertEquals(expected, entry.getSpecializationIndex());

        expected = 10.5;
        entry.setWeightedMethodsPerClass(expected);
        assertEquals(expected, entry.getWeightedMethodsPerClass());

        expected = 11.5;
        entry.setNumberOfStaticAttributes(expected);
        assertEquals(expected, entry.getNumberOfStaticAttributes());
    }
    
    @Test
    public void shouldReturnSetValue2() {
        EclipseMetricsEntryMethodLevel entry = new EclipseMetricsEntryMethodLevel();

        String methodName = "org.apache.commons.math3.stat.descriptive.SummaryStatistics~clear";
        entry.setMethodName(methodName);
        assertEquals(methodName, entry.getMethodName());

        Double expected = 2.5;
        entry.setMethodLinesOfCode(expected);
        assertEquals(expected, entry.getMethodLinesOfCode());

        expected = 3.5;
        entry.setNestedBlockDepth(expected);
        assertEquals(expected, entry.getNestedBlockDepth());

        expected = 4.5;
        entry.setMcCabeCyclomaticComplexity(expected);
        assertEquals(expected, entry.getMcCabeCyclomaticComplexity());

        expected = 5.5;
        entry.setNumberOfParameters(expected);
        assertEquals(expected, entry.getNumberOfParameters());
    }

}
