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
package org.impressivecode.depress.mg.estimationscoremetric;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.impressivecode.depress.common.OutputTransformer;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeLogger.LEVEL;
import org.impressivecode.depress.mg.estimationscoremetric.EstimationScoreMetricTableFactory;
/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class EstimationScoreMetricOutputTransformer implements OutputTransformer<EstimationScoreMetricType> {

    private static final NodeLogger LOGGER = NodeLogger.getLogger(EstimationScoreMetricOutputTransformer.class);
    private final DataTableSpec tableSpec;

    public EstimationScoreMetricOutputTransformer(final DataTableSpec tableSpec) {
        this.tableSpec = checkNotNull(tableSpec,"table specifikation can not be null.") ;
    }

    @Override
    public BufferedDataTable transform(final List<EstimationScoreMetricType> data, final ExecutionContext exec)
            throws CanceledExecutionException {
        checkNotNull(data,"Output data can not be null.") ;
        checkNotNull(exec,"ExecutionContext can not be null.") ;

        BufferedDataContainer container = createDataContainer(exec);
        for (EstimationScoreMetricType metric : data) {
        	
            progress(exec);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Transforming metric, class: " + metric.getClassName());
            }

            if (LOGGER.isEnabledFor(LEVEL.ALL)) {
                LOGGER.debug("Transforming metric:" + metric.toString());
            }
            
            DataRow row = EstimationScoreMetricTableFactory.createTableRow(metric);
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
