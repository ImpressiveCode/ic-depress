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
package org.impressivecode.depress.metric.pmd;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 * @author Tomasz Banach
 * @author Łukasz Waga
 * @author Monika Pruszkowska
 * 
 */
public class PMDEntryTest {

    @Test
    public void shouldReturnSetValue() {
        PMDEntry entry = new PMDEntry();

        String expected = "2";
        entry.setBeginLine(expected);
        assertEquals(expected, entry.getBeginLine());

        expected = "6";
        entry.setEndLine(expected);
        assertEquals(expected, entry.getEndLine());

        expected = "12";
        entry.setBeginColumn(expected);
        assertEquals(expected, entry.getBeginColumn());

        expected = "16";
        entry.setEndColumn(expected);
        assertEquals(expected, entry.getEndColumn());

        expected = "sample_rule";
        entry.setRule(expected);
        assertEquals(expected, entry.getRule());

        expected = "sample_ruleSet";
        entry.setRuleSet(expected);
        assertEquals(expected, entry.getRuleSet());


        expected = "package.name.then.ClassName$InternalClassName";
        entry.setClassName(expected);
        assertEquals(expected, entry.getClassName());

        expected = "this.is.an.info.Url";
        entry.setInfoUrl(expected);
        assertEquals(expected, entry.getInfoUrl());

        expected = "11";
        entry.setPriority(expected);
        assertEquals(expected, entry.getPriority());

        expected = "This is a proper text message";
        entry.setMessageText(expected);
        assertEquals(expected, entry.getMessageText());

    }

}
