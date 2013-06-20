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

package org.impressivecode.depress.test.data.anonymisation;

import org.impressivecode.depress.data.anonymisation.ColumnCryptoTransformer;
import org.junit.Before;
import org.junit.Test;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.StringCell;

/**
 * 
 * @author Andrzej Dudek, Wrocław Institute of Technology
 * @author Marek Majchrzak, Impressive Code
 */

public class DataAnonymisationTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceprion1() throws Exception {
        new ColumnCryptoTransformer(null, null) {
            @Override
            protected DataCell transformCell(DataCell dataCell) {
                return null;
            }
        };
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceprion2() throws Exception {
        DataTableSpec spec = new DataTableSpec(new DataColumnSpecCreator("string cell", StringCell.TYPE).createSpec());

        new ColumnCryptoTransformer(spec, null) {
            @Override
            protected DataCell transformCell(DataCell dataCell) {
                return null;
            }
        };
    }
}
