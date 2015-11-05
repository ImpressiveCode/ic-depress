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
package org.impressivecode.depress.data.anonymisation;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.util.filter.InputFilter;
import org.knime.core.node.util.filter.column.DataColumnSpecFilterConfiguration;

/**
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
final class ConfigurationFactory {
    private ConfigurationFactory() {
    }

    static DataColumnSpecFilterConfiguration configuration(final String cfg) {
        return new DataColumnSpecFilterConfiguration(cfg, new TypeColumnFilter());
    }

    static InputFilter<DataColumnSpec> filter() {
        return new TypeColumnFilter();
    }

    private static class TypeColumnFilter extends InputFilter<DataColumnSpec> {

        @Override
        public final boolean include(final DataColumnSpec cspec) {
            return checkStructure(cspec.getType());
        }

    }

    static boolean checkStructure(final DataType type) {
        return type.equals(StringCell.TYPE) || type.isCollectionType()
                && type.getCollectionElementType().equals(StringCell.TYPE);
    }

}
