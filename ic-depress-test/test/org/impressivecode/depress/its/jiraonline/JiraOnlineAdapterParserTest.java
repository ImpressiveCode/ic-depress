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
package org.impressivecode.depress.its.jiraonline;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;

import org.impressivecode.depress.its.jiraonline.historymodel.JiraOnlineIssueChangeRowItem;
import org.junit.Test;

/**
 * 
 * @author Marcin Kunert, Wroclaw University of Technology
 * 
 */
public class JiraOnlineAdapterParserTest {

    @Test
    public void shouldParseMultipleIssues() {
        // TODO issue list parser test
        // given

        // when

        // then
    }

    @Test
    public void shouldParseSingleIssueHistory() throws Exception {

        // given
        Calendar calendar = Calendar.getInstance();
        String path = getClass().getResource("single_issue_with_history.txt").getPath();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));

        String json = reader.readLine();

        // when
        List<JiraOnlineIssueChangeRowItem> history = JiraOnlineAdapterParser.parseSingleIssue(json);

        // then
        assertThat(history.get(0).getKey(), is("SECOAUTH-179"));

        assertThat(history.size(), is(4));

        assertThat(history.get(0).getAuthor(), is("david_syer"));

        calendar.set(Calendar.YEAR, 2012);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 17);
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 55);
        calendar.set(Calendar.SECOND, 34);
        calendar.set(Calendar.MILLISECOND, 738);

        assertThat(history.get(0).getTimestamp(), is(calendar.getTime()));
        assertThat(history.get(0).getAuthor(), is("david_syer"));
        assertThat(history.get(0).getField(), is("timeoriginalestimate"));
        assertThat(history.get(0).getChangedFrom(), is(nullValue()));
        assertThat(history.get(0).getChangedTo(), is("0"));

        assertThat(history.get(1).getTimestamp(), is(calendar.getTime()));
        assertThat(history.get(1).getAuthor(), is("david_syer"));
        assertThat(history.get(1).getField(), is("timeestimate"));
        assertThat(history.get(1).getChangedFrom(), is(nullValue()));
        assertThat(history.get(1).getChangedTo(), is("0"));

        calendar.set(Calendar.YEAR, 2012);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 17);
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 56);
        calendar.set(Calendar.SECOND, 55);
        calendar.set(Calendar.MILLISECOND, 639);

        assertThat(history.get(2).getTimestamp(), is(calendar.getTime()));
        assertThat(history.get(2).getAuthor(), is("david_syer"));
        assertThat(history.get(2).getField(), is("resolution"));
        assertThat(history.get(2).getChangedFrom(), is(nullValue()));
        assertThat(history.get(2).getChangedTo(), is("Complete"));

        assertThat(history.get(3).getTimestamp(), is(calendar.getTime()));
        assertThat(history.get(3).getAuthor(), is("david_syer"));
        assertThat(history.get(3).getField(), is("status"));
        assertThat(history.get(3).getChangedFrom(), is("Open"));
        assertThat(history.get(3).getChangedTo(), is("Resolved"));

    }
}
