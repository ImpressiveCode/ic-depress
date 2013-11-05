/*
 * ------------------------------------------------------------------------
 *
 *  Copyright (C) 2003 - 2013
 *  University of Konstanz, Germany and
 *  KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * -------------------------------------------------------------------
 *
 * History
 *   Jun 25, 2009 (ohl): created
 */
package org.impressivecode.depress.its.clearquest;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowIterator;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;

/**
 *
 * @author Peter Ohl, KNIME.com, Zurich, Switzerland
 */
public class ClearQuestTableSettings {

    private static final NodeLogger LOGGER = NodeLogger
            .getLogger(ClearQuestTableSettings.class);

    private final ClearQuestUserSettings m_userSettings;

    private final DataTableSpec m_spec;

    private final Set<Integer> m_skippedCols;

    private String m_firstSheetName;

    /**
     *
     * @param userSettings The user settings
     * @param wb The workbook
     * @throws InvalidSettingsException If an exception occurs
     * @throws FileNotFoundException If an exception occurs
     * @throws IOException If an exception occurs
     * @throws InvalidFormatException If an exception occurs
     */
    public ClearQuestTableSettings(final ClearQuestUserSettings userSettings, final Workbook wb)
            throws InvalidSettingsException, FileNotFoundException,
            IOException, InvalidFormatException {
        this(userSettings, null, wb);
    }

    /**
     *
     * @param userSettings The user settings
     * @param tableSpec Specs of the table
     * @param wb The workbook
     * @throws InvalidSettingsException If an exception occurs
     * @throws FileNotFoundException If an exception occurs
     * @throws IOException If an exception occurs
     * @throws InvalidFormatException If an exception occurs
     */
    public ClearQuestTableSettings(final ClearQuestUserSettings userSettings,
            final DataTableSpec tableSpec, final Workbook wb) throws InvalidSettingsException,
            FileNotFoundException, IOException, InvalidFormatException {
        String errMsg = userSettings.getStatus(false);
        if (errMsg != null) {
            throw new IllegalArgumentException(errMsg);
        }

        m_userSettings = ClearQuestUserSettings.clone(userSettings);

        // set the range of the rows and columns to read
        if (m_userSettings.getReadAllData()) {
            // clear any possibly set first/last row/col
            m_userSettings.setFirstColumn(-1);
            m_userSettings.setLastColumn(-1);
            m_userSettings.setFirstRow(-1);
            m_userSettings.setLastRow(-1);
            m_userSettings.setReadAllData(false);
        }
        if (m_userSettings.getLastColumn() < 0
                || m_userSettings.getLastRow() < 0) {
            // if bounds are not user set - figure them out
            setMinMaxColumnAndRow(m_userSettings, wb);
        }

        // this analyzes the types of the columns
        HashSet<Integer> skippedcols = new HashSet<Integer>();
        if (tableSpec == null) {
            m_spec = createSpec(m_userSettings, skippedcols, wb);
        } else {
            m_spec = tableSpec;
        }
        m_skippedCols = new HashSet<Integer>();
        m_skippedCols.addAll(skippedcols);
    }

    /**
     * Uses the specified table spec and skipped cols to get the column names
     * and column types from. They must fit the file set in the user settings!
     * This constructor does not analyze the XL file.
     *
     */
    private ClearQuestTableSettings(final ClearQuestUserSettings userSettings,
            final DataTableSpec tableSpec, final Set<Integer> skippedCols)
            throws InvalidSettingsException, FileNotFoundException, IOException {
        String errMsg = userSettings.getStatus(false);
        if (errMsg != null) {
            throw new IllegalArgumentException(errMsg);
        }
        if (userSettings.getReadAllData()) {
            throw new IllegalArgumentException("Constructor not suitable if"
                    + " area of interest is not set.");
        }

        m_userSettings = ClearQuestUserSettings.clone(userSettings);

        // this analyzes the types of the columns
        m_skippedCols = new HashSet<Integer>();
        if (skippedCols != null) {
            m_skippedCols.addAll(skippedCols);
        }
        m_spec = tableSpec;

    }

    /**
     *
     * @return
     */
    public DataTableSpec getDataTableSpec() {
        return m_spec;
    }

    /**
     * Runs through the selected sheet and sets the minimum of all first cell
     * and the maximum of all last cell numbers of all rows. That is, it returns
     * the columns containing data in the data sheet. (If the minimum is already
     * set to a valid index, it doesn't change it - in order to support for open
     * ranges.) It ignores all "skip" flags, i.e. count in empty and hidden
     * columns.
     *
     * @param settings specifies the sheet to read. firstColumn and lastColumn
     *            in this object will be set by this method
     * @throws InvalidSettingsException
     * @throws IOException
     * @throws FileNotFoundException
     * @throws InvalidFormatException
     */
    private static void setMinMaxColumnAndRow(final ClearQuestUserSettings settings,
                                              final Workbook wb)
            throws InvalidSettingsException, IOException,
            FileNotFoundException, InvalidFormatException {
        if (settings == null) {
            throw new NullPointerException("Settings can't be null");
        }
        if (settings.getFileLocation() == null) {
            throw new NullPointerException("File location must be set.");
        }

        String sheetName = settings.getSheetName();
        if (sheetName == null || sheetName.isEmpty()) {
            sheetName = ClearQuestTable.getFirstSheetNameWithData(wb);
        }

        Sheet sheet = wb.getSheet(sheetName);
        if (sheet == null) {
            throw new InvalidSettingsException("Sheet '"
                    + sheetName + "' not available");
        }

        short firstColIdx = -1; // stores the cell index
        short lastColNum = -1; // stores the cell index + 1

        int maxRowIdx = ClearQuestTable.getLastRowIdx(sheet);
        int minRowIdx = sheet.getFirstRowNum();

        for (int row = minRowIdx; row <= maxRowIdx; row++) {
            Row r = sheet.getRow(row);
            if (r == null) {
                continue;
            }
            if (r.getFirstCellNum() < 0) {
                // no cell in this row
                continue;
            }
            if (firstColIdx < 0 || r.getFirstCellNum() < firstColIdx) {
                firstColIdx = r.getFirstCellNum();
            }
            if (lastColNum < r.getLastCellNum()) {
                lastColNum = r.getLastCellNum();
            }
        }

        lastColNum--; // now it's the index

        if (firstColIdx < 0 || lastColNum < 0 || lastColNum < firstColIdx) {
            if (settings.getFirstColumn() < 0) {
                // only change first column if not set
                settings.setFirstColumn(0);
                settings.setLastColumn(0);
            } else {
                settings.setLastColumn(settings.getFirstColumn());
            }
        } else {
            if (settings.getFirstColumn() < 0) {
                // only change first column if not set
                settings.setFirstColumn(firstColIdx);
                settings.setLastColumn(lastColNum);
            } else {
                // preserve user set range
                if (settings.getLastColumn() < 0) {
                    int last = lastColNum;
                    if (last < settings.getFirstColumn()) {
                        // avoid invalid settings
                        settings.setLastColumn(settings.getFirstColumn());
                    } else {
                        settings.setLastColumn(last);
                    }
                }
            }
        }

        if (settings.getFirstRow() < 0) {
            // change only if not user set
            settings.setFirstRow(minRowIdx);
            settings.setLastRow(maxRowIdx);
        } else {
            if (settings.getLastRow() < 0) {
                // change only if not user set
                int last = maxRowIdx;
                if (last < settings.getFirstRow()) {
                    settings.setLastRow(settings.getFirstRow());
                } else {
                    settings.setLastRow(last);
                }
            }
        }
    }

    /**
     * Creates and returns a new table spec from the current settings.
     *
     * @return a new table spec from the current settings
     * @throws InvalidSettingsException if settings are invalid.
     * @throws InvalidFormatException
     */
    private static DataTableSpec createSpec(final ClearQuestUserSettings settings,
            final Set<Integer> skippedCols, final Workbook wb) throws InvalidSettingsException,
            IOException, InvalidFormatException {

        ArrayList<DataType> columnTypes =
                analyzeColumnTypes(settings, skippedCols, wb);

        int numOfCols = columnTypes.size();
        String[] colHdrs = createColHeaders(settings, numOfCols, skippedCols, wb);
        assert colHdrs.length == numOfCols;

        DataColumnSpec[] colSpecs = new DataColumnSpec[numOfCols];

        for (int col = 0; col < numOfCols; col++) {
            assert (col < columnTypes.size() && columnTypes.get(col) != null);
            colSpecs[col] =
                    new DataColumnSpecCreator(colHdrs[col],
                            columnTypes.get(col)).createSpec();
        }

        // create a name
        String sheetName = settings.getSheetName();
        if (sheetName == null || sheetName.isEmpty()) {
            sheetName = ClearQuestTable.getFirstSheetNameWithData(wb);
        }
        String tableName =
                settings.getSimpleFilename() + " [" + sheetName + "]";
        return new DataTableSpec(tableName, colSpecs);
    }

    /**
     * Either reads the column names from the sheet, of generates new ones.
     */
    private static String[] createColHeaders(final ClearQuestUserSettings settings,
            final int numOfCols, final Set<Integer> skippedCols, final Workbook wb)
            throws InvalidSettingsException {

        String[] result = null;
        if (settings.getHasColHeaders() && !settings.getKeepXLColNames()) {
            result = readColumnHeaders(settings, numOfCols, skippedCols, wb);
        }
        if (result == null) {
            result = new String[numOfCols];
        }
        fillEmptyColHeaders(settings, skippedCols, result);
        return result;
    }

    private static void fillEmptyColHeaders(final ClearQuestUserSettings settings,
            final Set<Integer> skippedCols, final String[] colHdrs) {
        // XL Sheets don't have more than 256 columns
        HashSet<String> names = new HashSet<String>();
        for (int i = 0; i < colHdrs.length; i++) {
            if (colHdrs[i] != null) {
                names.add(colHdrs[i]);
            }
        }

        int xlOffset = 0;

        for (int i = 0; i < colHdrs.length; i++) {
            while (skippedCols.contains(xlOffset)) {
                xlOffset++;
            }
            if (colHdrs[i] == null) {
                String colName = "Col" + i;
                if (settings.getKeepXLColNames()) {
                    colName =
                    		ClearQuestTable.getColLabel(settings.getFirstColumn()
                                    + xlOffset);
                }
                colHdrs[i] = getUniqueName(colName, names);
                names.add(colHdrs[i]);
            }
            xlOffset++;
        }
    }

    /**
     * Looks at the specified XLS file and tries to determine the type of all
     * columns contained. Also returns the number of columns and rows in all
     * sheets.
     *
     * @param settings the pre-set settings.
     * @return the result settings
     * @throws IOException if the specified file couldn't be read.
     * @throws InvalidSettingsException if settings are invalid
     * @throws InvalidFormatException
     */
    private static ArrayList<DataType> analyzeColumnTypes(
            final ClearQuestUserSettings settings, final Set<Integer> skippedCols,
            final Workbook wb)
            throws IOException, InvalidSettingsException,
            InvalidFormatException {

        if (settings == null) {
            throw new NullPointerException("Settings can't be null");
        }
        if (settings.getFileLocation() == null) {
            throw new NullPointerException("File location must be set.");
        }

            return setColumnTypes(wb, settings, skippedCols);

    }

    /**
     * Traverses the specified sheet in the file and detects the type for all
     * columns in the sheets.
     *
     * @param wb The workbook
     * @param settings The user settings
     * @param skippedCols The skipped columns
     * @throws IOException If an exception occurs
     */
    private static ArrayList<DataType> setColumnTypes(final Workbook wb,
            final ClearQuestUserSettings settings, final Set<Integer> skippedCols)
            throws IOException {

        int colNum = settings.getLastColumn() - settings.getFirstColumn() + 1;

        ArrayList<DataType> colTypes =
                new ArrayList<DataType>(Arrays.asList(new DataType[colNum]));
        skippedCols.clear();

        String sheetName = settings.getSheetName();
        if (sheetName == null || sheetName.isEmpty()) {
            sheetName = ClearQuestTable.getFirstSheetNameWithData(wb);
        }
        Sheet sh = wb.getSheet(sheetName);
        if (sh != null) {
            FormulaEvaluator evaluator =
                    wb.getCreationHelper().createFormulaEvaluator();
            int maxRowIdx = ClearQuestTable.getLastRowIdx(sh);
            int minRowIdx = sh.getFirstRowNum();
            if (settings.getLastRow() < maxRowIdx) {
                maxRowIdx = settings.getLastRow();
            }
            if (settings.getFirstRow() > minRowIdx) {
                minRowIdx = settings.getFirstRow();
            }
            for (int row = minRowIdx; row <= maxRowIdx; row++) {
                Row r = sh.getRow(row);
                if (r == null) {
                    continue;
                }
                // skip the row that contains the column names
                if (settings.getHasColHeaders()
                        && row == settings.getColHdrRow()) {
                    continue;
                }
                int knimeColIdx = -1;
                for (int xlCol = settings.getFirstColumn(); xlCol <= settings
                        .getLastColumn(); xlCol++) {
                    knimeColIdx++;
                    if (settings.getHasRowHeaders()
                            && xlCol == settings.getRowHdrCol()) {
                        // skip the column with the row IDs
                        skippedCols.add(xlCol);
                        continue;
                    }
                    if (sh.isColumnHidden(xlCol)
                            && settings.getSkipHiddenColumns()) {
                        skippedCols.add(xlCol);
                        continue;
                    }
                    Cell cell = r.getCell(xlCol);
                    if (cell != null) {
                        // determine the type
                        switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_BLANK:
                            // missing cell - doesn't change any type
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            // KNIME has no boolean - use String
                            colTypes.set(knimeColIdx, StringCell.TYPE);
                            break;
                        case Cell.CELL_TYPE_ERROR:
                            if (settings.getUseErrorPattern()) {
                                // error patterns are of type string
                                colTypes.set(knimeColIdx, StringCell.TYPE);
                            } else {
                                // a missing cell will be included
                                if (colTypes.get(knimeColIdx) == null) {
                                    // don't leave it null, or its skipped
                                    colTypes.set(knimeColIdx,
                                            DataType.getType(DataCell.class));
                                }
                            }
                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            CellValue cellValue = null;
                            try {
                                cellValue = evaluator.evaluate(cell);
                            } catch (Exception e) {
                                if (settings.getUseErrorPattern()) {
                                    // assuming it also fails when iterating
                                    colTypes.set(knimeColIdx, StringCell.TYPE);
                                } else {
                                    // a missing cell will be included
                                    if (colTypes.get(knimeColIdx) == null) {
                                        // don't leave it null, otherwise its
                                        // skipped
                                        colTypes.set(knimeColIdx, DataType
                                                .getType(DataCell.class));
                                    }
                                }
                                break;
                            }
                            switch (cellValue.getCellType()) {
                            case Cell.CELL_TYPE_BOOLEAN:
                                colTypes.set(knimeColIdx, StringCell.TYPE);
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                // numeric could be double, int or date
                                if (colTypes.get(knimeColIdx) == StringCell.TYPE) {
                                    // string takes all
                                    break;
                                }
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    // we use StringCells for date format
                                    // (using a DataAndTime cell leads to UTC
                                    // time. With string, we get the entered
                                    // time (as string) and it can be translated
                                    // in date/time with an additional node)
                                    colTypes.set(knimeColIdx, StringCell.TYPE);
                                    break;
                                }
                                Double num = cellValue.getNumberValue();
                                if (num.isInfinite() || num.isNaN()) {
                                    // only Double supports NaN
                                    colTypes.set(knimeColIdx, DoubleCell.TYPE);
                                    break;
                                }
                                if (new Double(num.intValue()).equals(num)) {
                                    // could be represented as int
                                    DataType currType =
                                            colTypes.get(knimeColIdx);
                                    if (currType == null
                                            || currType == DataType
                                                    .getType(DataCell.class)) {
                                        colTypes.set(knimeColIdx, IntCell.TYPE);
                                    }
                                } else {
                                    colTypes.set(knimeColIdx, DoubleCell.TYPE);
                                }
                                break;
                            case Cell.CELL_TYPE_STRING:
                                colTypes.set(knimeColIdx, StringCell.TYPE);
                                break;
                            case Cell.CELL_TYPE_BLANK:
                                // gets a missing cell - doesn't change type
                                break;
                            case Cell.CELL_TYPE_ERROR:
                                if (settings.getUseErrorPattern()) {
                                    // error patterns are of type string
                                    colTypes.set(knimeColIdx, StringCell.TYPE);
                                } else {
                                    // a missing cell will be included
                                    if (colTypes.get(knimeColIdx) == null) {
                                        // don't leave it null, otherwise its
                                        // skipped
                                        colTypes.set(knimeColIdx, DataType
                                                .getType(DataCell.class));
                                    }
                                }
                                break;
                            case Cell.CELL_TYPE_FORMULA:
                                // will never happen after evaluation
                                break;
                            }
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            // numeric could be double, int or date
                            if (colTypes.get(knimeColIdx) == StringCell.TYPE) {
                                // string takes all
                                break;
                            }
                            if (DateUtil.isCellDateFormatted(cell)) {
                                // we use StringCells for date format
                                // (using a DataAndTime cell leads to UTC time.
                                // With string, we get the entered time (as
                                // string) and it can be translated in date/time
                                // with an additional node)
                                colTypes.set(knimeColIdx, StringCell.TYPE);
                                break;
                            }
                            Double num = cell.getNumericCellValue();
                            if (num.isInfinite() || num.isNaN()) {
                                // only Double supports NaN
                                colTypes.set(knimeColIdx, DoubleCell.TYPE);
                                break;
                            }
                            if (new Double(num.intValue()).equals(num)) {
                                // could be represented as int
                                if (colTypes.get(knimeColIdx) == null
                                        || colTypes.get(knimeColIdx) == DataType
                                                .getType(DataCell.class)) {
                                    colTypes.set(knimeColIdx, IntCell.TYPE);
                                }
                            } else {
                                colTypes.set(knimeColIdx, DoubleCell.TYPE);
                            }
                            break;
                        case Cell.CELL_TYPE_STRING:
                            colTypes.set(knimeColIdx, StringCell.TYPE);
                            break;
                        default:
                            LOGGER.error("Unexpected cell type ("
                                    + cell.getCellType() + ")");
                            break;
                        }

                    }
                }

            }
        }

        // null types represent empty columns (except skipped hidden cols)

        for (int c = 0; c < colTypes.size(); c++) {
            int xlCol = c + settings.getFirstColumn();
            DataType type = colTypes.get(c);
            if (!skippedCols.contains(xlCol) && type == null) {
                if (settings.getSkipEmptyColumns()) {
                    skippedCols.add(xlCol);
                } else {
                    colTypes.set(c, DataType.getType(DataCell.class));
                }
            }
        }
        // remove skipped cols from type list
        if (skippedCols.size() > 0) {
            ArrayList<DataType> result = new ArrayList<DataType>();
            for (int c = 0; c < colTypes.size(); c++) {
                if (!skippedCols.contains(c + settings.getFirstColumn())) {
                    // (skippedCols contains xl-indices)
                    result.add(colTypes.get(c));
                }
            }
            return result;
        } else {
            return colTypes;
        }
    }

    private static String getPoiTypeName(final int poiType) {
        switch (poiType) {
        case Cell.CELL_TYPE_BLANK:
            return "blank";
        case Cell.CELL_TYPE_STRING:
            return "string";
        case Cell.CELL_TYPE_BOOLEAN:
            return "boolean";
        case Cell.CELL_TYPE_ERROR:
            return "error";
        case Cell.CELL_TYPE_NUMERIC:
            return "numeric";
        case Cell.CELL_TYPE_FORMULA:
            return "formula";
        }
        return "unknown[" + poiType + "]";
    }

    /**
     * Reads the contents of the specified column header row. Names not
     * specified in the sheet are null in the result. Uniquifies the names from
     * the sheet. The length of the returned array is determined by the first
     * and last column to read. The result is null, if the hasColHdr flag is not
     * set, or the specified header row is not in the sheet.
     *
     * @param settings valid settings
     * @param numOfCols number of columns to read headers for
     * @param skippedCols columns in the sheet being skipped (due to emptiness
     *            or row headers)
     * @return Returns null, if sheet contains no column headers, or the
     *         specified header row is not in the sheet, otherwise an array with
     *         unique names, or null strings, if the sheet didn't contain a name
     *         for that column.
     * @throws InvalidSettingsException if settings are invalid
     */
    private static String[] readColumnHeaders(final ClearQuestUserSettings settings,
            final int numOfCols, final Set<Integer> skippedCols, final Workbook wb)
            throws InvalidSettingsException {

        String[] result = new String[numOfCols];

        if (!settings.getHasColHeaders()) {
            return result;
        }

        try {

        	ClearQuestUserSettings colHdrSettings = ClearQuestUserSettings.clone(settings);

            // this avoids endless recursion
            colHdrSettings.setKeepXLNames(true);

            // analyze the file and set the table spec
            ClearQuestTableSettings tableSettings =
                    new ClearQuestTableSettings(colHdrSettings, wb);

            // now use the settings to read the header row
            colHdrSettings.setHasColHeaders(false);
            // the row ID refers to the row in the XL file
            colHdrSettings.setSkipEmptyRows(false);
            // the table settings contain the interesting range
            colHdrSettings.setFirstColumn(tableSettings.getFirstColumn());
            colHdrSettings.setLastColumn(tableSettings.getLastColumn());
            // we only need the header row
            colHdrSettings.setFirstRow(colHdrSettings.getColHdrRow());
            colHdrSettings.setLastRow(colHdrSettings.getColHdrRow());

            // create a table spec for reading col headers (all string cols)
            DataColumnSpec[] colSpecs =
                    new DataColumnSpec[tableSettings.m_spec.getNumColumns()];
            for (int i = 0; i < colSpecs.length; i++) {
                colSpecs[i] =
                        new DataColumnSpecCreator("Col" + i, StringCell.TYPE)
                                .createSpec();
            }
            // create the table settings to read the header row

            ClearQuestTableSettings tableS =
                    new ClearQuestTableSettings(colHdrSettings, new DataTableSpec(
                            colSpecs), tableSettings.m_skippedCols);
            ClearQuestTable table = new ClearQuestTable(tableS, wb);
            RowIterator iterator = table.iterator();
            DataRow row = null;
            if (iterator.hasNext()) {
                row = iterator.next();
            }
            if (row == null) {
                // table had too few rows
                LOGGER.warn("Specified column header row not contained "
                        + "in sheet");
                return result;
            }
            if (numOfCols != row.getNumCells()) {
                LOGGER.debug("The column header row just read has "
                        + row.getNumCells() + " cells, but we expected "
                        + numOfCols);
            }
            HashSet<String> names = new HashSet<String>();
            for (int i = 0; i < row.getNumCells(); i++) {
                if (i >= result.length) {
                    break;
                }
                if (!row.getCell(i).isMissing()) {
                    result[i] = getUniqueName(row.getCell(i).toString(), names);
                    names.add(result[i]);
                }
            }

        } catch (Exception e) {
            NodeLogger.getLogger(ClearQuestUserSettings.class).debug(
                    "Caught exception while accessing file "
                            + settings.getFileLocation()
                            + ". Creating synthetic column names", e);
        }

        return result;
    }

    private static String getUniqueName(final String name,
            final Set<String> names) {
        int cnt = 2;
        String unique = name;
        while (names.contains(unique)) {
            unique = name + "_" + cnt++;
        }
        return unique;
    }

    /*
     * ---------------- setter and getter ------------------------------------
     */

    /**
     * @return the fileLocation
     */
    public String getFileLocation() {
        return m_userSettings.getFileLocation();
    }

    public BufferedInputStream getBufferedInputStream() throws IOException {
        return m_userSettings.getBufferedInputStream();
    }

    public static BufferedInputStream getBufferedInputStream(
            final String location) throws IOException {
        return ClearQuestUserSettings.getBufferedInputStream(location);
    }

    /**
     * Loads a workbook from the file system.
     *
     * @param path Path to the workbook
     * @return The workbook or null if it could not be loaded
     */
    public static Workbook getWorkbook(final String path) {
        Workbook workbook = null;
        InputStream in = null;
        try {
            in = ClearQuestTableSettings.getBufferedInputStream(path);
            // This should be the only place in the code where a workbook gets loaded
            workbook = WorkbookFactory.create(in);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e2) {
                    // ignore
                }
            }
        }
        return workbook;
    }

    /**
     * @param wb The workbook
     * @return the index of the sheet to read
     */
    public String getSheetName(final Workbook wb) {
        String sheetName = m_userSettings.getSheetName();
        if (sheetName == null || sheetName.isEmpty()) {
            if (m_firstSheetName == null) {
                try {
                    m_firstSheetName =
                    		ClearQuestTable.getFirstSheetNameWithData(wb);
                } catch (Exception e) {
                    m_firstSheetName = "<error>";
                }
            }
            sheetName = m_firstSheetName;
        }
        return sheetName;
    }

    /**
     * @return the firstRow
     */
    public int getFirstRow() {
        return m_userSettings.getFirstRow();
    }

    /**
     * @return the lastRow
     */
    public int getLastRow() {
        return m_userSettings.getLastRow();
    }

    /**
     * @return the firstColumn
     */
    public int getFirstColumn() {
        return m_userSettings.getFirstColumn();
    }

    /**
     * @return the lastColumn
     */
    public int getLastColumn() {
        return m_userSettings.getLastColumn();
    }

    /**
     * Set with empty or hidden columns. The index in the set is the offset from
     * the firstColumn.
     *
     * @return the skippedCols
     */
    public Set<Integer> getSkippedCols() {
        return Collections.unmodifiableSet(m_skippedCols);
    }

    /**
     * @return the skipEmptyRows
     */
    public boolean getSkipEmptyRows() {
        return m_userSettings.getSkipEmptyRows();
    }

    /**
     * @return the colHdrRow
     */
    public int getColHdrRow() {
        return m_userSettings.getColHdrRow();
    }

    /**
     * @return the hasColHeaders
     */
    public boolean getHasColHeaders() {
        return m_userSettings.getHasColHeaders();
    }

    /**
     * @return the hasRowHeaders
     */
    public boolean getHasRowHeaders() {
        return m_userSettings.getHasRowHeaders();
    }

    /**
     * @return the rowHdrCol
     */
    public int getRowHdrCol() {
        return m_userSettings.getRowHdrCol();
    }

    /**
     * @return the missValuePattern
     */
    public String getMissValuePattern() {
        return m_userSettings.getMissValuePattern();
    }

    /**
     * @return
     * @see org.knime.ext.poi.node.read2.XLSUserSettings#getKeepXLColNames()
     */
    public boolean getKeepXLColNames() {
        return m_userSettings.getKeepXLColNames();
    }

    /**
     * @return
     * @see org.knime.ext.poi.node.read2.XLSUserSettings#getUniquifyRowIDs()
     */
    public boolean getUniquifyRowIDs() {
        return m_userSettings.getUniquifyRowIDs();
    }

    /**
     * @see org.knime.ext.poi.node.read2.XLSUserSettings#getUseErrorPattern()
     * @return
     */
    public boolean getUseErrorPattern() {
        return m_userSettings.getUseErrorPattern();
    }

    /**
     * @see org.knime.ext.poi.node.read2.XLSUserSettings#setUseErrorPattern(boolean)
     * @param useit
     */
    public void setUseErrorPattern(final boolean useit) {
        m_userSettings.setUseErrorPattern(useit);
    }

    /**
     *
     * @return
     * @see org.knime.ext.poi.node.read2.XLSUserSettings#getErrorPattern()
     * @see org.knime.ext.poi.node.read2.XLSUserSettings#getErrorPattern()
     */
    public String getErrorPattern() {
        return m_userSettings.getErrorPattern();
    }

    /**
     *
     * @param pattern
     * @see org.knime.ext.poi.node.read2.XLSUserSettings#setErrorPattern(String)
     */
    public void setErrorPattern(final String pattern) {
        m_userSettings.setErrorPattern(pattern);
    }
}
