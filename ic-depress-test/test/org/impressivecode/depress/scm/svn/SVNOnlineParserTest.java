package org.impressivecode.depress.scm.svn;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tmatesoft.svn.core.SVNException;

public class SVNOnlineParserTest {

    private final static String repoZipPath = SVNOnlineParserTest.class.getResource("test-repo.zip").getPath();
    private String repoPath;
    private File tempDir = null;

    private SVNOnlineLogParser parser;

    @Before
    public void setUp() throws Exception {
        unpack();
        specificCommit();
    }

    @Test
    public void shouldSpecificCommitAuthorMatch() throws Exception {
        assertEquals("Marcin", specificCommit().getAuthor());
    }

    @Test
    public void shouldSpecificCommitMessageMatch() throws Exception {
        assertEquals("Init commit for test local repository", specificCommit().getMessage());
    }

    @Test
    public void shouldSpecificCommitFilesSizeMatch() throws Exception {
        assertThat(specificCommit().getFiles()).hasSize(14); // Remember we only
        // count .java
        // files
    }

    @Test(expected = SVNException.class)
    public void shouldThrowFileNotFound() throws Exception {
        new SVNOnlineLogParser().parseEntries("fake_path", SVNParserOptions.options(null, null));
    }

    @Test
    public void shouldFindMarkers() throws Exception {
        assertThat(specificCommit().getMarkers()).containsOnly("9");
    }

    @After
    public void tearDown() throws Exception {
        if (tempDir != null && tempDir.exists()) {
            deleteRecursive(tempDir);
        }
    }

    public void unpack() throws IOException, ZipException {
        tempDir = File.createTempFile("temp-SVNOnlineLogParserTest-", Long.toString(System.nanoTime()));

        if (!tempDir.delete()) {
            throw new IOException("Cannot delete temp file: " + tempDir.getAbsolutePath());
        }

        if (!tempDir.mkdir()) {
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

    private SVNCommit specificCommit() throws IOException, ParseException, SVNException {
        this.parser = new SVNOnlineLogParser();
        this.parser.parseEntries(repoPath, SVNParserOptions.options("#([0-9]+)", "org."));

        for (SVNCommit c : parser.parseEntries(repoPath, SVNParserOptions.options("#([0-9]+)", "org."))) {
            if (c.getId().equals("2")) {
                return c;
            }
        }
        throw new IllegalStateException("Fail");
    }

}
