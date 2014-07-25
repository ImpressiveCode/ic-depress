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
package org.impressivecode.depress.mr.pitest;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;

/**
 * 
 * @author Zuzanna Pacholczyk, Capgemini Polska
 * 
 */
public class PitestAdapterTableFactory {	

	private static final String DETECTION = "Detected";
	private static final String MUTATION_STATUS = "MutationStatus";
    private static final String SOURCE_FILE = "SourceFile";
    private static final String MUTATED_CLASS = "MutatedClass";
    private static final String MUTATED_METHOD = "MutatedMethod";
    private static final String METHOD_DESCRIPTION = "MethodDescripton";
    private static final String LINE_NUMBER = "LineNumber";
    private static final String MUTATOR = "Mutator";
    private static final String INDEX = "Index";
    private static final String KILLING_TEST = "KillingTest";

    public PitestAdapterTableFactory() {

    }

    public static DataTableSpec[] createTableSpec() {
        return new DataTableSpec[] { createDataColumnSpec() };
    }

    public static DataTableSpec createDataColumnSpec() {
        DataColumnSpec[] allColSpecs = { 
                new DataColumnSpecCreator(MUTATION_STATUS, StringCell.TYPE).createSpec(), 
                new DataColumnSpecCreator(DETECTION, BooleanCell.TYPE).createSpec(), 
                new DataColumnSpecCreator(SOURCE_FILE, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(MUTATED_CLASS, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(MUTATED_METHOD, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(METHOD_DESCRIPTION, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(LINE_NUMBER, IntCell.TYPE).createSpec(), 
                new DataColumnSpecCreator(MUTATOR, StringCell.TYPE).createSpec(),
		        new DataColumnSpecCreator(INDEX, IntCell.TYPE).createSpec(), 
		        new DataColumnSpecCreator(KILLING_TEST, StringCell.TYPE).createSpec()};
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        return outputSpec;
    }
}
