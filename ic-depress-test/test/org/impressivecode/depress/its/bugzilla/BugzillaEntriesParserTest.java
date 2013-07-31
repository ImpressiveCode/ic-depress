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
package org.impressivecode.depress.its.bugzilla;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.text.ParseException;
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
public class BugzillaEntriesParserTest {

    @Test
    public void shouldParseEntries() throws ParserConfigurationException, SAXException, IOException, ParseException {
        List<ITSDataType> entries = parse();

        assertThat(entries).hasSize(2);
        assertEntry(entries.get(0));
    }

    private List<ITSDataType> parse() throws ParserConfigurationException, SAXException, IOException, ParseException {
        BugzillaEntriesParser parser = new BugzillaEntriesParser();
        String path = getClass().getResource("bugzilla.xml").getPath();
        List<ITSDataType> entries = parser.parseEntries(path);
        return entries;
    }

    private void assertEntry(final ITSDataType its) {
        assertThat(its.getIssueId()).isEqualTo("534051");
        assertThat(its.getComments()).hasSize(18);
        assertThat(its.getCreated().toString()).isEqualTo("Thu Dec 10 23:11:00 CET 2009"); //2009-12-10 14:11:00 -0800
        assertThat(its.getDescription()).isEqualTo("desc");
        assertThat(its.getFixVersion()).containsExactly("mozilla1.9.2");
        assertThat(its.getLink()).isEqualTo("https://bugzilla.mozilla.org/show_bug.cgi?id=534051");
        assertThat(its.getPriority()).isEqualTo(ITSPriority.CRITICAL);
        assertThat(its.getResolved().toString()).isEqualTo("Sat Aug 14 10:35:59 CEST 2010"); //2010-08-14 01:35:59 -0700 delta_ts
        assertThat(its.getStatus()).isEqualTo(ITSStatus.RESOLVED);
        assertThat(its.getSummary()).isEqualTo("Workers: Don't change the global object while GC is running");
        assertThat(its.getType()).isEqualTo(ITSType.BUG);
        assertThat(its.getResolution()).isEqualTo(ITSResolution.FIXED);
        assertThat(its.getUpdated()).isNull(); //?? last modified -> resolved 
        assertThat(its.getVersion()).containsExactly("Trunk");
        assertThat(its.getAssignees()).containsOnly("bent.mozilla1");
        assertThat(its.getReporter()).isEqualTo("bent.mozilla");
        assertThat(its.getCommentAuthors()).containsOnly("bent.mozilla","jorendorff", "jst", "samuel.sidler+old","dveditz", "hskupin", "dolske");
    }
}
