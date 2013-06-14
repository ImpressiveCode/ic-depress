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

package org.impressivecode.depress.scm.svn;

import static org.impressivecode.depress.scm.SCMAdapterTableFactory.createDataColumnSpec;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.impressivecode.depress.common.OutputTransformer;
import org.impressivecode.depress.scm.SCMAdapterTableFactory;
import org.impressivecode.depress.scm.SCMAdapterTransformer;
import org.impressivecode.depress.scm.SCMDataType;
import org.impressivecode.depress.scm.svn.SVNLogLoader.IReadProgressListener;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.google.common.collect.Lists;

public class SVNNodeModel extends NodeModel {

    private static final NodeLogger logger = NodeLogger.getLogger(SVNNodeModel.class);

    class ModelRowReader implements IReadProgressListener {

        private ExecutionContext exec;
        private List<SCMDataType> data;

        public ModelRowReader(ExecutionContext inExec) {
            exec = inExec;
            data = Lists.newLinkedList();
        }

        @Override
        public void onReadProgress(double inProgres, SCMDataType inRow) {
            if (inRow != null) {
                data.add(inRow);
            }
            exec.setProgress(inProgres);
        }

        public List<SCMDataType> getData() {
            return data;
        }

        @Override
        public void checkLoading() throws CanceledExecutionException {
            exec.checkCanceled();
        }
    }

    private ModelRowReader reader;
    private SVNLogLoader loader;

    /**
     * Constructor for the node model.
     */
    protected SVNNodeModel() {
        super(0, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        return new DataTableSpec[] { SCMAdapterTableFactory.createDataColumnSpec() };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {

        logger.warn(SVNLocale.iStartLoading());

        reader = new ModelRowReader(exec);

        loader = SVNLogLoader.create(SVNSettings.SVN_PATH.getStringValue());

        logger.warn(SVNLocale.iInitOnlineRepo(SVNSettings.SVN_PATH.getStringValue()));

        try {

            loader.load(SVNSettings.SVN_PATH.getStringValue(), SVNSettings.SVN_ISSUE_MARKER.getStringValue(),
                    SVNSettings.SVN_PACKAGE.getStringValue(), reader);

            OutputTransformer<SCMDataType> transformer = new SCMAdapterTransformer(createDataColumnSpec());

            BufferedDataTable out = transformer.transform(reader.getData(), exec);

            logger.warn(SVNLocale.iEndLoading());

            return new BufferedDataTable[] { out };

        } catch (CanceledExecutionException e) {
            logger.error(SVNLocale.iCancelLoading());
        } catch (Exception e) {
            logger.error(SVNLocale.iSVNInternalError(), e);
        }

        return new BufferedDataTable[] {};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        SVNSettings.loadSettingsFrom(settings);
        logger.warn(SVNLocale.iSettingsLoaded());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        SVNSettings.saveSettingsTo(settings);
        logger.warn(SVNLocale.iSettingsSaved());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        SVNSettings.validateSettings(settings);
    }

}
