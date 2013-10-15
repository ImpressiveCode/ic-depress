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
package org.impressivecode.depress.support.sourcecrawler;

import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;

/**
 * 
 * @author Pawel Nosal, ImpressiveCode
 * 
 */
public class CrawlerAdapterTableFactory {

    private CrawlerAdapterTableFactory() {

    }

    public static DataTableSpec[] createTableSpec() {
        return new DataTableSpec[] { createDataColumnSpec() };
    }

    public static DataTableSpec createDataColumnSpec() {
        List<DataColumnSpec> allSpec = ColumnNames.getAllSpec();
        DataColumnSpec[] dataColumnSpecs = new DataColumnSpec[ColumnNames.getAllSpec().size()];
        dataColumnSpecs = allSpec.toArray(dataColumnSpecs);
        DataTableSpec outputSpec = new DataTableSpec(dataColumnSpecs);
        return outputSpec;
    }

    public static DataRow createTableRow(final String id, final String name, final String type, final boolean exception, final boolean inner,
            final boolean test, final String sourcePackage, final String path) {
        DataCell[] cells = new DataCell[7];
        prepareCells(name, type, exception, inner, test, sourcePackage, path, cells);
        DataRow row = new DefaultRow(id, cells);
        return row;
    }

    private static void prepareCells(final String name, final String type, final boolean exception, final boolean inner, final boolean test,
            final String sourcePackage, final String path, final DataCell[] cells) {
        cells[0] = new StringCell(sourcePackage + "." + name);
        cells[1] = new StringCell(Boolean.toString(exception));
        cells[2] = new StringCell(Boolean.toString(inner));
        cells[3] = new StringCell(Boolean.toString(test));
        cells[4] = new StringCell(type);
        cells[5] = new StringCell(sourcePackage);
        cells[6] = new StringCell(path);
    }

}
