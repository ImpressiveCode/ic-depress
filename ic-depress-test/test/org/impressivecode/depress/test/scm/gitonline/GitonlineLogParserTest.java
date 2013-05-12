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

package org.impressivecode.depress.test.scm.gitonline;

import static org.fest.assertions.Assertions.assertThat;
import static org.impressivecode.depress.scm.gitonline.GitonlineParserOptions.options;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.impressivecode.depress.scm.SCMOperation;
import org.impressivecode.depress.scm.gitonline.GitCommit;
import org.impressivecode.depress.scm.gitonline.GitonlineLogParser;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Tomasz Kuzemko
 * @author Slawomir Kapłoński
 * @author Marek Majchrzak, Impressive Code
 */

public class GitonlineLogParserTest {

    private final static String repoPath = GitonlineLogParserTest.class.getResource("").getPath()+"test_repo";
    
    //private final static String repoPath = "/home/slawek/dev/java/ic-depress/.git";
    private GitonlineLogParser parser;

    @Before
    public void setUp() throws Exception {
        specificCommit();
    }

    private GitCommit specificCommit() throws IOException, ParseException, NoHeadException, GitAPIException {
        this.parser = new GitonlineLogParser();
        this.parser.parseEntries(repoPath, options("#([0-9]+)", "org."));
        for (GitCommit c : parser.parseEntries(repoPath, options("#([0-9]+)", "org."))) {
            if (c.getId().equals("45a2beca9d97777733e1a472e54c003551b7d9b1")) {
                return c;
            }
        }
        throw new IllegalStateException("Fail");
    }

    @Test(expected = NoHeadException.class)
    public void shouldThrowFileNotFound() throws Exception {
        new GitonlineLogParser().parseEntries("fake_path", options(null, null));
    }

    @Test
    public void shouldCountCommits() throws Exception {
        GitonlineLogParser parser = new GitonlineLogParser();
        assertEquals(183, parser.parseEntries(repoPath, options("#([0-9]+)", "org.")).size());
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
        System.out.println(specificCommit().getMessage());
        assertEquals("#9 base version of PO Metric introduced", specificCommit().getMessage());
    }

    @Test
    public void shouldFindMarkers() throws Exception {
        assertThat(specificCommit().getMarkers()).containsOnly("9");
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
