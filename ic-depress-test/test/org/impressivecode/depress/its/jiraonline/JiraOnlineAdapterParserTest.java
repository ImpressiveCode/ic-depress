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

import org.impressivecode.depress.its.jiraonline.historymodel.JiraOnlineIssueHistory;
import org.junit.Test;

/**
 * 
 * @author Marcin Kunert, Wroclaw University of Technology
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
        JiraOnlineIssueHistory issue = JiraOnlineAdapterParser.parseSingleIssue(json);

        // then
        assertThat(issue.getKey(), is("SECOAUTH-179"));
        
        assertThat(issue.getChangelog().getHistories().size(), is(2));
        
        assertThat(issue.getChangelog().getHistories().get(0).getAuthor().getName(), is("david_syer"));
        assertThat(issue.getChangelog().getHistories().get(0).getItems().size(), is(2));
        
        //TODO timestamp check
        calendar.set(Calendar.YEAR, 2012);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 17);
        assertThat(issue.getChangelog().getHistories().get(1).getTimestamp(), is(calendar.getTime()));
        
        assertThat(issue.getChangelog().getHistories().get(0).getItems().get(0).getFieldName(), is("timeoriginalestimate"));
        assertThat(issue.getChangelog().getHistories().get(0).getItems().get(0).getFrom(), is(nullValue()));
        assertThat(issue.getChangelog().getHistories().get(0).getItems().get(0).getTo(), is("0"));

        assertThat(issue.getChangelog().getHistories().get(0).getItems().get(1).getFieldName(), is("timeestimate"));
        assertThat(issue.getChangelog().getHistories().get(0).getItems().get(1).getFrom(), is(nullValue()));
        assertThat(issue.getChangelog().getHistories().get(0).getItems().get(1).getTo(), is("0"));
        
        assertThat(issue.getChangelog().getHistories().get(1).getAuthor().getName(), is("david_syer"));
        assertThat(issue.getChangelog().getHistories().get(1).getItems().size(), is(2));
        
        calendar.set(Calendar.YEAR, 2012);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 17);
        assertThat(issue.getChangelog().getHistories().get(1).getTimestamp(), is(calendar.getTime()));
        
        assertThat(issue.getChangelog().getHistories().get(1).getItems().get(0).getFieldName(), is("resolution"));
        assertThat(issue.getChangelog().getHistories().get(1).getItems().get(0).getFrom(), is(nullValue()));
        assertThat(issue.getChangelog().getHistories().get(1).getItems().get(0).getTo(), is("Complete"));

        assertThat(issue.getChangelog().getHistories().get(1).getItems().get(1).getFieldName(), is("status"));
        assertThat(issue.getChangelog().getHistories().get(1).getItems().get(1).getFrom(), is("Open"));
        assertThat(issue.getChangelog().getHistories().get(1).getItems().get(1).getTo(), is("Resolved"));
        
        
    }
}
