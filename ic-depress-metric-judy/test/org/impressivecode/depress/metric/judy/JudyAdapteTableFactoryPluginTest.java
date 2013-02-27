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
package org.impressivecode.depress.metric.judy;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;
import org.knime.core.data.DataRow;
import org.knime.core.data.def.DoubleCell;
/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class JudyAdapteTableFactoryPluginTest {

	@Test
	public void shouldCreateTableSpec() {
		assertEquals(JudyAdapteTableFactory.createDataColumnSpec().getNumColumns(), 1);
		assertTrue(JudyAdapteTableFactory.createDataColumnSpec().containsName("MutantsScore"));
		assertEquals(JudyAdapteTableFactory.createDataColumnSpec().getColumnSpec(0).getType(),DoubleCell.TYPE);
	}

	@Test
	public void shouldCreateDataColumnSpec() {
		assertEquals(JudyAdapteTableFactory.createTableSpec().length, 1);
	}

	@Test
	public void shouldCreateTableRow() {
		DataRow row = JudyAdapteTableFactory.createTableRow("Key", BigDecimal.ONE);
		assertEquals(row.getKey().getString(), "Key");
		assertEquals(((DoubleCell)row.getCell(0)).getDoubleValue(), 1.0, 0.0);
	}

}
