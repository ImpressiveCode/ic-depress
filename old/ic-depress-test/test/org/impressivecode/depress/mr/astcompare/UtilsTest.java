package org.impressivecode.depress.mr.astcompare;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.impressivecode.depress.mr.astcompare.utils.Utils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;

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
public class UtilsTest {

    private static Calendar calendar;
    private static SimpleDateFormat dateFormat;

    @BeforeClass
    public static void setUp() {
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    }

    @Test
    public void getCurrentDateTest() {
        assertEquals(dateFormat.format(calendar.getTime()), Utils.getCurrentDate());
    }

    @Test
    public void getCurrentDayPlusTest() {
        Calendar tempcalendar = (Calendar) calendar.clone();
        tempcalendar.add(Calendar.MONTH, 3);
        assertEquals(dateFormat.format(tempcalendar.getTime()), Utils.getCurrentDayPlus(Calendar.MONTH, 3));
    }

    @Test
    public void getDateAsStringTest() {
        long timeInMillis = 1401123880091L;
        assertEquals("26-05-2014", Utils.getDateAsString(timeInMillis));
    }

    @Test
    public void validateWrongDateTest() {
        try {
            Utils.validateDate("46-05-2014");
        } catch (InvalidSettingsException ex) {
            assertEquals("Wrong date format! Please use DD-MM-YYYY", ex.getMessage());
        }
    }

    @Test
    public void getTimeTest() {
        long timeInMillis = 1401055200000L;
        assertEquals(timeInMillis, Utils.getTime("26-05-2014"));
    }

    @Test
    public void addWeeksToDateTest() {
        long timeInMillis = 1401055200000L;
        assertEquals(1401660000000L, Utils.addWeeksToDate(timeInMillis, 1));
    }

    @Test
    public void getWeeksAsIntegerTest() {
        assertEquals(2, Utils.getWeeksAsInteger("2"));
        assertEquals(4, Utils.getWeeksAsInteger("4"));
        assertEquals(8, Utils.getWeeksAsInteger("8"));
        assertEquals(1000, Utils.getWeeksAsInteger("All"));
    }

}
