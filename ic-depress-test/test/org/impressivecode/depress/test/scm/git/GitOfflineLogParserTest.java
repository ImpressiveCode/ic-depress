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

package org.impressivecode.depress.test.scm.git;

import static org.fest.assertions.Assertions.assertThat;
import static org.impressivecode.depress.scm.git.GitParserOptions.options;
import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.impressivecode.depress.scm.SCMOperation;
import org.impressivecode.depress.scm.git.GitCommit;
import org.impressivecode.depress.scm.git.GitOfflineLogParser;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Tomasz Kuzemko
 * @author Slawomir Kapłoński
 * @author Marek Majchrzak, Impressive Code
 */

public class GitOfflineLogParserTest {

    private final static String logFilePath = GitOfflineLogParserTest.class.getResource("git-test-log.txt").getPath();
    private GitOfflineLogParser parser;

    @Before
    public void setUp() throws Exception {
        specificCommit();
    }

    private GitCommit specificCommit() throws IOException, ParseException {
        this.parser = new GitOfflineLogParser();
        for (GitCommit c : parser.parseEntries(logFilePath, options("#([0-9]+)", "org."))) {
            if (c.getId().equals("45a2beca9d97777733e1a472e54c003551b7d9b1")) {
                return c;
            }
        }
        throw new IllegalStateException("Fail");
    }

    @Test(expected = FileNotFoundException.class)
    public void shouldThrowFileNotFound() throws Exception {
        new GitOfflineLogParser().parseEntries("fake_path", options(null, null));
    }

    @Test
    public void shouldCountCommits() throws Exception {
        assertEquals(51, parser.parseEntries(logFilePath, options(null, null)).size());
    }

    @Test
    public void shouldSpecificCommitDateMatch() throws Exception {
        assertThat(specificCommit().getDate()).isEqualTo(new Date(1363636154*1000l));
    }

    @Test
    public void shouldSpecificCommitAuthorMatch() throws Exception {
        assertEquals("Marek Majchrzak", specificCommit().getAuthor());
    }

    @Test
    public void shouldSpecificCommitMessageMatch() throws Exception {
        assertEquals("#9 base version of PO Metric introduced, #18 this is just matcher test", specificCommit().getMessage());
    }

    @Test
    public void shouldFindMarkers() throws Exception {
        assertThat(specificCommit().getMarkers()).containsOnly("9", "18");
    }

    @Test
    public void shouldSpecificCommitFilesSizeMatch() throws Exception {
        assertThat(specificCommit().getFiles()).hasSize(14);
    }

    @Test
    public void shouldParseJavaFile() throws Exception {
        assertThat(specificCommit().getFiles().get(0).getJavaClass()).isEqualTo("org.impressivecode.depress.metric.po.ChangeData");
    }

    @Test
    public void shouldSpecificCommitFilesMatch() throws Exception {

        assertEquals("ic-depress-metric-po/src/org/impressivecode/depress/metric/po/ChangeData.java",
                specificCommit().getFiles().get(0).getPath());
        assertEquals(SCMOperation.ADDED, specificCommit().getFiles().get(1).getOperation());

        assertEquals("ic-depress-metric-po/src/org/impressivecode/depress/metric/po/ChangeHistoryTransformer.java",
                specificCommit().getFiles().get(2).getPath());
        assertEquals(SCMOperation.DELETED, specificCommit().getFiles().get(3).getOperation());

        assertEquals(
                "ic-depress-metric-po/test/org/impressivecode/depress/metric/po/PeopleOrganizationMetricProcessorTest.java",
                specificCommit().getFiles().get(13).getPath());
        assertEquals(SCMOperation.ADDED, specificCommit().getFiles().get(13).getOperation());

    }
}
