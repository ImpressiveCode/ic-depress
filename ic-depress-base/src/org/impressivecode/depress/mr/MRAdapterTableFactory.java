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
	package org.impressivecode.depress.mr;
	import java.math.BigDecimal;

	import org.knime.core.data.DataCell;
	import org.knime.core.data.DataColumnSpec;
	import org.knime.core.data.DataColumnSpecCreator;
	import org.knime.core.data.DataRow;
	import org.knime.core.data.DataTableSpec;
	import org.knime.core.data.def.DefaultRow;
	import org.knime.core.data.def.DoubleCell;

	/**
	 * 
	 * @author Marek Majchrzak, ImpressiveCode
	 * @author Zuzanna Pacholczyk, Capgemini Poland
	 * 
	 */



public class MRAdapterTableFactory {

    private static final String MUTANTS_SCORE = "MutationScoreIndicator";

    public static DataTableSpec[] createTableSpec() {
        return new DataTableSpec[] { createDataColumnSpec() };
    }

    public static DataTableSpec createDataColumnSpec() {
        DataColumnSpec[] allColSpecs = new DataColumnSpec[1];
        allColSpecs[0] = new DataColumnSpecCreator(MUTANTS_SCORE, DoubleCell.TYPE).createSpec();
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        return outputSpec;
    }

    public static DataRow createTableRow(final String className, final BigDecimal score) {
        DataCell[] cells = new DataCell[1];
        cells[0] = getScoreCell(score);
        DataRow row = new DefaultRow(className, cells);
        return row;
    }

    private static DataCell getScoreCell(final BigDecimal score) {
        return new DoubleCell(score != null ? score.doubleValue() : 0);
    }


}
