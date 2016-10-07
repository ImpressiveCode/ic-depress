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
package org.impressivecode.depress.mr.ckjm;

import static org.impressivecode.depress.common.Cells.integerCell;
import static org.impressivecode.depress.common.Cells.doubleOrMissingCell;
import org.impressivecode.depress.mr.ckjm.ChidamberKemererJavaMetricsXmlResult.Class;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;

/**
 * 
 * @author Zuzanna Pacholczyk, Capgemini Poland
 * 
 */

public class ChidamberKemererJavaMetricsTableFactory {

   // public final static String CLASS_NAME = "Class";
    public final static String WMC = "WMC";
    public final static String DIT = "DIT";
    public final static String NOC = "NOC";
    public final static String CBO = "CBO";
    public final static String RFC = "RFC";
    public final static String LCOM = "LCOM";
    public final static String CA = "CA";
    public final static String CE = "CE";
    public final static String NPM = "NPM";
    public final static String LCOM3 = "LCOM3";
    public final static String LOC = "LOC";
    public final static String DAM = "DAM";
    public final static String MOA = "MOA";
    public final static String MFA = "MFA";
    public final static String CAM = "CAM";
    public final static String IC = "IC";
    public final static String CBM = "CBM";
    public final static String AMC = "AMC";
	
	public static DataTableSpec[] createTableSpec() {
	    return new DataTableSpec[] { createDataColumnSpec() };
	}        protected String name;
    protected short wmc;
    protected byte dit, noc; 
    protected short cbo, rfc, lcom, ca;
    protected byte ce;
    protected short npm;
    protected float lcom3;
    protected int loc;
    protected float dam;
    protected short moa;
    protected float mfa, cam;
    protected byte ic, cbm;
    protected float amc;
	
	public static DataTableSpec createDataColumnSpec() {
		DataColumnSpec[] allColSpecs = { new DataColumnSpecCreator(WMC, IntCell.TYPE).createSpec(),
				new DataColumnSpecCreator(DIT, IntCell.TYPE).createSpec(), new DataColumnSpecCreator(NOC, IntCell.TYPE).createSpec(),
				new DataColumnSpecCreator(CBO, IntCell.TYPE).createSpec(), new DataColumnSpecCreator(RFC, IntCell.TYPE).createSpec(),
				new DataColumnSpecCreator(LCOM, IntCell.TYPE).createSpec(), new DataColumnSpecCreator(CA, IntCell.TYPE).createSpec(),
				new DataColumnSpecCreator(CE, IntCell.TYPE).createSpec(), new DataColumnSpecCreator(NPM, IntCell.TYPE).createSpec(),
				new DataColumnSpecCreator(LCOM3, DoubleCell.TYPE).createSpec(), new DataColumnSpecCreator(LOC, IntCell.TYPE).createSpec(),
				new DataColumnSpecCreator(DAM, DoubleCell.TYPE).createSpec(), new DataColumnSpecCreator(MOA, IntCell.TYPE).createSpec(),
				new DataColumnSpecCreator(MFA, DoubleCell.TYPE).createSpec(), new DataColumnSpecCreator(CAM, DoubleCell.TYPE).createSpec(),
				new DataColumnSpecCreator(IC, IntCell.TYPE).createSpec(), new DataColumnSpecCreator(CBM, IntCell.TYPE).createSpec(),
				new DataColumnSpecCreator(AMC, DoubleCell.TYPE).createSpec()};
        return new DataTableSpec(allColSpecs);
	}
	
    public static DataRow createTableRow(final Class clazz) {
        DataCell[] cells = { integerCell((int)clazz.getWmc()), integerCell((int)clazz.getDit()), integerCell((int)clazz.getNoc()),
        		integerCell((int)clazz.getCbo()), integerCell((int)clazz.getRfc()), integerCell((int)clazz.getLcom()), integerCell((int)clazz.getCa()),
        		integerCell((int)clazz.getCe()), integerCell((int)clazz.getNpm()), doubleOrMissingCell((double)clazz.getLcom3()), integerCell((int)clazz.getLoc()),
        		doubleOrMissingCell((double)clazz.getDam()), integerCell((int)clazz.getMoa()), doubleOrMissingCell((double)clazz.getMfa()),
        		doubleOrMissingCell((double)clazz.getCam()), integerCell((int)clazz.getIc()), integerCell((int)clazz.getCbm()), doubleOrMissingCell((double)clazz.getAmc())};
        DataRow row = new DefaultRow(clazz.getName(), cells);
        return row;
    }

}