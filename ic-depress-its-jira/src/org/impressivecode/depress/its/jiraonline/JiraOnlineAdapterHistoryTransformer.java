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
package org.impressivecode.depress.its.jiraonline;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.impressivecode.depress.its.jiraonline.JiraOnlineAdapterHistoryTableFactory.createTableRow;

import java.util.List;

import org.impressivecode.depress.common.OutputTransformer;
import org.impressivecode.depress.its.jiraonline.historymodel.JiraOnlineIssueChangeRowItem;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeLogger.LEVEL;

/**
 * 
 * @author Marcin Kunert, Wroclaw University of Technology
 * 
 */
public class JiraOnlineAdapterHistoryTransformer implements OutputTransformer<JiraOnlineIssueChangeRowItem> {

    private static final NodeLogger LOGGER = NodeLogger.getLogger(JiraOnlineAdapterHistoryTransformer.class);

    private final DataTableSpec tableSpec;

    public JiraOnlineAdapterHistoryTransformer(final DataTableSpec tableSpec) {
        checkNotNull(tableSpec, "table specifikation can not be null.");
        this.tableSpec = tableSpec;
    }

    @Override
    public BufferedDataTable transform(List<JiraOnlineIssueChangeRowItem> entries, ExecutionContext exec)
            throws CanceledExecutionException {
        BufferedDataContainer container = createDataContainer(exec);
        for (JiraOnlineIssueChangeRowItem entry : entries) {
            progress(exec);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Transforming issue entry, issueId: " + entry.getKey());
            }

            if (LOGGER.isEnabledFor(LEVEL.ALL)) {
                LOGGER.debug("Transforming issue entry:" + entry.toString());
            }
            DataRow row = createTableRow(entry);
            container.addRowToTable(row);
        }
        container.close();
        BufferedDataTable out = container.getTable();
        return out;
    }

    private BufferedDataContainer createDataContainer(final ExecutionContext exec) {
        return exec.createDataContainer(tableSpec);
    }

    private void progress(final ExecutionContext exec) throws CanceledExecutionException {
        exec.checkCanceled();
    }

}
