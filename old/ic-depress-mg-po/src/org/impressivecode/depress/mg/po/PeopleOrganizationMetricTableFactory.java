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
package org.impressivecode.depress.mg.po;

import org.impressivecode.depress.scm.SCMAdapterTableFactory;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class PeopleOrganizationMetricTableFactory {

    private static final String ORGANISATION_INTERSECTION_FACTOR = "OIF";
    private static final String OVERALL_ORGANIZATION_OWNERSHIP = "OOW";
    private static final String LEVEL_OF_ORGAN_CODE_OWNERSHIP = "OCO";
    private static final String PERCENTAGE_OF_ORG_CONTR_TO_DEV = "PO";
    private static final String DEPTH_OF_MASTER_OWNERSHIP = "DMO";
    private static final String EDIT_FREQUENCY = "EF";
    private static final String NUMBER_OF_EX_EGINEERS = "NOEE";
    private static final String NUMBER_OF_ENGINEERS = "NOE";
    private static final String NUMBER_OF_INTERNSHIP = "NOE1";
    private static final String NUMBER_OF_ENGINEERS_JUNIOR = "NOE2";
    private static final String NUMBER_OF_ENGINEERS_ENGINEER = "NOE3";
    private static final String NUMBER_OF_ENGINEERS_SENIOR = "NOE4";
    private static final String NUMBER_OF_ENGINEERS_ARCHITECT = "NOE5";

    public static final String EX_ENGINEER = "ExEngineer";
    public static final String EXPIERIENCE = "ExpirienceLevel";
    public static final String DEVELOPER = "Name";
    public static final String ORGANIZATION_STRUCTURE = "Organization";

    private PeopleOrganizationMetricTableFactory() {

    }

    static DataTableSpec[] createTableSpec() {
        return new DataTableSpec[] { createDataColumnSpec() };
    }

    static DataTableSpec createDataColumnSpec() {
        DataColumnSpec[] allColSpecs = {
                new DataColumnSpecCreator(NUMBER_OF_ENGINEERS, IntCell.TYPE).createSpec(),
                new DataColumnSpecCreator(NUMBER_OF_EX_EGINEERS, IntCell.TYPE).createSpec(),
                new DataColumnSpecCreator(EDIT_FREQUENCY, IntCell.TYPE).createSpec(),
                new DataColumnSpecCreator(DEPTH_OF_MASTER_OWNERSHIP, IntCell.TYPE).createSpec(),
                new DataColumnSpecCreator(PERCENTAGE_OF_ORG_CONTR_TO_DEV, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(LEVEL_OF_ORGAN_CODE_OWNERSHIP, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(OVERALL_ORGANIZATION_OWNERSHIP, DoubleCell.TYPE).createSpec(),
                new DataColumnSpecCreator(ORGANISATION_INTERSECTION_FACTOR, IntCell.TYPE).createSpec(),
                new DataColumnSpecCreator(NUMBER_OF_INTERNSHIP, IntCell.TYPE).createSpec(),
                new DataColumnSpecCreator(NUMBER_OF_ENGINEERS_JUNIOR, IntCell.TYPE).createSpec(),
                new DataColumnSpecCreator(NUMBER_OF_ENGINEERS_ENGINEER, IntCell.TYPE).createSpec(),
                new DataColumnSpecCreator(NUMBER_OF_ENGINEERS_SENIOR, IntCell.TYPE).createSpec(),
                new DataColumnSpecCreator(NUMBER_OF_ENGINEERS_ARCHITECT, IntCell.TYPE).createSpec(),
        };
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        return outputSpec;
    }

    static DataTableSpec createDevDataColumnSpec() {
        DataColumnSpec[] allColSpecs = { 
                new DataColumnSpecCreator(DEVELOPER, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(EXPIERIENCE, IntCell.TYPE).createSpec(),
                new DataColumnSpecCreator(EX_ENGINEER, IntCell.TYPE).createSpec(),
                new DataColumnSpecCreator(ORGANIZATION_STRUCTURE, StringCell.TYPE).createSpec()};
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        return outputSpec;
    }

    public static DataTableSpec createHistoryColumnSpec() {
        DataColumnSpec[] allColSpecs = {
                new DataColumnSpecCreator(SCMAdapterTableFactory.RESOURCE_NAME, StringCell.TYPE).createSpec(),
                new DataColumnSpecCreator(SCMAdapterTableFactory.AUTHOR_COLNAME, StringCell.TYPE).createSpec(),
        };
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        return outputSpec;
    }
}
