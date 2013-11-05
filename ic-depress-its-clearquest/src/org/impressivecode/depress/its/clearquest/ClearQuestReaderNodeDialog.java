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
 *   Apr 8, 2009 (ohl): created
 */
package org.impressivecode.depress.its.clearquest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.config.Config;
import org.knime.core.node.tableview.TableView;
import org.knime.core.node.util.FilesHistoryPanel;
import org.knime.core.node.util.ViewUtils;
import org.knime.core.util.SwingWorkerWithContext;

/**
 * The dialog to the XLS reader.
 *
 * @author Peter Ohl, KNIME.com, Zurich, Switzerland
 */
public class ClearQuestReaderNodeDialog extends NodeDialogPane {

    private final FilesHistoryPanel m_fileName = new FilesHistoryPanel(
            "HPQCReader", ".xls", ".xlsx");

    private final JComboBox m_sheetName = new JComboBox();

    private final JCheckBox m_hasColHdr = new JCheckBox();

    private final JTextField m_colHdrRow = new JTextField();

    private final JCheckBox m_hasRowIDs = new JCheckBox();

    private final JTextField m_rowIDCol = new JTextField();

    private final JTextField m_firstRow = new JTextField();

    private final JTextField m_lastRow = new JTextField();

    private final JTextField m_firstCol = new JTextField();

    private final JTextField m_lastCol = new JTextField();

    private final JCheckBox m_readAllData = new JCheckBox();

    private final TableView m_fileTable = new TableView();

    private ClearQuestTable m_fileDataTable = null;

    private final JPanel m_fileTablePanel = new JPanel();

    private final JPanel m_previewTablePanel = new JPanel();

    private final TableView m_previewTable = new TableView();

    private ClearQuestTable m_previewDataTable = null;

    private final JLabel m_previewMsg = new JLabel();

    private final JButton m_previewUpdate = new JButton();

    private final JCheckBox m_skipEmptyCols = new JCheckBox();

    private final JCheckBox m_skipEmptyRows = new JCheckBox();

    private final JCheckBox m_uniquifyRowIDs = new JCheckBox();

    private final JRadioButton m_formulaMissCell = new JRadioButton();

    private final JRadioButton m_formulaStringCell = new JRadioButton();

    private final JTextField m_formulaErrPattern = new JTextField();

    private Workbook m_workbook = null;

    private String m_workbookPath = null;

    private static final int LEFT_INDENT = 25;

    /* flag to temporarily disable listeners during loading of settings */
    private final AtomicBoolean m_loading = new AtomicBoolean(false);

    private static final String SCANNING = "/* scanning... */";

    private static final String FIRST_SHEET = "<first sheet with data>";

    /** config key used to store data table spec. */
    static final String HPQC_CFG_TABLESPEC = "HPQC_DataTableSpec";

    /** config key used to store id of settings used to create table spec. */
    static final String HPQC_CFG_ID_FOR_TABLESPEC = "HPQC_SettingsForSpecID";

    private String m_fileAccessError = null;

    private static final String PREVIEWBORDER_MSG =
            "Preview with current settings";

    /**
     *
     */
    public ClearQuestReaderNodeDialog() {

        JPanel dlgTab = new JPanel();
        dlgTab.setLayout(new BoxLayout(dlgTab, BoxLayout.Y_AXIS));

        JComponent fileBox = getFileBox();
        fileBox.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Select file to read:"));
        dlgTab.add(fileBox);

        JPanel settingsBox = new JPanel();
        settingsBox.setLayout(new BoxLayout(settingsBox, BoxLayout.Y_AXIS));
        settingsBox.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Adjust Settings:"));
        settingsBox.add(getSheetBox());
        settingsBox.add(getColHdrBox());
        settingsBox.add(getRowIDBox());
        settingsBox.add(getAreaBox());
        settingsBox.add(getXLErrBox());
        settingsBox.add(getOptionsBox());
        dlgTab.add(settingsBox);
        dlgTab.add(Box.createVerticalGlue());
        dlgTab.add(Box.createVerticalGlue());
        dlgTab.add(getTablesBox());

        addTab("HPQC Reader Settings", new JScrollPane(dlgTab));

    }

    private JComponent getFileBox() {
        Box fBox = Box.createHorizontalBox();
        fBox.add(Box.createHorizontalGlue());
        m_fileName.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent e) {
                fileNameChanged();
            }
        });
        fBox.add(m_fileName);
        fBox.add(Box.createHorizontalGlue());
        return fBox;
    }

    private JComponent getSheetBox() {
        Box sheetBox = Box.createHorizontalBox();
        sheetBox.add(Box.createHorizontalGlue());
        sheetBox.add(new JLabel("Select the sheet to read:"));
        sheetBox.add(Box.createHorizontalStrut(5));
        m_sheetName.setPreferredSize(new Dimension(170, 25));
        m_sheetName.setMaximumSize(new Dimension(170, 25));
        m_sheetName.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    sheetNameChanged();
                }
            }
        });
        m_sheetName.setRenderer(new BasicComboBoxRenderer() {
            /**
             * {@inheritDoc}
             */
            @Override
            public Component getListCellRendererComponent(final JList list,
                    final Object value, final int index,
                    final boolean isSelected, final boolean cellHasFocus) {
                if ((index > -1) && (value != null)) {
                    list.setToolTipText(value.toString());
                } else {
                    list.setToolTipText(null);
                }
                return super.getListCellRendererComponent(list, value, index,
                        isSelected, cellHasFocus);
            }
        });
        sheetBox.add(m_sheetName);
        sheetBox.add(Box.createHorizontalGlue());
        return sheetBox;
    }

    private void sheetNameChanged() {
        m_sheetName.setToolTipText((String)m_sheetName.getSelectedItem());
        if (m_loading.get()) {
            return;
        }
        updateFileTable();
        updatePreviewTable();
    }

    private JComponent getColHdrBox() {
        Box colHdrBox = Box.createHorizontalBox();
        colHdrBox.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Column Names:"));

        m_hasColHdr.setText("Table contains column names in row number:");
        m_hasColHdr.setToolTipText("Enter a number. First row has number 1.");
        m_hasColHdr.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                checkBoxChanged();
            }
        });
        m_colHdrRow.setPreferredSize(new Dimension(75, 25));
        m_colHdrRow.setMaximumSize(new Dimension(75, 25));
        addFocusLostListener(m_colHdrRow);

        colHdrBox.add(Box.createHorizontalStrut(LEFT_INDENT));
        colHdrBox.add(m_hasColHdr);
        colHdrBox.add(Box.createHorizontalStrut(3));
        colHdrBox.add(m_colHdrRow);
        colHdrBox.add(Box.createHorizontalGlue());
        return colHdrBox;
    }

    private void checkBoxChanged() {
        m_colHdrRow.setEnabled(m_hasColHdr.isSelected());
        m_rowIDCol.setEnabled(m_hasRowIDs.isSelected());
        m_uniquifyRowIDs.setEnabled(m_hasRowIDs.isSelected());
        m_firstCol.setEnabled(!m_readAllData.isSelected());
        m_lastCol.setEnabled(!m_readAllData.isSelected());
        m_firstRow.setEnabled(!m_readAllData.isSelected());
        m_lastRow.setEnabled(!m_readAllData.isSelected());
        invalidatePreviewTable();
    }

    private JComponent getRowIDBox() {

        Box rowBox = Box.createHorizontalBox();
        m_hasRowIDs.setText("Table contains row IDs in column:");
        m_hasRowIDs.setToolTipText("Enter A, B, C, .... or a number 1 ...");
        m_hasRowIDs.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                checkBoxChanged();
            }
        });
        m_rowIDCol.setPreferredSize(new Dimension(75, 25));
        m_rowIDCol.setMaximumSize(new Dimension(75, 25));
        m_rowIDCol.setToolTipText("Enter A, B, C, .... or a number 1 ...");
        addFocusLostListener(m_rowIDCol);
        rowBox.add(Box.createHorizontalStrut(LEFT_INDENT));
        rowBox.add(m_hasRowIDs);
        rowBox.add(Box.createHorizontalStrut(3));
        rowBox.add(m_rowIDCol);
        rowBox.add(Box.createHorizontalGlue());

        Box uniquifyRowIDBox = Box.createHorizontalBox();
        m_uniquifyRowIDs.setText("Make row IDs unique");
        m_uniquifyRowIDs.setToolTipText("If checked, row IDs are uniquified "
                + "by adding a suffix if necessary (could cause memory "
                + "problems with very large data sets).");
        m_uniquifyRowIDs.setSelected(false);
        m_uniquifyRowIDs.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                invalidatePreviewTable();
            }
        });
        uniquifyRowIDBox.add(Box.createHorizontalStrut(LEFT_INDENT));
        uniquifyRowIDBox.add(m_uniquifyRowIDs);
        uniquifyRowIDBox.add(Box.createHorizontalGlue());

        Box rowIDBox = Box.createVerticalBox();
        rowIDBox.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Row IDs:"));
        rowIDBox.add(rowBox);
        rowIDBox.add(uniquifyRowIDBox);
        return rowIDBox;
    }

    private JComponent getAreaBox() {

        Box rowsBox = Box.createHorizontalBox();
        m_firstRow.setPreferredSize(new Dimension(75, 25));
        m_firstRow.setMaximumSize(new Dimension(75, 25));
        addFocusLostListener(m_firstRow);
        m_lastRow.setPreferredSize(new Dimension(75, 25));
        m_lastRow.setMaximumSize(new Dimension(75, 25));
        addFocusLostListener(m_lastRow);
        rowsBox.add(Box.createVerticalGlue());
        rowsBox.add(Box.createVerticalGlue());
        rowsBox.add(new JLabel("and read rows from:"));
        rowsBox.add(Box.createHorizontalStrut(3));
        rowsBox.add(m_firstRow);
        rowsBox.add(Box.createHorizontalStrut(3));
        rowsBox.add(new JLabel("to:"));
        rowsBox.add(Box.createHorizontalStrut(3));
        rowsBox.add(m_lastRow);

        Box colsBox = Box.createHorizontalBox();
        m_firstCol.setPreferredSize(new Dimension(75, 25));
        m_firstCol.setMaximumSize(new Dimension(75, 25));
        addFocusLostListener(m_firstCol);
        m_lastCol.setPreferredSize(new Dimension(75, 25));
        m_lastCol.setMaximumSize(new Dimension(75, 25));
        addFocusLostListener(m_lastCol);
        colsBox.add(Box.createVerticalGlue());
        colsBox.add(Box.createVerticalGlue());
        colsBox.add(new JLabel("read columns from:"));
        colsBox.add(Box.createHorizontalStrut(3));
        colsBox.add(m_firstCol);
        colsBox.add(Box.createHorizontalStrut(3));
        colsBox.add(new JLabel("to:"));
        colsBox.add(Box.createHorizontalStrut(3));
        colsBox.add(m_lastCol);

        m_readAllData.setText("Read entire data sheet, or ...");
        m_readAllData.setToolTipText("If checked, cells that contain "
                + "something (data, format, color, etc.) are read in");
        m_readAllData.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                checkBoxChanged();
            }
        });
        m_readAllData.setSelected(false);

        Box allVBox = Box.createVerticalBox();
        allVBox.add(m_readAllData);
        allVBox.add(Box.createVerticalGlue());
        allVBox.add(Box.createVerticalGlue());

        Box fromToVBox = Box.createVerticalBox();
        fromToVBox.add(colsBox);
        fromToVBox.add(Box.createVerticalStrut(5));
        fromToVBox.add(rowsBox);

        Box areaBox = Box.createHorizontalBox();
        areaBox.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Select the columns and rows to read:"));
        areaBox.add(Box.createHorizontalStrut(LEFT_INDENT));
        areaBox.add(allVBox);
        areaBox.add(Box.createHorizontalStrut(10));
        areaBox.add(fromToVBox);
        areaBox.add(Box.createHorizontalGlue());
        return areaBox;
    }

    private JComponent getOptionsBox() {

        JComponent skipBox = getSkipEmptyThingsBox();

        Box optionsBox = Box.createHorizontalBox();
        optionsBox.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "More Options:"));
        optionsBox.add(skipBox);
        optionsBox.add(Box.createHorizontalGlue());
        return optionsBox;

    }

    private JComponent getSkipEmptyThingsBox() {
        Box skipColsBox = Box.createHorizontalBox();
        m_skipEmptyCols.setText("Skip empty columns");
        m_skipEmptyCols.setToolTipText("If checked, columns that contain "
                + "only missing values are not part of the output table");
        m_skipEmptyCols.setSelected(true);
        m_skipEmptyCols.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                invalidatePreviewTable();
            }
        });
        skipColsBox.add(Box.createHorizontalStrut(LEFT_INDENT));
        skipColsBox.add(m_skipEmptyCols);
        skipColsBox.add(Box.createHorizontalGlue());

        Box skipRowsBox = Box.createHorizontalBox();
        m_skipEmptyRows.setText("Skip empty rows");
        m_skipEmptyRows.setToolTipText("If checked, rows that contain "
                + "only missing values are not part of the output table");
        m_skipEmptyRows.setSelected(true);
        m_skipEmptyRows.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                invalidatePreviewTable();
            }
        });
        skipRowsBox.add(Box.createHorizontalStrut(LEFT_INDENT));
        skipRowsBox.add(m_skipEmptyRows);
        skipRowsBox.add(Box.createHorizontalGlue());

        Box skipBox = Box.createVerticalBox();
        skipBox.add(skipColsBox);
        skipBox.add(skipRowsBox);
        skipBox.add(Box.createVerticalGlue());
        return skipBox;
    }

    private JComponent getXLErrBox() {
        m_formulaMissCell.setText("Insert a missing cell");
        m_formulaMissCell.setToolTipText("A missing cell doesn't change the "
                + "column's type, but might be hard to spot");
        m_formulaStringCell.setText("Insert an error pattern:");
        m_formulaStringCell.setToolTipText("When the evaluation fails the "
                + "column becomes a string column");
        ButtonGroup bg = new ButtonGroup();
        bg.add(m_formulaMissCell);
        bg.add(m_formulaStringCell);
        m_formulaStringCell.setSelected(true);
        m_formulaErrPattern.setColumns(15);
        m_formulaErrPattern.setText(ClearQuestUserSettings.DEFAULT_ERR_PATTERN);
        addFocusLostListener(m_formulaErrPattern);
        m_formulaStringCell.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                m_formulaErrPattern.setEnabled(
                        m_formulaStringCell.isSelected());
                invalidatePreviewTable();
            }
        });

        JPanel missingBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
        missingBox.add(Box.createHorizontalStrut(LEFT_INDENT));
        missingBox.add(m_formulaMissCell);

        JPanel stringBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
        stringBox.add(Box.createHorizontalStrut(LEFT_INDENT));
        stringBox.add(m_formulaStringCell);
        stringBox.add(m_formulaErrPattern);

        Box formulaErrBox = Box.createVerticalBox();
        formulaErrBox.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "On evaluation error:"));
        formulaErrBox.add(stringBox);
        formulaErrBox.add(missingBox);
        formulaErrBox.add(Box.createVerticalGlue());
        return formulaErrBox;
    }

    private void fileNameChanged() {
        // Refresh the workbook when the selected file changed
        refreshWorkbook(m_fileName.getSelectedFile());
        if (m_loading.get()) {
            return;
        }
        m_previewUpdate.setEnabled(false);
        m_previewMsg.setText("Scanning input file...");
        clearTableViews();
        updateSheetListAndSelect(null);

    }

    /**
     * Reads from the currently selected file the list of worksheets (in a
     * background thread) and selects the provided sheet (if not null -
     * otherwise selects the first name). Calls {@link #sheetNameChanged()}
     * after the update.
     *
     * @param sheetName
     */
    private void updateSheetListAndSelect(final String sheetName) {
        m_sheetName.setModel(new DefaultComboBoxModel(new Object[]{SCANNING}));
        SwingWorker<String[], Object> sw = new SwingWorkerWithContext<String[], Object>() {

            @Override
            protected String[] doInBackgroundWithContext() throws Exception {
                String file = m_fileName.getSelectedFile();
                if (file != null && !file.isEmpty()) {
                    m_fileAccessError = null;
                    try {
                        ArrayList<String> sheetNames = ClearQuestTable.getSheetNames(m_workbook);
                        sheetNames.add(0, FIRST_SHEET);
                        return sheetNames.toArray(new String[sheetNames.size()]);
                    } catch (Exception fnf) {
                        NodeLogger.getLogger(ClearQuestReaderNodeDialog.class).error(
                                fnf.getMessage(), fnf);
                        m_fileAccessError = fnf.getMessage();
                        // return empty list then
                    }
                }
                return new String[] {};
            }

            /**
             * {@inheritDoc}
             */
            @Override
            protected void doneWithContext() {
                String[] names = new String[]{};
                try {
                    names = get();
                } catch (InterruptedException e) {
                    // ignore
                } catch (ExecutionException e) {
                    // ignore
                }
                m_sheetName.setModel(new DefaultComboBoxModel(names));
                if (names.length > 0) {
                    if (sheetName != null) {
                        m_sheetName.setSelectedItem(sheetName);
                    } else {
                        m_sheetName.setSelectedIndex(0);
                    }
                } else {
                    m_sheetName.setSelectedIndex(-1);
                }
                sheetNameChanged();
            }
        };
        sw.execute();
    }

    private JComponent getTablesBox() {

        JTabbedPane viewTabs = new JTabbedPane();

        m_fileTablePanel.setLayout(new BorderLayout());
        m_fileTablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "XL Sheet Content:"));
        m_fileTablePanel.add(m_fileTable, BorderLayout.CENTER);
        m_fileTable.getHeaderTable().setColumnName("Row No.");
        m_previewTablePanel.setLayout(new BorderLayout());
        m_previewTablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), PREVIEWBORDER_MSG));
        m_previewTablePanel.add(m_previewTable, BorderLayout.CENTER);
        m_previewUpdate.setText("refresh");
        m_previewUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                m_previewUpdate.setEnabled(false);
                updatePreviewTable();
            }
        });
        m_previewMsg.setForeground(Color.RED);
        m_previewMsg.setText("");
        Box errBox = Box.createHorizontalBox();
        errBox.add(m_previewUpdate);
        errBox.add(Box.createHorizontalStrut(5));
        errBox.add(m_previewMsg);
        errBox.add(Box.createHorizontalGlue());
        errBox.add(Box.createVerticalStrut(30));
        m_previewTablePanel.add(errBox, BorderLayout.NORTH);
        viewTabs.addTab("Preview", m_previewTablePanel);
        viewTabs.addTab("File Content", m_fileTablePanel);

        return viewTabs;
    }

    private synchronized void clearTableViews() {
        clearPreview();
        clearFileview();
    }

    private synchronized void clearPreview() {
        ViewUtils.runOrInvokeLaterInEDT(new Runnable() {
            @Override
            public void run() {
                m_previewTable.setDataTable(null);
                if (m_previewDataTable != null) {
                    m_previewDataTable.dispose();
                }
                m_previewDataTable = null;
            }
        });
    }

    private synchronized void clearFileview() {
        ViewUtils.runOrInvokeLaterInEDT(new Runnable() {
            @Override
            public void run() {
                m_fileTable.setDataTable(null);
                if (m_fileDataTable != null) {
                    m_fileDataTable.dispose();
                }
                m_fileDataTable = null;
            }
        });
    }

    /**
     * reads the current filename and sheetname and fills the file content view.
     */
    private synchronized void updateFileTable() {

        final String file = m_fileName.getSelectedFile();
        if (file == null || file.isEmpty()) {
            setFileTablePanelBorderTitle("<no file set>");
            clearTableViews();
            return;
        }
        final String sheet = (String)m_sheetName.getSelectedItem();
        if (sheet == null || sheet.isEmpty()) {
            String msg = "Error while accessing file";
            if (m_fileAccessError != null) {
                msg += ": " + m_fileAccessError;
            }
            setFileTablePanelBorderTitle(msg);
            clearTableViews();
            return;
        }
        if (sheet == SCANNING) {
            setFileTablePanelBorderTitle("still scanning input file...");
            clearTableViews();
            return;
        }
        if (m_loading.get()) {
            // do not read from the file while loading settings.
            return;
        }
        final AtomicReference<ClearQuestTable> dt =
                new AtomicReference<ClearQuestTable>(null);
        SwingWorker<String, Object> sw = new SwingWorkerWithContext<String, Object>() {
            @Override
            protected String doInBackgroundWithContext() throws Exception {
            	ClearQuestUserSettings s;
                try {
                    s = createFileviewSettings(file, sheet, m_workbook);
                    dt.set(new ClearQuestTable(s, m_workbook));
                    return "Content of XL sheet: " + sheet;
                } catch (Throwable t) {
                    NodeLogger.getLogger(ClearQuestReaderNodeDialog.class).debug(
                            "Unable to create setttings for file content view",
                            t);
                    clearTableViews();
                    return "<unable to create view>";
                }
            }

            /**
             * {@inheritDoc}
             */
            @Override
            protected void doneWithContext() {
                String msg;
                try {
                    msg = get();
                } catch (InterruptedException e) {
                    msg = "<unable to create view>";
                } catch (ExecutionException e) {
                    msg = "<unable to create view>";
                }
                setFileTablePanelBorderTitle(msg);
                m_fileTable.setDataTable(dt.get());
                if (m_fileDataTable != null) {
                    m_fileDataTable.dispose();
                }
                m_fileDataTable = dt.get();
            }
        };
        clearFileview();
        setFileTablePanelBorderTitle("Updating file content view...");
        sw.execute();
    }

    private void setFileTablePanelBorderTitle(final String title) {
        ViewUtils.invokeAndWaitInEDT(new Runnable() {
            @Override
            public void run() {
                Border b = m_fileTablePanel.getBorder();
                if (b instanceof TitledBorder) {
                    TitledBorder tb = (TitledBorder)b;
                    tb.setTitle(title);
                    m_fileTablePanel.repaint();
                }
            }
        });
    }

    private void setPreviewTablePanelBorderTitle(final String title) {
        ViewUtils.invokeAndWaitInEDT(new Runnable() {
            @Override
            public void run() {
                Border b = m_previewTablePanel.getBorder();
                if (b instanceof TitledBorder) {
                    TitledBorder tb = (TitledBorder)b;
                    tb.setTitle(title);
                    m_previewTablePanel.repaint();
                }
            }
        });
    }

    private synchronized void invalidatePreviewTable() {
        m_previewMsg.setText("Preview table is out of sync with current "
                + "settings. Please refresh.");
    }

    /**
     * Call in EDT.
     */
    private synchronized void updatePreviewTable() {
        // make sure user doesn't trigger it again
        m_previewUpdate.setEnabled(false);

        String file = m_fileName.getSelectedFile();
        if (file == null || file.isEmpty()) {
            m_previewMsg.setText("Set a filename.");
            clearTableViews();
            // enable the refresh button again
            m_previewUpdate.setEnabled(true);
            return;
        }
        String sheet = (String)m_sheetName.getSelectedItem();
        if (sheet == null || sheet.isEmpty()) {
            String msg = "Error while accessing file";
            if (m_fileAccessError != null) {
                msg += ": " + m_fileAccessError;
            }
            m_previewMsg.setText(msg);
            m_previewMsg.setToolTipText(msg);
            clearTableViews();
            // enable the refresh button again
            m_previewUpdate.setEnabled(true);
            return;
        }
        if (sheet == SCANNING) {
            clearTableViews();
            // enable the refresh button again
            m_previewUpdate.setEnabled(true);
            return;
        }

        if (m_loading.get()) {
            // do nothing while loading settings.
            return;
        }
        m_previewMsg.setText("Refreshing preview table....");

        final AtomicReference<ClearQuestTable> dt =
                new AtomicReference<ClearQuestTable>(null);

        SwingWorker<String, Object> sw = new SwingWorkerWithContext<String, Object>() {
            @Override
            protected String doInBackgroundWithContext() throws Exception {
            	ClearQuestUserSettings s;
                try {
                    s = createSettingsFromComponents();
                    dt.set(new ClearQuestTable(s, m_workbook));
                } catch (Throwable t) {
                    String msg = t.getMessage();
                    if (msg == null || msg.isEmpty()) {
                        msg = "no details, sorry.";
                    }
                    return msg;
                }
                return null;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            protected void doneWithContext() {
                try {
                    setPreviewTablePanelBorderTitle(PREVIEWBORDER_MSG);
                    String err = null;
                    try {
                        err = get();
                    } catch (InterruptedException e) {
                        err = e.getMessage();
                    } catch (ExecutionException e) {
                        err = e.getMessage();
                    }
                    if (err != null) {
                        m_previewMsg.setText(err);
                        clearPreview();
                        return;
                    }

                    m_previewMsg.setText("");
                    try {
                        String previewTxt =
                                PREVIEWBORDER_MSG + ": "
                                        + dt.get().getDataTableSpec().getName();
                        setPreviewTablePanelBorderTitle(previewTxt);
                        m_previewTable.setDataTable(dt.get());
                        if (m_previewDataTable != null) {
                            m_previewDataTable.dispose();
                        }
                        m_previewDataTable = dt.get();
                    } catch (Throwable t) {
                        m_previewMsg.setText(t.getMessage());
                    }
                } finally {
                    // enable the refresh button again
                    m_previewUpdate.setEnabled(true);
                }
            }
        };
        sw.execute();
    }

    private ClearQuestUserSettings createSettingsFromComponents()
            throws InvalidSettingsException {
    	ClearQuestUserSettings s = new ClearQuestUserSettings();

        s.setFileLocation(m_fileName.getSelectedFile());

        String sheetName = (String)m_sheetName.getSelectedItem();
        if (sheetName == FIRST_SHEET) {
            sheetName = null;
        }
        s.setSheetName(sheetName);

        s.setSkipEmptyColumns(m_skipEmptyCols.isSelected());
        s.setSkipEmptyRows(m_skipEmptyRows.isSelected());
        s.setReadAllData(m_readAllData.isSelected());

        s.setHasColHeaders(m_hasColHdr.isSelected());
        try {
            s.setColHdrRow(getNumberFromTextField(m_colHdrRow) - 1);
        } catch (InvalidSettingsException ise) {
            if (m_hasColHdr.isSelected()) {
                throw new InvalidSettingsException("Column Header Row: "
                        + ise.getMessage());
            }
            s.setColHdrRow(-1);
        }
        s.setUniquifyRowIDs(m_uniquifyRowIDs.isSelected());
        s.setHasRowHeaders(m_hasRowIDs.isSelected());
        try {
            s.setRowHdrCol(getColumnNumberFromTextField(m_rowIDCol) - 1);
        } catch (InvalidSettingsException ise) {
            if (m_hasRowIDs.isSelected()) {
                throw new InvalidSettingsException("Row Header Column Idx: "
                        + ise.getMessage());
            }
            s.setRowHdrCol(-1);
        }
        try {
            s.setFirstColumn(getColumnNumberFromTextField(m_firstCol) - 1);
        } catch (InvalidSettingsException ise) {
            if (!m_readAllData.isSelected()) {
                throw new InvalidSettingsException("First Column: "
                        + ise.getMessage());
            }
            s.setFirstColumn(-1);
        }
        try {
            s.setLastColumn(getColumnNumberFromTextField(m_lastCol) - 1);
        } catch (InvalidSettingsException ise) {
            // no last column specified
            s.setLastColumn(-1);
        }
        try {
            s.setFirstRow(getNumberFromTextField(m_firstRow) - 1);
        } catch (InvalidSettingsException ise) {
            if (!m_readAllData.isSelected()) {
                throw new InvalidSettingsException("First Row: "
                        + ise.getMessage());
            }
            s.setFirstRow(-1);
        }
        try {
            s.setLastRow(getNumberFromTextField(m_lastRow) - 1);
        } catch (InvalidSettingsException ise) {
            // no last row set
            s.setLastRow(-1);
        }

        // formula eval err handling
        s.setUseErrorPattern(m_formulaStringCell.isSelected());
        s.setErrorPattern(m_formulaErrPattern.getText());

        return s;
    }

    /**
     * Creates an int from the specified text field. Throws a ISE if the entered
     * value is empty, is not a number or zero or negative.
     */
    private int getNumberFromTextField(final JTextField t)
            throws InvalidSettingsException {
        String input = t.getText();
        if (input == null || input.isEmpty()) {
            throw new InvalidSettingsException("please enter a number.");
        }
        int i;
        try {
            i = Integer.parseInt(input);
        } catch (NumberFormatException nfe) {
            throw new InvalidSettingsException("not a valid integer number.");
        }
        if (i <= 0) {
            throw new InvalidSettingsException(
                    "number must be larger than zero.");
        }
        return i;
    }

    /**
     * Creates an int from the specified text field. It accepts numbers between
     * 1 and 256 (incl.) or XL column headers (starting at 'A', 'B', ... 'Z',
     * 'AA', etc.) Throws a ISE if the entered value is not valid.
     */
    private int getColumnNumberFromTextField(final JTextField t)
            throws InvalidSettingsException {
        String input = t.getText();
        if (input == null || input.isEmpty()) {
            throw new InvalidSettingsException("please enter a column number.");
        }
        int i;
        try {
            i = Integer.parseInt(input);
        } catch (NumberFormatException nfe) {
            try {
                i = ClearQuestTable.getColumnIndex(input.toUpperCase());
                // we want a number here (not an index)...
                i++;
            } catch (IllegalArgumentException iae) {
                throw new InvalidSettingsException(
                        "not a valid column number (enter a number or "
                                + "'A', 'B', ..., 'Z', 'AA', etc.)");
            }
        }
        if (i <= 0) {
            throw new InvalidSettingsException(
                    "column number must be larger than zero");
        }
        return i;
    }

    private void transferSettingsIntoComponents(final ClearQuestUserSettings s) {

        m_fileName.setSelectedFile(s.getFileLocation());

        m_skipEmptyCols.setSelected(s.getSkipEmptyColumns());
        m_skipEmptyRows.setSelected(s.getSkipEmptyRows());
        m_readAllData.setSelected(s.getReadAllData());

        // dialog shows numbers - internally we use indices
        m_hasColHdr.setSelected(s.getHasColHeaders());
        m_colHdrRow.setText("" + (s.getColHdrRow() + 1));
        m_hasRowIDs.setSelected(s.getHasRowHeaders());
        m_uniquifyRowIDs.setSelected(s.getUniquifyRowIDs());

        int val;
        val = s.getRowHdrCol(); // getColLabel wants an index
        if (val >= 0) {
            m_rowIDCol.setText(ClearQuestTable.getColLabel(val));
        } else {
            m_rowIDCol.setText("A");
        }
        val = s.getFirstColumn(); // getColLabel wants an index
        if (val >= 0) {
            m_firstCol.setText(ClearQuestTable.getColLabel(val));
        } else {
            m_firstCol.setText("A");
        }
        val = s.getLastColumn() + 1;
        if (val >= 1) {
            m_lastCol.setText(ClearQuestTable.getColLabel(val));
        } else {
            m_lastCol.setText("");
        }
        val = s.getFirstRow() + 1;
        if (val >= 1) {
            m_firstRow.setText("" + val);
        } else {
            m_firstRow.setText("1");
        }
        val = s.getLastRow() + 1;
        if (val >= 1) {
            m_lastRow.setText("" + val);
        } else {
            m_lastRow.setText("");
        }
        // formula error handling
        m_formulaStringCell.setSelected(s.getUseErrorPattern());
        m_formulaMissCell.setSelected(!s.getUseErrorPattern());
        m_formulaErrPattern.setText(s.getErrorPattern());
        m_formulaErrPattern.setEnabled(s.getUseErrorPattern());

        // clear sheet names
        m_sheetName.setModel(new DefaultComboBoxModel());
        // set new sheet names
        updateSheetListAndSelect(s.getSheetName());
        // set the en/disable state
        checkBoxChanged();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings)
            throws InvalidSettingsException {
        // we need at least a filename and sheet name
        String file = m_fileName.getSelectedFile();
        if (file == null || file.isEmpty()) {
            throw new InvalidSettingsException(
                    "Please select a file to read from.");
        }
        String sheet = (String)m_sheetName.getSelectedItem();
        if (sheet == SCANNING) {
            throw new InvalidSettingsException(
                    "Please wait until the file scanning "
                            + "finishes and select a worksheet.");
        }
        if (sheet == null || sheet.isEmpty()) {
            throw new InvalidSettingsException("Please select a worksheet.");
        }
        ClearQuestUserSettings s = createSettingsFromComponents();
        String errMsg = s.getStatus(true);
        if (errMsg != null) {
            throw new InvalidSettingsException(errMsg);
        }
        if (!m_previewMsg.getText().isEmpty()) {
            throw new InvalidSettingsException(m_previewMsg.getText());
        }
        s.save(settings);
        DataTable preview = m_previewDataTable;
        if (preview != null) {
            // if we have a preview table, store the DTS with the settings.
            // This is a hack around to avoid long configure times.
            // Causes the node's execute method to issue a bad warning, if the
            // file content changes between closing the dialog and execute()
            settings.addString(HPQC_CFG_ID_FOR_TABLESPEC, s.getID());
            Config subConf = settings.addConfig(HPQC_CFG_TABLESPEC);
            preview.getDataTableSpec().save(subConf);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadSettingsFrom(final NodeSettingsRO settings,
            final DataTableSpec[] specs) throws NotConfigurableException {
        m_loading.set(true);
        clearTableViews();
        try {
        	ClearQuestUserSettings s;
            try {
                s = ClearQuestUserSettings.load(settings);
            } catch (InvalidSettingsException e) {
                s = new ClearQuestUserSettings();
            }
            // Get the workbook when dialog is opened
            refreshWorkbook(s.getFileLocation());
            transferSettingsIntoComponents(s);
        } finally {
            m_loading.set(false);
        }
        // now refresh preview tables
        updateFileTable();
        updatePreviewTable();
    }

    /**
     * Sets first and last column/row parameters in the passed settings object.
     * File name and sheet index must be set.
     *
     * @param fileName file to read
     * @param sheetName sheet in that file
     * @return settings for the preview table
     * @throws InvalidSettingsException if file name or sheet index is not
     *             valid.
     * @throws FileNotFoundException if the file is not there
     * @throws IOException if an I/O Error occurred
     * @throws InvalidFormatException
     */
    private static ClearQuestUserSettings createFileviewSettings(
            final String fileName, final String sheetName, final Workbook wb)
            throws InvalidSettingsException, FileNotFoundException,
            IOException, InvalidFormatException {
        if (fileName == null || fileName.isEmpty()) {
            throw new NullPointerException("File location must be set.");
        }
        if (sheetName == null || sheetName.isEmpty()) {
            throw new InvalidSettingsException("Sheet name must be set.");

        }

        ClearQuestUserSettings result = new ClearQuestUserSettings();
        result.setFileLocation(fileName);
        result.setSheetName(sheetName);
        result.setHasColHeaders(false);
        result.setHasRowHeaders(false);
        result.setSkipEmptyColumns(false);
        result.setSkipEmptyRows(false);
        result.setSkipHiddenColumns(false);
        result.setKeepXLNames(true);
        result.setReadAllData(true);

        ClearQuestTableSettings tableSettings = new ClearQuestTableSettings(result, wb);

        // lets display at least 10 columns
        int colNum =
                tableSettings.getLastColumn() - tableSettings.getFirstColumn()
                        + 1;
        if (colNum < 10) {
            result.setReadAllData(false);
            int incr = 10 - colNum;
            if (tableSettings.getFirstColumn() <= incr) {
                incr -= tableSettings.getFirstColumn();
                result.setFirstColumn(0);
            } else {
                incr -= incr / 2;
                result.setFirstColumn(tableSettings.getFirstColumn() - incr / 2);
            }
            result.setLastColumn(tableSettings.getLastColumn() + incr);
            result.setFirstRow(tableSettings.getFirstRow());
            result.setLastRow(tableSettings.getLastRow());
        }
        return result;
    }

    private void addFocusLostListener(final JTextField field) {
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(final FocusEvent e) {
                invalidatePreviewTable();
            }

            @Override
            public void focusGained(final FocusEvent e) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void refreshWorkbook(final String path) {
        if (path == null) {
            m_workbook = null;
            m_workbookPath = null;
        } else if (!path.equals(m_workbookPath)) {
            m_workbook = ClearQuestTableSettings.getWorkbook(path);
            m_workbookPath = path;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClose() {
        // Remove references to XLSTable, that holds a reference to the workbook
        clearTableViews();
        // Remove own reference to the workbook
        m_workbook = null;
        m_workbookPath = null;
        // Now the garbage collector should be able to collect the workbook object
        super.onClose();
    }

}
