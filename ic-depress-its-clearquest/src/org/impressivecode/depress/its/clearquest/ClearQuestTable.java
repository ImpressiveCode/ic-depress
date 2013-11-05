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
 *   Apr 27, 2009 (ohl): created
 */
package org.impressivecode.depress.its.clearquest;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowIterator;
import org.knime.core.node.InvalidSettingsException;

/**
 *
 * @author Peter Ohl, KNIME.com, Zurich, Switzerland
 */
class ClearQuestTable implements DataTable {

    private final ClearQuestTableSettings m_settings;

    // list of all iterators to close the source, when the table is disposed of
    private final LinkedList<WeakReference<ClearQuestIterator>> m_iterators;

    private Workbook m_workbook;

    /**
     * Settings must contain a file name and must be run through analyze.
     *
     * @param settings settings to use for reading the XLS file.
     * @param wb The workbook
     * @throws InvalidSettingsException if settings are invalid
     * @throws IOException If table settings could not be instantiated
     * @throws InvalidFormatException If table settings could not be instaniated
     */
    ClearQuestTable(final ClearQuestUserSettings settings, final Workbook wb) throws InvalidSettingsException,
            IOException, InvalidFormatException {
        m_workbook = wb;
        if (settings == null) {
            throw new IllegalArgumentException(
                    "Settings with valid filename must be provided.");
        }
        m_iterators = new LinkedList<WeakReference<ClearQuestIterator>>();
        m_settings = new ClearQuestTableSettings(settings, wb);

    }

    /**
     * Settings will be taken over. Not cloned.
     *
     * @param tableSettings
     */
    public ClearQuestTable(final ClearQuestTableSettings tableSettings, final Workbook wb) {
        m_workbook = wb;
        if (tableSettings == null) {
            throw new IllegalArgumentException("Table settings can't be null");
        }
        m_iterators = new LinkedList<WeakReference<ClearQuestIterator>>();
        m_settings = tableSettings;

    }

    /**
     * Call this before releasing the last reference to this table. It closes
     * the underlying source file. Especially if the iterator didn't run to the
     * end of the table, it is required to call this method. Otherwise the file
     * handle is not released until the garbage collector cleans up. A call to
     * {@link RowIterator#next()} after disposing of the table has undefined
     * behavior.
     */
    public void dispose() {
        synchronized (m_iterators) {
            m_workbook = null;
            for (WeakReference<ClearQuestIterator> w : m_iterators) {
            	ClearQuestIterator i = w.get();
                if (i != null) {
                    i.close();
                }
            }
            m_iterators.clear();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public DataTableSpec getDataTableSpec() {
        return m_settings.getDataTableSpec();
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public RowIterator iterator() {
        try {
            synchronized (m_iterators) {
            	ClearQuestIterator i = new ClearQuestIterator(m_settings, m_workbook);
                m_iterators.add(new WeakReference<ClearQuestIterator>(i));
                return i;
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        } catch (InvalidSettingsException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Works around the weirdness of the POI/XLS API.
     *
     * @param sheet the sheet to examine
     * @return the number of rows in the sheet minus one.
     */
    public static int getLastRowIdx(final Sheet sheet) {
        int rowMaxIdx = sheet.getLastRowNum();
        if (rowMaxIdx == 0) {
            if (sheet.getPhysicalNumberOfRows() == 0) {
                return -1;
            }
        }
        return rowMaxIdx;
    }

    /**
     * Returns the names of the sheets contained in the specified file.
     *
     * @param wb The workbook
     * @return an array with sheet names
     */
    public static ArrayList<String> getSheetNames(final Workbook wb) {

        ArrayList<String> names = new ArrayList<String>();
        for (int sIdx = 0; sIdx < wb.getNumberOfSheets(); sIdx++) {
            names.add(wb.getSheetName(sIdx));
        }
        return names;
    }

    /**
     * Finds out the name of the first sheet in the workbook.
     *
     * @param wBook the workbook to examine.
     * @return the name of the first sheet
     */
    public static String getFirstSheetNameWithData(final Workbook wBook) {
        String result = null;
        for (int sIdx = 0; sIdx < wBook.getNumberOfSheets(); sIdx++) {
            String sheetName = wBook.getSheetName(sIdx);
            if (sIdx == 0) {
                // return the first sheet, in case there is no data in the book
                result = sheetName;
            }
            Sheet sheet = wBook.getSheet(sheetName);
            int maxRowIdx = ClearQuestTable.getLastRowIdx(sheet);
            int minRowIdx = sheet.getFirstRowNum();
            if (minRowIdx >= 0 && maxRowIdx >= 0 && minRowIdx <= maxRowIdx) {
                return sheetName;
            }
        }
        return result;

    }

    /**
     * Columns are displayed starting with "A", "B", "C", ...etc, then "Z",
     * "AA", "AB", etc... That is with 26 different symbols. Which is a number
     * system with 26 different digits. Unfortunately there is no zero in this
     * system. (After Z comes AA.) But 'Z' appears whenever there should be a
     * zero. For example, column 26 is coded 'Z', but should be '10' (one-zero)
     * and with letters A0 (a-zero). Same with 52 (which is two-zero) should be
     * B0 (b-zero), but is AZ. Whenever there should be a zero, the
     * representation gets a bit obscured. And that in the following way:
     * Instead of writing the symbol for zero and incrementing the next digit,
     * we write 'Z' and don't increment. So, in order to get this weird
     * representation we first create a normal number on the base 26 (with
     * symbols '@'=0, 'A'=1, ... 'Y'=25), and then convert each zero (symbol
     * '@') into a 'Z' and decrement the position left of it.
     *
     * @param colIdx the number to convert into a column label
     *
     * @return the column label representing the col with the specified index.
     */
    public static String getColLabel(final int colIdx) {

        if (colIdx < 0) {
            throw new IllegalArgumentException("column index can't be less"
                    + " then zero");
        }

        // Create a normal 26base number (with digits '@'(=0) to Y(=25))
        int maxDig = (int)((Math.log(colIdx + 1) / Math.log(26)) + 1);
        int remains = colIdx + 1;

        StringBuilder result = new StringBuilder();

        while (maxDig >= 0) {
            int nextPow = (int)Math.round(Math.pow(26, maxDig));
            char nextDig = toLetter(remains / nextPow);
            result.append(nextDig);
            remains = remains % nextPow;
            maxDig--;
        }

        // now replace the zero element ('@') with the Z(=26) - without
        // changing the value...
        while (result.lastIndexOf("@") >= 0) {

            // first, remove leading spaces (i.e. leading zeros)
            // (do that every time around)
            int lastLeadingSpace = -1;
            while ((result.length() > lastLeadingSpace + 1)
                    && (result.charAt(lastLeadingSpace + 1) == '@')) {
                lastLeadingSpace++;
            }
            if (lastLeadingSpace > -1) {
                result.delete(0, lastLeadingSpace + 1);
            }

            int zeroPos = result.lastIndexOf("@");
            if (zeroPos < 0) {
                // there were only leading zeros in this string. Done.
                break;
            }

            // replace the zero with 'Z' and decrement the higher value digit(s)
            result.setCharAt(zeroPos, 'Z');

            boolean carry = true; // always decrement the next position
            int decrPos = zeroPos - 1;
            while (carry && (decrPos >= 0)) {
                int decrVal = toNumber(result.charAt(decrPos));
                carry = (decrVal == 0); // keep decrementing if it flips over
                decrVal--;
                if (decrVal < 0) {
                    decrVal = 25;
                }
                result.setCharAt(decrPos, toLetter(decrVal));

                decrPos--;
            }

        }

        return result.toString();

    }

    private static char toLetter(final int number) {
        assert number <= 26;
        assert number >= 0;
        return (char)('@' + number);
    }

    private static int toNumber(final char c) {
        assert c <= 'Z';
        assert c >= '@';
        return c - '@';
    }

    /**
     * Computes the number of the column represented by the specified label.
     *
     * @param columnLabel string with the column's label. Must contain only
     *            capital letters.
     * @return the index of the column represented by the specified string.
     * @throws NullPointerException if the specified label is null.
     * @throws IllegalArgumentException if the specified label is empty or
     *             contains anything else but capital letters.
     */
    public static int getColumnIndex(final String columnLabel) {
        if (!columnLabel.matches("[A-Z]+")) {
            throw new IllegalArgumentException("Not a valid column label: '"
                    + columnLabel + "'");
        }
        if (columnLabel.isEmpty()) {
            return 0;
        }

        int result = 0;
        for (int d = 0; d < columnLabel.length(); d++) {
            int dig =
                    toNumber(columnLabel.charAt(columnLabel.length() - d - 1));
            result += dig * (int)Math.round(Math.pow(26, d));
        }

        return result - 1;
    }

}
