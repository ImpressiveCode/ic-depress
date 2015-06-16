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

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;

/**
 * @author Mateusz Kutyba, Wroclaw University of Technology
 */
public class AstMetricsTableFactory {

    private static final String METHOD_NAME = "MethodName";
    private static final String ALL_METHOD_HISTORIES = "allMethodHistories";
    private static final String METHOD_HISTORIES = "methodHistories";
    private static final String AUTHORS = "authors";
    private static final String STMT_ADDED = "stmtAdded";
    private static final String MAX_STMT_ADDED = "maxStmtAdded";
    private static final String AVG_STMT_ADDED = "avgStmtAdded";
    private static final String STMT_UPDATED = "stmtUpdated";
    private static final String MAX_STMT_UPDATED = "maxsSmtUpdated";
    private static final String AVG_STMT_UPDATED = "avgStmtUpdated";
    private static final String STMT_DELETED = "stmtDeleted";
    private static final String MAX_STMT_DELETED = "maxStmtDeleted";
    private static final String AVG_STMT_DELETED = "avgStmtDeleted";
    private static final String STMT_PARENT_CHANGED = "stmtParentChanged";
    private static final String CHURN = "churn";
    private static final String MAX_CHURN = "maxChurn";
    private static final String AVG_CHURN = "avgChurn";
    private static final String DECL = "decl";
    private static final String COND = "cond";
    private static final String ELSE_ADDED = "elseAdded";
    private static final String ELSE_DELETED = "elseDeleted";
    private static final String LOOPS_ADDED = "loopsAdded";
    private static final String LOOPS_UPDATED = "loopsUpdated";
    private static final String LOOPS_DELETED = "loopsDeleted";
    private static final String VARIABLES_ADDED = "variablesAdded";
    private static final String VARIABLES_UPDATED = "variablesUpdated";
    private static final String VARIABLES_DELETED = "variablesDeleted";
    private static final String ASSIGMENTS_ADDED = "assigmentsAdded";
    private static final String ASSIGMENTS_UPDATED = "assigmentsUpdated";
    private static final String ASSIGMENTS_DELETED = "assigmentsDeleted";
    private static final String RETURNS_ADDED = "returnsAdded";
    private static final String RETURNS_UPDATED = "returnsUpdated";
    private static final String RETURNS_DELETED = "returnsDeleted";
    private static final String NULLS_ADDED = "nullsAdded";
    private static final String NULLS_UPDATED = "nullsUpdated";
    private static final String NULLS_DELETED = "nullsDeleted";
    private static final String CASES_ADDED = "casesAdded";
    private static final String CASES_UPDATED = "casesUpdated";
    private static final String CASES_DELETED = "casesDeleted";
    private static final String BREAKS_ADDED = "breaksAdded";
    private static final String BREAKS_UPDATED = "breaksUpdated";
    private static final String BREAKS_DELETED = "breaksDeleted";
    private static final String OBJECTS_ADDED = "objectsAdded";
    private static final String OBJECTS_UPDATED = "objectsUpdated";
    private static final String OBJECTS_DELETED = "objectsDeleted";
    private static final String CATCHES_ADDED = "catchesAdded";
    private static final String CATCHES_UPDATED = "catchesUpdated";
    private static final String CATCHES_DELETED = "catchesDeleted";
    private static final String THROWS_ADDED = "throwsAdded";
    private static final String THROWS_UPDATED = "throwsUpdated";
    private static final String THROWS_DELETED = "throwsDeleted";
    private static final String LOC = "loc";

    private AstMetricsTableFactory() {

    }

    public static DataTableSpec[] createTableSpec() {
        return new DataTableSpec[] { createDataColumnSpec() };
    }

    public static DataTableSpec createDataColumnSpec() {
        DataColumnSpec[] allColSpecs = { new DataColumnSpecCreator(METHOD_NAME, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(ALL_METHOD_HISTORIES, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(METHOD_HISTORIES, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(AUTHORS, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(STMT_ADDED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(MAX_STMT_ADDED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(AVG_STMT_ADDED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(STMT_UPDATED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(MAX_STMT_UPDATED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(AVG_STMT_UPDATED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(STMT_DELETED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(MAX_STMT_DELETED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(AVG_STMT_DELETED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(STMT_PARENT_CHANGED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(CHURN, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(MAX_CHURN, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(AVG_CHURN, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(DECL, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(COND, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(ELSE_ADDED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(ELSE_DELETED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(LOOPS_ADDED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(LOOPS_UPDATED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(LOOPS_DELETED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(VARIABLES_ADDED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(VARIABLES_UPDATED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(VARIABLES_DELETED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(ASSIGMENTS_ADDED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(ASSIGMENTS_UPDATED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(ASSIGMENTS_DELETED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(RETURNS_ADDED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(RETURNS_UPDATED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(RETURNS_DELETED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(NULLS_ADDED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(NULLS_UPDATED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(NULLS_DELETED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(CASES_ADDED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(CASES_UPDATED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(CASES_DELETED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(BREAKS_ADDED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(BREAKS_UPDATED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(BREAKS_DELETED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(OBJECTS_ADDED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(OBJECTS_UPDATED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(OBJECTS_DELETED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(CATCHES_ADDED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(CATCHES_UPDATED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(CATCHES_DELETED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(THROWS_ADDED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(THROWS_UPDATED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(THROWS_DELETED, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(LOC, DoubleCell.TYPE).createSpec() };
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        return outputSpec;
    }
}
