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
package org.impressivecode.depress.support.commonmarker;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.impressivecode.depress.scm.SCMAdapterTableFactory.RESOURCE_NAME;

import java.util.List;
import java.util.Set;

import org.impressivecode.depress.common.DataTableSpecUtils;
import org.impressivecode.depress.common.InputTransformer;
import org.impressivecode.depress.common.TableCellReader;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowIterator;
import org.knime.core.node.InvalidSettingsException;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class MarkerInputTransformer implements InputTransformer<MarkerDataType> {

    private DataTableSpec minimalTableSpec;
    private DataTableSpec inputTableSpec;

    public MarkerInputTransformer() {
    }

    @Override
    public List<MarkerDataType> transform(final DataTable inTable) {
        checkNotNull(this.minimalTableSpec, "Minimal DataTableSpec hat to be set");
        checkNotNull(this.inputTableSpec, "Input DataTableSpec hat to be set");
        checkNotNull(inTable, "InTable has to be set");
        List<MarkerDataType> markerData = Lists.newArrayListWithExpectedSize(1000);
        RowIterator iterator = inTable.iterator();
        while (iterator.hasNext()) {
            markerData.add(marker(iterator.next()));
        }
        return markerData;
    }

    private MarkerDataType marker(final DataRow row) {
        TableCellReader reader = new TableCellReader(this.inputTableSpec, row);
        MarkerDataType marker = new MarkerDataType();
        marker.setResourceName(reader.stringOptional(RESOURCE_NAME));
        marker.setMarkers(reader.stringSetOptional(MarkerAdapterTableFactory.MARKER));
        return marker;
    }

    @Override
    public InputTransformer<MarkerDataType> validate() throws InvalidSettingsException {
        checkNotNull(this.minimalTableSpec, "Minimal DataTableSpec hat to be set");
        checkNotNull(this.inputTableSpec, "Input DataTableSpec hat to be set");

        Set<String> missing = DataTableSpecUtils.findMissingColumnSubset(this.inputTableSpec, this.minimalTableSpec);
        if (!missing.isEmpty()) {
            throw new InvalidSettingsException("History data table does not contain required columns. Missing: "
                    + Iterables.toString(missing));
        }
        return this;
    }

    @Override
    public InputTransformer<MarkerDataType> setMinimalSpec(final DataTableSpec spec) {
        this.minimalTableSpec = spec;
        return this;
    }

    @Override
    public InputTransformer<MarkerDataType> setInputSpec(final DataTableSpec spec) {
        this.inputTableSpec = spec;
        return this;
    }
}
