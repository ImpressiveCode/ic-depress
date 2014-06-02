package org.impressivecode.depress.metric.astcompare.utils;

import java.util.Map;
import java.util.Map.Entry;

import org.impressivecode.depress.metric.astcompare.db.DbHandler;
import org.impressivecode.depress.metric.astcompare.db.MetricVO;
import org.eclipse.core.resources.IProject;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import static org.impressivecode.depress.metric.astcompare.utils.Utils.*;

/**
 * @author Piotr Mitka
 */
public class DataTableHandler {

    private int index = 0;
    private final ExecutionContext exec;
    private DbHandler db;
    private BufferedDataContainer container;

    public DataTableHandler(ExecutionContext exec, DbHandler db) {
        super();
        this.exec = exec;
        this.db = db;
    }

    public BufferedDataTable loadDateFromDbToTable(IProject selectedProject, long revisionDateMin,
            long revisionDateMax, int weeks) throws CanceledExecutionException {

        long timeFrameStart = revisionDateMin;
        long timeFrameEnd = addWeeksToDate(timeFrameStart, weeks);
        container = exec.createDataContainer(new DataTableSpec(getColumnSpecification()));
        index = 0;
        exec.setProgress(0.85d);

        while (true) {
            exec.checkCanceled();

            if (timeFrameEnd <= revisionDateMax) {
                db.getDataFromDB(timeFrameStart, timeFrameEnd, selectedProject.getName(), revisionDateMin,
                        revisionDateMax);
                putEmptyRowToTableModel(getDateAsString(timeFrameStart) + "   -   " + getDateAsString(timeFrameEnd));
                putDataFromMapToTableModel(db.getMetrics());

                timeFrameStart = timeFrameEnd + 1;
            } else {
                if (timeFrameStart < revisionDateMax) {
                    timeFrameEnd = revisionDateMax;
                    continue;
                } else {
                    break;
                }
            }
            timeFrameEnd = addWeeksToDate(timeFrameStart, weeks);
        }

        container.close();
        BufferedDataTable out = container.getTable();
        exec.setProgress(1.0d);
        return out;
    }

    private void putEmptyRowToTableModel(String title) {
        DataCell[] cells = new DataCell[22];
        cells[0] = new StringCell(title);
        for (int i = 1; i < cells.length; i++) {
            cells[i] = DataType.getMissingCell();
        }
        RowKey key = new RowKey("RowKey_" + index++);
        DataRow row = new DefaultRow(key, cells);
        container.addRowToTable(row);

    }

    private void putDataFromMapToTableModel(Map<String, MetricVO> map) throws CanceledExecutionException {
        for (Entry<String, MetricVO> entry : map.entrySet()) {
            if (!entry.getValue().hasResults()) {
                DataCell[] cells = new DataCell[22];
                RowKey key = new RowKey("RowKey_" + index);
                cells[0] = new StringCell(entry.getValue().getMethodName());
                cells[1] = new DoubleCell(entry.getValue().getAllMethodHistories());
                cells[2] = new DoubleCell(entry.getValue().getMethodHistories());
                cells[3] = new DoubleCell(entry.getValue().getAuthors());
                cells[4] = new DoubleCell(entry.getValue().getStmtAdded());
                cells[5] = new DoubleCell(entry.getValue().getMaxStmtAdded());
                cells[6] = new DoubleCell(entry.getValue().getAvgStmtAdded());
                cells[7] = new DoubleCell(entry.getValue().getStmtUpdated());
                cells[8] = new DoubleCell(entry.getValue().getMaxStmtUpdated());
                cells[9] = new DoubleCell(entry.getValue().getAvgStmtUpdated());
                cells[10] = new DoubleCell(entry.getValue().getStmtDeleted());
                cells[11] = new DoubleCell(entry.getValue().getMaxStmtDeleted());
                cells[12] = new DoubleCell(entry.getValue().getAvgStmtDeleted());
                cells[13] = new DoubleCell(entry.getValue().getStmtParentChanged());
                cells[14] = new DoubleCell(entry.getValue().getChurn());
                cells[15] = new DoubleCell(entry.getValue().getMaxChurn());
                cells[16] = new DoubleCell(entry.getValue().getAvgChurn());
                cells[17] = new DoubleCell(entry.getValue().getDecl());
                cells[18] = new DoubleCell(entry.getValue().getCond());
                cells[19] = new DoubleCell(entry.getValue().getElseAdded());
                cells[20] = new DoubleCell(entry.getValue().getElseDeleted());
                cells[21] = new DoubleCell(entry.getValue().getBugCount());

                DataRow row = new DefaultRow(key, cells);
                container.addRowToTable(row);
                index++;
            }
            exec.checkCanceled();
        }
    }

    private DataColumnSpec[] getColumnSpecification() {
        DataColumnSpec[] allColSpecs = new DataColumnSpec[22];

        allColSpecs[0] = new DataColumnSpecCreator("methodName", StringCell.TYPE).createSpec();
        allColSpecs[1] = new DataColumnSpecCreator("allMethodHistories", DoubleCell.TYPE).createSpec();
        allColSpecs[2] = new DataColumnSpecCreator("methodHistories", DoubleCell.TYPE).createSpec();
        allColSpecs[3] = new DataColumnSpecCreator("authors", DoubleCell.TYPE).createSpec();
        allColSpecs[4] = new DataColumnSpecCreator("stmtAdded", DoubleCell.TYPE).createSpec();
        allColSpecs[5] = new DataColumnSpecCreator("maxStmtAdded", DoubleCell.TYPE).createSpec();
        allColSpecs[6] = new DataColumnSpecCreator("avgStmtAdded", DoubleCell.TYPE).createSpec();
        allColSpecs[7] = new DataColumnSpecCreator("stmtUpdated", DoubleCell.TYPE).createSpec();
        allColSpecs[8] = new DataColumnSpecCreator("maxsSmtUpdated", DoubleCell.TYPE).createSpec();
        allColSpecs[9] = new DataColumnSpecCreator("avgStmtUpdated", DoubleCell.TYPE).createSpec();
        allColSpecs[10] = new DataColumnSpecCreator("stmtDeleted", DoubleCell.TYPE).createSpec();
        allColSpecs[11] = new DataColumnSpecCreator("maxStmtDeleted", DoubleCell.TYPE).createSpec();
        allColSpecs[12] = new DataColumnSpecCreator("avgStmtDeleted", DoubleCell.TYPE).createSpec();
        allColSpecs[13] = new DataColumnSpecCreator("stmtParentChanged", DoubleCell.TYPE).createSpec();
        allColSpecs[14] = new DataColumnSpecCreator("churn", DoubleCell.TYPE).createSpec();
        allColSpecs[15] = new DataColumnSpecCreator("maxChurn", DoubleCell.TYPE).createSpec();
        allColSpecs[16] = new DataColumnSpecCreator("avgChurn", DoubleCell.TYPE).createSpec();
        allColSpecs[17] = new DataColumnSpecCreator("decl", DoubleCell.TYPE).createSpec();
        allColSpecs[18] = new DataColumnSpecCreator("cond", DoubleCell.TYPE).createSpec();
        allColSpecs[19] = new DataColumnSpecCreator("elseAdded", DoubleCell.TYPE).createSpec();
        allColSpecs[20] = new DataColumnSpecCreator("elseDeleted", DoubleCell.TYPE).createSpec();
        allColSpecs[21] = new DataColumnSpecCreator("bugCount", DoubleCell.TYPE).createSpec();
        return allColSpecs;
    }

}
