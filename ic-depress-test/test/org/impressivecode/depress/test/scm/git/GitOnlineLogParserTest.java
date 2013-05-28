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
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.impressivecode.depress.scm.SCMOperation;
import org.impressivecode.depress.scm.git.GitCommit;
import org.impressivecode.depress.scm.git.GitOnlineLogParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Tomasz Kuzemko
 * @author Slawomir Kapłoński
 */

public class GitOnlineLogParserTest {
    private final static String repoZipPath = GitOnlineLogParserTest.class.getResource("/").getPath()+"../test/org/impressivecode/depress/test/scm/gitonline/test_repo.zip";
    private String repoPath;
    private File tempDir = null;

    private GitOnlineLogParser parser;

    @Before
    public void setUp() throws Exception {
        unpack();
        specificCommit();
    }

    @After
    public void tearDown() throws Exception {
        if (tempDir != null && tempDir.exists()) {
            deleteRecursive(tempDir);
        }
    }

    public void unpack() throws IOException, ZipException {
        tempDir = File.createTempFile("temp-GitOnlineLogParserTest-", Long.toString(System.nanoTime()));

        if (!tempDir.delete())
        {
            throw new IOException("Cannot delete temp file: " + tempDir.getAbsolutePath());
        }

        if (!tempDir.mkdir())
        {
            throw new IOException("Cannot create temp dir: " + tempDir.getAbsolutePath());
        }

        ZipFile zip = new ZipFile(repoZipPath);
        zip.extractAll(tempDir.getAbsolutePath());

        repoPath = tempDir.getAbsolutePath() + File.separatorChar + ".git";
    }

    void deleteRecursive(final File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                deleteRecursive(c);
        }
        if (!f.delete())
            throw new FileNotFoundException("Failed to delete file: " + f);
    }

    private GitCommit specificCommit() throws IOException, ParseException, NoHeadException, GitAPIException {
        this.parser = new GitOnlineLogParser();
        this.parser.parseEntries(repoPath, options("#([0-9]+)", "org.", null));
        for (GitCommit c : parser.parseEntries(repoPath, options("#([0-9]+)", "org.", null))) {
            if (c.getId().equals("45a2beca9d97777733e1a472e54c003551b7d9b1")) {
                return c;
            }
        }
        throw new IllegalStateException("Fail");
    }

    @Test(expected = NoHeadException.class)
    public void shouldThrowFileNotFound() throws Exception {
        new GitOnlineLogParser().parseEntries("fake_path", options(null, null, null));
    }

    @Test
    public void shouldCountCommits() throws Exception {
        GitOnlineLogParser parser = new GitOnlineLogParser();
        assertEquals(183, parser.parseEntries(repoPath, options("#([0-9]+)", "org.", null)).size());
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
        assertEquals("#9 base version of PO Metric introduced", specificCommit().getMessage());
    }

    @Test
    public void shouldFindMarkers() throws Exception {
        assertThat(specificCommit().getMarkers()).containsOnly("9");
    }

    @Test
    public void shouldSpecificCommitFilesSizeMatch() throws Exception {
        assertThat(specificCommit().getFiles()).hasSize(14); // Remember we only count .java files
    }

    @Test
    public void shouldParseJavaFile() throws Exception {
        assertThat(specificCommit().getFiles().get(0).getJavaClass()).isEqualTo("org.impressivecode.depress.metric.po.ChangeData");
    }

    @Test
    public void shouldSpecificCommitFilesMatch() throws Exception {

        assertEquals("ic-depress-metric-po/src/org/impressivecode/depress/metric/po/ChangeData.java",
                specificCommit().getFiles().get(0).getPath());
        assertEquals(SCMOperation.ADDED, specificCommit().getFiles().get(0).getOperation());

        assertEquals("ic-depress-metric-po/src/org/impressivecode/depress/metric/po/ChangeHistoryTransformer.java",
                specificCommit().getFiles().get(2).getPath());
        assertEquals(SCMOperation.DELETED, specificCommit().getFiles().get(2).getOperation());

        assertEquals(
                "ic-depress-metric-po/test/org/impressivecode/depress/metric/po/PeopleOrganizationMetricProcessorTest.java",
                specificCommit().getFiles().get(13).getPath());
        assertEquals(SCMOperation.ADDED, specificCommit().getFiles().get(13).getOperation());

    }

    @Test
    public void shouldSpecificBranchCommitCountMatch() throws Exception {
        assertEquals(43, new GitOnlineLogParser().parseEntries(repoPath, options("#([0-9]+)", "org.", "master")).size());
    }

    @Test
    public void shouldSpecificBranchFirstCommitIdMatch() throws Exception {
        assertEquals("c10f2ad763c3c78ba267d473608253d9796542cc",
                new GitOnlineLogParser().parseEntries(repoPath, options("#([0-9]+)", "org.", "master")).get(0).getId());
    }

    @Test
    public void shouldSpecificRemoteBranchCommitCountMatch() throws Exception {
        assertEquals(108, new GitOnlineLogParser().parseEntries(repoPath, options("#([0-9]+)", "org.", "tomek/new-metrics")).size());
    }

    @Test
    public void shouldSpecificRemoteBranchFirstCommitIdMatch() throws Exception {
        assertEquals("a99f5a83953121301a0c615ed3d78b1869423d08",
                new GitOnlineLogParser().parseEntries(repoPath, options("#([0-9]+)", "org.", "tomek/new-metrics")).get(0).getId());
    }

    @Test
    public void shouldGetBranches() throws Exception {
        List<String> expectedBranches = new ArrayList<String>();
        expectedBranches.add("dev");
        expectedBranches.add("master");
        expectedBranches.add("origin/HEAD");
        expectedBranches.add("origin/dev");
        expectedBranches.add("origin/master");
        expectedBranches.add("origin/new-metrics");
        expectedBranches.add("pwr/dev");
        expectedBranches.add("pwr/master");
        expectedBranches.add("tomek/dev");
        expectedBranches.add("tomek/master");
        expectedBranches.add("tomek/new-metrics");

        assertArrayEquals(expectedBranches.toArray(), GitOnlineLogParser.getBranches(repoPath).toArray());
    }

    @Test(expected=NoHeadException.class)
    public void shouldThrowOnNonExistingRepo() throws Exception {
        GitOnlineLogParser.getBranches("/some/fake/path/.git");
    }

    @Test
    public void shouldGetCurrentBranch() throws Exception {
        assertEquals("dev", GitOnlineLogParser.getCurrentBranch(repoPath));
    }
}
