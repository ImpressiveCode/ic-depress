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
package org.impressivecode.depress.mr.jacoco;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DoubleCell;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class JaCoCoAdapterTableFactory {

    private static final String METHOD_COVERAGE = "MethodCoverage";
    private static final String COMPLEXITY_COVERAGE = "ComplexityCoverage";
    private static final String BRANCHE_COVERAGE = "BrancheCoverage";
    private static final String INSTRUCTIONS_COVERAGE = "InstructionsCoverage";
    private static final String LINE_COVERAGE = "LineCoverage";
    private static final String CLASS_COVERAGE = "ClassCoverage";

    private JaCoCoAdapterTableFactory() {

    }

    public static DataTableSpec[] createTableSpec() {
        return new DataTableSpec[] { createDataColumnSpec() };
    }

    public static DataTableSpec createDataColumnSpec() {
        DataColumnSpec[] allColSpecs = { 
                new DataColumnSpecCreator(LINE_COVERAGE, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(INSTRUCTIONS_COVERAGE, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(BRANCHE_COVERAGE, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(COMPLEXITY_COVERAGE, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(METHOD_COVERAGE, DoubleCell.TYPE).createSpec(), 
                new DataColumnSpecCreator(CLASS_COVERAGE, DoubleCell.TYPE).createSpec()};
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        return outputSpec;
    }
}
