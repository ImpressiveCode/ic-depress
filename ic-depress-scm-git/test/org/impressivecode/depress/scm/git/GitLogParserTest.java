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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import org.impressivecode.depress.scm.git.GitLogParser;
import org.junit.Test;

/*
 * 
 * @author: Tomasz Kuzemko
 * @author: Sławomir Kapłoński
 * 
 */

public class GitLogParserTest {

    static String logFilePath = "test/org/impressivecode/depress/scm/git/git-test-log.txt";

    @Test(expected = FileNotFoundException.class)
    public void shouldThrowFileNotFound() throws Exception {
        new GitLogParser("fake_path");
    }

    @Test
    public void shouldParse() throws Exception {
        GitLogParser parser = new GitLogParser(logFilePath);
        List<GitCommit> commits = parser.parse();
        assertEquals(50, commits.size());

        GitCommit commit = null;

        // Find commit which we'll use for some more testing:
        /*
        45a2beca9d97777733e1a472e54c003551b7d9b1
        2013-03-18 20:49:14 +0100
        Marek Majchrzak
        #9 base version of PO Metric introduced

        45a2beca9d97777733e1a472e54c003551b7d9b1
        :100644 100644 a76f08c2f2f5852d4054a5fc392c10a00c6115cd de702169b484411699495c93331ab7a5247d6c13 M  ic-depress-metric-po/META-INF/MANIFEST.MF
        :000000 100644 0000000000000000000000000000000000000000 227af564cefa0d4aba42e87cfffdb3420c832b73 A  ic-depress-metric-po/src/org/impressivecode/depress/metric/po/ChangeData.java
        :000000 100644 0000000000000000000000000000000000000000 030663900ce8cd6663d7a6b80fbf49bea722960c A  ic-depress-metric-po/src/org/impressivecode/depress/metric/po/ChangeDataTransformer.java
        :100644 000000 4b55bd69c918487ef23619fa710d6663aae553ad 0000000000000000000000000000000000000000 D  ic-depress-metric-po/src/org/impressivecode/depress/metric/po/ChangeHistoryTransformer.java
        :100644 000000 58e2da9c48139ee475fb661b6703b1a1778cc3b4 0000000000000000000000000000000000000000 D  ic-depress-metric-po/src/org/impressivecode/depress/metric/po/MetricProcessor.java
        :100644 000000 0b1ccc9f2c03868b4d67e4ff0ce01250e901f61a 0000000000000000000000000000000000000000 D  ic-depress-metric-po/src/org/impressivecode/depress/metric/po/POData.java
        :000000 100644 0000000000000000000000000000000000000000 541a49e4a5c232d1ec58b0c75057978a92169719 A  ic-depress-metric-po/src/org/impressivecode/depress/metric/po/PeopleOrganizationMetric.java
        :000000 100644 0000000000000000000000000000000000000000 66c92833a03bfe312dff6f8553a41b3cda23bce8 A  ic-depress-metric-po/src/org/impressivecode/depress/metric/po/PeopleOrganizationMetricProcessor.java
        :100644 100644 4bc3232033bc37939942c0383a97b283f0a2a611 462ee1047a4e721795df9966332e08a24f461910 M  ic-depress-metric-po/src/org/impressivecode/depress/metric/po/PeopleOrganizationMetricTableFactory.java
        :000000 100644 0000000000000000000000000000000000000000 87569c6ef2d64f93c58e936d936ac2c9d1d074b0 A  ic-depress-metric-po/src/org/impressivecode/depress/metric/po/PeopleOrganizationMetricTransformer.java
        :100644 100644 15a74d4f5436da9e2f1d9ff6376fb3affcdd9413 f8baf5e027757355753b374cf5589ff7a63a91c7 M  ic-depress-metric-po/src/org/impressivecode/depress/metric/po/PeopleOrganizationMetricsNodeModel.java
        :100644 100644 184c24e24f52d10af9dd2bb6583869bfb0d920e8 2adb448dbfdcfcb91270023e36c0bf52820289af M  ic-depress-metric-po/src/org/impressivecode/depress/metric/po/TeamMemberData.java
        :000000 100644 0000000000000000000000000000000000000000 5d847f3c3f8ecf48e7a70eb74e62a800b72f55c1 A  ic-depress-metric-po/src/org/impressivecode/depress/metric/po/TeamMemberDataTransformer.java
        :100644 000000 2c2006ff2af9394b887035c05ea0935fbf935e13 0000000000000000000000000000000000000000 D  ic-depress-metric-po/src/org/impressivecode/depress/metric/po/TeamMemberTransformer.java
        :000000 100644 0000000000000000000000000000000000000000 475ab374c18ec0446574f29c61dc7ef711704164 A  ic-depress-metric-po/test/org/impressivecode/depress/metric/po/PeopleOrganizationMetricProcessorTest.java
         */


        for (GitCommit c : commits) {
            if (c.getId().equals("45a2beca9d97777733e1a472e54c003551b7d9b1")) {
                commit = c;
                break;
            }
        }

        assertEquals(DateFormat.getDateTimeInstance().parse("2013-03-18 20:49:14 +0100"), commit.getDate());
        assertEquals("Marek Majchrzak", commit.getAuthor());
        assertEquals("#9 base version of PO Metric introduced", commit.getMessage());

        assertEquals(15, commit.files.size());

        assertEquals("ic-depress-metric-po/META-INF/MANIFEST.MF", commit.files.get(0).getPath());
        assertEquals(GitCommitFileOperation.Modified, commit.files.get(0).getOperation());

        assertEquals("ic-depress-metric-po/src/org/impressivecode/depress/metric/po/ChangeData.java", commit.files.get(1).getPath());
        assertEquals(GitCommitFileOperation.Added, commit.files.get(1).getOperation());

        assertEquals("ic-depress-metric-po/src/org/impressivecode/depress/metric/po/ChangeHistoryTransformer.java", commit.files.get(3).getPath());
        assertEquals(GitCommitFileOperation.Deleted, commit.files.get(3).getOperation());

        assertEquals("ic-depress-metric-po/test/org/impressivecode/depress/metric/po/PeopleOrganizationMetricProcessorTest.java", commit.files.get(14).getPath());
        assertEquals(GitCommitFileOperation.Added, commit.files.get(14).getOperation());
    }
}
