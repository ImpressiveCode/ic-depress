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
	package org.impressivecode.depress.mr.googlemetrics;
	import static org.impressivecode.depress.common.Cells.stringOrMissingCell;
	import static org.impressivecode.depress.common.Cells.doubleOrMissingCell;

import java.util.List;

	import org.knime.core.data.DataCell;
	import org.knime.core.data.DataColumnSpec;
	import org.knime.core.data.DataColumnSpecCreator;
	import org.knime.core.data.DataRow;
	import org.knime.core.data.DataTableSpec;
	import org.knime.core.data.def.DefaultRow;
	import org.knime.core.data.def.DoubleCell;
	import org.knime.core.data.def.StringCell;
import org.impressivecode.depress.mr.googlemetrics.GoogleMetricsXmlResult.MetricResultScope.MetricResult;

	/**
	 * 
	 * @author Zuzanna Pacholczyk, Capgemini Poland
	 * 
	 */



public class GoogleMetricsTableFactory {

    private static final String PARAMETERS_NUMBER = "AverageNumberOfParameters";
    private static final String CONSTRUCTORS_PER_TYPE_NUMBER = "AverageNumberOfConstructorsPerType";
    private static final String COMMENT_RATION = "CommentRatio";
    private static final String METHODS_NUMBER = "NumberOfMethods";
    private static final String SEMICOLONS_NUMBER = "NumberOfSemicolons";
    private static final String METHODS_PER_TYPE_NUMBER = "AverageNumberOfMethodsPerType";
    private static final String CONSTRUCTORS_NUMBER = "AverageNumberOfConstructors";
    private static final String ABSTRACTNESS = "Abstractness";
    private static final String CODE_LINES_NUMBER = "LinesOfCode";
    private static final String CHARACTERS_NUMBER = "NumberOfCharacters";
    private static final String COMMENTS_NUMBER = "NumberOfComments";
    private static final String LINES_PER_METHOD_NUMBER = "AverageNumberOfCodePerMethod";
    private static final String LINES_NUMBER = "AverageNumberOfLines";
    private static final String FIELDS_NUMBER = "NumberOfFields";
    private static final String TYPES_NUMBER = "NumberOfTypes";
    private static final String CYCLOMATIC_COMPLEXITY = "AverageCyclomaticComplexity";
    private static final String FIELDS_PER_TYPE = "AverageNumberOfFieldsPerType";
    private static final String WEIGHTED_METHODS = "WeightedMethods";
    private static final String BLOCK_DEPTH = "AverageBlockDepth";
    private static final String COUPLINGS = "EfferentCouplings";

    public static DataTableSpec[] createTableSpec() {
        return new DataTableSpec[] { createDataColumnSpec() };
    }

    public static DataTableSpec createDataColumnSpec() {
        DataColumnSpec[] allColSpecs = { new DataColumnSpecCreator(PARAMETERS_NUMBER, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(CONSTRUCTORS_PER_TYPE_NUMBER, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(COMMENT_RATION, StringCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(METHODS_NUMBER, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(SEMICOLONS_NUMBER, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(METHODS_PER_TYPE_NUMBER, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(CONSTRUCTORS_NUMBER, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(ABSTRACTNESS, StringCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(CODE_LINES_NUMBER, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(CHARACTERS_NUMBER, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(COMMENTS_NUMBER, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(LINES_PER_METHOD_NUMBER, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(LINES_NUMBER, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(FIELDS_NUMBER, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(TYPES_NUMBER, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(CYCLOMATIC_COMPLEXITY, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(FIELDS_PER_TYPE, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(WEIGHTED_METHODS, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(BLOCK_DEPTH, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(COUPLINGS, DoubleCell.TYPE).createSpec()};
        
        
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        return outputSpec;
    }

    public static DataRow createTableRow(final String className, final List<MetricResult> score) {
    	DataCell[] cells = { doubleOrMissingCell(Double.parseDouble(score.get(0).getValue())), doubleOrMissingCell(Double.parseDouble(score.get(1).getValue())),
    			stringOrMissingCell(score.get(2).getValue()), doubleOrMissingCell(Double.parseDouble(score.get(3).getValue())),
    			doubleOrMissingCell(Double.parseDouble(score.get(4).getValue())), doubleOrMissingCell(Double.parseDouble(score.get(5).getValue())),
    			doubleOrMissingCell(Double.parseDouble(score.get(6).getValue())), stringOrMissingCell(score.get(7).getValue()), 
    			doubleOrMissingCell(Double.parseDouble(score.get(8).getValue())), doubleOrMissingCell(Double.parseDouble(score.get(9).getValue().replaceAll(",","."))), 
    			doubleOrMissingCell(Double.parseDouble(score.get(10).getValue())), doubleOrMissingCell(Double.parseDouble(score.get(11).getValue())),
    			doubleOrMissingCell(Double.parseDouble(score.get(12).getValue())),doubleOrMissingCell(Double.parseDouble(score.get(13).getValue())),
    			doubleOrMissingCell(Double.parseDouble(score.get(14).getValue())), doubleOrMissingCell(Double.parseDouble(score.get(15).getValue())),
    			doubleOrMissingCell(Double.parseDouble(score.get(16).getValue())), doubleOrMissingCell(Double.parseDouble(score.get(17).getValue())),
    			doubleOrMissingCell(Double.parseDouble(score.get(18).getValue())), doubleOrMissingCell(Double.parseDouble(score.get(17).getValue()))};
        DataRow row = new DefaultRow(className, cells);
        return row;
    }



}
