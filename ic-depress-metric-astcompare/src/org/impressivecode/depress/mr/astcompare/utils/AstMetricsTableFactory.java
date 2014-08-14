package org.impressivecode.depress.mr.astcompare.utils;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DoubleCell;

public class AstMetricsTableFactory {

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

    private AstMetricsTableFactory() {

    }

    public static DataTableSpec[] createTableSpec() {
        return new DataTableSpec[] { createDataColumnSpec() };
    }

    public static DataTableSpec createDataColumnSpec() {
        DataColumnSpec[] allColSpecs = { new DataColumnSpecCreator(ALL_METHOD_HISTORIES, DoubleCell.TYPE).createSpec(),
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
                new DataColumnSpecCreator(ELSE_DELETED, DoubleCell.TYPE).createSpec() };
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        return outputSpec;
    }

}
