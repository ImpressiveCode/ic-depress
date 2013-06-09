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
 along with this program.  If not, see <http://www.gnu.org/licenses/
>.
 */

package org.impressivecode.depress.data.anonymisation;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.util.filter.NameFilterConfiguration.FilterResult;
import org.knime.core.node.util.filter.column.DataColumnSpecFilterConfiguration;

import com.google.common.base.Preconditions;

/**
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public abstract class CryptoNodeModel extends NodeModel {

    private final String configString;

    private DataColumnSpecFilterConfiguration configuration;

    protected CryptoNodeModel(final String configString) {
        super(1, 1);
        this.configString = configString;
    }

    @Override
    protected void reset() {
        // no op
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] data, final ExecutionContext exec) throws Exception {
        // TODO add validation before of column type before execution
        ColumnCryptoTransformer encryptor = createTransformer(data[0].getDataTableSpec());
        BufferedDataTable out = encryptor.transform(data[0], exec);
        return new BufferedDataTable[] { out };
    }

    @Override
    protected void saveInternals(final File nodeInternDir, final ExecutionMonitor exec) throws IOException,
    CanceledExecutionException {
        // no op
    }

    @Override
    protected void loadInternals(final File nodeInternDir, final ExecutionMonitor exec) throws IOException,
    CanceledExecutionException {
        // no op
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        return inSpecs;
    }

    private ColumnCryptoTransformer createTransformer(final DataTableSpec spec) {
        Preconditions.checkNotNull(configuration, "Configuration has to be initialized first");
        final FilterResult filter = configuration.applyTo(spec);
        final String[] transforms = filter.getIncludes();
        final ColumnCryptoTransformer transfomer = transformer(spec, transforms);
        return transfomer;
    }

    protected abstract ColumnCryptoTransformer transformer(final DataTableSpec spec, final String[] transforms);

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        if (configuration == null) {
            configuration = ConfigurationFactory.configuration(configString);
        }
        configuration.saveConfiguration(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        if (configuration == null) {
            configuration = ConfigurationFactory.configuration(configString);
        }
        configuration.loadConfigurationInModel(settings);
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        DataColumnSpecFilterConfiguration config = ConfigurationFactory.configuration(configString);
        config.loadConfigurationInModel(settings);
    }
}
