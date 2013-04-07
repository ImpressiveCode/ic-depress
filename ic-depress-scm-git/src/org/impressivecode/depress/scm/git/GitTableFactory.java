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

package org.impressivecode.depress.scm.git;

/*
 *
 * @author Tomasz Kuzemko
 * @author Sławomir Kapłoński
 *
 */

import org.impressivecode.depress.scm.SCMAdapterTableFactory;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.collection.CollectionCellFactory;
import org.knime.core.data.collection.SetCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;

import java.util.HashSet;
import java.util.Set;

public class GitTableFactory extends SCMAdapterTableFactory {
    
    private final static int COLUMNS = 5;
    private final static String ACTION_COLNAME  = "Action";
    private final static String MESSAGE_COLNAME = "Message";
    private final static String PATH_COLNAME    = "Path";
    private final static String DATE_COLNAME    = "Date";
    private final static String UID_COLNAME     = "Commit ID";
    
    public static DataTableSpec createDataColumnSpec() {
        DataTableSpec baseSpec = SCMAdapterTableFactory.createDataColumnSpec();
        DataColumnSpec[] allColSpecs = new DataColumnSpec[COLUMNS + baseSpec.getNumColumns()];

        int i = 0;
        for (; i < baseSpec.getNumColumns(); i++) {
            allColSpecs[i] = baseSpec.getColumnSpec(i);
        }
        
        allColSpecs[i++] = new DataColumnSpecCreator(ACTION_COLNAME, StringCell.TYPE).createSpec();
        allColSpecs[i++] = new DataColumnSpecCreator(MESSAGE_COLNAME, StringCell.TYPE).createSpec();
        allColSpecs[i++] = new DataColumnSpecCreator(PATH_COLNAME, StringCell.TYPE).createSpec();
        allColSpecs[i++] = new DataColumnSpecCreator(DATE_COLNAME, StringCell.TYPE).createSpec();
        allColSpecs[i++] = new DataColumnSpecCreator(UID_COLNAME, StringCell.TYPE).createSpec();
        
        return new DataTableSpec(allColSpecs);
    }
    
    static DataRow createTableRow(final String className, final Set<String> markers, final String author, final String operation, 
            final String message, final String path, final String commitDate, final String uid, final String commitID) {
        
        DataCell[] cells = new DataCell[8];
        cells[0] = prepareStringCell(className);
        cells[1] = prepareSetCell(markers);
        cells[2] = prepareStringCell(author);
        cells[3] = prepareStringCell(operation);
        cells[4] = prepareStringCell(message);
        cells[5] = prepareStringCell(path);
        cells[6] = prepareStringCell(commitDate);
        cells[7] = prepareStringCell(commitID);
        DataRow row = new DefaultRow(uid, cells);
        return row;
    }
    
    private static StringCell prepareStringCell(String value){
        return new StringCell(value);
    }
    
    private static SetCell prepareSetCell(Set<String> values){
        Set<StringCell> cellValues = new HashSet<StringCell>();
        for (String value : values){
            cellValues.add(new StringCell(value));
        }
        return CollectionCellFactory.createSetCell(cellValues);
    }
}
