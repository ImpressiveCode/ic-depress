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
package org.impressivecode.depress.metric.pmd;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.StringCell;

/**
 * 
 * @author Tomasz Banach
 * @author �ukasz Waga
 * @author Monika Pruszkowska
 * 
 */
public class PMDAdapterTableFactory {

    private static final String CLASS = "Class";
    private static final String BEGIN_LINE = "BeginLine";
    private static final String END_LINE = "EndLine";
    private static final String BEGIN_COLUMN = "BeginColumn";
    private static final String END_COLUMN = "EndColumn";
    private static final String RULE = "Rule";
    private static final String RULE_SET = "RuleSet";
    private static final String INFO_URL = "InfoURL";
    private static final String PRIORITY = "Priority";
    private static final String MESSAGE_TEXT = "MessageText";



    private PMDAdapterTableFactory() {

    }

    public static DataTableSpec[] createTableSpec() {
        return new DataTableSpec[] { createDataColumnSpec() };
    }

    public static DataTableSpec createDataColumnSpec() {
        DataColumnSpec[] allColSpecs = { 
                new DataColumnSpecCreator(CLASS, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(PRIORITY, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(BEGIN_LINE, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(END_LINE, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(BEGIN_COLUMN, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(END_COLUMN, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(RULE, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(RULE_SET, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(INFO_URL, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(MESSAGE_TEXT, StringCell.TYPE).createSpec(),

        };
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        return outputSpec;
    }
}
