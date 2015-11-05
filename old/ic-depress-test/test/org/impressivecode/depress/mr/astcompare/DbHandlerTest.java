package org.impressivecode.depress.mr.astcompare;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.impressivecode.depress.mr.astcompare.ast.SingleChangeInfo;
import org.impressivecode.depress.mr.astcompare.db.DbHandler;
import org.impressivecode.depress.mr.astcompare.db.Metric;
import org.junit.Before;
import org.junit.Test;

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
public class DbHandlerTest {

    private SingleChangeInfo data1;
    private SingleChangeInfo data2;
    private SingleChangeInfo data3;
    private SingleChangeInfo data4;
    private static final String PROJECT_NAME = "TEST_PROJECT";
    private DbHandler db;

    @Before
    public void setUp() throws IOException {
        db = DbHandler.getInstance();
        data1 = new SingleChangeInfo();
        data1.setAuthor("author1");
        data1.setFileName("Test1.java");
        data1.setMethodName("Test1.main()");
        data1.setChangeType("CONDITION_EXPRESSION_CHANGE");
        data1.setTimestamp(1L);

        data2 = new SingleChangeInfo();
        data2.setAuthor("author2");
        data2.setFileName("Test2.java");
        data2.setMethodName("Test2.main()");
        data2.setChangeType("STATEMENT_UPDATE");
        data2.setTimestamp(3L);

        data3 = new SingleChangeInfo();
        data3.setAuthor("author3");
        data3.setFileName("Test3.java");
        data3.setMethodName("Test3.main()");
        data3.setChangeType("RETURN_TYPE_DELETE");
        data3.setTimestamp(4L);

        data4 = new SingleChangeInfo();
        data4.setAuthor("author4");
        data4.setFileName("Test4.java");
        data4.setMethodName("Test4.main()");

        data4.setChangeType("ALTERNATIVE_PART_DELETE");
        data4.setTimestamp(6L);
    }

    @Test
    public void metricsResultTest() throws SQLException, IOException, Exception {
        db.connect();
        db.insertRevisionData(data1, PROJECT_NAME, 2L, 5L);
        db.insertRevisionData(data2, PROJECT_NAME, 2L, 5L);
        db.insertRevisionData(data3, PROJECT_NAME, 2L, 5L);
        db.insertRevisionData(data4, PROJECT_NAME, 2L, 5L);

        assertThat("db should contains data", db.isDataExistInDb(PROJECT_NAME, 2L, 5L), is(true));

        db.getDataFromDB(2L, 5L, PROJECT_NAME, 2L, 5L);
        Map<String, Metric> metrics = db.getMetrics();
        assertThat("wrong map size", metrics.size(), is(2));

        Metric metric1 = metrics.get("Test2.main()");
        assertThat("Wrong method name", metric1.getMethodName(), is("Test2.main()"));
        assertThat("Wrong metric result", metric1.getStmtUpdated(), is(1.0));
        assertThat("Wrong metric result", metric1.getElseDeleted(), is(0.0));
        assertThat("Wrong metric result", metric1.getMethodHistories(), is(1.0));
        assertThat("Wrong metric result", metric1.getAuthors(), is(1.0));
        assertThat("Wrong metric result", metric1.getChurn(), is(0.0));

        Metric metric2 = metrics.get("Test3.main()");
        assertThat("Wrong method name", metric2.getMethodName(), is("Test3.main()"));
        assertThat("Wrong metric result", metric2.getDecl(), is(1.0));
        assertThat("Wrong metric result", metric2.getStmtUpdated(), is(0.0));
        assertThat("Wrong metric result", metric2.getElseDeleted(), is(0.0));
        assertThat("Wrong metric result", metric2.getMethodHistories(), is(1.0));
        assertThat("Wrong metric result", metric2.getAuthors(), is(1.0));
        assertThat("Wrong metric result", metric2.getChurn(), is(0.0));

        assertTrue("should not contain metric", metrics.get("Test1.main()") == null);
        assertTrue("should not contain metric", metrics.get("Test4.main()") == null);

    }

}
