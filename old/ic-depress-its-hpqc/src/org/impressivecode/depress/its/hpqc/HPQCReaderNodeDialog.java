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
package org.impressivecode.depress.its.hpqc;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.impressivecode.depress.its.ITSAdapterTableFactory;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettings;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.util.FilesHistoryPanel;
import org.knime.ext.poi.node.read2.XLSReaderNodeDialog;
import org.knime.ext.poi.node.read2.XLSTableSettings;

/**
 * The dialog to the HPQC reader.
 * 
 * @author Łukasz Leśniczek, Wrocław, Poland
 * @author Mariusz Mulka, Wrocław, Poland
 * @author Łukasz Modliński, Wrocław, Poland
 * 
 */
public class HPQCReaderNodeDialog extends XLSReaderNodeDialog {

    private static final String TAB_NAME = "HPQC Reader Settings";
    private static final String SECOND_TAB = "Mapping";
    private ArrayList<JComboBox<BasicComboBoxRenderer>> m_columnNames;
    private HPQCUserSettings m_settings;

    public HPQCReaderNodeDialog() {
        super();
        super.renameTab("XLS Reader Settings", TAB_NAME);
        Container hpTab = (Container) this.getTab(TAB_NAME);
        Container secondContainer = (Container) ((Container) ((Container) hpTab.getComponent(0)).getComponent(0))
                .getComponent(1);
        // hide component: Column names
        secondContainer.getComponent(1).setVisible(false);
        secondContainer.getComponent(2).setVisible(false);
        final JPanel mappingBox = new JPanel();
        mappingBox.setLayout(new BoxLayout(mappingBox, BoxLayout.Y_AXIS));
        mappingBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Map columns:"));
        mappingBox.add(getColumnMappingBox());
        JPanel mappingTab = new JPanel();
        mappingTab.setLayout(new BoxLayout(mappingTab, BoxLayout.Y_AXIS));
        mappingTab.add(mappingBox);

        // get tabbed pane
        final JTabbedPane pane = (JTabbedPane) this.getPanel().getComponent(1);
        pane.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                // TODO Auto-generated method stub
                if (pane.getSelectedIndex() == 1) {
                    try {
                        updateDataTableSpec();
                    } catch (Throwable t) {
                        NodeLogger.getLogger(HPQCReaderNodeDialog.class).debug("Error while accessing file", t);
                    }
                }
            }

        });

        addTab(SECOND_TAB, new JScrollPane(mappingTab));
    }

    private Component getColumnMappingBox() {
        // TODO Auto-generated method stub
        Box mappingBox = Box.createVerticalBox();
        mappingBox.add(Box.createHorizontalGlue());
        DataTableSpec dts = ITSAdapterTableFactory.createDataColumnSpec();
        String[] itsColumnNames = dts.getColumnNames();
        m_columnNames = new ArrayList<JComboBox<BasicComboBoxRenderer>>(itsColumnNames.length);
        for (String columnName : itsColumnNames) {
            Box singleColumnBox = Box.createHorizontalBox();
            JLabel columnLabel = new JLabel(String.format("Select mapping for %s:", columnName));
            if (columnName.equals(ITSAdapterTableFactory.CREATION_DATE)) {
                columnLabel.setText(String.format("<html>Select mapping for %s<font color='red'>*</font>:</html>",
                        columnName));
                columnLabel.setToolTipText("Missing or incorrect values will be replaced with current date");
            }
            columnLabel.setPreferredSize(new Dimension(220, 25));
            columnLabel.setMaximumSize(new Dimension(220, 25));
            singleColumnBox.add(columnLabel);
            m_columnNames.add(addColumnToMappingBox(singleColumnBox));
            mappingBox.add(singleColumnBox);
        }

        return mappingBox;
    }

    @SuppressWarnings({ "serial", "unchecked" })
    private JComboBox<BasicComboBoxRenderer> addColumnToMappingBox(Box box) {
        box.add(Box.createHorizontalStrut(5));
        final JComboBox<BasicComboBoxRenderer> column = new JComboBox<BasicComboBoxRenderer>();
        column.setPreferredSize(new Dimension(220, 25));
        column.setMaximumSize(new Dimension(220, 25));
        column.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    sheetNameChanged(column);
                }
            }
        });
        column.setRenderer(new BasicComboBoxRenderer() {
            /**
             * {@inheritDoc}
             */
            @SuppressWarnings("rawtypes")
            @Override
            public Component getListCellRendererComponent(final JList list, final Object value, final int index,
                    final boolean isSelected, final boolean cellHasFocus) {
                if ((index > -1) && (value != null)) {
                    list.setToolTipText(value.toString());
                } else {
                    list.setToolTipText(null);
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }

            @Override
            public boolean equals(Object obj) {
                // TODO Auto-generated method stub
                return this.getText().equals(((BasicComboBoxRenderer) obj).getText());
            }
        });
        box.add(column);
        box.add(Box.createHorizontalGlue());
        return column;
    }

    private void sheetNameChanged(JComboBox<BasicComboBoxRenderer> column) {
        column.setToolTipText((String) column.getSelectedItem());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadSettingsFrom(final NodeSettingsRO settings, final DataTableSpec[] specs)
            throws NotConfigurableException {
        HPQCUserSettings hpSettings;
        try {
            hpSettings = HPQCUserSettings.load(settings);
        } catch (InvalidSettingsException e) {
            hpSettings = new HPQCUserSettings();
        }
        hpSettings.setHasColHeaders(true);
        NodeSettings clone = new NodeSettings("clone");
        hpSettings.save(clone);
        m_settings = hpSettings;
        super.loadSettingsFrom(clone, specs);
    }

    @Override
    protected void saveSettingsTo(NodeSettingsWO hpSettings) throws InvalidSettingsException {
        super.saveSettingsTo(hpSettings);
        String[] selectedColumns = new String[m_columnNames.size()];
        for (int i = 0; i < selectedColumns.length; i++) {
            selectedColumns[i] = String.valueOf(m_columnNames.get(i).getSelectedItem());
        }
        hpSettings.addStringArray(HPQCUserSettings.ITS_MAPPING, selectedColumns);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void updateDataTableSpec() throws FileNotFoundException, InvalidFormatException, InvalidSettingsException,
            IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = XLSReaderNodeDialog.class.getDeclaredField("m_fileName");
        field.setAccessible(true);
        FilesHistoryPanel file = (FilesHistoryPanel) field.get(this);
        if (!file.getSelectedFile().equals(m_settings.getFileLocation())
                || m_columnNames.get(0).getModel().getSize() == 0) {
            m_settings.setFileLocation(file.getSelectedFile());
            Workbook wb = XLSTableSettings.getWorkbook(m_settings.getFileLocation());
            XLSTableSettings tableSettings = new XLSTableSettings(m_settings, wb);
            DataTableSpec dts = tableSettings.getDataTableSpec();
            DefaultComboBoxModel model;
            for (int i = 0; i < m_columnNames.size(); i++) {
                model = new DefaultComboBoxModel(dts.getColumnNames());
                model.insertElementAt("", 0);
                model.setSelectedItem("");
                m_columnNames.get(i).setModel(model);
            }
        }
    }

    @Override
    protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs) throws NotConfigurableException {
        // TODO Auto-generated method stub
        HPQCUserSettings hpSettings;
        try {
            hpSettings = HPQCUserSettings.load(settings);
        } catch (InvalidSettingsException e) {
            hpSettings = new HPQCUserSettings();
        }
        hpSettings.setHasColHeaders(true);
        NodeSettings clone = new NodeSettings("clone");
        hpSettings.save(clone);
        m_settings = hpSettings;
        try {
            updateDataTableSpec();
        } catch (Throwable t) {
            NodeLogger.getLogger(HPQCReaderNodeDialog.class).debug("Error while loading settings", t);
        }
        if (m_settings.getMapping() != null) {
            for (int i = 0; i < m_columnNames.size(); i++) {
                m_columnNames.get(i).getModel().setSelectedItem(m_settings.getMapping()[i]);
            }
        }
        super.loadSettingsFrom(clone, specs);
    }
}
