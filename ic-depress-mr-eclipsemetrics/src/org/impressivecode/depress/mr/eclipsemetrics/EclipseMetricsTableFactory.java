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
package org.impressivecode.depress.mr.eclipsemetrics;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DoubleCell;

/**
 * 
 * @author Mateusz Kutyba, Wroclaw University of Technology
 * 
 */
public class EclipseMetricsTableFactory {

    private static final String NORM = "NumberOfOverriddenMethods";
    private static final String NOF = "NumberOfAttributes";
    private static final String NSC = "NumberOfChildren";
    private static final String NOM = "NumberOfMethods";
    private static final String DIT = "DepthOfInheritanceTree";
    private static final String LCOM = "LackOfCohesionOfMethods";
    private static final String NSM = "NumberOfStaticMethods";
    private static final String SIX = "SpecializationIndex";
    private static final String WMC = "WeightedMethodsPerClass";
    private static final String NSF = "NumberOfStaticAttributes";

    private static final String MLOC = "MethodLinesOfCode";
    private static final String NBD = "NestedBlockDepth";
    private static final String VG = "McCabeCyclomaticComplexity";
    private static final String PAR = "NumberOfParameters";

    private EclipseMetricsTableFactory() {

    }

    static DataTableSpec[] createTableSpec() {
        return new DataTableSpec[] { createDataColumnSpec(), createDataColumnSpecMethodLevel() };
    }

    public static DataTableSpec createDataColumnSpec() {
        DataColumnSpec[] allColSpecs = { new DataColumnSpecCreator(NORM, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(NOF, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(NSC, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(NOM, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(DIT, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(LCOM, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(NSM, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(SIX, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(WMC, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(NSF, DoubleCell.TYPE).createSpec() };
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        return outputSpec;
    }

    public static DataTableSpec createDataColumnSpecMethodLevel() {
        DataColumnSpec[] allColSpecs = { new DataColumnSpecCreator(MLOC, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(NBD, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(VG, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(PAR, DoubleCell.TYPE).createSpec() };
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        return outputSpec;
    }
}
