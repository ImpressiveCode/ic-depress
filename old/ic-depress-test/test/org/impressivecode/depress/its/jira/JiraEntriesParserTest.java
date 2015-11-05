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
package org.impressivecode.depress.its.jira;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSPriority;
import org.impressivecode.depress.its.ITSResolution;
import org.impressivecode.depress.its.ITSStatus;
import org.impressivecode.depress.its.ITSType;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class JiraEntriesParserTest {

    @Test
    public void shouldParseJiraEntries() throws ParserConfigurationException, SAXException, IOException, ParseException {
        List<ITSDataType> entries = parse();

        assertThat(entries).hasSize(2);
        assertEntry(entries.get(0));
    }

    @Test
    public void shouldParseJiraEntriesAndAcceptEmptyItems() throws ParserConfigurationException, SAXException,
            IOException, ParseException {
        List<ITSDataType> entries = parse();

        ITSDataType entry = entries.get(1);
        assertThat(entry.getResolved()).isNull();
        assertThat(entry.getComments()).isEmpty();
        assertThat(entry.getFixVersion()).isEmpty();
    }

    private List<ITSDataType> parse() throws ParserConfigurationException, SAXException, IOException, ParseException {

        String path = getClass().getResource("jira.xml").getPath();
        List<ITSDataType> entries = createJiraParser().parseEntries(path);
        return entries;
    }

    private void assertEntry(final ITSDataType its) {
        assertThat(its.getIssueId()).isEqualTo("LANG-736");
        assertThat(its.getComments()).hasSize(5);
        assertThat(its.getCreated().toString()).isEqualTo("Tue Aug 02 20:19:53 CEST 2011");
        assertThat(its.getDescription()).isNotEmpty();
        assertThat(its.getFixVersion()).containsExactly("3.1", "3.2");
        assertThat(its.getLink()).isEqualTo("https://issues.apache.org/jira/browse/LANG-736");
        assertThat(its.getPriority()).isEqualTo(ITSPriority.MAJOR);
        assertThat(its.getResolved()).isNotNull();
        assertThat(its.getStatus()).isEqualTo(ITSStatus.CLOSED);
        assertThat(its.getSummary()).isNotEmpty();
        assertThat(its.getType()).isEqualTo(ITSType.BUG);
        assertThat(its.getResolution()).isEqualTo(ITSResolution.FIXED);
        assertThat(its.getUpdated()).isNotNull();
        assertThat(its.getVersion()).containsExactly("3.0");
        assertThat(its.getAssignees()).containsOnly("garydgregory");
        assertThat(its.getReporter()).isEqualTo("garydgregory");
        assertThat(its.getCommentAuthors()).containsOnly("bayard", "mbenson", "garydgregory");
    }
    
    private JiraEntriesParser createJiraParser() {
        HashMap<String, String[]> priority = new HashMap<String, String[]>();
        priority.put(ITSPriority.MAJOR.getLabel(), new String[] {ITSPriority.MAJOR.getLabel()});
        HashMap<String, String[]> type = new HashMap<String, String[]>();
        type.put(ITSType.BUG.getLabel(), new String[] {ITSType.BUG.getLabel()});
        HashMap<String, String[]> resolution = new HashMap<String, String[]>();
        resolution.put(ITSResolution.FIXED.getLabel(), new String[] {ITSResolution.FIXED.getLabel()});
        HashMap<String, String[]> status = new HashMap<String, String[]>();
        status.put(ITSStatus.CLOSED.getLabel(), new String[] {ITSStatus.CLOSED.getLabel()});
    	return new JiraEntriesParser(priority, type, resolution, status);
    }
}
