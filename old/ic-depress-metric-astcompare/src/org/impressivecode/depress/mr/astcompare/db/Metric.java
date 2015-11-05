package org.impressivecode.depress.mr.astcompare.db;

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
public class Metric {

    private final String methodName;
    private Double allMethodHistories;
    private Double methodHistories;
    private Double authors;
    private Double stmtAdded;
    private Double maxStmtAdded;
    private Double avgStmtAdded;
    private Double stmtUpdated;
    private Double maxStmtUpdated;
    private Double avgStmtUpdated;
    private Double stmtDeleted;
    private Double maxStmtDeleted;
    private Double avgStmtDeleted;
    private Double stmtParentChanged;
    private Double churn;
    private Double maxChurn;
    private Double avgChurn;
    private Double decl;
    private Double cond;
    private Double elseAdded;
    private Double elseDeleted;

    public static final int ALL_METHOD_HISTORIES = 0;
    public static final int METHOD_HISTORIES = 1;
    public static final int AUTHORS = 2;
    public static final int STMT_ADDED = 3;
    public static final int MAX_STMT_ADDED = 4;
    public static final int AVG_STMT_ADDED = 5;
    public static final int STMT_UPDATED = 6;
    public static final int MAX_STMT_UPDATED = 7;
    public static final int AVG_STMT_UPDATED = 8;
    public static final int STMT_DELETED = 9;
    public static final int MAX_STMT_DELETED = 10;
    public static final int AVG_STMT_DELETED = 11;
    public static final int STATEMENT_PARENT_CHANGE = 12;
    public static final int CHURN = 13;
    public static final int MAX_CHURN = 14;
    public static final int AVG_CHURN = 15;
    public static final int DECL = 16;
    public static final int COND = 17;
    public static final int ELSE_ADDED = 18;
    public static final int ELSE_DELETED = 19;

    public Metric(String methodName) {
        this.methodName = methodName;
    }

    public boolean hasResults() {
        return hasMetric(allMethodHistories) || hasMetric(methodHistories) || hasMetric(authors)
                || hasMetric(stmtAdded) || hasMetric(maxStmtAdded) || hasMetric(avgStmtAdded) || hasMetric(stmtUpdated)
                || hasMetric(maxStmtUpdated) || hasMetric(avgStmtUpdated) || hasMetric(stmtDeleted)
                || hasMetric(maxStmtDeleted) || hasMetric(avgStmtDeleted) || hasMetric(stmtParentChanged)
                || hasMetric(churn) || hasMetric(maxChurn) || hasMetric(avgChurn) || hasMetric(decl) || hasMetric(cond)
                || hasMetric(elseAdded) || hasMetric(elseDeleted);
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(allMethodHistories, authors, avgChurn, avgStmtAdded,
                avgStmtDeleted, avgStmtUpdated, churn, cond, decl, elseAdded, elseDeleted, maxChurn, maxStmtAdded,
                maxStmtDeleted, maxStmtUpdated, methodHistories, methodName, stmtAdded, stmtDeleted, stmtParentChanged,
                stmtUpdated);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Metric other = (Metric) obj;

        return com.google.common.base.Objects.equal(allMethodHistories, other.allMethodHistories)
                && com.google.common.base.Objects.equal(authors, other.authors)
                && com.google.common.base.Objects.equal(avgChurn, other.avgChurn)
                && com.google.common.base.Objects.equal(avgStmtAdded, other.avgStmtAdded)
                && com.google.common.base.Objects.equal(avgStmtDeleted, other.avgStmtDeleted)
                && com.google.common.base.Objects.equal(avgStmtUpdated, other.avgStmtUpdated)
                && com.google.common.base.Objects.equal(churn, other.churn)
                && com.google.common.base.Objects.equal(cond, other.cond)
                && com.google.common.base.Objects.equal(decl, other.decl)
                && com.google.common.base.Objects.equal(elseAdded, other.elseAdded)
                && com.google.common.base.Objects.equal(elseDeleted, other.elseDeleted)
                && com.google.common.base.Objects.equal(maxChurn, other.maxChurn)
                && com.google.common.base.Objects.equal(maxStmtAdded, other.maxStmtAdded)
                && com.google.common.base.Objects.equal(maxStmtDeleted, other.maxStmtDeleted)
                && com.google.common.base.Objects.equal(maxStmtUpdated, other.maxStmtUpdated)
                && com.google.common.base.Objects.equal(methodHistories, other.methodHistories)
                && com.google.common.base.Objects.equal(methodName, other.methodName)
                && com.google.common.base.Objects.equal(stmtAdded, other.stmtAdded)
                && com.google.common.base.Objects.equal(stmtDeleted, other.stmtDeleted)
                && com.google.common.base.Objects.equal(stmtParentChanged, other.stmtParentChanged)
                && com.google.common.base.Objects.equal(stmtUpdated, other.stmtUpdated);
    }

    public void setValue(int metricName, Double value) {
        switch (metricName) {
        case ALL_METHOD_HISTORIES:
            allMethodHistories = value;
            break;
        case METHOD_HISTORIES:
            methodHistories = value;
            break;
        case AUTHORS:
            authors = value;
            break;
        case STMT_ADDED:
            stmtAdded = value;
            break;
        case MAX_STMT_ADDED:
            maxStmtAdded = value;
            break;
        case AVG_STMT_ADDED:
            avgStmtAdded = value;
            break;
        case STMT_UPDATED:
            stmtUpdated = value;
            break;
        case MAX_STMT_UPDATED:
            maxStmtUpdated = value;
            break;
        case AVG_STMT_UPDATED:
            avgStmtUpdated = value;
            break;
        case STMT_DELETED:
            stmtDeleted = value;
            break;
        case MAX_STMT_DELETED:
            maxStmtDeleted = value;
            break;
        case AVG_STMT_DELETED:
            avgStmtDeleted = value;
            break;
        case STATEMENT_PARENT_CHANGE:
            stmtParentChanged = value;
            break;
        case CHURN:
            churn = value;
            break;
        case MAX_CHURN:
            maxChurn = value;
            break;
        case AVG_CHURN:
            avgChurn = value;
            break;
        case DECL:
            decl = value;
            break;
        case COND:
            cond = value;
            break;
        case ELSE_ADDED:
            elseAdded = value;
            break;
        case ELSE_DELETED:
            elseDeleted = value;
            break;
        }
    }

    public String getMethodName() {
        return methodName;
    }

    public Double getAllMethodHistories() {
        return allMethodHistories;
    }

    public Double getMethodHistories() {
        return methodHistories;
    }

    public Double getAuthors() {
        return authors;
    }

    public Double getStmtAdded() {
        return stmtAdded;
    }

    public Double getMaxStmtAdded() {
        return maxStmtAdded;
    }

    public Double getAvgStmtAdded() {
        return avgStmtAdded;
    }

    public Double getStmtUpdated() {
        return stmtUpdated;
    }

    public Double getMaxStmtUpdated() {
        return maxStmtUpdated;
    }

    public Double getAvgStmtUpdated() {
        return avgStmtUpdated;
    }

    public Double getStmtDeleted() {
        return stmtDeleted;
    }

    public Double getMaxStmtDeleted() {
        return maxStmtDeleted;
    }

    public Double getAvgStmtDeleted() {
        return avgStmtDeleted;
    }

    public Double getStmtParentChanged() {
        return stmtParentChanged;
    }

    public Double getChurn() {
        return churn;
    }

    public Double getMaxChurn() {
        return maxChurn;
    }

    public Double getAvgChurn() {
        return avgChurn;
    }

    public Double getDecl() {
        return decl;
    }

    public Double getCond() {
        return cond;
    }

    public Double getElseAdded() {
        return elseAdded;
    }

    public Double getElseDeleted() {
        return elseDeleted;
    }

    private boolean hasMetric(Double value) {
        if (value != null) {
            return value.doubleValue() != 0.00d;
        }
        return false;
    }
}
