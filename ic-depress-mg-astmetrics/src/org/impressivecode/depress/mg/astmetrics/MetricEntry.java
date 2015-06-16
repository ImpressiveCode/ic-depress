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

/**
 * @author Mateusz Kutyba, Wroclaw University of Technology
 */
public class MetricEntry {

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
    private Double loopsAdded;
    private Double loopsUpdated;
    private Double loopsDeleted;
    private Double variablesAdded;
    private Double variablesUpdated;
    private Double variablesDeleted;
    private Double assigmentsAdded;
    private Double assigmentsUpdated;
    private Double assigmentsDeleted;
    private Double returnsAdded;
    private Double returnsUpdated;
    private Double returnsDeleted;
    private Double nullsAdded;
    private Double nullsUpdated;
    private Double nullsDeleted;
    private Double casesAdded;
    private Double casesUpdated;
    private Double casesDeleted;
    private Double breaksAdded;
    private Double breaksUpdated;
    private Double breaksDeleted;
    private Double objectsAdded;
    private Double objectsUpdated;
    private Double objectsDeleted;
    private Double catchesAdded;
    private Double catchesUpdated;
    private Double catchesDeleted;
    private Double throwsAdded;
    private Double throwsUpdated;
    private Double throwsDeleted;
    private Double loc;

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
    public static final int LOOPS_ADDED = 20;
    public static final int LOOPS_UPDATED = 21;
    public static final int LOOPS_DELETED = 22;
    public static final int VARIABLES_ADDED = 23;
    public static final int VARIABLES_UPDATED = 24;
    public static final int VARIABLES_DELETED = 25;
    public static final int ASSIGMENTS_ADDED = 26;
    public static final int ASSIGMENTS_UPDATED = 27;
    public static final int ASSIGMENTS_DELETED = 28;
    public static final int RETURNS_ADDED = 29;
    public static final int RETURNS_UPDATED = 30;
    public static final int RETURNS_DELETED = 31;
    public static final int NULLS_ADDED = 32;
    public static final int NULLS_UPDATED = 33;
    public static final int NULLS_DELETED = 34;
    public static final int CASES_ADDED = 35;
    public static final int CASES_UPDATED = 36;
    public static final int CASES_DELETED = 37;
    public static final int BREAKS_ADDED = 38;
    public static final int BREAKS_UPDATED = 39;
    public static final int BREAKS_DELETED = 40;
    public static final int OBJECTS_ADDED = 41;
    public static final int OBJECTS_UPDATED = 42;
    public static final int OBJECTS_DELETED = 43;
    public static final int CATCHES_ADDED = 44;
    public static final int CATCHES_UPDATED = 45;
    public static final int CATCHES_DELETED = 46;
    public static final int THROWS_ADDED = 47;
    public static final int THROWS_UPDATED = 48;
    public static final int THROWS_DELETED = 49;
    public static final int LOC = 50;

    public MetricEntry(String methodName) {
        this.methodName = methodName;
    }

    public boolean hasResults() {
        return hasMetric(allMethodHistories) || hasMetric(methodHistories) || hasMetric(authors)
                || hasMetric(stmtAdded) || hasMetric(maxStmtAdded) || hasMetric(avgStmtAdded) || hasMetric(stmtUpdated)
                || hasMetric(maxStmtUpdated) || hasMetric(avgStmtUpdated) || hasMetric(stmtDeleted)
                || hasMetric(maxStmtDeleted) || hasMetric(avgStmtDeleted) || hasMetric(stmtParentChanged)
                || hasMetric(churn) || hasMetric(maxChurn) || hasMetric(avgChurn) || hasMetric(decl) || hasMetric(cond)
                || hasMetric(elseAdded) || hasMetric(elseDeleted) || hasMetric(loopsAdded) || hasMetric(loopsUpdated)
                || hasMetric(loopsDeleted) || hasMetric(variablesAdded) || hasMetric(variablesUpdated)
                || hasMetric(variablesDeleted) || hasMetric(assigmentsAdded) || hasMetric(assigmentsUpdated)
                || hasMetric(assigmentsDeleted) || hasMetric(returnsAdded) || hasMetric(returnsUpdated)
                || hasMetric(returnsDeleted) || hasMetric(nullsAdded) || hasMetric(nullsUpdated)
                || hasMetric(nullsDeleted) || hasMetric(casesAdded) || hasMetric(casesUpdated)
                || hasMetric(casesDeleted) || hasMetric(breaksAdded) || hasMetric(breaksUpdated)
                || hasMetric(breaksDeleted) || hasMetric(objectsAdded) || hasMetric(objectsUpdated)
                || hasMetric(objectsDeleted) || hasMetric(catchesAdded) || hasMetric(catchesUpdated)
                || hasMetric(catchesDeleted) || hasMetric(throwsAdded) || hasMetric(throwsUpdated)
                || hasMetric(throwsDeleted) || hasMetric(loc);
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(allMethodHistories, authors, avgChurn, avgStmtAdded,
                avgStmtDeleted, avgStmtUpdated, churn, cond, decl, elseAdded, elseDeleted, maxChurn, maxStmtAdded,
                maxStmtDeleted, maxStmtUpdated, methodHistories, methodName, stmtAdded, stmtDeleted, stmtParentChanged,
                stmtUpdated, loopsAdded, loopsUpdated, loopsDeleted, variablesAdded, variablesUpdated,
                variablesDeleted, assigmentsAdded, assigmentsUpdated, assigmentsDeleted, returnsAdded, returnsUpdated,
                returnsDeleted, nullsAdded, nullsUpdated, nullsDeleted, casesAdded, casesUpdated, casesDeleted,
                breaksAdded, breaksUpdated, breaksDeleted, objectsAdded, objectsUpdated, objectsDeleted, catchesAdded,
                catchesUpdated, catchesDeleted, throwsAdded, throwsUpdated, throwsDeleted, loc);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MetricEntry other = (MetricEntry) obj;

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
                && com.google.common.base.Objects.equal(stmtUpdated, other.stmtUpdated)
                && com.google.common.base.Objects.equal(loopsAdded, other.loopsAdded)
                && com.google.common.base.Objects.equal(loopsUpdated, other.loopsUpdated)
                && com.google.common.base.Objects.equal(loopsDeleted, other.loopsDeleted)
                && com.google.common.base.Objects.equal(variablesAdded, other.variablesAdded)
                && com.google.common.base.Objects.equal(variablesUpdated, other.variablesUpdated)
                && com.google.common.base.Objects.equal(variablesDeleted, other.variablesDeleted)
                && com.google.common.base.Objects.equal(assigmentsAdded, other.assigmentsAdded)
                && com.google.common.base.Objects.equal(assigmentsUpdated, other.assigmentsUpdated)
                && com.google.common.base.Objects.equal(assigmentsDeleted, other.assigmentsDeleted)
                && com.google.common.base.Objects.equal(returnsAdded, other.returnsAdded)
                && com.google.common.base.Objects.equal(returnsUpdated, other.returnsUpdated)
                && com.google.common.base.Objects.equal(returnsDeleted, other.returnsDeleted)
                && com.google.common.base.Objects.equal(nullsAdded, other.nullsAdded)
                && com.google.common.base.Objects.equal(nullsUpdated, other.nullsUpdated)
                && com.google.common.base.Objects.equal(nullsDeleted, other.nullsDeleted)
                && com.google.common.base.Objects.equal(casesAdded, other.casesAdded)
                && com.google.common.base.Objects.equal(casesUpdated, other.casesUpdated)
                && com.google.common.base.Objects.equal(casesDeleted, other.casesDeleted)
                && com.google.common.base.Objects.equal(breaksAdded, other.breaksAdded)
                && com.google.common.base.Objects.equal(breaksUpdated, other.breaksUpdated)
                && com.google.common.base.Objects.equal(breaksDeleted, other.breaksDeleted)
                && com.google.common.base.Objects.equal(objectsAdded, other.objectsAdded)
                && com.google.common.base.Objects.equal(objectsUpdated, other.objectsUpdated)
                && com.google.common.base.Objects.equal(objectsDeleted, other.objectsDeleted)
                && com.google.common.base.Objects.equal(catchesAdded, other.catchesAdded)
                && com.google.common.base.Objects.equal(catchesUpdated, other.catchesUpdated)
                && com.google.common.base.Objects.equal(catchesDeleted, other.catchesDeleted)
                && com.google.common.base.Objects.equal(throwsAdded, other.throwsAdded)
                && com.google.common.base.Objects.equal(throwsUpdated, other.throwsUpdated)
                && com.google.common.base.Objects.equal(throwsDeleted, other.throwsDeleted)
                && com.google.common.base.Objects.equal(loc, other.loc);
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
        case LOOPS_ADDED:
            loopsAdded = value;
            break;
        case LOOPS_UPDATED:
            loopsUpdated = value;
            break;
        case LOOPS_DELETED:
            loopsDeleted = value;
            break;
        case VARIABLES_ADDED:
            variablesAdded = value;
            break;
        case VARIABLES_UPDATED:
            variablesUpdated = value;
            break;
        case VARIABLES_DELETED:
            variablesDeleted = value;
            break;
        case ASSIGMENTS_ADDED:
            assigmentsAdded = value;
            break;
        case ASSIGMENTS_UPDATED:
            assigmentsUpdated = value;
            break;
        case ASSIGMENTS_DELETED:
            assigmentsDeleted = value;
            break;
        case RETURNS_ADDED:
            returnsAdded = value;
            break;
        case RETURNS_UPDATED:
            returnsUpdated = value;
            break;
        case RETURNS_DELETED:
            returnsDeleted = value;
            break;
        case NULLS_ADDED:
            nullsAdded = value;
            break;
        case NULLS_UPDATED:
            nullsUpdated = value;
            break;
        case NULLS_DELETED:
            nullsDeleted = value;
            break;
        case CASES_ADDED:
            casesAdded = value;
            break;
        case CASES_UPDATED:
            casesUpdated = value;
            break;
        case CASES_DELETED:
            casesDeleted = value;
            break;
        case BREAKS_ADDED:
            breaksAdded = value;
            break;
        case BREAKS_UPDATED:
            breaksUpdated = value;
            break;
        case BREAKS_DELETED:
            breaksDeleted = value;
            break;
        case OBJECTS_ADDED:
            objectsAdded = value;
            break;
        case OBJECTS_UPDATED:
            objectsUpdated = value;
            break;
        case OBJECTS_DELETED:
            objectsDeleted = value;
            break;
        case CATCHES_ADDED:
            catchesAdded = value;
            break;
        case CATCHES_UPDATED:
            catchesUpdated = value;
            break;
        case CATCHES_DELETED:
            catchesDeleted = value;
            break;
        case THROWS_ADDED:
            throwsAdded = value;
            break;
        case THROWS_UPDATED:
            throwsUpdated = value;
            break;
        case THROWS_DELETED:
            throwsDeleted = value;
            break;
        case LOC:
            loc = value;
            break;
        }
    }

    public String getMethodName() {
        return methodName;
    }

    public Double getAllMethodHistories() {
        return (allMethodHistories == null ? 0 : allMethodHistories);
    }

    public Double getMethodHistories() {
        return (methodHistories == null ? 0 : methodHistories);
    }

    public Double getAuthors() {
        return (authors == null ? 0 : authors);
    }

    public Double getStmtAdded() {
        return (stmtAdded == null ? 0 : stmtAdded);
    }

    public Double getMaxStmtAdded() {
        return (maxStmtAdded == null ? 0 : maxStmtAdded);
    }

    public Double getAvgStmtAdded() {
        return (avgStmtAdded == null ? 0 : avgStmtAdded);
    }

    public Double getStmtUpdated() {
        return (stmtUpdated == null ? 0 : stmtUpdated);
    }

    public Double getMaxStmtUpdated() {
        return (maxStmtUpdated == null ? 0 : maxStmtUpdated);
    }

    public Double getAvgStmtUpdated() {
        return (avgStmtUpdated == null ? 0 : avgStmtUpdated);
    }

    public Double getStmtDeleted() {
        return (stmtDeleted == null ? 0 : stmtDeleted);
    }

    public Double getMaxStmtDeleted() {
        return (maxStmtDeleted == null ? 0 : maxStmtDeleted);
    }

    public Double getAvgStmtDeleted() {
        return (avgStmtDeleted == null ? 0 : avgStmtDeleted);
    }

    public Double getStmtParentChanged() {
        return (stmtParentChanged == null ? 0 : stmtParentChanged);
    }

    public Double getChurn() {
        return (churn == null ? 0 : churn);
    }

    public Double getMaxChurn() {
        return (maxChurn == null ? 0 : maxChurn);
    }

    public Double getAvgChurn() {
        return (avgChurn == null ? 0 : avgChurn);
    }

    public Double getDecl() {
        return (decl == null ? 0 : decl);
    }

    public Double getCond() {
        return (cond == null ? 0 : cond);
    }

    public Double getElseAdded() {
        return (elseAdded == null ? 0 : elseAdded);
    }

    public Double getElseDeleted() {
        return (elseDeleted == null ? 0 : elseDeleted);
    }

    public Double getLoopsAdded() {
        return (loopsAdded == null ? 0 : loopsAdded);
    }

    public Double getLoopsUpdated() {
        return (loopsUpdated == null ? 0 : loopsUpdated);
    }

    public Double getLoopsDeleted() {
        return (loopsDeleted == null ? 0 : loopsDeleted);
    }

    public Double getVariablesAdded() {
        return (variablesAdded == null ? 0 : variablesAdded);
    }

    public Double getVariablesUpdated() {
        return (variablesUpdated == null ? 0 : variablesUpdated);
    }

    public Double getVariablesDeleted() {
        return (variablesDeleted == null ? 0 : variablesDeleted);
    }

    public Double getAssigmentsAdded() {
        return (assigmentsAdded == null ? 0 : assigmentsAdded);
    }

    public Double getAssigmentsUpdated() {
        return (assigmentsUpdated == null ? 0 : assigmentsUpdated);
    }

    public Double getAssigmentsDeleted() {
        return (assigmentsDeleted == null ? 0 : assigmentsDeleted);
    }

    public Double getReturnsAdded() {
        return (returnsAdded == null ? 0 : returnsAdded);
    }

    public Double getReturnsUpdated() {
        return (returnsUpdated == null ? 0 : returnsUpdated);
    }

    public Double getReturnsDeleted() {
        return (returnsDeleted == null ? 0 : returnsDeleted);
    }

    public Double getNullsAdded() {
        return (nullsAdded == null ? 0 : nullsAdded);
    }

    public Double getNullsUpdated() {
        return (nullsUpdated == null ? 0 : nullsUpdated);
    }

    public Double getNullsDeleted() {
        return (nullsDeleted == null ? 0 : nullsDeleted);
    }

    public Double getCasesAdded() {
        return (casesAdded == null ? 0 : casesAdded);
    }

    public Double getCasesUpdated() {
        return (casesUpdated == null ? 0 : casesUpdated);
    }

    public Double getCasesDeleted() {
        return (casesDeleted == null ? 0 : casesDeleted);
    }

    public Double getBreaksAdded() {
        return (breaksAdded == null ? 0 : breaksAdded);
    }

    public Double getBreaksUpdated() {
        return (breaksUpdated == null ? 0 : breaksUpdated);
    }

    public Double getBreaksDeleted() {
        return (breaksDeleted == null ? 0 : breaksDeleted);
    }

    public Double getObjectsAdded() {
        return (objectsAdded == null ? 0 : objectsAdded);
    }

    public Double getObjectsUpdated() {
        return (objectsUpdated == null ? 0 : objectsUpdated);
    }

    public Double getObjectsDeleted() {
        return (objectsDeleted == null ? 0 : objectsDeleted);
    }

    public Double getCatchesAdded() {
        return (catchesAdded == null ? 0 : catchesAdded);
    }

    public Double getCatchesUpdated() {
        return (catchesUpdated == null ? 0 : catchesUpdated);
    }

    public Double getCatchesDeleted() {
        return (catchesDeleted == null ? 0 : catchesDeleted);
    }

    public Double getThrowsAdded() {
        return (throwsAdded == null ? 0 : throwsAdded);
    }

    public Double getThrowsUpdated() {
        return (throwsUpdated == null ? 0 : throwsUpdated);
    }

    public Double getThrowsDeleted() {
        return (throwsDeleted == null ? 0 : throwsDeleted);
    }

    public Double getLoc() {
        return (loc == null ? 0 : loc);
    }

    private boolean hasMetric(Double value) {
        if (value != null) {
            return value.doubleValue() != 0.00d;
        }
        return false;
    }
}
