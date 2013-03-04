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

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.StringCell;

public class GitTableFactory {
    
    private final static int COLUMNS = 8;
    private final static String CLASSNAME = "class";
    private final static String MARKER = "marker";
    private final static String AUTHOR = "author";
    private final static String ACTION = "action";
    private final static String MESSAGE = "message";
    private final static String PATHNAME = "path";
    private final static String DATE = "date";
    private final static String UID = "UID";
    
    static DataTableSpec createTable(){
        DataColumnSpec[] allColSpecs = new DataColumnSpec[COLUMNS];
        allColSpecs[0] = new DataColumnSpecCreator(CLASSNAME, StringCell.TYPE).createSpec();
        allColSpecs[1] = new DataColumnSpecCreator(MARKER, StringCell.TYPE).createSpec();
        allColSpecs[2] = new DataColumnSpecCreator(AUTHOR, StringCell.TYPE).createSpec();
        allColSpecs[3] = new DataColumnSpecCreator(ACTION, StringCell.TYPE).createSpec();
        allColSpecs[4] = new DataColumnSpecCreator(MESSAGE, StringCell.TYPE).createSpec();
        allColSpecs[5] = new DataColumnSpecCreator(PATHNAME, StringCell.TYPE).createSpec();
        allColSpecs[6] = new DataColumnSpecCreator(DATE, StringCell.TYPE).createSpec();
        allColSpecs[7] = new DataColumnSpecCreator(UID, StringCell.TYPE).createSpec();
        
        return new DataTableSpec(allColSpecs);
    }
    
}
