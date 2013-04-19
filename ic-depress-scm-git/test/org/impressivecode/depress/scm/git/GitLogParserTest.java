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

package org.impressivecode.depress.scm.git;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Tomasz Kuzemko
 * @author Slawomir Kapłoński
 * @author Marek Majchrzak, Impressive Code
 */

public class GitLogParserTest {

    private final static String logFilePath = GitLogParserTest.class.getResource("git-test-log.txt").getPath();
    private GitLogParser parser;

    @Before
    public void setUp() throws Exception {
        specificCommit();
    }

    private GitCommit specificCommit() throws IOException, ParseException {
        for (GitCommit c : parser.parse(logFilePath)) {
            if (c.getId().equals("45a2beca9d97777733e1a472e54c003551b7d9b1")) {
                return c;
            }
        }
        throw new IllegalStateException("Fail");
    }

    @Test(expected = FileNotFoundException.class)
    public void shouldThrowFileNotFound() throws Exception {
        new GitLogParser().parse("fake_path");
    }

    @Test
    public void shouldCountCommits() throws Exception {
        assertEquals(50, parser.parse(logFilePath).size());
    }

    @Test
    public void shouldSpecificCommitDateMatch() throws Exception {
        assertEquals(DateFormat.getDateTimeInstance().parse("2013-03-18 20:49:14 +0100"), specificCommit().getDate());
    }

    @Test
    public void shouldSpecificCommitAuthorMatch() throws Exception {
        assertEquals("Marek Majchrzak", specificCommit().getAuthor());
    }

    @Test
    public void shouldSpecificCommitMessageMatch() throws Exception {
        assertEquals("#9 base version of PO Metric introduced", specificCommit().getMessage());
    }

    @Test
    public void shouldSpecificCommitFilesSizeMatch() throws Exception {
        assertEquals(15, specificCommit().files.size());
    }

    @Test
    public void shouldSpecificCommitFilesMatch() throws Exception {
        assertEquals("ic-depress-metric-po/META-INF/MANIFEST.MF", specificCommit().files.get(0).getPath());
        assertEquals(GitCommitFileOperation.Modified, specificCommit().files.get(0).getOperation());

        assertEquals("ic-depress-metric-po/src/org/impressivecode/depress/metric/po/ChangeData.java",
                specificCommit().files.get(1).getPath());
        assertEquals(GitCommitFileOperation.Added, specificCommit().files.get(1).getOperation());

        assertEquals("ic-depress-metric-po/src/org/impressivecode/depress/metric/po/ChangeHistoryTransformer.java",
                specificCommit().files.get(3).getPath());
        assertEquals(GitCommitFileOperation.Deleted, specificCommit().files.get(3).getOperation());

        assertEquals(
                "ic-depress-metric-po/test/org/impressivecode/depress/metric/po/PeopleOrganizationMetricProcessorTest.java",
                specificCommit().files.get(14).getPath());
        assertEquals(GitCommitFileOperation.Added, specificCommit().files.get(14).getOperation());

    }
}
