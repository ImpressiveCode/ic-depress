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
import org.knime.core.data.date.DateAndTimeCell;
import org.knime.core.data.def.StringCell;

/**
 * @author Mateusz Kutyba, Wroclaw University of Technology
 */
public class GitOnlineAdapterExtendedTableFactory {

    private static final String METHOD_NAME = "MethodName";
    private static final String AUTHOR = "Author";
    private static final String MESSAGE = "Message";
    private static final String DATE = "Date";
    private static final String COMMIT_ID = "CommitID";

    private GitOnlineAdapterExtendedTableFactory() {
    }

    public static DataTableSpec[] createTableSpec() {
        return new DataTableSpec[] { createDataColumnSpec() };
    }

    public static DataTableSpec createDataColumnSpec() {
        DataColumnSpec[] allColSpecs = { new DataColumnSpecCreator(METHOD_NAME, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(AUTHOR, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(MESSAGE, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(DATE, DateAndTimeCell.TYPE).createSpec(),
                new DataColumnSpecCreator(COMMIT_ID, StringCell.TYPE).createSpec() };
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        return outputSpec;
    }

}
