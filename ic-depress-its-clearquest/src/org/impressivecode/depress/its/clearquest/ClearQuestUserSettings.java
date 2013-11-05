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
 *   Apr 3, 2009 (ohl): created
 */
package org.impressivecode.depress.its.clearquest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.util.FileUtil;

/**
 *
 * @author Peter Ohl, KNIME.com, Zurich, Switzerland
 */
public class ClearQuestUserSettings {

    private String m_fileLocation;

    private boolean m_readAllData;

    private int m_firstRow;

    private int m_lastRow;

    private int m_firstColumn;

    private int m_lastColumn;

    private String m_sheetName;

    private boolean m_hasColHeaders;

    private int m_colHdrRow;

    private boolean m_hasRowHeaders;

    private int m_rowHdrCol;

    private String m_missValuePattern;

    private boolean m_skipEmptyColumns;

    private boolean m_skipEmptyRows;

    private boolean m_skipHiddenColumns;

    private boolean m_keepXLColNames;

    private boolean m_uniquifyRowIDs;

    private String m_errorPattern;

    private boolean m_useErrorPattern;

    /** Default pattern for formula evaluation error StringCells */
    static final String DEFAULT_ERR_PATTERN = "#XL_EVAL_ERROR#";

    /**
     * Constructs a new settings object. All values are uninitialized.
     */
    public ClearQuestUserSettings() {
        m_fileLocation = null;

        m_sheetName = null;

        m_readAllData = true;
        m_firstRow = -1;
        m_lastRow = -1;
        m_firstColumn = -1;
        m_lastColumn = -1;

        m_hasColHeaders = true;
        m_colHdrRow = 0;
        m_hasRowHeaders = false;
        m_rowHdrCol = 0;

        m_missValuePattern = null;

        m_skipEmptyColumns = true;
        m_skipEmptyRows = true;
        m_skipHiddenColumns = true;

        m_keepXLColNames = false;
        m_uniquifyRowIDs = false;

        m_useErrorPattern = true;
        m_errorPattern = DEFAULT_ERR_PATTERN;
    }

    /**
     * Saves the current values.
     *
     * @param settings object to write values into
     */
    public void save(final NodeSettingsWO settings) {
        settings.addString("XLS_LOCATION", m_fileLocation);

        settings.addString("SHEET_NAME", m_sheetName);

        settings.addBoolean("READ_ALL_DATA", m_readAllData);
        settings.addInt("FIRST_ROW", m_firstRow);
        settings.addInt("LAST_ROW", m_lastRow);
        settings.addInt("FIRST_COL", m_firstColumn);
        settings.addInt("LAST_COL", m_lastColumn);

        settings.addBoolean("HAS_COL_HDRS", m_hasColHeaders);
        settings.addInt("COL_HDR_ROW", m_colHdrRow);
        settings.addBoolean("HAS_ROW_HDRS", m_hasRowHeaders);
        settings.addInt("ROW_HDR_COL", m_rowHdrCol);

        settings.addString("MISS_VAL_PATTERN", m_missValuePattern);
        settings.addBoolean("SKIP_EMPTY_COLS", m_skipEmptyColumns);
        settings.addBoolean("SKIP_EMPTY_ROWS", m_skipEmptyRows);
        settings.addBoolean("SKIP_HIDDEN_COLS", m_skipHiddenColumns);

        settings.addBoolean("KEEP_XL_HDR", m_keepXLColNames);
        settings.addBoolean("UNIQUIFY_ROWID", m_uniquifyRowIDs);

        settings.addBoolean("FORMULA_ERR_USE_PATTERN", m_useErrorPattern);
        settings.addString("FORMULA_ERR_PATTERN", m_errorPattern);
    }

    public String getID() {
        StringBuilder id = new StringBuilder("SettingsID:");
        id.append(getID(m_fileLocation));
        id.append(getID(m_sheetName));
        id.append(getID(m_readAllData));
        id.append(getID(m_firstRow));
        id.append(getID(m_lastRow));
        id.append(getID(m_firstColumn));
        id.append(getID(m_lastColumn));
        id.append(getID(m_hasColHeaders));
        id.append(getID(m_colHdrRow));
        id.append(getID(m_hasRowHeaders));
        id.append(getID(m_rowHdrCol));
        id.append(getID(m_missValuePattern));
        id.append(getID(m_skipEmptyColumns));
        id.append(getID(m_skipEmptyRows));
        id.append(getID(m_skipHiddenColumns));
        id.append(getID(m_keepXLColNames));
        id.append(getID(m_uniquifyRowIDs));
        id.append(getID(m_useErrorPattern));
        id.append(getID(m_errorPattern));
        return id.toString();
    }

    protected String getID(final String value) {
        if (value == null) {
            return "-";
        }
        if (value.isEmpty()) {
            return ".";
        }
        return value;
    }
    protected String getID(final boolean value) {
        return value ? "1" : "0";
    }
    protected String getID(final int value) {
        return "" + value;
    }

    /**
     * Creates a new settings object with values from the settings passed.
     *
     * @param settings the values to store in the new object
     * @return a new settings object
     * @throws InvalidSettingsException if the stored settings are incorrect.
     */
    public static ClearQuestUserSettings load(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	ClearQuestUserSettings result = new ClearQuestUserSettings();

        result.m_fileLocation = settings.getString("XLS_LOCATION");

        result.m_sheetName = settings.getString("SHEET_NAME");

        result.m_readAllData = settings.getBoolean("READ_ALL_DATA");
        result.m_firstColumn = settings.getInt("FIRST_COL");
        result.m_lastColumn = settings.getInt("LAST_COL");
        result.m_firstRow = settings.getInt("FIRST_ROW");
        result.m_lastRow = settings.getInt("LAST_ROW");

        result.m_hasColHeaders = settings.getBoolean("HAS_COL_HDRS");
        result.m_colHdrRow = settings.getInt("COL_HDR_ROW");
        result.m_hasRowHeaders = settings.getBoolean("HAS_ROW_HDRS");
        result.m_rowHdrCol = settings.getInt("ROW_HDR_COL");

        result.m_missValuePattern = settings.getString("MISS_VAL_PATTERN");
        result.m_skipEmptyColumns = settings.getBoolean("SKIP_EMPTY_COLS");
        result.m_skipEmptyRows = settings.getBoolean("SKIP_EMPTY_ROWS");
        result.m_skipHiddenColumns = settings.getBoolean("SKIP_HIDDEN_COLS");

        result.m_keepXLColNames = settings.getBoolean("KEEP_XL_HDR");
        result.m_uniquifyRowIDs = settings.getBoolean("UNIQUIFY_ROWID");

        // added in V2.2.3
        result.m_useErrorPattern =
                settings.getBoolean("FORMULA_ERR_USE_PATTERN", true);
        result.m_errorPattern =
                settings.getString("FORMULA_ERR_PATTERN", DEFAULT_ERR_PATTERN);

        return result;
    }

    /**
     * Creates a deep copy of the passed settings object.
     *
     * @param orig the settings object to clone
     * @return a copy of the argument
     * @throws InvalidSettingsException normally not
     */
    public static ClearQuestUserSettings clone(final ClearQuestUserSettings orig)
            throws InvalidSettingsException {
        NodeSettings clone = new NodeSettings("clone");
        orig.save(clone);
        return ClearQuestUserSettings.load(clone);
    }

    /**
     * Checks the settings and returns an error message - or null if everything
     * is alright.
     *
     * @param checkFileExistence if true existence and readability of the
     *            specified file location is checked (not the content/format
     *            though).
     * @return an error message or null if settings are okay
     */
    public String getStatus(final boolean checkFileExistence) {
        if (m_fileLocation == null || m_fileLocation.isEmpty()) {
            return "No file location specified";
        }
        if (checkFileExistence) {
            try {
                URL url = new URL(m_fileLocation);
                try {
                    FileUtil.openStreamWithTimeout(url).close();
                } catch (IOException ioe) {
                    return "Can't open specified location (" + m_fileLocation
                            + ")";
                }
            } catch (MalformedURLException mue) {
                // then try a file
                File f = new File(m_fileLocation);
                if (!f.exists()) {
                    return "Specified file doesn't exist ("
                            + f.getAbsolutePath() + ")";
                }
                if (!f.canRead()) {
                    return "Specified file is not readable ("
                            + f.getAbsolutePath() + ")";
                }
                if (!f.isFile()) {
                    return "Specified location is not a file ("
                            + f.getAbsolutePath() + ")";
                }
            }
        }

        if (m_sheetName == null || m_sheetName.isEmpty()) {
            // since 4.0 accept null or empty sheet names using the first sheet then
//            return "Sheet to read is not specified";
        }
        if (!m_readAllData) {
            if (m_firstColumn < 0) {
                return "'First column' index must be greater than zero";
            }
            if (m_firstRow < 0) {
                return "'First row' index must be greater than zero";
            }
            if (m_lastColumn >= 0 && m_lastColumn < m_firstColumn) {
                return "Last column to read can't be "
                        + "smaller than the first column";
            }
            if (m_lastRow >= 0 && m_lastRow < m_firstRow) {
                return "Last Row to read can't be smaller than the first row";
            }
        }
        if (m_hasColHeaders && m_colHdrRow < 0) {
            return "Row containing column headers is not specified";
        }
        if (m_hasRowHeaders && m_rowHdrCol < 0) {
            return "Column containing row IDs is not specified";
        }
        return null;
    }

    /*
     * ---------------- setter and getter ------------------------------------
     */

    /**
     * @param fileLocation the fileLocation to set
     */
    public void setFileLocation(final String fileLocation) {
        m_fileLocation = fileLocation;
    }

    /**
     * @return the fileLocation
     */
    public String getFileLocation() {
        return m_fileLocation;
    }

    /**
     * Opens and returns a new buffered input stream on the xls location.
     *
     * @return a new buffered input stream on the xls location.
     * @throws IOException
     */
    public BufferedInputStream getBufferedInputStream() throws IOException {
        return getBufferedInputStream(m_fileLocation);
    }

    /**
     * Opens and returns a new buffered input stream on the passed location. The
     * location could either be a filename or a URL.
     *
     * @param location a filename or a URL
     * @return a new opened buffered input stream.
     * @throws IOException
     */
    public static BufferedInputStream getBufferedInputStream(
            final String location) throws IOException {
        InputStream in;
        try {
            URL url = new URL(location);
            in = FileUtil.openStreamWithTimeout(url);
        } catch (MalformedURLException mue) {
            // then try a file
            in = new FileInputStream(location);
        }
        return new BufferedInputStream(in);

    }

    /**
     * @return the simple name of the file
     */
    public String getSimpleFilename() {
        try {
            URL url = new URL(m_fileLocation);
            String path = url.getPath();
            int idx = path.lastIndexOf('/');
            if (idx < 0 || idx >= path.length() - 2) {
                return path;
            } else {
                return path.substring(idx + 1);
            }
        } catch (MalformedURLException mue) {
            // then try a file
            File f = new File(m_fileLocation);
            return f.getName();
        }
    }

    /**
     * @param sheetName name of sheet to read
     */
    public void setSheetName(final String sheetName) {
        m_sheetName = sheetName;
    }

    /**
     * @return the index of the sheet to read
     */
    public String getSheetName() {
        return m_sheetName;
    }

    /**
     * @return the firstRow
     */
    public int getFirstRow() {
        return m_firstRow;
    }

    /**
     * @param firstRow the firstRow to set
     */
    public void setFirstRow(final int firstRow) {
        m_firstRow = firstRow;
    }

    /**
     * @return the lastRow
     */
    public int getLastRow() {
        return m_lastRow;
    }

    /**
     * @param lastRow the lastRow to set
     */
    public void setLastRow(final int lastRow) {
        m_lastRow = lastRow;
    }

    /**
     * @return the firstColumn
     */
    public int getFirstColumn() {
        return m_firstColumn;
    }

    /**
     * @param firstColumn the firstColumn to set
     */
    public void setFirstColumn(final int firstColumn) {
        m_firstColumn = firstColumn;
    }

    /**
     * @return the lastColumn
     */
    public int getLastColumn() {
        return m_lastColumn;
    }

    /**
     * @param lastColumn the lastColumn to set
     */
    public void setLastColumn(final int lastColumn) {
        m_lastColumn = lastColumn;
    }

    /**
     * @param skipHiddenColumns the skipHiddenColumns to set
     */
    public void setSkipHiddenColumns(final boolean skipHiddenColumns) {
        m_skipHiddenColumns = skipHiddenColumns;
    }

    /**
     * @return the skipHiddenColumns
     */
    public boolean getSkipHiddenColumns() {
        return m_skipHiddenColumns;
    }

    /**
     * @return true if empty columns are skipped
     */
    public boolean getSkipEmptyColumns() {
        return m_skipEmptyColumns;
    }

    /**
     * @param skipEmptyColumns the skipEmptyColumns to set
     */
    public void setSkipEmptyColumns(final boolean skipEmptyColumns) {
        m_skipEmptyColumns = skipEmptyColumns;
    }

    /**
     * @param skipEmptyRows the skipEmptyRows to set
     */
    public void setSkipEmptyRows(final boolean skipEmptyRows) {
        m_skipEmptyRows = skipEmptyRows;
    }

    /**
     * @return the skipEmptyRows
     */
    public boolean getSkipEmptyRows() {
        return m_skipEmptyRows;
    }

    /**
     * @return the colHdrRow
     */
    public int getColHdrRow() {
        return m_colHdrRow;
    }

    /**
     * @param colHdrRow the colHdrRow to set
     */
    public void setColHdrRow(final int colHdrRow) {
        m_colHdrRow = colHdrRow;
    }

    /**
     * @return the hasColHeaders
     */
    public boolean getHasColHeaders() {
        return m_hasColHeaders;
    }

    /**
     * @param hasColHeaders the hasColHeaders to set
     */
    public void setHasColHeaders(final boolean hasColHeaders) {
        m_hasColHeaders = hasColHeaders;
    }

    /**
     * @param hasRowHeaders the hasRowHeaders to set
     */
    public void setHasRowHeaders(final boolean hasRowHeaders) {
        m_hasRowHeaders = hasRowHeaders;
    }

    /**
     * @return the hasRowHeaders
     */
    public boolean getHasRowHeaders() {
        return m_hasRowHeaders;
    }

    /**
     * @param rowHdrCol the rowHdrCol to set
     */
    public void setRowHdrCol(final int rowHdrCol) {
        m_rowHdrCol = rowHdrCol;
    }

    /**
     * @return the rowHdrCol
     */
    public int getRowHdrCol() {
        return m_rowHdrCol;
    }

    /**
     * @return the keepXLColNames
     */
    public boolean getKeepXLColNames() {
        return m_keepXLColNames;
    }

    /**
     * @param keepXLColNames the keepXLColNames to set
     */
    public void setKeepXLNames(final boolean keepXLColNames) {
        m_keepXLColNames = keepXLColNames;
    }

    /**
     * @return the missValuePattern
     */
    public String getMissValuePattern() {
        return m_missValuePattern;
    }

    /**
     * @param missValuePattern the missValuePattern to set
     */
    public void setMissValuePattern(final String missValuePattern) {
        m_missValuePattern = missValuePattern;
    }

    /**
     * @return the readAllData
     */
    public boolean getReadAllData() {
        return m_readAllData;
    }

    /**
     * @param readAllData the readAllData to set
     */
    public void setReadAllData(final boolean readAllData) {
        m_readAllData = readAllData;
    }

    /**
     * @return the uniquifyRowIDs
     */
    public boolean getUniquifyRowIDs() {
        return m_uniquifyRowIDs;
    }

    /**
     * @param uniquifyRowIDs the uniquifyRowIDs to set
     */
    public void setUniquifyRowIDs(final boolean uniquifyRowIDs) {
        m_uniquifyRowIDs = uniquifyRowIDs;
    }

    /**
     *@see #setUseErrorPattern(boolean)
     * @param errorPattern the errorPattern that is inserted if a formula
     *            evaluation fails (only if the corresponding boolean flag is
     *            true).
     */
    public void setErrorPattern(final String errorPattern) {
        if (errorPattern == null) {
            m_errorPattern = "";
        } else {
            m_errorPattern = errorPattern;
        }
    }

    /**
     *@see #getUseErrorPattern()
     * @return the errorPattern. A StringCell with the pattern is inserted for
     *         the formula value in case the formula eval fails - and the
     *         corresponding flag is set true.
     */
    public String getErrorPattern() {
        return m_errorPattern;
    }

    /**
     * @see #setErrorPattern(String)
     * @param useErrorPattern if true the error pattern set is inserted (in a
     *            StringCell) if formula evaluations fail (causing the entire
     *            column to become a string column).
     */
    public void setUseErrorPattern(final boolean useErrorPattern) {
        m_useErrorPattern = useErrorPattern;
    }

    /**
     * @see XLSUserSettings#getErrorPattern()
     * @return true, if a StringCell is inserted when formula evaluation fails.
     *         Or false, if a missing cell is inserted instead.
     */
    public boolean getUseErrorPattern() {
        return m_useErrorPattern;
    }

}
