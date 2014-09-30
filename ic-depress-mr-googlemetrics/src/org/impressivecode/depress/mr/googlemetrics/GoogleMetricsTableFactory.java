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

    private static final String PARAMETERS_NUMBER = "AverageNumberofParameters";
    private static final String CONSTRUCTORS_PER_TYPE_NUMBER = "AverageNumberofConstructorsPerType";
    private static final String COMMENT_RATION = "CommentsRatio";
    private static final String METHODS_NUMBER = "NumberofMethods";
    private static final String SEMICOLONS_NUMBER = "NumberofSemicolons";
    private static final String METHODS_PER_TYPE_NUMBER = "AverageNumberofMethodsPerType";
    private static final String CONSTRUCTORS_NUMBER = "NumberofConstructors";
    private static final String ABSTRACTNESS = "Abstractness";
    private static final String CODE_LINES_NUMBER = "LinesofCode";
    private static final String CHARACTERS_NUMBER = "NumberofCharacters";
    private static final String COMMENTS_NUMBER = "NumberofComments";
    private static final String LINES_PER_METHOD_NUMBER = "AverageLinesOfCodePerMethod";
    private static final String LINES_NUMBER = "NumberofLines";
    private static final String FIELDS_NUMBER = "NumberofFields";
    private static final String TYPES_NUMBER = "NumberofTypes";
    private static final String CYCLOMATIC_COMPLEXITY = "AverageCyclomaticComplexity";
    private static final String FIELDS_PER_TYPE = "AverageNumberofFieldsPerType";
    private static final String WEIGHTED_METHODS = "WeightedMethods";
    private static final String BLOCK_DEPTH = "AverageBlockDepth";
    private static final String COUPLINGS = "EfferentCouplings";

    public static DataTableSpec[] createTableSpec() {
        return new DataTableSpec[] { createDataColumnSpec() };
    }

    public static DataTableSpec createDataColumnSpec() {
        DataColumnSpec[] allColSpecs = { new DataColumnSpecCreator("Name", StringCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(PARAMETERS_NUMBER, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(CONSTRUCTORS_PER_TYPE_NUMBER, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(COMMENT_RATION, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(METHODS_NUMBER, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(SEMICOLONS_NUMBER, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(METHODS_PER_TYPE_NUMBER, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(CONSTRUCTORS_NUMBER, DoubleCell.TYPE).createSpec(),
        			new DataColumnSpecCreator(ABSTRACTNESS, DoubleCell.TYPE).createSpec(),
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

    public static DataRow createTableRow(final String className, final List<MetricResult> score, final Long counter) {
    	DataCell[] cells = new DataCell[21];
    	for(int i =0; i< 21; i++) cells[i] = doubleOrMissingCell(null);
    	
    	cells[0] = stringOrMissingCell(className);
    	for( MetricResult s : score)
    	{
    		if(s.getName().replaceAll("\\s","").equals(PARAMETERS_NUMBER))
    				cells[1] = doubleOrMissingCell(Double.parseDouble(s.getValue()));
    		if(s.getName().replaceAll("\\s","").equals(CONSTRUCTORS_PER_TYPE_NUMBER))
				cells[2] = doubleOrMissingCell(Double.parseDouble(s.getValue()));
    		if(s.getName().replaceAll("\\s","").equals(COMMENT_RATION))
				cells[3] =  doubleOrMissingCell(Double.parseDouble(s.getValue().replace("/, $/", ".").replaceAll(",", "").replace("%", "")));
    		if(s.getName().replaceAll("\\s","").equals(METHODS_NUMBER))
				cells[4] = doubleOrMissingCell(Double.parseDouble(s.getValue().replace("/, $/", ".").replaceAll(",", "")));
    		if(s.getName().replaceAll("\\s","").equals(SEMICOLONS_NUMBER))
				cells[5] = doubleOrMissingCell(Double.parseDouble(s.getValue().replace("/, $/", ".").replaceAll(",", "")));
    		if(s.getName().replaceAll("\\s","").equals(METHODS_PER_TYPE_NUMBER))
				cells[6] = doubleOrMissingCell(Double.parseDouble(s.getValue().replace("/, $/", ".").replaceAll(",", "")));
    		if(s.getName().replaceAll("\\s","").equals(CONSTRUCTORS_NUMBER))
				cells[7] = doubleOrMissingCell(Double.parseDouble(s.getValue().replace("/, $/", ".").replaceAll(",", "")));
    		if(s.getName().replaceAll("\\s","").equals(ABSTRACTNESS))
				cells[8] =  doubleOrMissingCell(Double.parseDouble(s.getValue().replace("/, $/", ".").replaceAll(",", "").replace("%", "")));
    		if(s.getName().replaceAll("\\s","").equals(CODE_LINES_NUMBER))
				cells[9] = doubleOrMissingCell(Double.parseDouble(s.getValue().replace("/, $/", ".").replaceAll(",", "")));
    		if(s.getName().replaceAll("\\s","").equals(CHARACTERS_NUMBER))
				cells[10] = doubleOrMissingCell(Double.parseDouble(s.getValue().replace("/, $/", ".").replaceAll(",", "")));
    		if(s.getName().replaceAll("\\s","").equals(COMMENTS_NUMBER))
				cells[11] = doubleOrMissingCell(Double.parseDouble(s.getValue().replace("/, $/", ".").replaceAll(",", "")));
    		if(s.getName().replaceAll("\\s","").equals(LINES_PER_METHOD_NUMBER))
				cells[12] = doubleOrMissingCell(Double.parseDouble(s.getValue().replace("/, $/", ".").replaceAll(",", "")));
    		if(s.getName().replaceAll("\\s","").equals(LINES_NUMBER))
				cells[13] = doubleOrMissingCell(Double.parseDouble(s.getValue().replace("/, $/", ".").replaceAll(",", "")));
    		if(s.getName().replaceAll("\\s","").equals(FIELDS_NUMBER))
				cells[14] = doubleOrMissingCell(Double.parseDouble(s.getValue().replace("/, $/", ".").replaceAll(",", "")));
    		if(s.getName().replaceAll("\\s","").equals(TYPES_NUMBER))
				cells[15] = doubleOrMissingCell(Double.parseDouble(s.getValue().replace("/, $/", ".").replaceAll(",", "")));
    		if(s.getName().replaceAll("\\s","").equals(CYCLOMATIC_COMPLEXITY))
				cells[16] = doubleOrMissingCell(Double.parseDouble(s.getValue().replace("/, $/", ".").replaceAll(",", "")));
    		if(s.getName().replaceAll("\\s","").equals(FIELDS_PER_TYPE))
				cells[17] = doubleOrMissingCell(Double.parseDouble(s.getValue().replace("/, $/", ".").replaceAll(",", "")));
    		if(s.getName().replaceAll("\\s","").equals(WEIGHTED_METHODS))
				cells[18] = doubleOrMissingCell(Double.parseDouble(s.getValue().replace("/, $/", ".").replaceAll(",", "")));
    		if(s.getName().replaceAll("\\s","").equals(BLOCK_DEPTH))
				cells[19] = doubleOrMissingCell(Double.parseDouble(s.getValue().replace("/, $/", ".").replaceAll(",", "")));
    		if(s.getName().replaceAll("\\s","").equals(COUPLINGS))
				cells[20] = doubleOrMissingCell(Double.parseDouble(s.getValue().replace("/, $/", ".").replaceAll(",", "")));
    		
    	}
    		
        DataRow row = new DefaultRow(counter.toString(), cells);
        return row;
    }



}
