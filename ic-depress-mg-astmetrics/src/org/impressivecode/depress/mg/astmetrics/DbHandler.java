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
package org.impressivecode.depress.mg.astmetrics;

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

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

/**
 * @author Mateusz Kutyba, Wroclaw University of Technology
 */
public class DbHandler {
    public File dbFile;
    private Connection connection;
    private Statement statement;
    private Map<String, MetricEntry> metrics;
    private ExecutionContext exec;
    private Long progressCounter = 0l;
    private Long metricsTotalCount = 50l;
    private double progressFrom = 0.2d;
    private double progressTo = 0.2d;

    public void setExec(ExecutionContext exec) {
        this.exec = exec;
    }

    public DbHandler() throws IOException {
        metrics = new HashMap<String, MetricEntry>();
        createDbFile();
    }

    public void connect() throws SQLException, IOException, Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
        this.statement = connection.createStatement();
        createDB();
    }

    public void insertChangeData(SingleChangeInfo data) throws SQLException {
        insertChangeData(data.getMethodName(), data.getAuthor(), data.getRevisionId(), data.getChangeEntity(),
                data.getChangeType());
    }

    public boolean hasDataInDb() throws SQLException {
        ResultSet result = statement.executeQuery("SELECT count(*) FROM metrics;");
        while (result.next()) {
            return result.getInt(1) > 0;
        }
        return false;
    }

    public int countDataInDb() throws SQLException {
        ResultSet result = statement.executeQuery("SELECT count(*) FROM metrics;");
        while (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public Map<String, MetricEntry> getMetrics() {
        return metrics;
    }

    private String queryCountChangesOfType(String where) {
        String query = " SELECT MethodName, CAST(count(ChangeType) AS NUMERIC) FROM metrics WHERE " + where
                + " GROUP BY MethodName;";
        return query;
    }

    public void calculateMetrics() throws SQLException, CanceledExecutionException {
        clearMetricsMap();

        saveMetricResult(" SELECT MethodName, CAST(count(MethodName) AS NUMERIC) " + " FROM metrics "
                + " GROUP BY MethodName;", MetricEntry.ALL_METHOD_HISTORIES);

        saveMetricResult(
                " SELECT MethodName, CAST(count(MethodName) AS NUMERIC) "
                        + " FROM metrics "
                        + " WHERE ChangeType IN ('RETURN_TYPE_CHANGE', 'RETURN_TYPE_DELETE', 'RETURN_TYPE_INSERT', 'METHOD_RENAMING', 'PARAMETER_DELETE', 'PARAMETER_INSERT', 'PARAMETER_ORDERING_CHANGE', 'PARAMETER_RENAMING', 'DECREASING_ACCESSIBILITY_CHANGE', 'INCREASING_ACCESSIBILITY_CHANGE', 'PARAMETER_TYPE_CHANGE','STATEMENT_INSERT','STATEMENT_UPDATE','STATEMENT_DELETE', 'STATEMENT_PARENT_CHANGE', 'CONDITION_EXPRESSION_CHANGE','ALTERNATIVE_PART_INSERT','ALTERNATIVE_PART_DELETE','STATEMENT_ORDERING_CHANGE') "
                        + " GROUP BY MethodName;", MetricEntry.METHOD_HISTORIES);

        saveMetricResult(" SELECT MethodName, CAST(count(distinct Author) AS NUMERIC)  " + " FROM metrics "
                + " GROUP BY MethodName;", MetricEntry.AUTHORS);

        String STMT_ADDED_query = " SELECT MethodName, CAST(count(ChangeType) AS NUMERIC) AS total " + " FROM metrics "
                + " WHERE ChangeType = 'STATEMENT_INSERT'" + " GROUP BY MethodName";
        saveMetricResult(STMT_ADDED_query, MetricEntry.STMT_ADDED);

        saveMetricResult(" SELECT MethodName, CAST(max(total) AS NUMERIC)  FROM (" + STMT_ADDED_query
                + ", RevisionId) " + " GROUP BY MethodName;", MetricEntry.MAX_STMT_ADDED);

        saveMetricResult(" SELECT MethodName, CAST(avg(total) AS NUMERIC)  FROM (" + STMT_ADDED_query
                + ", RevisionId) " + " GROUP BY MethodName;", MetricEntry.AVG_STMT_ADDED);

        String STMT_UPDATED_query = " SELECT MethodName, CAST(count(ChangeType) AS NUMERIC) AS total "
                + " FROM metrics " + " WHERE ChangeType = 'STATEMENT_UPDATE'" + " GROUP BY MethodName";
        saveMetricResult(STMT_UPDATED_query, MetricEntry.STMT_UPDATED);

        saveMetricResult(" SELECT MethodName, CAST(max(total) AS NUMERIC)  FROM (" + STMT_UPDATED_query
                + ", RevisionId) " + " GROUP BY MethodName;", MetricEntry.MAX_STMT_UPDATED);

        saveMetricResult(" SELECT MethodName, CAST(avg(total) AS NUMERIC)  FROM (" + STMT_UPDATED_query
                + ", RevisionId) " + " GROUP BY MethodName;", MetricEntry.AVG_STMT_UPDATED);

        String STMT_DELETED_query = " SELECT MethodName, CAST(count(ChangeType) AS NUMERIC) AS total "
                + " FROM metrics " + " WHERE ChangeType = 'STATEMENT_DELETE'" + " GROUP BY MethodName";
        saveMetricResult(STMT_DELETED_query, MetricEntry.STMT_DELETED);

        saveMetricResult(" SELECT MethodName, CAST(max(total) AS NUMERIC)  FROM (" + STMT_DELETED_query
                + ", RevisionId) " + " GROUP BY MethodName;", MetricEntry.MAX_STMT_DELETED);

        saveMetricResult(" SELECT MethodName, CAST(avg(total) AS NUMERIC)  FROM (" + STMT_DELETED_query
                + ", RevisionId) " + " GROUP BY MethodName;", MetricEntry.AVG_STMT_DELETED);

        saveMetricResult(" SELECT MethodName, CAST(count(ChangeType) AS NUMERIC) AS total " + " FROM metrics "
                + " WHERE ChangeType = 'STATEMENT_PARENT_CHANGE'" + " GROUP BY MethodName",
                MetricEntry.STATEMENT_PARENT_CHANGE);

        String ChurnForRevisions_query = " SELECT m.MethodName, m.revisionid, COALESCE(totalA, 0), COALESCE(totalD, 0), COALESCE(totalA, 0)-COALESCE(totalD, 0) as churn_for_revision "
                + " FROM metrics m "
                + " LEFT JOIN ( "
                + "         SELECT MethodName,RevisionId, count(ChangeType) as totalA "
                + "         FROM metrics a "
                + "         WHERE ChangeType = 'STATEMENT_INSERT' "
                + "         GROUP BY MethodName, RevisionId "
                + " ) a "
                + " ON a.MethodName = m.MethodName AND a.RevisionId = m.RevisionId "
                + " LEFT JOIN ( "
                + "         SELECT MethodName,RevisionId, count(ChangeType) as totalD "
                + "         FROM metrics a "
                + "         WHERE ChangeType = 'STATEMENT_DELETE' "
                + "         GROUP BY MethodName, RevisionId "
                + " ) d "
                + " ON d.MethodName = m.MethodName AND d.RevisionId = m.RevisionId "
                + " WHERE m.ChangeType IN ('STATEMENT_INSERT', 'STATEMENT_DELETE') "
                + " GROUP BY m.MethodName, m.revisionid ";

        saveMetricResult(" SELECT MethodName, sum(churn_for_revision) FROM ( " + ChurnForRevisions_query + " ) "
                + " GROUP BY MethodName;", MetricEntry.CHURN);

        saveMetricResult(" SELECT MethodName, max(churn_for_revision) FROM ( " + ChurnForRevisions_query + " ) "
                + " GROUP BY MethodName;", MetricEntry.MAX_CHURN);

        saveMetricResult(" SELECT MethodName, avg(churn_for_revision) FROM ( " + ChurnForRevisions_query + " ) "
                + " GROUP BY MethodName;", MetricEntry.AVG_CHURN);

        saveMetricResult(
                " SELECT MethodName, CAST(count(ChangeType) AS NUMERIC) "
                        + " FROM metrics "
                        + " WHERE ChangeType IN ('RETURN_TYPE_CHANGE', 'RETURN_TYPE_DELETE', 'RETURN_TYPE_INSERT', 'METHOD_RENAMING', 'PARAMETER_DELETE', 'PARAMETER_INSERT', 'PARAMETER_ORDERING_CHANGE', 'PARAMETER_RENAMING', 'PARAMETER_TYPE_CHANGE', 'DECREASING_ACCESSIBILITY_CHANGE', 'INCREASING_ACCESSIBILITY_CHANGE') "
                        + " GROUP BY MethodName;", MetricEntry.DECL);

        saveMetricResult(" SELECT MethodName, CAST(count(ChangeType) AS NUMERIC) " + " FROM metrics "
                + " WHERE ChangeType = 'CONDITION_EXPRESSION_CHANGE' " + " GROUP BY MethodName;", MetricEntry.COND);

        saveMetricResult(" SELECT MethodName, CAST(count(ChangeType) AS NUMERIC) " + " FROM metrics "
                + " WHERE ChangeType = 'ALTERNATIVE_PART_INSERT' " + " GROUP BY MethodName;", MetricEntry.ELSE_ADDED);

        saveMetricResult(" SELECT MethodName, CAST(count(ChangeType) AS NUMERIC) " + " FROM metrics "
                + " WHERE ChangeType = 'ALTERNATIVE_PART_DELETE' " + " GROUP BY MethodName;", MetricEntry.ELSE_DELETED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_INSERT' AND (ChangeEntity LIKE 'FOR_STATEMENT%' OR ChangeEntity LIKE 'WHILE_STATEMENT%') "),
                MetricEntry.LOOPS_ADDED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_UPDATE' AND (ChangeEntity LIKE 'FOR_STATEMENT%' OR ChangeEntity LIKE 'WHILE_STATEMENT%') "),
                MetricEntry.LOOPS_UPDATED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_DELETE' AND (ChangeEntity LIKE 'FOR_STATEMENT%' OR ChangeEntity LIKE 'WHILE_STATEMENT%') "),
                MetricEntry.LOOPS_DELETED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_INSERT' AND ChangeEntity LIKE 'VARIABLE_DECLARATION_STATEMENT%' "),
                MetricEntry.VARIABLES_ADDED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_UPDATE' AND ChangeEntity LIKE 'VARIABLE_DECLARATION_STATEMENT%' "),
                MetricEntry.VARIABLES_UPDATED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_DELETE' AND ChangeEntity LIKE 'VARIABLE_DECLARATION_STATEMENT%' "),
                MetricEntry.VARIABLES_DELETED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_INSERT' AND ChangeEntity LIKE 'ASSIGNMENT%' "),
                MetricEntry.ASSIGMENTS_ADDED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_UPDATE' AND ChangeEntity LIKE 'ASSIGNMENT%' "),
                MetricEntry.ASSIGMENTS_UPDATED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_DELETE' AND ChangeEntity LIKE 'ASSIGNMENT%' "),
                MetricEntry.ASSIGMENTS_DELETED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_INSERT' AND ChangeEntity LIKE 'RETURN_STATEMENT%' "),
                MetricEntry.RETURNS_ADDED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_UPDATE' AND ChangeEntity LIKE 'RETURN_STATEMENT%' "),
                MetricEntry.RETURNS_UPDATED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_DELETE' AND ChangeEntity LIKE 'RETURN_STATEMENT%' "),
                MetricEntry.RETURNS_DELETED);

        saveMetricResult(
                queryCountChangesOfType("(ChangeType = 'STATEMENT_INSERT' OR ChangeType = 'ALTERNATIVE_PART_INSERT') AND ChangeEntity LIKE '%null%' "),
                MetricEntry.NULLS_ADDED);

        saveMetricResult(
                queryCountChangesOfType("(ChangeType = 'STATEMENT_UPDATE' OR ChangeType = 'ALTERNATIVE_PART_UPDATE') AND ChangeEntity LIKE '%null%' "),
                MetricEntry.NULLS_UPDATED);

        saveMetricResult(
                queryCountChangesOfType("(ChangeType = 'STATEMENT_DELETE' OR ChangeType = 'ALTERNATIVE_PART_DELETE') AND ChangeEntity LIKE '%null%' "),
                MetricEntry.NULLS_DELETED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_INSERT' AND ChangeEntity LIKE 'SWITCH_CASE%' "),
                MetricEntry.CASES_ADDED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_UPDATE' AND ChangeEntity LIKE 'SWITCH_CASE%' "),
                MetricEntry.CASES_UPDATED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_DELETE' AND ChangeEntity LIKE 'SWITCH_CASE%' "),
                MetricEntry.CASES_DELETED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_INSERT' AND ChangeEntity LIKE 'BREAK_STATEMENT%' "),
                MetricEntry.BREAKS_ADDED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_UPDATE' AND ChangeEntity LIKE 'BREAK_STATEMENT%' "),
                MetricEntry.BREAKS_UPDATED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_DELETE' AND ChangeEntity LIKE 'BREAK_STATEMENT%' "),
                MetricEntry.BREAKS_DELETED);

        saveMetricResult(
                queryCountChangesOfType("(ChangeType = 'STATEMENT_INSERT' OR ChangeType = 'ALTERNATIVE_PART_INSERT') AND (ChangeEntity LIKE '%new %' OR ChangeEntity LIKE '%Class.forName%' OR ChangeEntity LIKE '%.clone()%' OR ChangeEntity LIKE '%.newInstance()%' OR ChangeEntity LIKE '%.readObject()%') "),
                MetricEntry.OBJECTS_ADDED);

        saveMetricResult(
                queryCountChangesOfType("(ChangeType = 'STATEMENT_UPDATE' OR ChangeType = 'ALTERNATIVE_PART_UPDATE') AND (ChangeEntity LIKE '%new %' OR ChangeEntity LIKE '%Class.forName%' OR ChangeEntity LIKE '%.clone()%' OR ChangeEntity LIKE '%.newInstance()%' OR ChangeEntity LIKE '%.readObject()%') "),
                MetricEntry.OBJECTS_UPDATED);

        saveMetricResult(
                queryCountChangesOfType("(ChangeType = 'STATEMENT_DELETE' OR ChangeType = 'ALTERNATIVE_PART_DELETE') AND (ChangeEntity LIKE '%new %' OR ChangeEntity LIKE '%Class.forName%' OR ChangeEntity LIKE '%.clone()%' OR ChangeEntity LIKE '%.newInstance()%' OR ChangeEntity LIKE '%.readObject()%') "),
                MetricEntry.OBJECTS_DELETED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_INSERT' AND ChangeEntity LIKE 'CATCH_CLAUSE%' "),
                MetricEntry.CATCHES_ADDED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_UPDATE' AND ChangeEntity LIKE 'CATCH_CLAUSE%' "),
                MetricEntry.CATCHES_UPDATED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_DELETE' AND ChangeEntity LIKE 'CATCH_CLAUSE%' "),
                MetricEntry.CATCHES_DELETED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_INSERT' AND ChangeEntity LIKE 'THROW_STATEMENT%' "),
                MetricEntry.THROWS_ADDED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_UPDATE' AND ChangeEntity LIKE 'THROW_STATEMENT%' "),
                MetricEntry.THROWS_UPDATED);

        saveMetricResult(
                queryCountChangesOfType("ChangeType = 'STATEMENT_DELETE' AND ChangeEntity LIKE 'THROW_STATEMENT%' "),
                MetricEntry.THROWS_DELETED);

        connection.close();
    }

    private void createDbFile() throws IOException {
        if (dbFile == null) {
            dbFile = File.createTempFile("AstLogParser", "DbFile");
            dbFile.deleteOnExit();
        }
    }

    public void destroyDbFile() {
        if (dbFile != null) {
            dbFile.delete();
        }
    }

    private void createDB() throws SQLException {
        statement.executeUpdate("DROP TABLE IF EXISTS Metrics;");
        statement
                .executeUpdate("CREATE TABLE Metrics(MethodName TEXT, Author TEXT, RevisionId TEXT, ChangeEntity TEXT, ChangeType TEXT);");

    }

    private void insertChangeData(String methodName, String author, String revisionId, String changeEntity,
            String changeType) throws SQLException {
        PreparedStatement statement = this.connection.prepareStatement("INSERT INTO Metrics VALUES(?,?,?,?,?);");
        statement.setString(1, methodName);
        statement.setString(2, author);
        statement.setString(3, revisionId);
        statement.setString(4, changeEntity);
        statement.setString(5, changeType);

        statement.executeUpdate();
    }

    private void clearMetricsMap() {
        metrics.clear();
        if (!metrics.isEmpty()) {
            metrics = new HashMap<String, MetricEntry>();
        }
    }

    private void saveMetricResult(String query, int metricName) throws SQLException, CanceledExecutionException {
        ResultSet result = statement.executeQuery(query);
        while (result.next()) {
            insertMetric(result.getString(1), metricName, result.getDouble(2));
        }
        checkIfCancelledAndSetProgress(progressFrom + progressTo * ++progressCounter / metricsTotalCount);
    }

    private void insertMetric(String methodName, int metricName, double value) throws SQLException {
        if (metrics.containsKey(methodName)) {
            metrics.get(methodName).setValue(metricName, value);
        } else {
            MetricEntry metricEntry = new MetricEntry(methodName);
            metricEntry.setValue(metricName, value);
            metrics.put(methodName, metricEntry);
        }
    }

    private void checkIfCancelledAndSetProgress(Double progress) throws CanceledExecutionException {
        if (exec != null) {
            exec.checkCanceled();

            // no progress change
            if (progress != null) {
                exec.setProgress(progress);
            }
        }
    }

}
