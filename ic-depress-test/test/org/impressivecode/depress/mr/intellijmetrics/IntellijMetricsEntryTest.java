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

import static org.junit.Assert.*;

import org.junit.Test;

import org.impressivecode.depress.mr.intellijmetrics.IntellijMetricsEntry;

/**
 * 
 * @author Maciej Mickiewicz, Wroclaw University of Technology
 * 
 */
public class IntellijMetricsEntryTest {
	
	@Test
	public void shouldReturnSettedValue() {
		IntellijMetricsEntry intellijEntry = new IntellijMetricsEntry();
		
        String className = "org.some.package.ClassName$SubClass";
        intellijEntry.setClassName(className);
        assertEquals(className, intellijEntry.getClassName());

        int expected = 1;
        intellijEntry.setSeverityErrorCounter(expected);
        assertEquals(expected, intellijEntry.getSeverityErrorCounter());
        
        expected = 11;
        intellijEntry.setSeverityInfoCounter(expected);
        assertEquals(expected, intellijEntry.getSeverityInfoCounter());
        
        expected = 111;
        intellijEntry.setSeverityServerProblemCounter(expected);
        assertEquals(expected, intellijEntry.getSeverityServerProblemCounter());
        
        expected = 2;
        intellijEntry.setSeverityTypoCounter(expected);
        assertEquals(expected, intellijEntry.getSeverityTypoCounter());
        
        expected = 22;
        intellijEntry.setSeverityWarningCounter(expected);
        assertEquals(expected, intellijEntry.getSeverityWarningCounter());
        
        expected = 222;
        intellijEntry.setSeverityWeakWarningCounter(expected);
        assertEquals(expected, intellijEntry.getSeverityWeakWarningCounter());
        
        String severityId = "ERROR";
        expected = 666;
        intellijEntry.setValue(severityId, expected);
        assertEquals(expected, intellijEntry.getValue(severityId));
                       
	}
}
