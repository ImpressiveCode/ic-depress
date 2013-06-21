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
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.RowKey;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * 
 * @author Andrzej Dudek, Wrocław Institute of Technology
 * 
 */

public class DataAnonymisationTest {
    
    protected final BufferedDataTable createBufferedDataTable(
            final ExecutionContext exec)
            throws Exception {
        // the DataTableSpec of the final table
        DataTableSpec spec = new DataTableSpec(
                new DataColumnSpecCreator("A", StringCell.TYPE).createSpec()
                );
        // init the container
        BufferedDataContainer container = exec.createDataContainer(spec);

        // add arbitrary number of rows to the container
        DataRow firstRow = new DefaultRow(new RowKey("first"), new DataCell[]{
            new StringCell("A1")
        });
        container.addRowToTable(firstRow);
        DataRow secondRow = new DefaultRow(new RowKey("second"), new DataCell[]{
            new StringCell("B1")
        });
        container.addRowToTable(secondRow);

        // finally close the container and get the result table.
        container.close();
        BufferedDataTable result = container.getTable();
        return result;
    }
        

    @Before
    public void setUp() throws Exception {
    }

    @Test(expected = NullPointerException.class)
    public void paramethersShouldThrowNullPointerExceprion1() throws Exception {
        new ColumnCryptoTransformer(null, null) {
            @Override
            protected DataCell transformCell(DataCell dataCell) {
                return null;
            }
        };
    }

    @Test(expected = NullPointerException.class)
    public void paramethersShouldThrowNullPointerExceprion2() throws Exception {
        DataTableSpec spec = new DataTableSpec(new DataColumnSpecCreator("string cell", StringCell.TYPE).createSpec());

        new ColumnCryptoTransformer(spec, null) {
            @Override
            protected DataCell transformCell(DataCell dataCell) {
                return null;
            }
        };
    }

    @Test(expected = NullPointerException.class)
    public void paramethersShouldThrowNullPointerExceprion3() throws Exception {
        String[] transforms = {};
        new ColumnCryptoTransformer(null, transforms) {
            @Override
            protected DataCell transformCell(DataCell dataCell) {
                return null;
            }
        };
    }
    
    @Test
    public void paramethersShouldSucceed() {
        String[] transforms = {};
        DataTableSpec spec = new DataTableSpec(new DataColumnSpecCreator("string cell", StringCell.TYPE).createSpec());

        new ColumnCryptoTransformer(spec, transforms) {
            @Override
            protected DataCell transformCell(DataCell dataCell) {
                return null;
            }
        };
        
        assertTrue(true);
    }
    
    @Test
    public void shouldEncryptText() throws Exception {
     // given
        ExecutionContext exec = mock(ExecutionContext.class);
        BufferedDataContainer container = mock(BufferedDataContainer.class);
        when(exec.createDataContainer(Mockito.any(DataTableSpec.class))).thenReturn(container);
        BufferedDataTable data = createBufferedDataTable(exec);
        DataTableSpec spec = new DataTableSpec(
                        new DataColumnSpecCreator("A", StringCell.TYPE).createSpec()
                        );
        String[] transforms = spec.getColumnNames();
        ColumnCryptoTransformer transformer = new ColumnCryptoTransformer(spec, transforms ) {
            
            @Override
            protected DataCell transformCell(DataCell dataCell) {
                // TODO Auto-generated method stub
                return null;
            }
        };

        transformer.transform(data, exec);
        
//        BufferedDataContainer container = 
//                mock(BufferedDataContainer.class);
//        DataTable inTable = mock(DataTable.class);
//        RowIterator iterator = mock(RowIterator.class);
//        when(iterator.hasNext()).thenReturn(true, false);
//        DataRow row = mock(DataRow.class);
//        when(row.getCell(Mockito.eq(0))).thenReturn(Cells.stringCell("resource"));
//        when(row.getCell(Mockito.eq(1))).thenReturn(Cells.stringSetCell(Sets.newHashSet("A", "B")));
//        when(iterator.next()).thenReturn(row);
//        when(inTable.iterator()).thenReturn(iterator);
//        // when
//        BufferedDataTable data = transformer.transform(container.getTable(), exec);
//        // then
//        assertThat(data).hasSize(1);
//        assertThat(data.get(0).getResourceName()).isEqualTo("resource");
//        assertThat(data.get(0).getMarkers()).containsOnly("A", "B");
    }

}
