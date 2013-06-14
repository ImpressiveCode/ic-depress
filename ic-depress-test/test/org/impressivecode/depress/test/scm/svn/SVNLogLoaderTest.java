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

package org.impressivecode.depress.test.scm.svn;

import static org.fest.assertions.Assertions.assertThat;

import org.impressivecode.depress.scm.SCMDataType;
import org.impressivecode.depress.scm.SCMOperation;
import org.impressivecode.depress.scm.svn.SVNLogLoader;
import org.impressivecode.depress.scm.svn.SVNLogLoader.IReadProgressListener;
import org.junit.Before;
import org.junit.Test;
import org.knime.core.node.CanceledExecutionException;

public class SVNLogLoaderTest {

    private final static String logFilePath = SVNLogLoaderTest.class.getResource("svn-test.log.txt").getPath();
    private SVNLogLoader parser;
    private SCMDataType commit;

    class TestListener implements IReadProgressListener {

        @Override
        public void onReadProgress(double inProgres, SCMDataType inRow) {
        }

        @Override
        public void checkLoading() throws CanceledExecutionException {
        }
    }

    @Before
    public void setUp() throws Exception {
        specificLog();
    }

    private SCMDataType specificLog() throws Exception {
        this.parser = SVNLogLoader.create("");

        parser.load(logFilePath, "#32", ".", new IReadProgressListener() {

            @Override
            public void onReadProgress(double inProgres, SCMDataType inRow) {
                commit = inRow;
            }

            @Override
            public void checkLoading() throws CanceledExecutionException {
            }
        });

        return commit;

    }

    @Test
    public void shouldSpecificCommitDateMatch() throws Exception {
        assertThat(specificLog().getCommitDate())
                .isEqualTo(parser.getDateFormat().parse("2010-04-02T20:21:15.411465Z"));
    }

    @Test
    public void shouldSpecificCommitMessageMatch() throws Exception {
        assertThat(specificLog().getMessage()).isEqualTo("#32 Test message");
    }

    @Test
    public void shouldSpecificCommitUidMatch() throws Exception {
        assertThat(specificLog().getCommitID()).isEqualTo("27");
    }

    @Test
    public void shouldSpecificCommitActionMatch() throws Exception {
        assertThat(specificLog().getOperation()).isEqualTo(SCMOperation.MODIFIED);
    }

    @Test
    public void shouldSpecificCommitFilePathMatch() throws Exception {
        assertThat(specificLog().getPath()).isEqualTo("/trunk/canvg.java");
    }

    @Test
    public void shouldSpecificCommitResourceMatch() throws Exception {
        assertThat(specificLog().getResourceName()).isEqualTo("Canvg");
    }

    @Test(expected = Exception.class)
    public void invalidOnlinePath() throws Exception {
        SVNLogLoader.create("").load("/test1.xml", null, null, null);
    }

    @Test(expected = Exception.class)
    public void invalidLocalPath() throws Exception {
        SVNLogLoader.create("http://").load("http://svn.test/;user", null, null, null);
    }
}
