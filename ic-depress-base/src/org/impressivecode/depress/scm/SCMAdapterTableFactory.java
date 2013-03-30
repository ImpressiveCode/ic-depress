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

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.collection.SetCell;
import org.knime.core.data.def.StringCell;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class SCMAdapterTableFactory {
    public static final String MARKER = "Marker";
    public static final String AUTHOR_COLNAME = "Author";
    public static final String RESOURCE_NAME = "Class";

    public static DataTableSpec createDataColumnSpec() {
        DataColumnSpec[] allColSpecs = new DataColumnSpec[3];
        allColSpecs[0] = new DataColumnSpecCreator(RESOURCE_NAME, StringCell.TYPE).createSpec();
        allColSpecs[1] = new DataColumnSpecCreator(MARKER, SetCell.getCollectionType(StringCell.TYPE)).createSpec();
        allColSpecs[2] = new DataColumnSpecCreator(AUTHOR_COLNAME, StringCell.TYPE).createSpec();
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        //TODO add missing columns and improve tests
        return outputSpec;
    }
}
