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

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.util.filter.column.DataColumnSpecFilterConfiguration;
import org.knime.core.node.util.filter.column.DataColumnSpecFilterPanel;

/**
 * @author Marek Majchrzak, ImpressiveCode
 * @author Maciej Borkowski, Capgemini Poland
 */
public class CryptoNodeDialog extends NodeDialogPane {
    private final DataColumnSpecFilterPanel filterPanel;

    CryptoNodeDialog(final String tabName) {
        filterPanel = new DataColumnSpecFilterPanel(true, ConfigurationFactory.filter());
        super.addTab(tabName, filterPanel);
    }

    @Override
    protected void loadSettingsFrom(final NodeSettingsRO settings, final DataTableSpec[] specs)
            throws NotConfigurableException {
        final DataTableSpec spec = specs[0];
        if (spec == null || spec.getNumColumns() == 0) {
            throw new NotConfigurableException("No columns available for " + "selection.");
        }

        DataColumnSpecFilterConfiguration config = ConfigurationFactory.configuration(CryptoNodeModel.CFG_KEY_FILTER);
        config.loadConfigurationInDialog(settings, addRowId(specs[0]));
        filterPanel.loadConfiguration(config, addRowId(specs[0]));
    }

    private DataTableSpec addRowId(final DataTableSpec dataTableSpec) {
        DataTableSpec rowIdSpec = new DataTableSpec(new String[] { "Row ID" }, new DataType[] { StringCell.TYPE });
        return new DataTableSpec(dataTableSpec, rowIdSpec);
    }

    @Override
    public void saveSettingsTo(final NodeSettingsWO settings) throws InvalidSettingsException {
        DataColumnSpecFilterConfiguration config = ConfigurationFactory.configuration(CryptoNodeModel.CFG_KEY_FILTER);
        filterPanel.saveConfiguration(config);
        config.saveConfiguration(settings);
    }
}
