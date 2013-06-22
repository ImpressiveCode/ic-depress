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
package org.impressivecode.depress.scm;

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
public class SCMInputTransformer implements InputTransformer<SCMDataType> {

    private final DataTableSpec minimalTableSpec;

    public SCMInputTransformer(final DataTableSpec tableSpec) {
        checkNotNull(tableSpec, "DataTableSpec hat to be set");
        this.minimalTableSpec = tableSpec;
    }

    @Override
    public List<SCMDataType> transform(final DataTable inTable) {
        checkNotNull(inTable, "InTable has to be set");
        List<SCMDataType> scmData = Lists.newArrayListWithExpectedSize(1000);
        RowIterator iterator = inTable.iterator();
        while (iterator.hasNext()) {
            scmData.add(scm(iterator.next()));
        }
        return scmData;
    }

    private SCMDataType scm(final DataRow row) {
        TableCellReader reader = new TableCellReader(minimalTableSpec, row);
        SCMDataType scm = new SCMDataType();
        scm.setResourceName(reader.stringOptional(RESOURCE_NAME));
        scm.setMarkers(reader.stringSetOptional(SCMAdapterTableFactory.MARKER));
        return scm;
    }

    @Override
    public void validate(final DataTableSpec spec) throws InvalidSettingsException {
        checkNotNull(spec, "DataTableSpec hat to be set");

        Set<String> missing = DataTableSpecUtils.findMissingColumnSubset(spec, this.minimalTableSpec);
        if (!missing.isEmpty()) {
            throw new InvalidSettingsException("History data table does not contain required columns. Missing: "
                    + Iterables.toString(missing));
        }
    }
}
