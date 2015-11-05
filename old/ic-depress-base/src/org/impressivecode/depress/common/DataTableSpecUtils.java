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
package org.impressivecode.depress.common;

import java.util.Iterator;
import java.util.Set;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;

import com.google.common.collect.Sets;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class DataTableSpecUtils {
    public static Set<String> findMissingColumnSubset(final DataTableSpec dataTableSpec,
            final DataTableSpec subsetDataTableSpec) {
        Set<String> missing = Sets.newHashSet();
        Iterator<DataColumnSpec> iterator = subsetDataTableSpec.iterator();
        while (iterator.hasNext()) {
            DataColumnSpec spec = iterator.next();
            boolean hasColumn = spec.equalStructure(dataTableSpec.getColumnSpec(spec.getName()));
            if (!hasColumn) {
                missing.add(spec.getName() + ":" + spec.getType());
            }
        }

        return missing;
    }
}
