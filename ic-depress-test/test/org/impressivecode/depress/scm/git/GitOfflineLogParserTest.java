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

import static org.fest.assertions.Assertions.assertThat;
import static org.impressivecode.depress.scm.SCMParserOptions.options;
import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.impressivecode.depress.scm.SCMOperation;
import org.impressivecode.depress.scm.SCMParserOptions;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Tomasz Kuzemko
 * @author Slawomir Kapłoński
 * @author Marek Majchrzak, Impressive Code
 * @author Maciej Borkowski, Capgemini Polska
 */
public class GitOfflineLogParserTest {

    private final static String logFilePath = GitOfflineLogParserTest.class.getResource("git-test-log.txt").getPath();
    private GitOfflineLogParser parser;

    @Before
    public void setUp() throws Exception {
        specificCommit();
    }

    private GitCommit specificCommit() throws IOException, ParseException {
    	ArrayList<String> ext = new ArrayList<String>();
    	ext.add(".java");
    	SCMParserOptions parserOptions = options("org.", ext);
        this.parser = new GitOfflineLogParser(parserOptions);
        for (GitCommit c : parser.parseEntries(logFilePath)) {
            if (c.getId().equals("45a2beca9d97777733e1a472e54c003551b7d9b1")) {
                return c;
            }
        }
        throw new IllegalStateException("Fail");
    }
    
    private GitCommit specificCommit(ArrayList<String> ext) throws IOException, ParseException {
    	SCMParserOptions parserOptions = options("org.", ext);
        this.parser = new GitOfflineLogParser(parserOptions);
        for (GitCommit c : parser.parseEntries(logFilePath)) {
            if (c.getId().equals("b4f3088d8894ac224535a31ccf4d1600d3fc0c57")) {
                return c;
            }
        }
        throw new IllegalStateException("Fail");
    }

    private GitCommit packageCommit(String packageName) throws IOException, ParseException {
    	ArrayList<String> ext = new ArrayList<String>();
    	ext.add(".java");
    	SCMParserOptions parserOptions = options(packageName, ext);
        this.parser = new GitOfflineLogParser(parserOptions);
        for (GitCommit c : parser.parseEntries(logFilePath)) {
            if (c.getId().equals("b4f3088d8894ac224535a31ccf4d1600d3fc0c57")) {
                return c;
            }
        }
        throw new IllegalStateException("Fail");
    }
    
    @Test(expected = FileNotFoundException.class)
    public void shouldThrowFileNotFound() throws Exception {
        parser.parseEntries("fake_path");
    }

    @Test
    public void shouldCountCommits() throws Exception {
        assertEquals(51, parser.parseEntries(logFilePath).size());
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
    
    @Test
    public void shouldCommitWithExtensionsMatch() throws Exception {
    	ArrayList<String> ext = new ArrayList<String>();
    	ext.add(".txt");
        assertEquals(
        		"ic-depress-metric-eclipsemetrics/LICENSE.txt",
                specificCommit(ext).getFiles().get(3).getPath());
        ext.add(".xml");
        assertEquals(
        		"ic-depress-metric-eclipsemetrics/LICENSE.txt",
                specificCommit(ext).getFiles().get(3).getPath());
        ext.add(".classpath");
        assertEquals(
        		"ic-depress-metric-checkstyle/LICENSE.txt",
                specificCommit(ext).getFiles().get(3).getPath());
        ext.add("*");
        assertEquals(
        		"ic-depress-metric-checkstyle/.project",
                specificCommit(ext).getFiles().get(3).getPath());
    }
    
    @Test
    public void shouldCommitWithPackageMatch() throws Exception {
    	assertThat(packageCommit("org.spring.").getFiles()).hasSize(0);
    }
}
