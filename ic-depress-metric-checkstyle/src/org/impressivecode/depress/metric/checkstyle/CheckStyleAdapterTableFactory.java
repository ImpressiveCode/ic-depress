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
package org.impressivecode.depress.metric.checkstyle;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.StringCell;

/**
 * 
 * @author Tomasz Banach
 * @author Łukasz Waga
 * @author Monika Pruszkowska
 * 
 */
public class CheckStyleAdapterTableFactory {

    private static final String SOURCE_PLACE = "SourcePlace";
    private static final String MESSAGE_TEXT = "MessageText";
    private static final String SEVERITY_TYPE = "SeverityType";
    private static final String LINE_NUMBER = "LineNumber";
    private static final String COLUMN_NUMBER = "ColumnNumber";
    private static final String CLASS = "Class";


    private CheckStyleAdapterTableFactory() {

    }

    public static DataTableSpec[] createTableSpec() {
        return new DataTableSpec[] { createDataColumnSpec() };
    }

    public static DataTableSpec createDataColumnSpec() {
        DataColumnSpec[] allColSpecs = { 
                new DataColumnSpecCreator(CLASS, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(LINE_NUMBER, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(COLUMN_NUMBER, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(SEVERITY_TYPE, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(MESSAGE_TEXT, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(SOURCE_PLACE, StringCell.TYPE).createSpec(),
        };
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        return outputSpec;
    }
}
