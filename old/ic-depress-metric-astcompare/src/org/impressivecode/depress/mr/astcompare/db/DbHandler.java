package org.impressivecode.depress.mr.astcompare.db;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.impressivecode.depress.mr.astcompare.ast.SingleChangeInfo;

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
public class DbHandler {
    private static DbHandler instance;
    private File dbFile;
    private Connection connection;
    private Statement statement;
    private Map<String, Metric> metrics;

    private DbHandler() throws IOException {
        metrics = new HashMap<String, Metric>();
        createDbFile();
    }

    public static DbHandler getInstance() throws IOException {
        if (instance == null) {
            instance = new DbHandler();
        }
        return instance;
    }

    public void connect() throws SQLException, IOException, Exception {
        Class.forName("org.sqlite.JDBC");
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
        this.statement = connection.createStatement();
        createDB();
    }

    public void insertRevisionData(SingleChangeInfo data, String selectedProjectName, long revisionDateMin,
            long revisionDateMax) throws SQLException {
        insertRevisionData(data.getTimestamp(), data.getRevisionId(), data.getFileName(), data.getMethodName(),
                data.getAuthor(), data.getComment(), data.getChangeEntity(), data.getChangeType(), selectedProjectName,
                revisionDateMin, revisionDateMax);
    }

    public boolean isDataExistInDb(String project, long revisionDateMin, long revisionDateMax) throws SQLException {

        ResultSet result = statement.executeQuery("SELECT count(*) FROM metrics WHERE Project = '" + project
                + "' AND RevRange = '" + revisionDateMin + "-" + revisionDateMax + "';");
        while (result.next()) {
            return result.getInt(1) > 0;
        }
        return false;
    }

    public Map<String, Metric> getMetrics() {
        return metrics;
    }

    public void getDataFromDB(long timeFrameStart, long timeFrameEnd, String projectName, long revisionDateMin,
            long revisionDateMax) throws SQLException {
        clearMetricsMap();
        getResult(
                "SELECT MethodName, CAST(count(MethodName)AS NUMERIC) from metrics group by methodName having RevisionTime between "
                        + timeFrameStart + " and " + timeFrameEnd + " AND Project = '" + projectName
                        + "' AND RevRange = '" + revisionDateMin + "-" + revisionDateMax + "';",
                Metric.ALL_METHOD_HISTORIES);
        getResult(
                "SELECT MethodName, CAST(count(MethodName)AS NUMERIC) from metrics group by methodName having ChangeType IN ('RETURN_TYPE_CHANGE', 'RETURN_TYPE_DELETE', 'RETURN_TYPE_INSERT', 'METHOD_RENAMING', 'PARAMETER_DELETE', 'PARAMETER_INSERT', 'PARAMETER_ORDERING_CHANGE', 'PARAMETER_RENAMING', 'DECREASING_ACCESSIBILITY_CHANGE', 'INCREASING_ACCESSIBILITY_CHANGE', 'PARAMETER_TYPE_CHANGE','STATEMENT_INSERT','STATEMENT_UPDATE','STATEMENT_DELETE', 'STATEMENT_PARENT_CHANGE', 'CONDITION_EXPRESSION_CHANGE','ALTERNATIVE_PART_INSERT','ALTERNATIVE_PART_DELETE') AND RevisionTime between "
                        + timeFrameStart
                        + " and "
                        + timeFrameEnd
                        + " AND Project = '"
                        + projectName
                        + "' AND RevRange = '" + revisionDateMin + "-" + revisionDateMax + "';",
                Metric.METHOD_HISTORIES);
        getResult(
                "SELECT MethodName,  CAST(count(distinct Author) AS NUMERIC)  from metrics  group by methodName having RevisionTime between "
                        + timeFrameStart
                        + " and "
                        + timeFrameEnd
                        + " AND Project = '"
                        + projectName
                        + "' AND RevRange = '" + revisionDateMin + "-" + revisionDateMax + "';", Metric.AUTHORS);
        getResult(
                "SELECT MethodName,  CAST(count(ChangeType) AS NUMERIC) from metrics  group by methodName, ChangeType having ChangeType = 'STATEMENT_INSERT' and RevisionTime between "
                        + timeFrameStart
                        + " and "
                        + timeFrameEnd
                        + " AND Project = '"
                        + projectName
                        + "' AND RevRange = '" + revisionDateMin + "-" + revisionDateMax + "';", Metric.STMT_ADDED);
        getResult(
                "select  methodName, CAST(max(total) AS NUMERIC)  from (SELECT methodName, count(ChangeType) as  total from metrics group by  MethodName, ChangeType, revisionId having ChangeType = 'STATEMENT_INSERT' and RevisionTime between "
                        + timeFrameStart
                        + " and "
                        + timeFrameEnd
                        + " AND Project = '"
                        + projectName
                        + "' AND RevRange = '" + revisionDateMin + "-" + revisionDateMax + "') group by methodName;",
                Metric.MAX_STMT_ADDED);
        getResult(
                "select  methodName, CAST(avg(total) AS NUMERIC)  from (SELECT methodName, count(ChangeType) as  total from metrics group by  MethodName, ChangeType, revisionId having ChangeType = 'STATEMENT_INSERT' and RevisionTime between "
                        + timeFrameStart
                        + " and "
                        + timeFrameEnd
                        + " AND Project = '"
                        + projectName
                        + "' AND RevRange = '" + revisionDateMin + "-" + revisionDateMax + "') group by methodName;",
                Metric.AVG_STMT_ADDED);
        getResult(
                "SELECT MethodName,  CAST(count(ChangeType) AS NUMERIC) from metrics  group by methodName, ChangeType  having ChangeType = 'STATEMENT_UPDATE' and RevisionTime between "
                        + timeFrameStart
                        + " and "
                        + timeFrameEnd
                        + " AND Project = '"
                        + projectName
                        + "' AND RevRange = '" + revisionDateMin + "-" + revisionDateMax + "';", Metric.STMT_UPDATED);
        getResult(
                "select  methodName, CAST(max(total) AS NUMERIC)  from (SELECT methodName, count(ChangeType) as  total from metrics group by  MethodName, ChangeType, revisionId having ChangeType = 'STATEMENT_UPDATE' and RevisionTime between "
                        + timeFrameStart
                        + " and "
                        + timeFrameEnd
                        + " AND Project = '"
                        + projectName
                        + "' AND RevRange = '" + revisionDateMin + "-" + revisionDateMax + "') group by methodName;",
                Metric.MAX_STMT_UPDATED);
        getResult(
                "select  methodName, CAST(avg(total) AS NUMERIC)  from (SELECT methodName, count(ChangeType) as  total from metrics group by  MethodName, ChangeType, revisionId having ChangeType = 'STATEMENT_UPDATE' and RevisionTime between "
                        + timeFrameStart
                        + " and "
                        + timeFrameEnd
                        + " AND Project = '"
                        + projectName
                        + "' AND RevRange = '" + revisionDateMin + "-" + revisionDateMax + "') group by methodName;",
                Metric.AVG_STMT_UPDATED);
        getResult(
                "SELECT MethodName,  CAST(count(ChangeType) AS NUMERIC) from metrics  group by methodName, ChangeType having ChangeType = 'STATEMENT_DELETE' and RevisionTime between "
                        + timeFrameStart
                        + " and "
                        + timeFrameEnd
                        + " AND Project = '"
                        + projectName
                        + "' AND RevRange = '" + revisionDateMin + "-" + revisionDateMax + "';", Metric.STMT_DELETED);
        getResult(
                "select  methodName, CAST(max(total) AS NUMERIC)  from (SELECT methodName, count(ChangeType) as  total from metrics group by  MethodName, ChangeType, revisionId having ChangeType = 'STATEMENT_DELETE' and RevisionTime between "
                        + timeFrameStart
                        + " and "
                        + timeFrameEnd
                        + " AND Project = '"
                        + projectName
                        + "' AND RevRange = '" + revisionDateMin + "-" + revisionDateMax + "') group by methodName;",
                Metric.MAX_STMT_DELETED);

        getResult(
                "select  methodName, CAST(avg(total) AS NUMERIC)  from (SELECT methodName, count(ChangeType) as  total from metrics group by  MethodName, ChangeType, revisionId having ChangeType = 'STATEMENT_DELETE' and RevisionTime between "
                        + timeFrameStart
                        + " and "
                        + timeFrameEnd
                        + " AND Project = '"
                        + projectName
                        + "' AND RevRange = '" + revisionDateMin + "-" + revisionDateMax + "') group by methodName;",
                Metric.AVG_STMT_DELETED);
        getResult(
                "SELECT MethodName,  CAST(count(ChangeType) AS NUMERIC) from metrics  group by methodName, ChangeType  having ChangeType = 'STATEMENT_PARENT_CHANGE' and RevisionTime between "
                        + timeFrameStart
                        + " and "
                        + timeFrameEnd
                        + " AND Project = '"
                        + projectName
                        + "' AND RevRange = '" + revisionDateMin + "-" + revisionDateMax + "';",
                Metric.STATEMENT_PARENT_CHANGE);
        getResult(
                "select methodname, sum(substract) from (select  m.methodname, totalA-totalD as substract from metrics m, "
                        + "(SELECT methodname,revisionid, count(ChangeType) as totalA from metrics a  group by methodname, changetype, revisionid having ChangeType = 'STATEMENT_INSERT') a, "
                        + "(SELECT methodname,revisionid, count(ChangeType) as totalD from metrics a  group by methodname, changetype, revisionid having ChangeType = 'STATEMENT_DELETE') d "
                        + " where m.changetype in ('STATEMENT_INSERT', 'STATEMENT_DELETE') and a.methodname = m.methodname and a.revisionid = m.revisionid and d.methodname = m.methodname and d.revisionid = m.revisionid and RevisionTime between "
                        + timeFrameStart + " and " + timeFrameEnd + " AND Project = '" + projectName
                        + "' AND RevRange = '" + revisionDateMin + "-" + revisionDateMax
                        + "' group by m.methodname, m.revisionid) group by methodname;", Metric.CHURN);
        getResult(
                "select methodname, max(substract) from (select  m.methodname, totalA-totalD as substract from metrics m, "
                        + "(SELECT methodname,revisionid, count(ChangeType) as totalA from metrics a  group by methodname, changetype, revisionid having ChangeType = 'STATEMENT_INSERT') a, "
                        + "(SELECT methodname,revisionid, count(ChangeType) as totalD from metrics a  group by methodname, changetype, revisionid having ChangeType = 'STATEMENT_DELETE') d "
                        + " where m.changetype in ('STATEMENT_INSERT', 'STATEMENT_DELETE') and a.methodname = m.methodname and a.revisionid = m.revisionid and d.methodname = m.methodname and d.revisionid = m.revisionid and RevisionTime between "
                        + timeFrameStart + " and " + timeFrameEnd + " AND Project = '" + projectName
                        + "' AND RevRange = '" + revisionDateMin + "-" + revisionDateMax
                        + "' group by m.methodname, m.revisionid) group by methodname;", Metric.MAX_CHURN);
        getResult(
                "select methodname, avg(substract) from (select  m.methodname, totalA-totalD as substract from metrics m, "
                        + "(SELECT methodname,revisionid, count(ChangeType) as totalA from metrics a  group by methodname, changetype, revisionid having ChangeType = 'STATEMENT_INSERT') a, "
                        + "(SELECT methodname,revisionid, count(ChangeType) as totalD from metrics a  group by methodname, changetype, revisionid having ChangeType = 'STATEMENT_DELETE') d "
                        + " where m.changetype in ('STATEMENT_INSERT', 'STATEMENT_DELETE') and a.methodname = m.methodname and a.revisionid = m.revisionid and d.methodname = m.methodname and d.revisionid = m.revisionid and RevisionTime between "
                        + timeFrameStart + " and " + timeFrameEnd + " AND Project = '" + projectName
                        + "' AND RevRange = '" + revisionDateMin + "-" + revisionDateMax
                        + "' group by m.methodname, m.revisionid) group by methodname;", Metric.AVG_CHURN);
        getResult(
                "SELECT MethodName,  CAST(count(ChangeType) AS NUMERIC) from metrics group by methodName, ChangeType having ChangeType IN ('RETURN_TYPE_CHANGE', 'RETURN_TYPE_DELETE', 'RETURN_TYPE_INSERT', 'METHOD_RENAMING', 'PARAMETER_DELETE', 'PARAMETER_INSERT', 'PARAMETER_ORDERING_CHANGE', 'PARAMETER_RENAMING', 'PARAMETER_TYPE_CHANGE', 'DECREASING_ACCESSIBILITY_CHANGE', 'INCREASING_ACCESSIBILITY_CHANGE') and RevisionTime between "
                        + timeFrameStart
                        + " and "
                        + timeFrameEnd
                        + " AND Project = '"
                        + projectName
                        + "' AND RevRange = '" + revisionDateMin + "-" + revisionDateMax + "';", Metric.DECL);
        getResult(
                "SELECT MethodName,  CAST(count(ChangeType) AS NUMERIC) from metrics  group by methodName, ChangeType having ChangeType = 'CONDITION_EXPRESSION_CHANGE' and RevisionTime between "
                        + timeFrameStart
                        + " and "
                        + timeFrameEnd
                        + " AND Project = '"
                        + projectName
                        + "' AND RevRange = '" + revisionDateMin + "-" + revisionDateMax + "';", Metric.COND);
        getResult(
                "SELECT MethodName,  CAST(count(ChangeType) AS NUMERIC) from metrics  group by methodName, ChangeType having ChangeType = 'ALTERNATIVE_PART_INSERT' and RevisionTime between "
                        + timeFrameStart
                        + " and "
                        + timeFrameEnd
                        + " AND Project = '"
                        + projectName
                        + "' AND RevRange = '" + revisionDateMin + "-" + revisionDateMax + "';", Metric.ELSE_ADDED);
        getResult(
                "SELECT MethodName,  CAST(count(ChangeType) AS NUMERIC) from metrics  group by methodName, ChangeType having ChangeType = 'ALTERNATIVE_PART_DELETE' and RevisionTime between "
                        + timeFrameStart
                        + " and "
                        + timeFrameEnd
                        + " AND Project = '"
                        + projectName
                        + "' AND RevRange = '" + revisionDateMin + "-" + revisionDateMax + "';", Metric.ELSE_DELETED);
    }

    private void createDbFile() throws IOException {
        if (dbFile == null) {
            dbFile = File.createTempFile("AstPluginCompare", "DbFile");
            dbFile.deleteOnExit();
        }

    }

    private void createDB() throws SQLException {
        statement.executeUpdate("drop table if exists Metrics;");
        statement
                .executeUpdate("create table Metrics(RevisionTime bigint, RevisionId text, FileName text, MethodName text, Author text, Comment text, ChangeEntity text, ChangeType text, Project text, RevRange text);");

    }

    private void insertRevisionData(long timestamp, String revisionId, String fileName, String methodName,
            String author, String comment, String changeEntity, String changeType, String projectName,
            long revisionDateMin, long revisionDateMax) throws SQLException {
        PreparedStatement statement = this.connection
                .prepareStatement("insert into Metrics values(?,?,?,?,?,?,?,?,?,?);");
        statement.setLong(1, timestamp);
        statement.setString(2, revisionId);
        statement.setString(3, fileName);
        statement.setString(4, methodName);
        statement.setString(5, author);
        statement.setString(6, comment);
        statement.setString(7, changeEntity);
        statement.setString(8, changeType);
        statement.setString(9, projectName);
        statement.setString(10, revisionDateMin + "-" + revisionDateMax);

        statement.executeUpdate();
    }

    private void clearMetricsMap() {
        metrics.clear();
        if (!metrics.isEmpty()) {
            metrics = new HashMap<String, Metric>();
        }
    }

    private void getResult(String query, int metricName) throws SQLException {

        ResultSet result = statement.executeQuery(query);
        while (result.next()) {
            insertMetric(result.getString(1), metricName, result.getDouble(2));
        }

    }

    private void insertMetric(String methodName, int metricName, double value) {
        if (metrics.containsKey(methodName)) {
            metrics.get(methodName).setValue(metricName, value);
        } else {
            Metric metric = new Metric(methodName);
            metric.setValue(metricName, value);
            metrics.put(methodName, metric);
        }
    }

}
