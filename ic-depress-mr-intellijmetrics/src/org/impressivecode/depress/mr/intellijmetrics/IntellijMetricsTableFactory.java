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
package org.impressivecode.depress.mr.intellijmetrics;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DoubleCell;

/**
 *
 * @author Maciej Mickiewicz, Wroclaw University of Technology
 *
 */
public class IntellijMetricsTableFactory {

    private static final String TYPO_SEVERITY = "TYPO";
    private static final String INFO_SEVERITY = "INFO";
    private static final String SERVERPROBLEM_SEVERITY = "SERVER PROBLEM";
    private static final String WEAKWARNING_SEVERITY = "WEAK WARNING";
    private static final String WARNING_SEVERITY = "WARNING";
    private static final String ERROR_SEVERITY = "ERROR";

    private IntellijMetricsTableFactory() {

    }

    public static DataTableSpec[] createTableSpec() {
        return new DataTableSpec[] { createDataColumnSpec() };
    }

    public static DataTableSpec createDataColumnSpec() {
        DataColumnSpec[] allColSpecs = {
                new DataColumnSpecCreator(TYPO_SEVERITY, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(INFO_SEVERITY, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(SERVERPROBLEM_SEVERITY, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(WEAKWARNING_SEVERITY, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(WARNING_SEVERITY, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(ERROR_SEVERITY, DoubleCell.TYPE).createSpec()};
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        return outputSpec;
    }
}
