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
package org.impressivecode.depress.metric.datasource;


import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.StringCell;



/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class DataSourceAdapterTableFactory {




    private DataSourceAdapterTableFactory() {

    }
    
    public static DataTableSpec[] createTableSpec() {
        return new DataTableSpec[] { createDataColumnSpec() };
    }

    public static DataTableSpec createDataColumnSpec() {
    	DataColumnSpec[] temp  = new DataColumnSpec[9];
    	temp[0] = new DataColumnSpecCreator("Class Name", StringCell.TYPE).createSpec();
    	temp[1] = new DataColumnSpecCreator("Method Name", StringCell.TYPE).createSpec();
    	temp[2] = new DataColumnSpecCreator("Public", BooleanCell.TYPE).createSpec();
    	temp[3] = new DataColumnSpecCreator("Protected", BooleanCell.TYPE).createSpec();
    	temp[4] = new DataColumnSpecCreator("Private", BooleanCell.TYPE).createSpec();
    	temp[5] = new DataColumnSpecCreator("Static", BooleanCell.TYPE).createSpec();
    	temp[6] = new DataColumnSpecCreator("Final", BooleanCell.TYPE).createSpec();
    	temp[7] = new DataColumnSpecCreator("Abstract", BooleanCell.TYPE).createSpec();
    	temp[8] = new DataColumnSpecCreator("Location", StringCell.TYPE).createSpec();
    	DataTableSpec output = new DataTableSpec(temp);
    	return output;
    }
}
