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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.poi.ss.usermodel.Workbook;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeCreationContext;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 *
 * @author Peter Ohl, KNIME.com, Zurich, Switzerland
 */
public class ClearQuestReaderNodeModel extends NodeModel {

    private static final NodeLogger LOGGER = NodeLogger
            .getLogger(ClearQuestReaderNodeModel.class);

    private ClearQuestUserSettings m_settings = new ClearQuestUserSettings();

    private DataTableSpec m_dts = null;

    private String m_dtsSettingsID = null;

    /**
     *
     */
    public ClearQuestReaderNodeModel() {
        super(0, 1);
    }

    ClearQuestReaderNodeModel(final NodeCreationContext context) {
        this();
        m_settings.setFileLocation(context.getUrl().toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File nodeInternDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        /*
         * This is a special "deal" for the reader: The reader, if previously
         * executed, has data at it's output - even if the file that was read
         * doesn't exist anymore. In order to warn the user that the data cannot
         * be recreated we check here if the file exists and set a warning
         * message if it doesn't.
         */
        String fName = m_settings.getFileLocation();
        if (fName == null || fName.isEmpty()) {
            return;
        }

        try {
            new URL(fName);
            // don't check URLs - don't open a stream.
            return;
        } catch (MalformedURLException mue) {
            // continue on a file
        }
        File location = new File(fName);

        if (!location.canRead() || location.isDirectory()) {
            setWarningMessage("The file '" + location.getAbsolutePath()
                    + "' can't be accessed anymore!");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        m_settings = ClearQuestUserSettings.load(settings);
        try {
            String settingsID = settings.getString(
            		ClearQuestReaderNodeDialog.HPQC_CFG_ID_FOR_TABLESPEC);
            if (!m_settings.getID().equals(settingsID)) {
                throw new InvalidSettingsException("IDs don't match");
            }
            NodeSettingsRO dtsConfig =
                    settings.getNodeSettings(ClearQuestReaderNodeDialog.HPQC_CFG_TABLESPEC);
            m_dts = DataTableSpec.load(dtsConfig);
            m_dtsSettingsID = settingsID;
        } catch (InvalidSettingsException ise) {
            LOGGER.debug("No DTS saved in settings");
            // it's optional - if it's not saved we create it later
            m_dts = null;
            m_dtsSettingsID = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File nodeInternDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        if (m_settings != null) {
            m_settings.save(settings);
            if (m_dts != null) {
                settings.addString(ClearQuestReaderNodeDialog.HPQC_CFG_ID_FOR_TABLESPEC,
                        m_dtsSettingsID);
                m_dts.save(settings
                        .addConfig(ClearQuestReaderNodeDialog.HPQC_CFG_TABLESPEC));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	ClearQuestUserSettings.load(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {

        if (m_settings == null) {
            throw new InvalidSettingsException("Node not configured.");
        }

        String errMsg = m_settings.getStatus(true);
        if (errMsg != null) {
            throw new InvalidSettingsException(errMsg);
        }

        // make sure the DTS still fits the settings
        if (!m_settings.getID().equals(m_dtsSettingsID)) {
            m_dts = null;
        }
        if (m_dts == null) {
            // this could take a while.
            LOGGER.debug("Building DTS during configure...");
            ClearQuestTableSettings s;
            try {
                // Configure is an isolated call so do not keep the workbook in memory
                Workbook wb = ClearQuestTableSettings.getWorkbook(m_settings.getFileLocation());
                s = new ClearQuestTableSettings(m_settings, wb);
            } catch (Exception e) {
                String execMsg = e.getMessage();
                if (execMsg == null) {
                    execMsg = e.getClass().getSimpleName();
                }
                throw new InvalidSettingsException(execMsg);
            }
            m_dts = s.getDataTableSpec();
            m_dtsSettingsID = m_settings.getID();
        }
        return new DataTableSpec[]{m_dts};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
        // Execute is an isolated call so do not keep the workbook in memory
        Workbook wb = ClearQuestTableSettings.getWorkbook(m_settings.getFileLocation());
        ClearQuestTable table = new ClearQuestTable(m_settings, wb);
        return new BufferedDataTable[]{exec
                .createBufferedDataTable(table, exec)};

    }

}
