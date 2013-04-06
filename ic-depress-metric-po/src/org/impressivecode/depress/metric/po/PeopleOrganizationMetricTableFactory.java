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
package org.impressivecode.depress.metric.po;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.DefaultRow;
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
    private static final String NUMBER_OF_ENGINEERS_JUNIOR = "NOE1";
    private static final String NUMBER_OF_ENGINEERS_ENGINEER = "NOE2";
    private static final String NUMBER_OF_ENGINEERS_SENIOR = "NOE3";
    private static final String NUMBER_OF_ENGINEERS_ARCHITECT = "NOE4";

    private static final String EX_ENGINEER = "ExEngineer";
    private static final String EXPIERIENCE = "ExpirienceLevel";
    private static final String DEVELOPER = "DeveloperName";
    private static final String EXTERNAL_ENGINEER = "External";

    private PeopleOrganizationMetricTableFactory() {

    }

    static DataTableSpec[] createTableSpec() {
        return new DataTableSpec[] { createDataColumnSpec() };
    }

    static DataTableSpec createDataColumnSpec() {
        DataColumnSpec[] allColSpecs = new DataColumnSpec[12];
        allColSpecs[0] = new DataColumnSpecCreator(NUMBER_OF_ENGINEERS, IntCell.TYPE).createSpec();
        allColSpecs[1] = new DataColumnSpecCreator(NUMBER_OF_EX_EGINEERS, IntCell.TYPE).createSpec();
        allColSpecs[2] = new DataColumnSpecCreator(EDIT_FREQUENCY, IntCell.TYPE).createSpec();
        allColSpecs[3] = new DataColumnSpecCreator(DEPTH_OF_MASTER_OWNERSHIP, DoubleCell.TYPE).createSpec();
        allColSpecs[4] = new DataColumnSpecCreator(PERCENTAGE_OF_ORG_CONTR_TO_DEV, DoubleCell.TYPE).createSpec();
        allColSpecs[5] = new DataColumnSpecCreator(LEVEL_OF_ORGAN_CODE_OWNERSHIP, DoubleCell.TYPE).createSpec();
        allColSpecs[6] = new DataColumnSpecCreator(OVERALL_ORGANIZATION_OWNERSHIP, DoubleCell.TYPE).createSpec();
        allColSpecs[7] = new DataColumnSpecCreator(ORGANISATION_INTERSECTION_FACTOR, IntCell.TYPE).createSpec();
        allColSpecs[8] = new DataColumnSpecCreator(NUMBER_OF_ENGINEERS_JUNIOR, IntCell.TYPE).createSpec();
        allColSpecs[9] = new DataColumnSpecCreator(NUMBER_OF_ENGINEERS_ENGINEER, IntCell.TYPE).createSpec();
        allColSpecs[10] = new DataColumnSpecCreator(NUMBER_OF_ENGINEERS_SENIOR, IntCell.TYPE).createSpec();
        allColSpecs[11] = new DataColumnSpecCreator(NUMBER_OF_ENGINEERS_ARCHITECT, IntCell.TYPE).createSpec();
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        return outputSpec;
    }

    static DataTableSpec createDevDataColumnSpec() {
        DataColumnSpec[] allColSpecs = new DataColumnSpec[3];
        allColSpecs[0] = new DataColumnSpecCreator(DEVELOPER, StringCell.TYPE).createSpec();
        allColSpecs[1] = new DataColumnSpecCreator(EXPIERIENCE, IntCell.TYPE).createSpec();
        allColSpecs[2] = new DataColumnSpecCreator(EX_ENGINEER, BooleanCell.TYPE).createSpec();
        allColSpecs[2] = new DataColumnSpecCreator(EXTERNAL_ENGINEER, BooleanCell.TYPE).createSpec();
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        return outputSpec;
    }

    static DataRow createTableRow(final String className, final POData value) {
        DataCell[] cells = getPOCells(value);
        DataRow row = new DefaultRow(className, cells);
        return row;
    }

    private static DataCell[] getPOCells(final POData value) {
        DataCell[] cells = new DataCell[13];
        cells[0] = new IntCell(value.getNOE());
        cells[1] = new IntCell(value.getNOEE());
        cells[2] = new IntCell(value.getEF());
        cells[3] = new DoubleCell(value.getDMO());
        cells[4] = new DoubleCell(value.getPO());
        cells[5] = new DoubleCell(value.getOCO());
        cells[6] = new DoubleCell(value.getOOW());
        cells[7] = new IntCell(value.getOIF());
        cells[8] = new IntCell(value.getNOE1());
        cells[9] = new IntCell(value.getNOE2());
        cells[10] = new IntCell(value.getNOE3());
        cells[11] = new IntCell(value.getNOE4());
        return cells;
    }
}
